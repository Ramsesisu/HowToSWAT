package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.howtoswat.utils.AdminUtils;

public class HealCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "" + ChatColor.BOLD + "HEAL" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (args.length > 0) {
                if (AdminUtils.isSupporter(player.getUniqueId().toString())) {
                    if (Bukkit.getServer().getPlayer(args[0]) != null) {
                        player = Bukkit.getServer().getPlayer(args[0]);

                        sender.sendMessage(PREFIX + "Der Spieler " + ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + " wird geheilt.");
                    }
                }
            }

            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            for (PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());

            for (double x = -1; x <= 1; x++) {
                for (double y = 0; y <= 2; y++) {
                    for (double z = -1; z <= 1; z++) {
                        Bukkit.getServer().getWorld(player.getWorld().getName()).spawnParticle(Particle.HEART, player.getLocation().add(x, y, z), 1);
                    }
                }
            }

            player.sendMessage(PREFIX + "Du wurdest geheilt.");
        }
        return true;
    }
}
