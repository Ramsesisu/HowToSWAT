package org.howtoswat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class HelpCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "HELP" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            player.sendMessage(PREFIX + "HowToSWAT-Befehlsliste:");
            Map<String, Command> commands = Bukkit.getServer().getCommandMap().getKnownCommands();
            for (Object name : commands.keySet().stream().sorted().toArray()) {
                if (name.toString().startsWith("howtoswat:")) {
                    player.sendMessage(ChatColor.YELLOW + " /" + ChatColor.BOLD + name.toString().replace("howtoswat:", ""));
                    Command cmd = commands.get(name.toString());
                    player.sendMessage(ChatColor.GRAY + "   " + ChatColor.ITALIC + cmd.getDescription());
                    player.sendMessage(ChatColor.DARK_GRAY + "    " + cmd.getUsage());
                }
            }
        }
        return true;
    }
}
