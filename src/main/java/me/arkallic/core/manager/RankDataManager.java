package me.arkallic.core.manager;

import me.arkallic.core.data.RankData;
import me.arkallic.core.model.Rank;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RankDataManager {
    private final Map<String, Rank> rankList = new HashMap<>();
    private final RankData rankData;
    
    public RankDataManager(RankData rankData) {
        this.rankData = rankData;
    }

    public void initialize() {
        for (String s : rankData.getRanks()) {

            String desc = rankData.getDescription(s);
            String prefix = rankData.getPrefix(s);
            int hierarchy = rankData.getHierarchy(s);
            boolean defaultRank = rankData.getDefault(s);

            Rank rank = new Rank(s, desc, prefix, hierarchy, defaultRank);
            rankList.put(rank.getName(), rank);
        }
    }

    public Rank getDefault() {
        for (String s : rankData.getRanks()) {
            Rank rank = getRank(s);
            if (rank.isDefault()) {
                return rank;
            }
        }
        return null;
    }

    public Collection<Rank> getRanks() {
        return rankList.values();
    }

    public Rank getRank(String rank) {
        return rankList.get(rank);
    }
}
