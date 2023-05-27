package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SPAWN" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_PURPLE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            Location loc = player.getLocation();

            player.setBedSpawnLocation(loc.add(0, 1, 0), true);

            player.sendMessage(PREFIX + "Dein Spawnpunkt wurde auf " + ChatColor.LIGHT_PURPLE + loc.getBlockX() + ChatColor.DARK_PURPLE + ", " + ChatColor.LIGHT_PURPLE + (loc.getBlockY() - 1) + ChatColor.DARK_PURPLE + ", " + ChatColor.LIGHT_PURPLE + loc.getBlockZ() + ChatColor.DARK_PURPLE + " gesetzt.");
        }
        return true;
    }
}
