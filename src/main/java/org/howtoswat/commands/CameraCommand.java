package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CameraCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "CAMERA" + ChatColor.DARK_GRAY + "] " + ChatColor.LIGHT_PURPLE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (player.getGameMode() == GameMode.SPECTATOR) {
                if (BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
                    player.setGameMode(GameMode.CREATIVE);
                } else {
                    player.setGameMode(GameMode.SURVIVAL);
                }

                player.sendMessage(PREFIX + "Du hast den Zuschauermodus beendet.");
            } else {
                String name = player.getPlayerListName();
                if (name.contains(FlyCommand.SUFFIX.replace(" ", ""))) {
                    player.setPlayerListName(name.substring(0, name.length() - FlyCommand.SUFFIX.length()));
                }
                player.setGameMode(GameMode.SPECTATOR);

                player.sendMessage(PREFIX + "Du bist nun im Zuschauermodus.");
            }
        }
        return true;
    }
}
