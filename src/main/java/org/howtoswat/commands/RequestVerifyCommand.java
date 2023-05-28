package org.howtoswat.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;
import org.howtoswat.utils.VerifyUtils;

import java.util.UUID;

public class RequestVerifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (!AdminUtils.isVerified(player.getUniqueId().toString())) {
                for (String uuid : AdminUtils.getAdmins()) {
                    Player admin = Bukkit.getServer().getPlayer(UUID.fromString(uuid));
                    if (admin != null) {
                        VerifyUtils.addRequest(player, admin, VerifyCommand.PREFIX + ChatColor.DARK_GRAY + player.getName() + ChatColor.GRAY + " möchte verifiziert werden.", () -> VerifyCommand.verify(player, admin));
                    }
                }
            }

            player.sendMessage(VerifyUtils.PREFIX + "Deine Verifizierungs-Anfrage wurde gestellt.");
        }
        return true;
    }
}
