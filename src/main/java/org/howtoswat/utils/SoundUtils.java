package org.howtoswat.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void playLocalSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void playGlobalSound(Location loc, Sound sound, int volume, float pitch) {
        for (Entity entity : loc.getNearbyEntities(volume, volume, volume)) {
            if (entity instanceof Player) {
                Player player = ((Player) entity).getPlayer();
                player.playSound(loc, sound, (float) (volume / loc.distance(player.getLocation())), pitch);
            }
        }
    }
}
