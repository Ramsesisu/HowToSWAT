package org.howtoswat.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.howtoswat.utils.AdminUtils;

public class TeleportHandler implements Listener {

    @EventHandler
    public static void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            if (!AdminUtils.isSupporter(player.getUniqueId().toString())) {
                event.setCancelled(true);
            }
        }
    }
}
