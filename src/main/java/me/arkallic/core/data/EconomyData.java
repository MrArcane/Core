package me.arkallic.core.data;

import me.arkallic.core.Core;
import me.arkallic.core.util.MessageUtil;
import me.arkallic.core.wrappers.YMLFileWrapper;

public class EconomyData extends YMLFileWrapper {

    private final Core core;

    public EconomyData(Core core) {
        super("", "Economy.yml", core);
        this.core = core;
    }

    public int getCoins() {
        return this.getConfig().getInt("Settings.Coins");
    }
    public void pay(int amount) {
        this.getConfig().set("Settings.Coins", this.getCoins() + amount);
        MessageUtil.log("Server paid: " +amount+ " New balance: " + getCoins());
    }
    public void payOut(int amount) {
        this.getConfig().set("Settings.Coins", this.getCoins() - amount);
        MessageUtil.log("Server paid out: " +amount+ " coins. New balance: " + getCoins());
    }
}
