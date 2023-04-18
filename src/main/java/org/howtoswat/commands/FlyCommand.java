package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "FLY" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA;
    private static final String PLAYERSUFFIX = ChatColor.AQUA + " " + ChatColor.BOLD + "F";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (player.getGameMode() == GameMode.SURVIVAL) {
                String name = player.getPlayerListName();

                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);

                    player.setPlayerListName(name.substring(0, name.length() - 8));

                    player.sendMessage(PREFIX + "Flugmodus wurde " + ChatColor.RED + "deaktiviert" + ChatColor.AQUA + ".");
                } else {
                    player.setAllowFlight(true);

                    player.setPlayerListName(name + PLAYERSUFFIX);

                    player.sendMessage(PREFIX + "Flugmodus wurde " + ChatColor.GREEN + "aktiviert" + ChatColor.AQUA + ".");
                }
            }
        }
        return true;
    }
}
