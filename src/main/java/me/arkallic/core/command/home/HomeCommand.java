package me.arkallic.core.command.home;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.data.LangData;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.model.Home;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.send;

public class HomeCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final LangData langData;

    public HomeCommand(PlayerDataManager playerDataManager, LangData langData) {
        this.playerDataManager = playerDataManager;
        this.langData = langData;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {


        if (sender instanceof Player p) {

            UUID uuid = p.getUniqueId();
            PlayerData pd = playerDataManager.getPlayerData(uuid);
            HashMap<String, Home> homes = pd.getHomes();

            if (pd.getCurrentHomes() == 0) {
                send(p, langData.noHomes);
                return true;
            }
            if (args.length == 0) {
                if (pd.getCurrentHomes() == 0 && homes.size() > 1) {
                    for (String s : homes.keySet()) {
                        Home home = pd.getHome(s);
                        Location loc = home.getLocation();
                        send(p, String.format("&7[&c%s&7]: (&4%s&7) X: &c%s &7Y: &c%s &7Z: &c%s", home.getName().toUpperCase(), loc.getWorld().getName().toUpperCase(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
                    }
                    send(p, "&cUSAGE: &7/home [NAME]");
                    return true;
                }
                for (String s : homes.keySet()) {
                    Home home = pd.getHome(s);
                    p.teleport(home.getLocation());
                    send(p,  langData.teleportHome.replace("%HOME%", home.getName()));
                    return true;
                }

            }

            if (args.length == 1) {
                String input = args[0].toLowerCase();
                Home home = pd.getHome(input);
                if (home == null) {
                    send(p,  langData.invalidHome);
                    return true;
                }
                p.teleport(home.getLocation());
                send(p,  langData.teleportHome.replace("%HOME%", home.getName()));
                return true;
            }

            return true;
        }
        return false;
    }
}
