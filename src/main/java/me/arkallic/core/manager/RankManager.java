package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.model.Rank;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RankManager {
    private final Map<String, Rank> rankList = new HashMap<>();
    private final Core core;
    
    public RankManager(Core core) {
        this.core = core;
    }

    public void initialize() {
        for (String s : core.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
            ConfigurationSection rankSection = core.getConfig().getConfigurationSection("Ranks." + s);
            Rank rank = new Rank(s, rankSection.getString("Description"), rankSection.getString("Prefix"), rankSection.getInt("Hierarchy"), rankSection.getBoolean("Default"));
            rankList.put(rank.getName(), rank);
        }
    }

    public Collection<Rank> getRanks() {
        return rankList.values();
    }

    public Rank getRank(String rank) {
        return rankList.get(rank);
    }
}
