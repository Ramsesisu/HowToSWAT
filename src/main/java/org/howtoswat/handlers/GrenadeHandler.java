package org.howtoswat.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.howtoswat.commands.BuildmodeCommand;
import org.howtoswat.enums.Grenade;

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
            for (Grenade grenade : Grenade.values()) {
                if (grenade.getItem().getItem().getType() == event.getItemDrop().getItemStack().getType()) {
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
                                int distance = (int) (Math.ceil(living.getLocation().distance(thrown.getLocation())) / 2);
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
