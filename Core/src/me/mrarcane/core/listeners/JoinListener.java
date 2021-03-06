package me.mrarcane.core.listeners;

import me.mrarcane.core.Main;
import me.mrarcane.core.utils.BroadcastUtils;
import me.mrarcane.core.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static me.mrarcane.core.utils.ChatUtils.sendAlert;
import static me.mrarcane.core.utils.ChatUtils.sendChat;
import static me.mrarcane.core.utils.PlayerUtils.getColor;
import static me.mrarcane.core.utils.PlayerUtils.getMail;

/**
 * File generated by: MrArcane
 * 11/7/2017
 **/
public class JoinListener implements Listener {

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        FileConfiguration cfg = Main.getInstance().getConfig();
        PlayerUtils pd = new PlayerUtils(p.getUniqueId().toString());
        ConfigurationSection pFile = pd.getConfigurationSection("Player");
        List<String> nList = cfg.getStringList("New login");
        if (Bukkit.getOnlinePlayers().size() > 0) {
            BroadcastUtils.restart(Main.getInstance());
        }
        if (cfg.getBoolean("General.Perks")) {
            sendChat(p, Main.getInstance().getConfig().getString("General.Perks message"));
        }
        if (!p.hasPlayedBefore()) {
            ConfigurationSection s = cfg.getConfigurationSection("Spawn.Guest spawn");
            if (cfg.getConfigurationSection("Spawn.Guest spawn") != null) {
                p.teleport(new Location(Bukkit.getWorld(s.getString("w")), s.getDouble("x"), s.getDouble("y"), s.getDouble("z")));
            }
            for (String msg : nList) {
                sendChat(p, msg.replace("{player}", p.getName()));
            }
            pd.createSection("Player");
            pd.save();
            sendAlert(String.format("&7%s &aHas joined for the first time!", p.getName()));
            pd.getConfigurationSection("Player").set("Time", System.currentTimeMillis());
            pd.save();
            return;
        }
        if (pFile.get("Last online") != null) {
            pFile.set("Last online", null);
            pFile.set("Time", System.currentTimeMillis());
            pd.save();
        }
        getColor(p);
        getMail(p);
    }
}

