package me.mrarcane.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.mrarcane.core.utils.ChatUtils.sendChat;

/**
 * File generated by: MrArcane
 * 2/27/2018
 **/
public class SendChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                sendChat(sender, "&cPlayer is offline");
                return true;
            }
            String msg = "";
            for (int i = 1; i < args.length; i++) {
                msg = msg + " " + args[i];
            }
            sendChat(p, msg.trim().replace("{player}", p.getDisplayName()));
            return true;
        }
        sendChat(sender, "&cYou must be console to do this.");
        return false;
    }
}