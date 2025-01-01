package me.arkallic.core.handler;

import me.arkallic.core.Core;
import me.arkallic.core.util.DefaultFontInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.logging.Level;

public class MessageHandler {


    public static void sendCentredMessage(CommandSender sender, String message) {
        if(message == null || message.equals("")) {
            sender.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }
        int CENTER_PX = 154;
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        sender.sendMessage(sb.toString() + message);
    }

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
