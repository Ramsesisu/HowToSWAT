package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "FLY" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA;
    public static final String SUFFIX = ChatColor.AQUA + " " + ChatColor.BOLD + "F";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (player.getGameMode() == GameMode.SURVIVAL) {
                String name = player.getPlayerListName();

                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);

                    player.setPlayerListName(name.substring(0, name.length() - 8));

                    player.sendMessage(PREFIX + "Flug-Modus wurde " + ChatColor.RED + "deaktiviert" + ChatColor.AQUA + ".");
                } else {
                    player.setAllowFlight(true);

                    if (name.contains(SUFFIX.replace(" ", ""))) {
                        name = name.substring(0, name.length() - SUFFIX.length());
                    }
                    player.setPlayerListName(name + SUFFIX);

                    player.sendMessage(PREFIX + "Flug-Modus wurde " + ChatColor.GREEN + "aktiviert" + ChatColor.AQUA + ".");
                }
            }
        }
        return true;
    }
}
