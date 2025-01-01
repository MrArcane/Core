package me.arkallic.core.command.home;

import me.arkallic.core.handler.LangHandler;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.model.Home;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static me.arkallic.core.handler.MessageHandler.send;
import static me.arkallic.core.model.PlayerData.*;

public class HomeCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangHandler langHandler;

    public HomeCommand(PlayerDataManager playerDataManager, LangHandler langHandler) {
        this.playerDataManager = playerDataManager;
        this.langHandler = langHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {


        if (sender instanceof Player p) {
            HashMap<String, Home> homes = playerDataManager.getHomes(p.getUniqueId());

            if (!playerDataManager.hasHomes(p.getUniqueId())) {
                send(p, langHandler.NO_HOMES);
                return true;
            }
            if (args.length == 0) {
                if (playerDataManager.hasHomes(p.getUniqueId()) && homes.size() > 1) {
                    for (String s : homes.keySet()) {
                        Home home = playerDataManager.getHome(p.getUniqueId(), s);
                        Location loc = home.getLocation();
                        send(p, String.format("&7[&c%s&7]: (&4%s&7) X: &c%s &7Y: &c%s &7Z: &c%s", home.getName().toUpperCase(), loc.getWorld().getName().toUpperCase(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
                    }
                    send(p, "&cUSAGE: &7/home [NAME]");
                    return true;
                }
                for (String s : homes.keySet()) {
                    Home home = playerDataManager.getHome(p.getUniqueId(), s);
                    p.teleport(home.getLocation());
                    send(p,  langHandler.TELEPORT_HOME.replace("%HOME%", home.getName()));
                    return true;
                }

            }

            if (args.length == 1) {
                String input = args[0].toLowerCase();
                Home home = playerDataManager.getHome(p.getUniqueId(), input);
                if (home == null) {
                    send(p,  langHandler.INVALID_HOME);
                    return true;
                }
                p.teleport(home.getLocation());
                send(p,  langHandler.TELEPORT_HOME.replace("%HOME%", home.getName()));
                return true;
            }

            return true;
        }
        return false;
    }
}
