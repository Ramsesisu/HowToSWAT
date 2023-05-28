package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.howtoswat.enums.Items;
import org.howtoswat.enums.Kevlar;
import org.howtoswat.handlers.ExplosiveHandler;
import org.howtoswat.utils.AdminUtils;
import org.howtoswat.utils.VerifyUtils;

import static org.howtoswat.HowToSWAT.PLUGIN;

public class SprengguertelCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "SPRENGI" + ChatColor.DARK_GRAY + "] " + ChatColor.YELLOW;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            ItemStack chestplate = player.getInventory().getChestplate();
            if (chestplate != null) {
                for (Items check : Items.values()) {
                    if (check.getItem().getType() == chestplate.getType()) {
                        if (check.needsVerify()) {
                            if (!AdminUtils.isVerified(player.getUniqueId().toString())) {
                                VerifyUtils.verifyMessage(player);
                                return true;
                            }
                        }
                    }
                }
            }
            if (args.length > 0) {
                if (!BuildmodeCommand.buildmode.contains(player.getUniqueId())) {
                    if (player.getInventory().getChestplate() != null) {
                        Bukkit.getScheduler().runTaskLater(PLUGIN, () -> {
                            for (Kevlar kevlar : Kevlar.values()) {
                                if (player.getInventory().getChestplate() != null) {
                                    if (kevlar.getItem().getItem().getType() == player.getInventory().getChestplate().getType()) {
                                        if (kevlar.isExplosive()) {
                                            ExplosiveHandler.explode(player, player.getLocation(), 6);

                                            player.sendMessage(PREFIX + "Dein Sprenggürtel ist explodiert.");
                                            return;
                                        }
                                    }
                                }
                            }
                            player.sendMessage(PREFIX + "Du trägst keinen Sprenggürtel!");
                        }, Integer.parseInt(args[0]) * 20L);
                    }
                }
            } else {
                player.sendMessage(PREFIX + "Du hast keinen Timer angegeben!");
            }
        }
        return true;
    }
}
