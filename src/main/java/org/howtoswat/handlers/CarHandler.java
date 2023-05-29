package org.howtoswat.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.howtoswat.commands.CarCommand;

import java.util.Objects;

public class CarHandler implements Listener {

    @EventHandler
    public void onEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Player player = (Player) event.getEntered();
            Player owner = Bukkit.getServer().getPlayer(event.getVehicle().getCustomName());
            if (owner == null) {
                event.getVehicle().remove();
                event.setCancelled(true);
                return;
            }
            if (!Objects.equals(player.getName(), Objects.requireNonNull(owner).getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            Player player = (Player) event.getExited();
            if (CarCommand.cartasks.get(player.getUniqueId()) != null) {
                CarCommand.cartasks.get(player.getUniqueId()).cancel();
            }
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    @EventHandler
    public void onCarHit(VehicleDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCarCollision(VehicleEntityCollisionEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getVehicle().getPassengers().contains(Bukkit.getServer().getPlayer(event.getVehicle().getCustomName()))) {
                if (event.getEntity() != Bukkit.getServer().getPlayer(event.getVehicle().getCustomName())) {
                    Player player = (Player) event.getEntity();
                    player.setVelocity(Bukkit.getServer().getPlayer(event.getVehicle().getCustomName()).getLocation().getDirection().multiply(3));
                    player.damage(14);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 8, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 8, 1));
                }
            }
        }
    }
}
