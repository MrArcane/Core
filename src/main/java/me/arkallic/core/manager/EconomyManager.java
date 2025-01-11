package me.arkallic.core.manager;

import me.arkallic.core.Core;
import me.arkallic.core.data.EconomyData;
import me.arkallic.core.model.Shop;
import me.arkallic.core.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private final Map<Core, EconomyData> economyMap = new HashMap<>();
    private final Map<String, Shop> shopMap = new HashMap<>();
    private final Core core;

    public EconomyManager(Core core) {
        this.core = core;
    }

    public void initialize() {
        EconomyData economyData = new EconomyData(core);

        loadShops();

        MessageUtil.log(String.format("Economy loaded, Server balance: %s coins.", economyData.getCoins()));
    }

    public void loadShops() {
        EconomyData economyData = new EconomyData(core);
        economyMap.putIfAbsent(core, economyData);

        for (String s : economyData.getShops()) {
            shopMap.computeIfAbsent(s, _ -> this.getEconomy().getShop(s));
            MessageUtil.log(s);
        }
    }

    public EconomyData getEconomy() {
       return this.economyMap.get(core);
    }

    public Map<String, Shop> getShops() {
        return this.shopMap;
    }


}
