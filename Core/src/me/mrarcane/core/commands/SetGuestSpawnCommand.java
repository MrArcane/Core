package me.mrarcane.core.commands;

import me.mrarcane.core.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static me.mrarcane.core.utils.ChatUtils.sendChat;

/**
 * File generated by: MrArcane
 * 3/10/2018
 **/
public class SetGuestSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        FileConfiguration cfg = Main.getInstance().getConfig();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getLocation();
            if (cfg.getConfigurationSection("Spawn") == null) {
                cfg.createSection("Spawn");
            }
            if (cfg.getConfigurationSection("Spawn.Guest spawn") == null) {
                cfg.createSection("Spawn.Guest spawn");
            }
            if (args.length == 0) {
                cfg.getConfigurationSection("Spawn.Guest spawn").set("x", loc.getX());
                cfg.getConfigurationSection("Spawn.Guest spawn").set("y", loc.getY() + 1);
                cfg.getConfigurationSection("Spawn.Guest spawn").set("z", loc.getZ());
                cfg.getConfigurationSection("Spawn.Guest spawn").set("w", loc.getWorld().getName());
                Main.getInstance().saveConfig();
                sendChat(sender, "&aGuest spawn point set at your location!");
                return true;
            }
            return true;
        }
        return false;
    }
}