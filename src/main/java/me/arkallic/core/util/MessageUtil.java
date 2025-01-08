package me.arkallic.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class MessageUtil {


    public static String ONLY_PLAYERS_COMMAND = "This command is only available to players.";
    public static String NO_PERMISSION_COMMAND = "&cYou're not authorized to use this command.";
    public static String PLAYER_OFFLINE = "&c&lPlayer Offline: &7Couldn't find that player.";

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void log(String message) {
        Bukkit.getLogger().info(message);
    }

    public static void log(Level level, String message) {
        Bukkit.getLogger().log(level, message);
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }
    public static void send(CommandSender sender, String... args) {
        sender.sendMessage(color(Arrays.toString(args)));
    }

    public static void broadcast(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p, message);
        }
    }
}
