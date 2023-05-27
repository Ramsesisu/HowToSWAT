package org.howtoswat.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.howtoswat.enums.Guns;

import java.util.Objects;

public class ExplosiveHandler implements Listener {

    @EventHandler
    public static void onRocket(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            for (Guns gun : Guns.values()) {
                if (Objects.equals(gun.getItem().getName(), event.getEntity().getCustomName())) {
                    if (gun.isExplosive()) {
                        Location loc = event.getHitBlock().getLocation();

                        for (Entity entity : loc.getNearbyEntities(5, 5, 5)) {
                            if (entity instanceof Player) {
                                Player player = ((Player) entity).getPlayer();

                                player.setLastDamageCause(new EntityDamageEvent(event.getEntity(), EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 0));
                                if (!player.isInvulnerable()) player.setHealth(0);
                            }
                        }

                        loc.createExplosion(12, false, false);

                        for (Entity entity : loc.getNearbyEntities(20, 20, 20)) {
                            if (entity instanceof Player) {
                                double distance = loc.distance(entity.getLocation());
                                ((Player) entity).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) (1400 / distance), (int) (20 / distance)));
                            }
                        }
                    }
                }
            }
        }
    }
}
