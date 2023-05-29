package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.howtoswat.utils.AdminUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisableCommandCommand implements CommandExecutor, TabCompleter {

    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "COMMAND" + ChatColor.DARK_GRAY + "] " + ChatColor.BLUE;
    public static final List<Command> disabled = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (AdminUtils.isAdmin(player.getUniqueId().toString())) {
                if (args.length > 0) {
                    Command cmd = Bukkit.getServer().getCommandMap().getCommand(args[0].replaceFirst("/", ""));
                    if (cmd != null) {
                        if (cmd.getPermission() == null || player.hasPermission(cmd.getPermission())) {
                            if (disabled.remove(cmd)) {
                                Bukkit.getServer().broadcastMessage(PREFIX + "Der Befehl " + ChatColor.AQUA + "/" + cmd.getName() + ChatColor.BLUE + " wurde " + ChatColor.GREEN + "aktiviert" + ChatColor.BLUE + ".");
                            } else {
                                disabled.add(cmd);

                                Bukkit.getServer().broadcastMessage(PREFIX + "Der Befehl " + ChatColor.AQUA + "/" + cmd.getName() + ChatColor.BLUE + " wurde " + ChatColor.RED + "deaktiviert" + ChatColor.BLUE + ".");
                            }
                        } else {
                            player.sendMessage(PREFIX + "Du hast keine Berechtigung für " + ChatColor.AQUA + "/" + cmd.getName() + ChatColor.BLUE + "!");
                        }
                    } else {
                        player.sendMessage(PREFIX + "Der Befehl " + ChatColor.AQUA + args[0] + ChatColor.BLUE + " wurde nicht gefunden!");
                    }
                } else {
                    player.sendMessage(PREFIX + "Gib einen Befehl an!");
                }
            } else {
                player.sendMessage(PREFIX + "Du bist kein Admin!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Set<String> targets = new HashSet<>();
        for (Command cmd : Bukkit.getServer().getCommandMap().getKnownCommands().values()) targets.add(cmd.getName());
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
