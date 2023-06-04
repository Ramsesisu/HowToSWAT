package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.handlers.DamageHandler;
import org.howtoswat.handlers.GrenadeHandler;
import org.howtoswat.handlers.GunHandler;
import org.howtoswat.utils.AdminUtils;

public class CoolDownCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "COOLDOWN" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    if (Bukkit.getServer().getPlayer(args[0]) != null) {
                        player = Bukkit.getServer().getPlayer(args[0]);

                        sender.sendMessage(PREFIX + "Die Cooldowns von " + ChatColor.GRAY + player.getName() + ChatColor.DARK_GRAY + " werden zurückgesetzt.");
                    }
                }
                EquipCommand.cooldowns.put(player.getUniqueId(), 0L);
                UseCommand.cooldowns.put(player.getUniqueId(), 0L);
                GunHandler.cooldowntimes.put(player.getUniqueId(), 0);
                GrenadeHandler.cooldowntimes.put(player.getUniqueId(), 0);
                DamageHandler.cooldowntimes.put(player.getUniqueId(), 0);
                EquipCommand.explosivecooldowns.put(player.getUniqueId(), 0L);

                player.sendMessage(PREFIX + "Deine Cooldowns wurden zurückgesetzt.");
            } else {
                player.sendMessage(PREFIX + "Du bist kein Supporter!");
            }
        }
        return true;
    }
}
