package me.arkallic.core.command;

import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.log("Only players can use this command.");
            return true;
        }


        return false;
    }
}
