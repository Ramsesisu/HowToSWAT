package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BombeCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "BOMBE" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static final HashMap<UUID, Location> bombs = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            Block block = player.getWorld().getBlockAt(player.getLocation());

            if (bombs.containsKey(player.getUniqueId())) {
                Location loc = bombs.get(player.getUniqueId());
                loc.getWorld().getBlockAt(loc).setType(Material.AIR);

                bombs.remove(player.getUniqueId());

                player.sendMessage(PREFIX + "Du hast die Bombe bei " + ChatColor.RED + loc.getBlockX() + ChatColor.GRAY + ", " + ChatColor.RED + loc.getBlockY() + ChatColor.GRAY + ", " + ChatColor.RED + loc.getBlockZ() + ChatColor.GRAY + " entfernt.");
            } else {
                if (!block.getType().isSolid()) {
                    block.setType(Material.TNT);

                    bombs.put(player.getUniqueId(), block.getLocation());

                    player.sendMessage(PREFIX + "Du hast eine Bombe bei " + ChatColor.RED + block.getX() + ChatColor.GRAY + ", " + ChatColor.RED + block.getY() + ChatColor.GRAY + ", " + ChatColor.RED + block.getZ() + ChatColor.GRAY + " gelegt.");
                } else {
                    player.sendMessage(PREFIX + "Hier kannst du keine Bombe platzieren!");
                }
            }
        }
        return true;
    }
}
