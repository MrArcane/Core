package me.arkallic.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.arkallic.core.util.MessageUtil.send;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

    }
}
