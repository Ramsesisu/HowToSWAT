package org.howtoswat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.handlers.GunHandler;
import org.howtoswat.utils.AdminUtils;

public class CoolDownCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "COOLDOWN" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                EquipCommand.cooldowns.put(player.getUniqueId(), 0L);
                UseCommand.cooldowns.put(player.getUniqueId(), 0L);
                GunHandler.cooldowns.put(player.getUniqueId(), 0L);

                player.sendMessage(PREFIX + "Deine Cooldowns wurden zurückgesetzt.");
            } else {
                player.sendMessage(PREFIX + "Du bist kein Admin!");
            }
        }
        return true;
    }
}
