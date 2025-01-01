package me.arkallic.core.command;

import me.arkallic.core.Core;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.manager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.handler.MessageHandler.send;

public class SetRankCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final RankManager rankManager;

    public SetRankCommand(RankManager rankManager, PlayerDataManager playerDataManager) {
        this.rankManager = rankManager;
        this.playerDataManager = playerDataManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            send(sender, "&c/setrank [PLAYER] [RANK]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            if (rankManager.getRank(args[1]) == null) {
                send(sender, "&4That rank is invalid!");
                return true;
            }
            String rank = rankManager.getRank(args[1]).getName();
            playerDataManager.setRank(target.getUniqueId(), rank, true);
            send(sender, String.format("&7You've set: &a%s's &7rank to: &c%s", target.getName(), playerDataManager.getRank(target.getUniqueId()).getName()));
            return true;
        }
        return false;
    }
}
