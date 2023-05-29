package org.howtoswat.handlers;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
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
        if (event.getEntity() instanceof Arrow) {
            event.getEntity().remove();

            Block block = event.getHitBlock();
            PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(block.hashCode(), new BlockPosition(block.getX(), block.getY(), block.getZ()), 3);
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
