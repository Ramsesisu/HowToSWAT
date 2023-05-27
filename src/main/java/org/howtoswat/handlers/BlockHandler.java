package org.howtoswat.handlers;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.howtoswat.commands.BuildmodeCommand;

public class BlockHandler implements Listener {

    @EventHandler
    public static void onBreak(BlockBreakEvent event) {
        if (!BuildmodeCommand.buildmode.contains(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public static void onPlace(BlockPlaceEvent event) {
        if (!BuildmodeCommand.buildmode.contains(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public static void onInpact(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) event.getEntity().remove();
    }
}
