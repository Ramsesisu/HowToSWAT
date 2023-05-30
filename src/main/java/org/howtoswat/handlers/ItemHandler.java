package org.howtoswat.handlers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.commands.DisableItemCommand;
import org.howtoswat.enums.Gun;
import org.howtoswat.enums.Items;
import org.howtoswat.utils.AdminUtils;

public class ItemHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public static void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null) {
            for (Items items : Items.values()) {
                if (item.getType() == items.getItem().getType()) {
                    if (item.getItemMeta().getDisplayName().equals(items.getItem().getItemMeta().getDisplayName())) {
                        if (DisableItemCommand.disabled.contains(item.getItemMeta().getDisplayName())) {
                            player.sendMessage(DisableItemCommand.PREFIX + "Das Item " + ChatColor.AQUA + StringUtils.capitalize(items.getName()) + ChatColor.BLUE + " ist deaktiviert!");
                            if (!AdminUtils.isAdmin(player.getUniqueId().toString())) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onKevlar(InventoryClickEvent event) {
        if (event.getSlot() == 38) {
            ItemStack item = event.getCursor();
            if (item != null) {
                for (Items items : Items.values()) {
                    if (item.getType() == items.getItem().getType()) {
                        if (item.getItemMeta().getDisplayName().equals(items.getItem().getItemMeta().getDisplayName())) {
                            if (DisableItemCommand.disabled.contains(item.getItemMeta().getDisplayName())) {
                                if (event.getWhoClicked() instanceof Player) {
                                    Player player = (Player) event.getWhoClicked();
                                    player.sendMessage(DisableItemCommand.PREFIX + "Das Item " + ChatColor.AQUA + StringUtils.capitalize(items.getName()) + ChatColor.BLUE + " ist deaktiviert!");
                                    if (!AdminUtils.isAdmin(player.getUniqueId().toString())) {
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public static void onDrop(PlayerDropItemEvent event) {
        if (!BuildmodeCommand.buildmode.contains(event.getPlayer().getUniqueId())) {
            for (Items item : Items.values()) {
                if (event.getItemDrop().getItemStack().getType() == item.getItem().getType()) {
                    if (!item.isDroppable()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onDamage(PlayerItemDamageEvent event) {
        for (Gun gun : Gun.values()) if (gun.getItem().getItem().getType() == event.getItem().getType()) event.setCancelled(true);
    }

    @EventHandler
    public static void onPickup(EntityPickupItemEvent event) {
        if (!BuildmodeCommand.buildmode.contains(event.getEntity().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public static void onMerge(ItemMergeEvent event) {
        if (event.getEntity().getItemStack().getType() == Material.SKULL_ITEM) event.setCancelled(true);
    }

    @EventHandler
    public static void onSwitch(PlayerSwapHandItemsEvent event) {
        if (!BuildmodeCommand.buildmode.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onOffHand(InventoryClickEvent event) {
        if (event.getSlot() == 40) {
            Inventory inventory = event.getClickedInventory();
            if (inventory.getHolder() instanceof Player) {
                Player player = (Player) inventory.getHolder();
                if (!BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
