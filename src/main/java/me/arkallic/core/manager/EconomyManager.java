package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.data.EconomyData;
import me.arkallic.core.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private final Map<Core, EconomyData> economyMap = new HashMap<>();
    private final Core core;

    public EconomyManager(Core core) {
        this.core = core;
    }

    public void initialize() {
        EconomyData economyData = new EconomyData(core);
        economyMap.putIfAbsent(core, economyData);
        MessageUtil.log(String.format("Economy loaded, Server balance: %s coins.", economyData.getCoins()));
    }

    public EconomyData getEconomy() {
       return this.economyMap.get(core);
    }
}
