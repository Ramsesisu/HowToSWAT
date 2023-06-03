package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

import java.util.Objects;

public class BaustelleCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "BAUSTELLE" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isBuilder(player.getUniqueId().toString())) {
                Location loc = player.getLocation();

                if (Objects.equals(player.getWorld().getName(), "Baustelle")) {
                    loc.setWorld(Bukkit.getWorld("Training"));
                    player.teleport(loc);

                    String name = player.getPlayerListName();
                    if (name.contains(BuildmodeCommand.SUFFIX.replace(" ", ""))) {
                        name = name.substring(0, name.length() - BuildmodeCommand.SUFFIX.length());
                    }
                    player.setPlayerListName(name);
                    if (BuildmodeCommand.buildmode.remove(player.getUniqueId())) {
                        if (player.getGameMode() == GameMode.CREATIVE) {
                            player.setGameMode(GameMode.SURVIVAL);
                        }
                    }

                    player.sendMessage(PREFIX + "Du hast die Baustelle verlassen.");
                } else {
                    loc.setWorld(Bukkit.getWorld("Baustelle"));
                    player.teleport(loc);

                    player.sendMessage(PREFIX + "Du hast die Baustelle betreten.");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Builder!");
            }
        }
        return true;
    }
}
