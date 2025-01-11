package me.arkallic.core.data;

import me.arkallic.core.Core;
import me.arkallic.core.wrappers.YMLFileWrapper;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class LogData extends YMLFileWrapper {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    public LogData(String name, Core core) {
        super("Logs", name, core);
    }

    public void addCommand(LogData log, UUID uuid, String command) {
        Player player = Bukkit.getPlayer(uuid);
        log.getConfig().set(localDateTime + " " + player.getName(), command);
    }

    public void addShop(LogData log, UUID uuid, String shop) {
        Player player = Bukkit.getPlayer(uuid);
        log.getConfig().set(localDateTime + " " + player.getName(), shop);
    }
}
