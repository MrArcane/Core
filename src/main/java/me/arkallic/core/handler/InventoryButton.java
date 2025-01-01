package me.arkallic.core.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class InventoryButton {

    private final ItemStack icon;

    public InventoryButton(ItemStack icon) {
        this.icon = icon;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public abstract void onClick(InventoryClickEvent event);
}
