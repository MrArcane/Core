package me.arkallic.core.data;

import me.arkallic.core.Core;
import me.arkallic.core.model.Shop;
import me.arkallic.core.util.MessageUtil;
import me.arkallic.core.wrappers.YMLFileWrapper;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class EconomyData extends YMLFileWrapper {

    private final Core core;

    public EconomyData(Core core) {
        super("", "Economy.yml", core);
        this.core = core;
    }

    public int getCoins() {
        return this.getConfig().getInt("Settings.Coins");
    }

    public void payServer(int amount) {
        this.getConfig().set("Settings.Coins", this.getCoins() + amount);
        MessageUtil.log("Server paid: " +amount+ " New balance: " + getCoins());
    }
    public void payServer(PlayerData playerData, int amount) {
        this.getConfig().set("Settings.Coins", this.getCoins() + amount);
        playerData.subtractCoins(amount);
        MessageUtil.log("Server paid: " +amount+ " New balance: " + getCoins());
    }
    public void payPlayer(PlayerData playerData, int amount) {
        this.getConfig().set("Settings.Coins", this.getCoins() - amount);
        playerData.addCoins(amount);
        MessageUtil.log("Server paid out: " +amount+ " coins. New balance: " + getCoins());
    }
    public Shop getShop(String s) {
        ConfigurationSection shopSection = this.getConfig().getConfigurationSection("Shops." + s);
        ItemStack itemStack = shopSection.getItemStack("Itemstack");
        Location location = shopSection.getLocation("Location");
        int buyPrice = shopSection.getInt("Buy-price");
        int sellPrice = shopSection.getInt("Sell-price");
        Shop shop = new Shop(itemStack, buyPrice, sellPrice, location);
        return shop;
    }
    public Set<String> getShops() {
        return this.getConfig().getConfigurationSection("Shops").getKeys(false);
    }

    public void saveShop(Shop shop) {
        Location loc = shop.getLocation();
        ItemStack itemStack = shop.getItemStack();

        if (this.getConfig().getString(itemStack.getType().name()) != null) {
            return;
        }
        String shopName = "Shops." + itemStack.getType().name() + ".";
        this.getConfig().set(shopName + "Itemstack", itemStack);
        this.getConfig().set(shopName + "Location", loc);
        this.getConfig().set(shopName + "Buy-price", shop.getBuyPrice());
        this.getConfig().set(shopName + "Sell-price", shop.getSellPrice());
    }

    public void deleteShop(Shop shop) {
        ItemStack itemStack = shop.getItemStack();
        this.getConfig().set("Shops."+itemStack.getType().name(), null);
    }
}
