package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.HowToSWAT;
import org.howtoswat.utils.AdminUtils;
import org.howtoswat.utils.DataUtils;
import org.howtoswat.utils.VerifyUtils;

import java.util.UUID;

public class VerifyCommand implements CommandExecutor {

    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "" + ChatColor.BOLD + "VERIFY" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    Player verified = Bukkit.getPlayer(args[0]);
                    verify(verified, player);
                    return true;
                }
            }

            if (AdminUtils.isVerified(player.getUniqueId().toString())) {
                player.sendMessage(PREFIX + "Du bist aktuell verifiziert.");
            } else {
                VerifyUtils.verifyMessage(player);
            }
        }
        return true;
    }

    public static void verify(Player verified, Player admin) {
        String uuid = verified.getUniqueId().toString();

        if (AdminUtils.isVerified(uuid)) {
            HowToSWAT.verifies.remove(uuid);

            for (String name : AdminUtils.getAdmins()) {
                Player player = Bukkit.getPlayer(UUID.fromString(name));
                if (player != null) {
                    player.sendMessage(PREFIX + ChatColor.DARK_GRAY + verified.getName() + ChatColor.GRAY + " wurde von " + ChatColor.DARK_GRAY + admin.getName() + ChatColor.GRAY + " falsifiziert.");
                }
            }
        } else {
            HowToSWAT.verifies.add(uuid);

            verified.sendMessage(PREFIX + "Du wurdest von " + ChatColor.DARK_GRAY + admin.getName() + ChatColor.GRAY + " verifiziert.");
            for (String name : AdminUtils.getAdmins()) {
                Player player = Bukkit.getPlayer(UUID.fromString(name));
                if (player != null) {
                    player.sendMessage(PREFIX + ChatColor.DARK_GRAY + verified.getName() + ChatColor.GRAY + " wurde von " + ChatColor.DARK_GRAY + admin.getName() + ChatColor.GRAY + " verifiziert.");
                }
            }
        }

        DataUtils.saveValues(HowToSWAT.verifysave, HowToSWAT.verifyconfig, "verify", HowToSWAT.verifies);
    }
}
