package me.mrarcane.core.utils;

import me.mrarcane.core.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.mrarcane.core.utils.ChatUtils.color;
import static me.mrarcane.core.utils.ChatUtils.sendChat;

/**
 * File generated by: MrArcane
 * 2/12/2018
 **/
public class PlayerUtils extends YamlConfiguration {

    public static HashMap<Player, ItemStack[]> inv = new HashMap<>();
    public static HashMap<Player, ItemStack[]> armorInv = new HashMap<>();
    public static Map<Player, Location> locationMap = new HashMap<>();
    private static FileConfiguration cfg = Main.getInstance().getConfig();
    private String player;

    public PlayerUtils(String player){
        this.player = player + (player.endsWith(".yml") ? "" : ".yml");
        createFile();
    }
    private void createFile() {
        try {
            File file = new File(Main.getInstance().getDataFolder() + "/Players/", player);
            if (!file.exists()){
                if (Main.getInstance().getResource(player) != null){
                    Main.getInstance().saveResource(player, false);
                } else {
                    save(file);
                }
            } else {
                load(file);
                save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void save(){
        try {
            save(new File(Main.getInstance().getDataFolder() + "/Players/", player));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static String timePlayer(Long time) {
        Long now = System.currentTimeMillis();
        Long date = now - time;
        String fulldate;

        long seconds = date / 1000 % 60;
        long minutes = date / (60 * 1000) % 60;
        long hours = date / (60 * 60 * 1000) % 24;
        long days = date / (24 * 60 * 60 * 1000);

        return fulldate = days+"d, "+hours+"h, "+minutes+"m, "+seconds+"s";
    }
    public static void getColor(Player p) {
        int count = 0;
        PermissionUser u = PermissionsEx.getUser(p);
        ConfigurationSection rSection = cfg.getConfigurationSection("Tablist");
        for (String rank : rSection.getKeys(false)) {
            if (u.inGroup(rank)) {
                count++;
            }
        }
        String rName[] = new String[count];
        int priority[] = new int[count];
        count = 0;
        for (String rank : rSection.getKeys(false)) {
            if (u.inGroup(rank)) {
                rName[count] = rank;
                priority[count] = rSection.getConfigurationSection(rank).getInt("priority");
                count++;
            }
        }
        if (count > 1) {
            int pri1 = priority[0];
            int pri2 = 0;
            int i = 1;
            while (i < count) {
                pri2 = priority[i];
                boolean b = comparePriorities(pri1, pri2);
                if (!b) {
                    pri1 = pri2;
                }
                i++;
            }
            for (int j = 0; j < priority.length; j++) {
                if (priority[j] == pri1) {
                    p.setPlayerListName(color(rSection.getConfigurationSection(rName[j]).getString("name").replace("{player}", p.getDisplayName())));
                }
            }
        } else {
            p.setPlayerListName(color(rSection.getConfigurationSection(rName[0]).getString("name").replace("{player}", p.getDisplayName())));
        }
    }
    private static boolean comparePriorities(int p1, int p2) {
        if (p1 > p2) {
            return true;
        } else {
            return false;
        }
    }
    public static void adminMode(Player p) {
        PlayerInventory pi = p.getInventory();
        if (inv.containsKey(p)) {
            p.teleport(locationMap.get(p));
            pi.setContents(inv.get(p));
            pi.setStorageContents(armorInv.get(p));
            p.setOp(false);
            inv.remove(p);
            armorInv.remove(p);
            locationMap.remove(p);
            p.setGameMode(GameMode.SURVIVAL);
            sendChat(p, "&6Switched to player mode.");
        } else if (!inv.containsKey(p)) {
            inv.put(p, pi.getContents());
            armorInv.put(p, pi.getStorageContents());
            locationMap.put(p, p.getLocation());
            pi.clear();
            p.setOp(true);
            sendChat(p, "&6Switched to admin mode.");
        }
    }
    public static void getMail(Player p) {
        PlayerUtils pd = new PlayerUtils(p.getUniqueId().toString());
        List<String> m = pd.getConfigurationSection("Player").getStringList("Mail");
        if (m.size() > 0) {
            sendChat(p, "&6You have mail! &7/mail read");
        } else {
            sendChat(p, "&6You have no new mail.");
        }
    }
}