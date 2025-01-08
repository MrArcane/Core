package me.arkallic.core.command.coins;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.manager.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.util.MessageUtil.*;

public class BalanceCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public BalanceCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    /**
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            send(sender, ONLY_PLAYERS_COMMAND);
            return true;
        }

        if (!player.hasPermission("core.balance")) {
            send(sender, NO_PERMISSION_COMMAND);
            return true;
        }

        PlayerData pd = playerDataManager.getPlayerData(player.getUniqueId());
        String balance = "&6Your current balance is: &7" + pd.getCoins();
        send(sender, balance);


        return true;
    }
}
