package me.arkallic.core.model;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Shop {

    private final ItemStack itemStack;
    private final int sellPrice;
    private final int buyPrice;
    private final Location location;

    public Shop(ItemStack itemStack, int buyPrice, int sellPrice, Location location) {
        this.itemStack = itemStack;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.location = location;
    }

    public ItemStack getItemStack() {
        ItemStack clone = new ItemStack(itemStack);
        return clone;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public Location getLocation() {
        return location;
    }
}
