package me.mrarcane.core.commands;

import me.mrarcane.core.Main;
import me.mrarcane.core.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

import static me.mrarcane.core.utils.ChatUtils.sendChat;

/**
 * File generated by: MrArcane
 * 1/15/2018
 **/
@SuppressWarnings("ALL")
public class MailCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerUtils pd = new PlayerUtils(p.getUniqueId().toString());
            if (args.length == 0) {
                sendChat(p, "&cUsage: /mail <read/send/delete/clear>");
                return true;
            }
            if (args[0].equalsIgnoreCase("read")) {
                List<String> m = pd.getConfigurationSection("Player").getStringList("Mail");
                if (m.size() == 0) {
                    sendChat(p, "&cNo mail detected.");
                    return true;
                }
                sendChat(p, String.format("&eInbox (&7%s&e):", m.size()));
                for (int i = 0; i < m.size(); i++) {
                    sendChat(p,(i + 1) + ". " + m.get(i));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("send")) {
                if (args.length <= 2) {
                    sendChat(p, "&cUsage: /mail send <player> <message>");
                    return true;
                }
                OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);
                if (t.isOnline()) {
                    sendChat(t.getPlayer(), "&aMail received! &7/mail read");
                }
                if (!t.hasPlayedBefore()) {
                    sendChat(p, String.format("&c%s doesn't exist in our datebase.", args[1]));
                    return true;
                }
                File file = new File(Main.getInstance().getDataFolder() + "/Players/", t.getUniqueId().toString() + ".yml");
                if (!file.exists()) {
                    sendChat(p, String.format("&c%s hasn't played since core has been updated to V0.6.", t.getName()));
                    return true;
                }
                PlayerUtils td = new PlayerUtils(t.getUniqueId().toString());
                if (td.getConfigurationSection("Player") == null) {
                    td.createSection("Player");
                    td.save();
                }
                StringBuilder sb = new StringBuilder();
                List<String> m = td.getConfigurationSection("Player").getStringList("Mail");
                String msg = "";
                for (int i = 2; i < args.length; i++) {
                    msg = String.valueOf(sb.append(args[i]).append(" "));
                }
                m.add(String.format("&e%s: &7%s", p.getName(), msg));
                td.getConfigurationSection("Player").set("Mail", m);
                td.save();
                sendChat(p, String.format("&aMessage '&7%s&a' sent to %s", msg.trim(), t.getName()));
                return true;
            }
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
                List<String> m = pd.getConfigurationSection("Player").getStringList("Mail");
                int i = Integer.valueOf(args[1]) - 1;
                sendChat(p,"&2Message was deleted successfully.");
                m.remove(i);
                pd.getConfigurationSection("Player").set("Mail", m);
                pd.save();
                return true;
            }
            if (args[0].equalsIgnoreCase("clear")) {
                List<String> m = pd.getConfigurationSection("Player").getStringList("Mail");
                if (m.size() == 0) {
                    sendChat(p, "&cNo mail to clear.");
                    return true;
                }
                pd.getConfigurationSection("Player").set("Mail", null);
                pd.save();
                sendChat(p, "&aInbox cleared.");
                return true;
            } else {
                sendChat(p, "&cIncorrect command, try again.");
            }
            return true;
        }
        return false;
    }
}