package me.arkallic.core.command;

import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.data.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.arkallic.core.handler.MessageHandler.send;

public class CoreCommand implements CommandExecutor {
    private final PlayerDataManager playerDataManager;
    public CoreCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            UUID uuid = p.getUniqueId();
             if (args.length == 0) {
                 send(p, PlayerData.defaultData.RANK);
                return true;
            }
            return true;
        }
        return false;
    }
}
