package me.arkallic.core.command.home;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.util.IntUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.log;
import static me.arkallic.core.util.MessageUtil.send;

public class MaxHomesCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public MaxHomesCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player p) {

            UUID uuid = p.getUniqueId();
            PlayerData pd = playerDataManager.getPlayerData(uuid);

            if (args.length == 0) {
                send(p, "&cUSAGE: &7/maxhomes [ADD/REMOVE] [INT] [PLAYER]");
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            PlayerData td = playerDataManager.getPlayerData(target.getUniqueId());
            int MAX_HOMES = td.getMaxHomes();

            if (!playerDataManager.playerDataExists(target.getUniqueId())) {
                send(p, "That player doesn't exist!");
                return true;
            }
            if (!IntUtil.isInt(args[1])) {
                send(p, "&cUSAGE: &7/maxhomes [ADD/REMOVE] [AMOUNT] [PLAYER]");
                return true;
            }
            int i = Integer.parseInt(args[1]);
            if (args[0].equalsIgnoreCase("add")) {
                td.setMaxHomes(MAX_HOMES + i);
                send(p, String.format("&7You modified &a%s &7home limit to: &c%s", target.getName(), MAX_HOMES + i));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                td.setMaxHomes(MAX_HOMES - i);
                send(p, String.format("&7You modified &a%s &7home limit to: &c%s", target.getName(), MAX_HOMES - i));
                return true;
            }
            return true;
        }

        //Console
        if (args.length == 0) {
            log("USAGE: /maxhomes [ADD/REMOVE] [INT] [PLAYER]");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
        PlayerData td = playerDataManager.getPlayerData(target.getUniqueId());
        int MAX_HOMES = td.getMaxHomes();

        if (!playerDataManager.playerDataExists(target.getUniqueId())) {
            log("That player doesn't exist!");
            return true;
        }

        if (!IntUtil.isInt(args[1])) {
            log("USAGE: maxhomes [ADD/REMOVE] [AMOUNT] [PLAYER]");
            return true;
        }

        int i = Integer.parseInt(args[1]);

        if (args[0].equalsIgnoreCase("set")) {
            td.setMaxHomes(MAX_HOMES + i);
            log(String.format("You modified %s home limit to: &c%s", target.getName(), MAX_HOMES + i));
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            td.setMaxHomes(MAX_HOMES - i);
            log(String.format("You modified %s &7home limit to: %s", target.getName(), MAX_HOMES - i));
            return true;
        }
        return false;
    }
}
