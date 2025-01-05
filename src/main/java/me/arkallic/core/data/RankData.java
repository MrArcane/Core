package me.arkallic.core.data;

import me.arkallic.core.Core;
import me.arkallic.core.wrappers.YMLFileWrapper;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class RankData {

    private final Core core;
    private final YMLFileWrapper wrapper;

    public RankData(Core core) {
        this.core = core;
        this.wrapper = new YMLFileWrapper("", "Ranks.yml", core);
    }
    private FileConfiguration getConfig() {
        return this.wrapper.getConfig();
    }

    public void initialize() {
        if (!wrapper.getFile().exists()) {
            core.saveResource("Ranks.yml", false);
        }
    }

    public Set<String> getRanks() {
        return getConfig().getKeys(false);
    }

    public String getName(String s) {
        return getConfig().getConfigurationSection(s).getName();
    }

    public String getDescription(String s) {
        return getConfig().getConfigurationSection(s).getString("Description");
    }

    public String getPrefix(String s) {
        return getConfig().getConfigurationSection(s).getString("Prefix");
    }

    public int getHierarchy(String s) {
        return getConfig().getConfigurationSection(s).getInt("Hierarchy");
    }

    public boolean getDefault(String s) {
        return getConfig().getConfigurationSection(s).getBoolean("Default");
    }
}
