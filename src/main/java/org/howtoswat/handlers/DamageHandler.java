package org.howtoswat.handlers;

import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.howtoswat.enums.Gun;
import org.howtoswat.enums.Melee;
import org.howtoswat.utils.SoundUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class DamageHandler implements Listener {

    @EventHandler
    public static void onImpact(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();

            if (event.getDamager() instanceof Arrow) {
                if (GunHandler.hasSpawnschutz(player)) {
                    event.setCancelled(true);
                    return;
                }

                Entity bullet = event.getDamager();
                for (Gun gun : Gun.values()) {
                    if (Objects.equals(gun.getItem().getName(), bullet.getCustomName())) {
                        event.setDamage(gun.getDamage());
                    }
                }

                SoundUtils.playLocalSound(((Player) ((Arrow) event.getDamager()).getShooter()), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

                ItemStack chestplate = player.getInventory().getChestplate();
                if (chestplate != null) {
                    if (chestplate.getType() == Material.LEATHER_CHESTPLATE) {
                        event.setDamage(1);
                        chestplate.setDurability((short) (chestplate.getDurability() + 3));
                    }
                }

                KillHandler.lastdamage.put(player.getUniqueId(), bullet.getCustomName());

                ((CraftLivingEntity) player).getHandle().getDataWatcher().set(new DataWatcherObject<>(10, DataWatcherRegistry.b),-1);
            }
        }
    }

    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final HashMap<UUID, Integer> cooldowntimes = new HashMap<>();

    @EventHandler
    public static void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();

                if (GunHandler.hasSpawnschutz(damager)) GunHandler.endSpawnschutz(damager);
                if (GunHandler.hasSpawnschutz(player)) {
                    event.setCancelled(true);
                    return;
                }

                ItemStack chestplate = player.getInventory().getChestplate();
                if (chestplate != null) {
                    if (chestplate.getType() == Material.LEATHER_CHESTPLATE) {
                        chestplate.setDurability((short) (chestplate.getDurability() + 1));
                    }
                }

                if (damager.getInventory().getItemInMainHand() != null) {
                    ItemStack item = damager.getInventory().getItemInMainHand();
                    for (Melee melee : Melee.values()) {
                        if (item.getType() == melee.getItem().getItem().getType()) {
                            cooldowntimes.putIfAbsent(player.getUniqueId(), 0);
                            if (cooldowns.containsKey(player.getUniqueId())) {
                                long secondsLeft = cooldowns.get(player.getUniqueId()) + cooldowntimes.get(player.getUniqueId()) - System.currentTimeMillis();
                                if (secondsLeft > 0L) {
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                            event.setDamage(melee.getDamage());

                            String[] lore = Objects.requireNonNull(item.getLore()).toArray()[0].toString().replace(ChatColor.GOLD + "", "").replace(ChatColor.DARK_GRAY + "", "").split("/");
                            int durability = Integer.parseInt(lore[0]);
                            int maxdurability = Integer.parseInt(lore[1]);
                            String dura = ChatColor.translateAlternateColorCodes('&', "&6" + (durability - 1) + "&8/&6" + maxdurability);
                            item.setLore(Collections.singletonList(dura));

                            if (player.isBlocking()) {
                                player.damage(melee.getDamage());
                            }

                            KillHandler.lastdamage.put(player.getUniqueId(), melee.getItem().getName());

                            cooldowntimes.put(player.getUniqueId(), melee.getCooldown());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
