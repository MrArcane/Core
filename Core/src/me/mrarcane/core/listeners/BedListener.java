package me.mrarcane.core.listeners;

import me.mrarcane.core.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import static me.mrarcane.core.utils.ChatUtils.sendChat;

/**
 * File generated by: MrArcane
 * 1/21/2018
 **/
public class BedListener implements Listener {

    @EventHandler
    private void getInBed(final PlayerBedEnterEvent e) {
        if (Main.getInstance().getConfig().getConfigurationSection("General").getBoolean("Perks") /**&& Bukkit.getOnlinePlayers().size() > 1**/) {
            sendChat(e.getPlayer(), "&6To start day vote type /day.");
        }
    }
}
