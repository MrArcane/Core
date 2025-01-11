package me.arkallic.core.listener;

import me.arkallic.core.data.PlayerData;
import me.arkallic.core.manager.EconomyManager;
import me.arkallic.core.manager.PlayerDataManager;
import me.arkallic.core.model.Shop;
import me.arkallic.core.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static me.arkallic.core.util.MessageUtil.*;

public class ShopListener implements Listener {

    private final PlayerDataManager playerDataManager;
    private final EconomyManager economyManager;
    private final HashMap<UUID, Long> lastInteractionTime = new HashMap<>();
    private static final long COOLDOWN_TIME = 500; // 500 ms cooldown

    public ShopListener(EconomyManager economyManager, PlayerDataManager playerDataManager) {
        this.economyManager = economyManager;
        this.playerDataManager = playerDataManager;
    }

    private void updateSign(Shop shop, SignChangeEvent signChangeEvent) {
        signChangeEvent.setLine(0, color("&e&m   &r&7[&aShop&7]&e&m   "));
        signChangeEvent.setLine(1, color(String.format("&a%s: &7%s", formatItemName(shop.getItemStack().getType()), shop.getItemStack().getAmount())));
        signChangeEvent.setLine(2, color(String.format("&4L: &eBuy &7%s Coins", shop.getBuyPrice())));
        signChangeEvent.setLine(3, color(String.format("&4R: &eSell &7%s Coins", shop.getSellPrice())));
        }

