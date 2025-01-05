package me.arkallic.core.command;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.manager.RankDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.arkallic.core.util.MessageUtil.send;

public class SetRankCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final RankDataManager rankDataManager;

    public SetRankCommand(RankDataManager rankDataManager, PlayerDataManager playerDataManager) {
        this.rankDataManager = rankDataManager;
        this.playerDataManager = playerDataManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            send(sender, "&c/setrank [PLAYER] [RANK]");
            return true;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            send(sender, args[0]+ " is offline.");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        PlayerData pd = playerDataManager.getPlayerData(target.getUniqueId());

        if (rankDataManager.getRank(args[1]) == null) {
            send(sender, "&4That rank is invalid!");
            return true;
        }
        String rank = rankDataManager.getRank(args[1]).getName();
        pd.setRank(rank);
        send(sender, String.format("&7You've set: &a%s's &7rank to: &c%s", target.getName(), pd.getRank()));
        return true;
    }
}
