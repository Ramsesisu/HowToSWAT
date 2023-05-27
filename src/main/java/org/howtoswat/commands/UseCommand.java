package org.howtoswat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.howtoswat.enums.Drugs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UseCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "USE" + ChatColor.DARK_GRAY + "] " + ChatColor.RED;

    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            int cooldownTime = 3;
            if (cooldowns.containsKey(player.getUniqueId())) {
                long secondsLeft = cooldowns.get(player.getUniqueId()) / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                if (secondsLeft > 0L)
                    return true;
            }
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            if (args.length == 0) {
                player.sendMessage(PREFIX + "Du hast keine Droge angeben!");
            } else {
                for (Drugs drug : Drugs.values()) {
                    if (drug.getNames().contains(args[0].toLowerCase())) {
                        for (PotionEffect potionEffect : drug.getEffects()) player.addPotionEffect(potionEffect);

                        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                        entities.add(player);
                        for (Entity entity : entities) {
                            if (entity instanceof Player) {
                                Player nearby = ((Player) entity).getPlayer();
                                nearby.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 100.0F, 1.0F);
                                nearby.sendMessage(ChatColor.RED + player.getName() + " hat " + StringUtils.capitalize(drug.getNames().get(0)) + " genommen.");
                            }
                        }
                        return true;
                    }
                }
                player.sendMessage(PREFIX + "Die Droge " + ChatColor.DARK_RED + args[0] + ChatColor.RED + " wurde nicht gefunden!");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        String[] targets = {"Schmerzmittel", "Kokain", "Marihuana", "Methamphetamin"};
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
