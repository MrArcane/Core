package me.arkallic.core.listener;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.data.LangData;
import me.arkallic.core.manager.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.arkallic.core.util.MessageUtil.broadcast;
import static me.arkallic.core.util.MessageUtil.send;

public class PlayerDataListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final LangData langData;

    public PlayerDataListener(PlayerDataManager playerDataManager, LangData langData) {
        this.playerDataManager = playerDataManager;
        this.langData = langData;
    }
    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playerDataManager.register(p.getUniqueId());
        PlayerData pd = playerDataManager.getPlayerData(p.getUniqueId());

        if (p.hasPlayedBefore()) {
            send(p, langData.welcomeBack.replace("%PLAYER%", pd.getDisplayName()));
            return;
        }

            broadcast(langData.newPlayer.replace("%PLAYER%", pd.getDisplayName()));
    }

    @EventHandler
    private void playerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        playerDataManager.unregister(p.getUniqueId());
    }
}
