package me.arkallic.core.command.coins;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.util.IntUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.*;

public class CoinsCommand implements CommandExecutor {


    private final PlayerDataManager playerDataManager;

    public CoinsCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    /**
     * @param sender  Source of the command
     * @param cmd Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;

        if (!player.hasPermission("core.coins")) {
            send(sender, NO_PERMISSION_COMMAND);
            return true;
        }

        if (args.length == 0) {
            showPermissions(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        return switch (subCommand) {
            case "send" -> handleSendCommand(sender, args);
            case "give" -> handleGiveCommand(sender, args);
            case "remove" -> handleRemoveCommand(sender, args);
            default -> {
                send(sender, "&cUnknown command. Use /coins for help.");
                yield false;
            }
        };
    }

    private void showPermissions(CommandSender sender) {
        send(sender, "&6Available commands:");
        if (sender.hasPermission("core.coins.modify")) {
            send(sender, "/coins give [PLAYER] [AMOUNT]");
            send(sender, "/coins remove [PLAYER] [AMOUNT]");
        }
        send(sender, "/coins send [PLAYER] [AMOUNT]");
    }

    private boolean validateArgs(CommandSender sender, String[] args) {

        if (args.length != 3) {
            send(sender, "Usage: /coins <command>");
            return true;
        }
        if (Bukkit.getPlayer(args[1]) == null) {
            send(sender, PLAYER_OFFLINE);
            return true;
        }
        if (!IntUtil.isInt(args[2]) || Integer.parseInt(args[2]) <= 0) {
            send(sender, "Please provide a valid positive number for the amount.");
            return false;
        }

        return false;
    }

    private boolean handleRemoveCommand(CommandSender sender, String[] args) {

        if (validateArgs(sender, args)) return true;

        Player target = Bukkit.getPlayer(args[1]);
        PlayerData td = playerDataManager.getPlayerData(target.getUniqueId());
        int targetCoins = td.getCoins();
        int amount = Integer.parseInt(args[2]);
        int newCoinsAmount = targetCoins - amount;

        if (amount > targetCoins) {
            newCoinsAmount = 0;
        }

        td.setCoins(newCoinsAmount, target.getUniqueId(), "&aNew coins balance: &7" + newCoinsAmount);
        send(sender, String.format("&aRemoved &7%s coins &afrom &7%s.", amount, target.getName()));
        return true;
    }


    private boolean handleGiveCommand(CommandSender sender, String[] args) {

        if (validateArgs(sender, args)) return true;

        Player target = Bukkit.getPlayer(args[1]);
        PlayerData td = playerDataManager.getPlayerData(target.getUniqueId());
        int targetCoins = td.getCoins();
        int amount = Integer.parseInt(args[2]);
        int newCoinsAmount = targetCoins + amount;
        td.setCoins(newCoinsAmount, target.getUniqueId(), "&aNew coins balance: &7" + newCoinsAmount);
        send(sender, String.format("&aGave &7%s coins &ato &7%s.", amount, target.getName()));
        return true;
    }

    private boolean handleSendCommand(CommandSender sender, String[] args) {

        if (validateArgs(sender, args)) return true;

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);
        PlayerData pd = playerDataManager.getPlayerData(player.getUniqueId());
        PlayerData td = playerDataManager.getPlayerData(target.getUniqueId());
        int senderCoins = pd.getCoins();
        int targetCoins = td.getCoins();
        int amount = Integer.parseInt(args[2]);

        if (args[1].equals(player.getName())) {
            send(sender, "&4&lYou cannot send coins to yourself.");
            return true;
        }

        if (amount > senderCoins || senderCoins == 0) {
            send(sender, "&c&lYou don't have enough coins.");
            player.performCommand("balance");
            return true;
        }

        int targetNewCoinsAmount = targetCoins + amount;
        int senderNewCoinsAmount = senderCoins - amount;

        pd.setCoins(senderNewCoinsAmount, player.getUniqueId(), String.format("&bYou sent &a%s &f%s &bcoins.", td.getDisplayName(), amount));
        td.setCoins(targetNewCoinsAmount, target.getUniqueId(), String.format("&b%s &7sent you &a%s &7coins, &fBalance: &e%s", pd.getDisplayName(), amount, targetNewCoinsAmount));
        send(sender, String.format("&aTransaction successful! You sent &7%s coins &ato &7%s.", amount, target.getName()));
        return true;
    }
}
