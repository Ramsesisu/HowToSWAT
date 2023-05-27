package org.howtoswat.handlers;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.enums.Gun;
import org.howtoswat.utils.SoundUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class GunHandler implements Listener {

    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    public static final HashMap<UUID, Integer> cooldowntimes = new HashMap<>();

    @EventHandler
    public static void onClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            if (player.getGameMode() == GameMode.SURVIVAL) {
                for (Gun gun : Gun.values()) {
                    ItemStack item = event.getItem();
                    if (item != null) {
                        if (gun.getItem().getItem().getType() == item.getType()) {
                            cooldowntimes.putIfAbsent(player.getUniqueId(), 0);
                            if (cooldowns.containsKey(player.getUniqueId())) {
                                long secondsLeft = cooldowns.get(player.getUniqueId()) + cooldowntimes.get(player.getUniqueId()) - System.currentTimeMillis();
                                if (secondsLeft > 0L) {
                                    player.sendActionBar(ChatColor.GRAY + "Du kannst diese Waffe gerade nicht benutzen...");
                                    return;
                                }
                            }
                            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                            String[] lore = Objects.requireNonNull(item.getLore()).toArray()[0].toString().replace(ChatColor.GOLD + "", "").replace(ChatColor.DARK_GRAY + "", "").split("/");
                            int ammo = Integer.parseInt(lore[0]);
                            int maxammo = Integer.parseInt(lore[1]);

                            if (ammo == 0) {
                                SoundUtils.playLocalSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 1F, 0F);

                                ammo = gun.getAmmo();
                                if (maxammo != 0) {
                                    if (maxammo - ammo < 0) {
                                        ammo = maxammo;
                                    }

                                    String ammos = ChatColor.translateAlternateColorCodes('&', "&6" + ammo + "&8/&6" + (maxammo - ammo));
                                    item.setLore(Collections.singletonList(ammos));
                                    player.sendActionBar(ammos);

                                    cooldowntimes.put(player.getUniqueId(), gun.getReloadCooldown());
                                }
                                return;
                            }

                            SoundUtils.playLocalSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 0.5F, 0F);
                            if (gun.isExplosive()) {
                                SoundUtils.playGlobalSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 100, gun.getPitch());
                            } else {
                                SoundUtils.playGlobalSound(player.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 100, gun.getPitch());
                            }

                            Arrow bullet = player.launchProjectile(Arrow.class, player.getLocation().getDirection());
                            bullet.setGravity(false);
                            bullet.setVelocity(bullet.getVelocity().multiply(gun.getVelocity()));
                            bullet.setPickupStatus(Arrow.PickupStatus.DISALLOWED);

                            bullet.setCustomName(gun.getItem().getName());

                            String ammos = ChatColor.translateAlternateColorCodes('&', "&6" + (ammo - 1) + "&8/&6" + maxammo);
                            item.setLore(Collections.singletonList(ammos));
                            player.sendActionBar(ammos);

                            Location origin = player.getEyeLocation().add(0, 0.2, 0);
                            Vector direction = origin.getDirection();
                            Location loc = origin.add(direction);
                            player.spawnParticle(Particle.SMOKE_NORMAL, loc.add(direction.clone().multiply(0.5D)), 1, 0.05D, 0.05D, 0.05D, 0.0D);

                            cooldowntimes.put(player.getUniqueId(), gun.getCooldown());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
            for (Gun gun : Gun.values()) {
                ItemStack item = event.getItemDrop().getItemStack();
                if (gun.getItem().getItem().getType() == item.getType()) {
                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                    String[] lore = Objects.requireNonNull(item.getLore()).toArray()[0].toString().replace(ChatColor.GOLD + "", "").replace(ChatColor.DARK_GRAY + "", "").split("/");
                    int ammo = Integer.parseInt(lore[0]);
                    int maxammo = Integer.parseInt(lore[1]);

                    if (ammo < gun.getAmmo()) {
                        SoundUtils.playLocalSound(player, Sound.ITEM_FLINTANDSTEEL_USE, 1F, 0F);

                        int diffammo = gun.getAmmo() - ammo;
                        if (maxammo != 0) {
                            if (maxammo - diffammo < 0) {
                                diffammo = maxammo;
                            }

                            String ammos = ChatColor.translateAlternateColorCodes('&', "&6" + (ammo + diffammo) + "&8/&6" + (maxammo - diffammo));
                            item.setLore(Collections.singletonList(ammos));
                            player.sendActionBar(ammos);

                            long secondsLeft = cooldowns.get(player.getUniqueId()) + cooldowntimes.get(player.getUniqueId()) - System.currentTimeMillis();
                            if (secondsLeft < gun.getReloadCooldown()) cooldowntimes.put(player.getUniqueId(), gun.getReloadCooldown());
                        }
                    }
                }
            }
        }
    }
}