    private String formatItemName(Material material) {
        String name = material.name().toLowerCase().replace('_', ' ');
        return Arrays.stream(name.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private ItemStack parseItem(String line, Player player) {
        if (line == null || line.isEmpty()) {
            send(player, "&cItem line is empty! Use: <item> <amount>.");
            return null;
        }

        String[] parts = line.split(" ");
        if (parts.length != 2) {
            send(player, "&cInvalid format! Use: <item> <amount> on line 2.");
            return null;
        }

        Material material = Material.matchMaterial(parts[0]);
        if (material == null) {
            send(player, "&cInvalid item type: " + parts[0] + ". Use /itemlist for valid items.");
            return null;
        }

        try {
            int amount = Integer.parseInt(parts[1]);
            if (amount <= 0) {
                send(player, "&cAmount must be greater than 0.");
                return null;
            }
            if (amount > 64) {
                send(player, "&cAmount cannot exceed 64.");
                return null;
            }
            return new ItemStack(material, amount);
        } catch (NumberFormatException ex) {
            send(player, "&cInvalid amount format: " + parts[1]);
            MessageUtil.log("Failed to parse item amount: " + line);
            return null;
        }
    }

    private int parsePrice(String line, String type, Player player) {
        if (line == null || line.isEmpty()) {
            send(player, "&c" + type + " price line is empty! Please provide a valid integer.");
            return -1;
        }

        try {
            int price = Integer.parseInt(line);

            if (price < 0) {
                send(player, "&c" + type + " price must be a non-negative value.");
                return -1;
            }

            return price;
        } catch (NumberFormatException ex) {
            send(player, "&cInvalid " + type + " price format: " + line + ". Please use a valid integer.");
            log("Failed to parse price: " + line + " (" + type + ")");
            return -1;
        }
    }

    @EventHandler
    public void createShop(SignChangeEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        if (!player.hasPermission("core.shop.create")) {
            return;
        }

        if (!"shop".equalsIgnoreCase(e.getLine(0))) {
            return;
        }


            // Validate and parse input
            ItemStack itemStack = parseItem(e.getLine(1), player);
            if (itemStack == null) return;

            int buyPrice = parsePrice(e.getLine(2), "buy", player);
            if (buyPrice < 0) return;

            int sellPrice = parsePrice(e.getLine(3), "sell", player);
            if (sellPrice < 0) return;
            Shop newShop = new Shop(itemStack, buyPrice, sellPrice, block.getLocation());

            economyManager.getShops().put(newShop.getItemStack().getType().name(), newShop);
            economyManager.getEconomy().saveShop(newShop);

            updateSign(newShop, e);
            send(player, "&aShop created successfully!");
    }

    @EventHandler
    public void deleteShop(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        for (Shop shop : economyManager.getShops().values()) {

            if (block.getLocation().equals(shop.getLocation())) {
                economyManager.getEconomy().deleteShop(shop);
                economyManager.getShops().remove(shop.getItemStack().getType().name());
                send(player, "&aShop removed!");
                break;
            }
        }
    }

        @EventHandler
        public void useShop(PlayerInteractEvent e) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK) {
                return;
            }

            if (e.getHand() != EquipmentSlot.HAND) {
                return;
            }

            Player player = e.getPlayer();
            Block block = e.getClickedBlock();

            if (block == null || !(block.getState() instanceof Sign)) {
                return;
            }

            UUID playerUUID = player.getUniqueId();
            long currentTime = System.currentTimeMillis();
            if (lastInteractionTime.containsKey(playerUUID)) {
                long lastTime = lastInteractionTime.get(playerUUID);
                if (currentTime - lastTime < COOLDOWN_TIME) {
                    return;
                }
            }
            lastInteractionTime.put(playerUUID, currentTime);

            for (Shop shop : economyManager.getShops().values()) {
                if (block.getLocation().equals(shop.getLocation())) {
                    if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                        handleBuy(playerUUID, shop);
                    } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        handleSell(playerUUID, shop);
                    }
                    return;
                }
            }
        }

    private void handleBuy(UUID uuid, Shop shop) {
        Player player = Bukkit.getPlayer(uuid);

        PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
        if (playerData == null) {
            send(player, "&4Player data couldn't be retrieved. Please contact a staff member!");
            return;
        }

        ItemStack shopItemStack = shop.getItemStack();
        int amount = shopItemStack.getAmount();
        int buyPrice = shop.getBuyPrice();

        if (playerData.getCoins() < buyPrice) {
            send(player, "&4You don't have enough coins to make this purchase!");
            return;
        }

        Map<Integer, ItemStack> leftover = player.getInventory().addItem(new ItemStack(shopItemStack.getType(), amount));

        if (!leftover.isEmpty()) {
            leftover.values().forEach(item ->
                    player.getWorld().dropItemNaturally(player.getLocation(), item)
            );
            send(player, "&cYour inventory was full, so some items were dropped!");
        } else {
            economyManager.getEconomy().payServer(playerData, buyPrice);
            send(player, String.format("&aYou successfully purchased &7x%d &e%s&a!", amount, formatItemName(shopItemStack.getType())));
        }
    }

    private void handleSell(UUID uuid, Shop shop) {
        Player player = Bukkit.getPlayer(uuid);

        PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
        if (playerData == null) {
            send(player, "&4Player data couldn't be received, let a staff member know!");
            return;
        }

        // Validate shop is registered and legitimate
        if (!economyManager.getShops().containsValue(shop)) {
            send(player, "&cThis shop is invalid or no longer exists.");
            return;
        }

        // Ensure the server has enough coins
        if (economyManager.getEconomy().getCoins() < shop.getSellPrice()) {
            send(player, "&cSorry, the server doesn't have enough funds to buy this.");
            return;
        }

        // Validate player inventory has the required items
        ItemStack requiredItem = shop.getItemStack();
        int sellAmount = requiredItem.getAmount();

        int playerItemCount = player.getInventory().all(requiredItem.getType())
                .values()
                .stream()
                .mapToInt(ItemStack::getAmount)
                .sum();

        if (playerItemCount < sellAmount) {
            send(player, "&cYou don't have enough items to sell.");
            return;
        }

        // Remove items from the player's inventory
        int remainingToRemove = sellAmount;
        for (ItemStack item : player.getInventory().all(requiredItem.getType()).values()) {
            if (item.getAmount() <= remainingToRemove) {
                remainingToRemove -= item.getAmount();
                player.getInventory().remove(item);
            } else {
                item.setAmount(item.getAmount() - remainingToRemove);
                break;
            }
        }

        economyManager.getEconomy().payPlayer(playerData, shop.getSellPrice());
        send(player, "&aYou sold " + sellAmount + " " + requiredItem.getType().name() +
                " for " + shop.getSellPrice() + " coins.");
    }

    @EventHandler
    private void disableEdit(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        for (Shop shop : economyManager.getShops().values()) {
            if (block.getLocation().equals(shop.getLocation())) {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}

