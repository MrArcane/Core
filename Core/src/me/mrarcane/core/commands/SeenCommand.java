package me.mrarcane.core.commands;

import me.mrarcane.core.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import static me.mrarcane.core.utils.ChatUtils.sendChat;
import static me.mrarcane.core.utils.PlayerUtils.timePlayer;

/**
 * File generated by: MrArcane
 * 1/15/2018
 **/
public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 0) {
                sendChat(sender, "&cUsage: /seen <player>");
                return true;
            }

            OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
            if (!t.hasPlayedBefore()) {
                sendChat(sender, String.format("&c%s doesn't exist in our datebase.", args[1]));
                return true;
            }

            PlayerUtils td = new PlayerUtils(t.getUniqueId().toString());
            ConfigurationSection pFile = td.getConfigurationSection("Player");
            if (t.isOnline()) {
                sendChat(sender, String.format("&eOnline since: &7%s", timePlayer(pFile.getLong("Time"))));
                return true;
            }
            if (pFile.getString("Last online") != null) {
                sendChat(sender, String.format("&eLast seen: &7%s", timePlayer(pFile.getLong("Last online"))));
            } else {
                sendChat(sender, String.format("&eLast seen: &7%s", timePlayer(pFile.getLong("Time"))));
            }
            if (sender.hasPermission("core.admin")) {
                sendChat(sender, String.format("&eLocation: &7%s&e, &7%s&e, &7%s&e, &7%s&e", pFile.getInt("x"), pFile.getInt("y"), pFile.getInt("z"), pFile.getString("world")));
                return true;
            }
        return false;
    }
}
