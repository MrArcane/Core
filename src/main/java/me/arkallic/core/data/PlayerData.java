package me.arkallic.core.data;

import me.arkallic.core.Core;
import me.arkallic.core.model.Home;
import me.arkallic.core.util.MessageUtil;
import me.arkallic.core.wrappers.YMLFileWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.core.util.MessageUtil.send;


public class PlayerData extends YMLFileWrapper {

    private final HashMap<String, Home> homes = new HashMap<>();

    public PlayerData(UUID uuid, Core core) {
        super("players", uuid + ".yml", core);
        loadHomes();
    }



    public void setDefaultData(UUID uuid, int maxHomes, int coins) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (!this.exists() || getConfig().getKeys(false).isEmpty()) {

            if (!getConfig().contains(defaultData.displayname)) {
                getConfig().set(defaultData.displayname, player.getName());
            }
            if (!getConfig().contains(defaultData.max_homes)) {
                getConfig().set(defaultData.max_homes, maxHomes);
            }
            if (!getConfig().contains(defaultData.coins)) {
                getConfig().set(defaultData.coins, coins);
            }
        }
    }

    private void loadHomes() {

        if (getConfig().getConfigurationSection(defaultData.homes) == null) {
            return;
        }

        ConfigurationSection homeSection = this.getConfig().getConfigurationSection(defaultData.homes);
        for (String s : homeSection.getKeys(false)) {
            Home home = new Home(s, homeSection.getLocation(s));
            this.homes.putIfAbsent(home.getName(), home);
        }
    }

    public interface defaultData {
        String max_homes = "Settings.Max-homes";
        String homes = "Homes.";
        String displayname = "Settings.Display-name";
        String coins = "Settings.Coins";
    }

    public boolean exists() {
        return this.getFile().exists();
    }


    public int getCoins() {
        return getConfig().getInt(defaultData.coins);
    }
    public void setCoins(int amount, UUID uuid, String message) {
        send(Bukkit.getPlayer(uuid), message);
        getConfig().set(defaultData.coins, amount);
    }
    public void subtractCoins(int amount) {
        int finalCoins = getCoins() - amount;
        getConfig().set(defaultData.coins, finalCoins);
    }
    public void addCoins(int amount) {
        int finalCoins = getCoins() + amount;
        getConfig().set(defaultData.coins, finalCoins);
    }

    public void setCoins(int amount) {
        getConfig().set(defaultData.coins, amount);
    }



    public HashMap<String, Home> getHomes() {
        return homes;
    }

    public Home getHome(String name) {
        return homes.get(name);
    }

    public void setHome(String name, Location location) {
        Home home = new Home(name, location);
        homes.putIfAbsent(name, home);
        getConfig().set(defaultData.homes + name, home.getLocation());
    }
    public void deleteHome(Home home) {
        homes.remove(home.getName());
        getConfig().set(defaultData.homes + home.getName(), null);
    }

    public boolean homeExists(String s) {
        return this.homes.containsKey(s);
    }

    public int getMaxHomes() {

        if (!getConfig().contains(defaultData.max_homes)) {
            return 0;
        }

        return this.getConfig().getInt(defaultData.max_homes);
    }
    public void setMaxHomes(int amount) {
        this.getConfig().set(defaultData.max_homes, amount);
    }



    public String getDisplayName() {
        return this.getConfig().getString(defaultData.displayname);
    }
    public void setDisplayName(String name) {
        getConfig().set(defaultData.displayname, name);
    }


    public int getCurrentHomes() {

        if (getConfig().getConfigurationSection(defaultData.homes) == null) {
            return 0;
        }

        return getConfig().getConfigurationSection(defaultData.homes).getKeys(false).size();
    }
}


