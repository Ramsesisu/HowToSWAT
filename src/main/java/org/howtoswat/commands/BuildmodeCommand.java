package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BuildmodeCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "" + ChatColor.BOLD + "BUILD" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    public static final String SUFFIX = ChatColor.GREEN + " " + ChatColor.BOLD + "B";

    public static final List<UUID> buildmode = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isBuilder(player.getUniqueId().toString())) {
                if (Objects.equals(player.getWorld().getName(), "Baustelle") || AdminUtils.isAdmin(player.getUniqueId().toString())) {
                    String name = player.getPlayerListName();

                    if (buildmode.contains(player.getUniqueId())) {
                        buildmode.remove(player.getUniqueId());

                        if (player.getGameMode() != GameMode.SPECTATOR) {
                            player.setGameMode(GameMode.SURVIVAL);
                        }
                        player.setInvulnerable(false);

                        player.setPlayerListName(name.substring(0, name.length() - 8));

                        player.sendMessage(PREFIX + "Du hast den Buildmode verlassen.");
                    } else {
                        buildmode.add(player.getUniqueId());

                        player.setGameMode(GameMode.CREATIVE);
                        player.setInvulnerable(true);

                        if (name.contains(FlyCommand.SUFFIX.replace(" ", ""))) {
                            name = name.substring(0, name.length() - FlyCommand.SUFFIX.length());
                        }
                        player.setPlayerListName(name + SUFFIX);

                        player.sendMessage(PREFIX + "Du hast den Buildmode betreten.");
                    }
                } else {
                    player.sendMessage(PREFIX + "Du kannst den Buildmode nur auf der Baustelle betreten!");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Builder!");
            }
        }
        return true;
    }
}
