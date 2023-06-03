package org.howtoswat.handlers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.commands.DisableItemCommand;
import org.howtoswat.enums.Grenade;
import org.howtoswat.enums.Items;
import org.howtoswat.utils.AdminUtils;

import java.util.HashMap;
import java.util.UUID;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class GrenadeHandler implements Listener {

    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final HashMap<UUID, Integer> cooldowntimes = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public static void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
            ItemStack item = event.getItemDrop().getItemStack();
            if (item != null) {
                for (Grenade grenade : Grenade.values()) {
                    Items items = grenade.getItem();
                    if (items.getItem().getType() == item.getType()) {
                        if (item.getItemMeta().getDisplayName().equals(items.getItem().getItemMeta().getDisplayName())) {
                            if (DisableItemCommand.disabled.contains(item.getItemMeta().getDisplayName())) {
                                player.sendMessage(DisableItemCommand.PREFIX + "Das Item " + ChatColor.AQUA + StringUtils.capitalize(items.getName()) + ChatColor.BLUE + " ist deaktiviert!");
                                if (!AdminUtils.isAdmin(player.getUniqueId().toString())) {
                                    return;
                                }
                            }
                        }
                        cooldowntimes.putIfAbsent(player.getUniqueId(), 0);
                        if (cooldowns.containsKey(player.getUniqueId())) {
                            long secondsLeft = cooldowns.get(player.getUniqueId()) + cooldowntimes.get(player.getUniqueId()) - System.currentTimeMillis();
                            if (secondsLeft > 0L) {
                                event.setCancelled(true);
                                return;
                            }
                        }
                        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                        Item thrown = event.getItemDrop();
                        Vector velocity = player.getLocation().getDirection();
                        thrown.setVelocity(velocity.multiply(1.2D));

                        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> {
                            for (Entity entity : thrown.getNearbyEntities(10, 10, 10)) {
                                if (entity instanceof LivingEntity) {
                                    LivingEntity living = (LivingEntity) entity;
                                    if (living instanceof Player) {
                                        if (GunHandler.hasSpawnschutz(((Player) living).getPlayer())) {
                                            continue;
                                        }
                                    }
                                    int distance = (int) (Math.ceil(living.getLocation().distance(thrown.getLocation())) / 2);
                                    if (distance < 1) distance = 1;
                                    living.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200 / distance, 0));
                                    living.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400 / distance, 0));
                                }
                            }

                            thrown.remove();
                        }, 60L);

                        cooldowntimes.put(player.getUniqueId(), grenade.getCooldown());
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            ItemStack item = event.getItem();
            if (item != null) {
                for (Grenade grenade : Grenade.values()) {
                    Items items = grenade.getItem();
                    if (items.getItem().getType() == item.getType()) {
                        if (item.getItemMeta().getDisplayName().equals(items.getItem().getItemMeta().getDisplayName())) {
                            if (DisableItemCommand.disabled.contains(item.getItemMeta().getDisplayName())) {
                                if (!AdminUtils.isAdmin(player.getUniqueId().toString())) {
                                    return;
                                }
                            }
                        }
                        cooldowntimes.putIfAbsent(player.getUniqueId(), 0);
                        if (cooldowns.containsKey(player.getUniqueId())) {
                            long secondsLeft = cooldowns.get(player.getUniqueId()) + cooldowntimes.get(player.getUniqueId()) - System.currentTimeMillis();
                            if (secondsLeft > 0L) {
                                event.setCancelled(true);
                                return;
                            }
                        }
                        player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
                        onDrop(new PlayerDropItemEvent(player, player.getWorld().dropItem(player.getLocation(), item.asOne())));
                    }
                }
            }
        }
    }
}
