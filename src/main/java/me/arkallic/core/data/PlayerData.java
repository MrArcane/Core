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


public class PlayerData extends YMLFileWrapper {

    private final HashMap<String, Home> homes = new HashMap<>();

    public PlayerData(UUID uuid, Core core) {
        super("players", uuid + ".yml", core);
        loadHomes();
    }



    public void setDefaultData(UUID uuid, String rank, int maxHomes, int currency) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        if (!this.exists() || getConfig().getKeys(false).isEmpty()) {

            if (!getConfig().contains(defaultData.DISPLAYNAME)) {
                getConfig().set(defaultData.DISPLAYNAME, player.getName());
            }
            if (!getConfig().contains(defaultData.RANK)) {
                getConfig().set(defaultData.RANK, rank);
            }
            if (!getConfig().contains(defaultData.MAX_HOMES)) {
                getConfig().set(defaultData.MAX_HOMES, maxHomes);
            }
            if (!getConfig().contains(defaultData.currency)) {
                getConfig().set(defaultData.currency, currency);
            }
        }
    }

    private void loadHomes() {

        if (getConfig().getConfigurationSection(defaultData.HOMES) == null) {
            return;
        }

        ConfigurationSection homeSection = this.getConfig().getConfigurationSection(defaultData.HOMES);
        for (String s : homeSection.getKeys(false)) {
            Home home = new Home(s, homeSection.getLocation(s));
            this.homes.putIfAbsent(home.getName(), home);
        }
    }

    public interface defaultData {
        String RANK = "Settings.Rank";
        String MAX_HOMES = "Settings.Max-homes";
        String HOMES = "Homes.";
        String DISPLAYNAME = "Settings.Display-name";
        String currency = "Settings.Currency";
    }

    public boolean exists() {
        return this.getFile().exists();
    }


    public int getCurrency() {
        return getConfig().getInt(defaultData.currency);
    }
    public void setCurrency(int amount) {
        getConfig().set(defaultData.currency, amount);
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
        getConfig().set(defaultData.HOMES + name, home.getLocation());
    }
    public void deleteHome(Home home) {
        homes.remove(home.getName());
        getConfig().set(defaultData.HOMES + home.getName(), null);
    }
    public int HomeAmount() {
        return this.homes.size();
    }
    public boolean homeExists(String s) {
        MessageUtil.log(String.valueOf(this.homes.containsKey(s)));
        return this.homes.containsKey(s);
    }


    public int getMaxHomes() {

        if (!getConfig().contains(defaultData.MAX_HOMES)) {
            return 0;
        }

        return this.getConfig().getInt(defaultData.MAX_HOMES);
    }
    public void setMaxHomes(int amount) {
        this.getConfig().set(defaultData.MAX_HOMES, amount);
    }



    public String getDisplayName() {
        return this.getConfig().getString(defaultData.DISPLAYNAME);
    }
    public void setDisplayName(String name) {
        getConfig().set(defaultData.DISPLAYNAME, name);
    }


    /**
     * to-do for ranks
     * finish hierarchy system
     * add rankup system
     */
    public String getRank() {
        return getConfig().getString(defaultData.RANK);
    }
    public void setRank(String name) {
        getConfig().set(defaultData.RANK + name, name);
    }


    public int getCurrentHomes() {

        if (getConfig().getConfigurationSection(defaultData.HOMES) == null) {
            return 0;
        }

        return getConfig().getConfigurationSection(defaultData.HOMES).getKeys(false).size();
    }
}


