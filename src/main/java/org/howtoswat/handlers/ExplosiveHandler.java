package org.howtoswat.handlers;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.howtoswat.enums.Gun;

import java.util.Objects;

public class ExplosiveHandler implements Listener {

    @EventHandler
    public static void onExplode(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public static void onRocket(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            for (Gun gun : Gun.values()) {
                if (Objects.equals(gun.getItem().getName(), event.getEntity().getCustomName())) {
                    if (gun.isExplosive()) {
                        explode(event.getEntity(), event.getHitBlock().getLocation(), 10);
                    }
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    public static void explode(Entity damagee, Location loc, int power) {
        int range = power / 2;
        for (Entity entity : loc.getNearbyEntities(range, range, range)) {
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;

                living.setLastDamageCause(new EntityDamageEvent(damagee, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 0));
                if (!living.isInvulnerable()) {
                    if (living instanceof Player) {
                        if (GunHandler.hasSpawnschutz(((Player) living).getPlayer())) continue;
                        if (((Player) living).getGameMode() != GameMode.SURVIVAL) continue;
                    }
                    living.setHealth(0);
                }
            }
        }

        loc.createExplosion(power, false, false);

        range = power * 2;
        for (LivingEntity living : loc.getNearbyLivingEntities(range)) {
            double distance = loc.distance(living.getLocation());
            if (living instanceof Player) {
                if (GunHandler.hasSpawnschutz(((Player) living).getPlayer())) continue;
            }
            living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) (1400 / distance), (int) (20 / distance)));
        }
    }

    @EventHandler
    public static void onExplode(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            if (event.getEntity() instanceof Player) {
                if (GunHandler.hasSpawnschutz(((Player) event.getEntity()).getPlayer())) {
                    event.setCancelled(true);
                }
            }
            if (event.getEntity() instanceof Item) {
                event.setCancelled(true);
            }
        }
    }
}
