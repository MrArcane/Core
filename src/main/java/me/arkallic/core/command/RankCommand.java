package me.arkallic.core.command;

import me.arkallic.core.manager.RankDataManager;
import me.arkallic.core.model.Rank;
import me.arkallic.core.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.util.MessageUtil.*;

public class RankCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final RankDataManager rankDataManager;

    public RankCommand(RankDataManager rankDataManager, PlayerDataManager playerDataManager) {
        this.rankDataManager = rankDataManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            send(sender, "&7-".repeat(15));
            send(sender, "&aRank system");
            send(sender, "&7-".repeat(15));
            send(sender, "&7/rank list &7- &aShows a list of ranks.");
            send(sender, "&7/setrank [player] [rank]&7- &aSets the players rank.");
            send(sender, "&7/rank [player] &7- &aShows the players rank.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (sender instanceof Player) {
                for (Rank rank : rankDataManager.getRanks()) {
                    send(sender, String.format("&7[&2%s&7]:", rank.getName()));
                    send(sender, String.format("&7Description &a%s", rank.getDescription()));
                    if (rank.getPrefix() == null) {
                        send(sender, "&7Prefix &cNothing");
                    } else {
                        send(sender, String.format("&7Prefix &c%s", rank.getPrefix()));
                    }
                }
                return true;
            }
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            assert target != null;
            send(sender, playerDataManager.getRank(target.getUniqueId()).getName());
            return true;
        }

        return false;
    }
}
