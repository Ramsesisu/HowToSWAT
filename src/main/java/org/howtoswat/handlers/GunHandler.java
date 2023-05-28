package org.howtoswat.handlers;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.enums.Gun;
import org.howtoswat.enums.Items;
import org.howtoswat.utils.AdminUtils;
import org.howtoswat.utils.SoundUtils;
import org.howtoswat.utils.VerifyUtils;

import java.util.*;

import static org.howtoswat.HowToSWAT.PLUGIN;

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
                            for (Items check : Items.values()) {
                                if (check.getItem().getType() == item.getType()) {
                                    if (check.needsVerify()) {
                                        if (!AdminUtils.isVerified(player.getUniqueId().toString())) {
                                            VerifyUtils.verifyMessage(player);
                                            return;
                                        }
                                    }
                                }
                            }
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
                            Bukkit.getScheduler().runTaskLater(PLUGIN, bullet::remove, 3 * 20L);

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

    // Kann optimiert werden:
    public static final HashMap<String, Long> flammilast = new HashMap<>();

    public static final HashMap<String, BukkitTask> flammireleasetask = new HashMap<>();

    public static final HashMap<String, BukkitTask> flammifinaltask = new HashMap<>();

    public static final HashMap<String, Boolean> flammiloading = new HashMap<>();

    public static final HashMap<String, Boolean> flammiloaded = new HashMap<>();

    public static final HashMap<String, Long> flammiloadingcounter = new HashMap<>();

    public static final HashMap<String, String> flammireloadmsg = new HashMap<>();

    @EventHandler
    public static void onFlamethrower(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            if (player.getGameMode() == GameMode.SURVIVAL) {
                if (event.getItem().getType() == Items.FLAMMENWERFER.getItem().getType()) {
                    if (player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getItemMeta().getDisplayName().contains("Sprenggürtel")) {
                        player.sendMessage(ChatColor.GRAY + "In dieser Situation darfst du nicht schießen!");
                    }
                    ItemStack flammi = player.getInventory().getItemInMainHand();
                    ItemMeta meta = flammi.getItemMeta();
                    String strlore = String.valueOf(meta.getLore());
                    String[] ammos = strlore.split("/");
                    ammos[0] = ammos[0].substring(3, ammos[0].length() - 2).replace("§", "");
                    int ammo = Integer.parseInt(ammos[0]);
                    if (ammo == 0) {
                        event.setCancelled(true);
                        return;
                    }
                    flammilast.putIfAbsent(player.getName(), 1L);
                    flammiloaded.putIfAbsent(player.getName(), Boolean.FALSE);
                    flammiloading.putIfAbsent(player.getName(), Boolean.FALSE);
                    flammiloadingcounter.putIfAbsent(player.getName(), 100L);
                    flammireleasetask.putIfAbsent(player.getName(), null);
                    flammifinaltask.putIfAbsent(player.getName(), null);
                    flammireloadmsg.putIfAbsent(player.getName(), ChatColor.translateAlternateColorCodes('&', "&8&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛"));
                    if (flammilast.get(player.getName()) > 0L) {
                        if (System.currentTimeMillis() - flammiloadingcounter.get(player.getName()) >= 1050L)
                            if (flammiloading.get(player.getName())) {
                                flammireloadmsg.put(player.getName(), flammireloadmsg.get(player.getName()).replaceFirst("8", "a"));
                                player.sendActionBar(flammireloadmsg.get(player.getName()));
                                flammiloadingcounter.put(player.getName(), flammilast.get(player.getName()));
                            } else if (flammiloaded.get(player.getName())) {
                                ArrayList<String> lore = new ArrayList<>();
                                String templore = ChatColor.translateAlternateColorCodes('&', "&6" + (ammo - 1) + "&8/&6" + 500);
                                lore.add(templore);
                                meta.setLore(lore);
                                flammi.setItemMeta(meta);
                                flammiloadingcounter.put(player.getName(), flammilast.get(player.getName()));
                            }
                        if (flammifinaltask.get(player.getName()) == null)
                            flammifinaltask.put(player.getName(), Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(PLUGIN, () -> {
                                if (flammiloading.get(player.getName()) && !(Boolean) flammiloaded.get(player.getName())) {
                                    flammiloaded.put(player.getName(), Boolean.TRUE);
                                    flammiloading.put(player.getName(), Boolean.FALSE);
                                    player.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&a⬛&a⬛&a⬛&a⬛&a⬛&a⬛&a⬛&a⬛&a⬛&a⬛"));
                                }
                                flammifinaltask.put(player.getName(), null);
                            }, 200L));
                        if (System.currentTimeMillis() - flammilast.get(player.getName()) <= 250L) {
                            if (flammireleasetask.get(player.getName()) != null && Bukkit.getServer().getScheduler().isQueued(flammireleasetask.get(player.getName()).getTaskId()))
                                Bukkit.getServer().getScheduler().cancelTask(flammireleasetask.get(player.getName()).getTaskId());
                            flammireleasetask.put(player.getName(), Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(PLUGIN, () -> {
                                flammireloadmsg.put(player.getName(), ChatColor.translateAlternateColorCodes('&', "&8&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛&8⬛"));
                                flammiloading.put(player.getName(), Boolean.FALSE);
                                flammiloaded.put(player.getName(), Boolean.FALSE);
                                if (flammifinaltask.get(player.getName()) != null && Bukkit.getServer().getScheduler().isQueued(flammifinaltask.get(player.getName()).getTaskId())) {
                                    Bukkit.getServer().getScheduler().cancelTask(flammifinaltask.get(player.getName()).getTaskId());
                                    flammifinaltask.put(player.getName(), null);
                                }
                            }, 40L));
                        } else {
                            flammiloading.put(player.getName(), Boolean.TRUE);
                        }
                    }
                    flammilast.put(player.getName(), System.currentTimeMillis());
                    if (flammiloaded.get(player.getName())) {
                        Location origin = player.getEyeLocation().subtract(0.0D, 0.5D, 0.0D);
                        Vector direction = origin.getDirection();
                        direction.normalize();
                        for (int i = 0; i < 20.0D; i++) {
                            Location loc = origin.add(direction);
                            loc.getWorld().spawnParticle(Particle.FLAME, loc.subtract(direction.clone().multiply(0.75D)), 1, 0.05D, 0.05D, 0.05D, 0.0D);
                        }
                        List<Entity> nearbyE = player.getNearbyEntities(5.0D, 5.0D, 5.0D);
                        ArrayList<LivingEntity> livingE = new ArrayList<>();
                        for (Entity e : nearbyE) {
                            if (e instanceof LivingEntity)
                                livingE.add((LivingEntity)e);
                        }
                        BlockIterator bItr = new BlockIterator(player, 5);
                        while (bItr.hasNext()) {
                            Block block = bItr.next();
                            int bx = block.getX();
                            int by = block.getY();
                            int bz = block.getZ();
                            for (LivingEntity e : livingE) {
                                Location loc = e.getLocation();
                                double ex = loc.getX();
                                double ey = loc.getY();
                                double ez = loc.getZ();
                                if (bx - 0.75D <= ex && ex <= bx + 1.75D && bz - 0.75D <= ez && ez <= bz + 1.75D && (by - 1) <= ey && ey <= by + 2.5D) {
                                    e.damage(1.0D);
                                    e.setFireTicks(100);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
