package me.arkallic.core.listener;

import me.arkallic.core.handler.LangHandler;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.manager.RankDataManager;
import me.arkallic.core.model.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.arkallic.core.util.MessageUtil.broadcast;
import static me.arkallic.core.util.MessageUtil.send;

public class PlayerDataListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final LangHandler langHandler;
    private final RankDataManager rankDataManager;

    public PlayerDataListener(PlayerDataManager playerDataManager, LangHandler langHandler, RankDataManager rankDataManager) {
        this.playerDataManager = playerDataManager;
        this.langHandler = langHandler;
        this.rankDataManager = rankDataManager;
    }
    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playerDataManager.register(p.getUniqueId());
        if (p.hasPlayedBefore()) {
            send(p, langHandler.WELCOME_BACK.replace("%PLAYER%", playerDataManager.getDisplayName(p.getUniqueId())));
            return;
        }
        for (Rank r : this.rankDataManager.getRanks()) {
            if (r.isDefault()) {
                playerDataManager.setRank(p.getUniqueId(), r.getName(), false);
            }
        }
            broadcast(langHandler.NEW_PLAYER.replace("%PLAYER%", playerDataManager.getDisplayName(p.getUniqueId())));
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        playerDataManager.unregister(p.getUniqueId());
    }
}
