package org.howtoswat.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.enums.Gun;
import org.howtoswat.enums.Items;

public class ItemHandler implements Listener {

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
