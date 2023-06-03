package org.howtoswat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.howtoswat.enums.Items;
import org.howtoswat.utils.DataUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveCommand implements CommandExecutor {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "SAVE" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (EquipCommand.equipments.containsKey(player.getUniqueId())) {
                List<Object> hotbar = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    try {
                        player.getInventory().getItem(i).getItemMeta().getDisplayName();
                        for (Items item : Items.values()) {
                            try {
                                if (Objects.equals(item.getItem().getItemMeta().getDisplayName(), player.getInventory().getItem(i).getItemMeta().getDisplayName())) {
                                    if (hotbar.contains(item.getName())) {
                                        hotbar.add(Items.AIR.getName());
                                    } else {
                                        hotbar.add(item.getName());
                                    }
                                }
                            } catch (NullPointerException ignored) {
                            }
                        }
                    } catch (NullPointerException e) {
                        hotbar.add(Items.AIR.getName());
                    }
                }

                File save = new File("data" + File.separator + "equipment" + File.separator + player.getUniqueId().toString() + ".yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(save);

                String equip = EquipCommand.equipments.get(player.getUniqueId());
                DataUtils.checkFile(save, config, equip, new ArrayList<>());

                DataUtils.saveValues(save, config, equip, hotbar);

                player.sendMessage(PREFIX + "Die Equipkonfiguration " + ChatColor.DARK_GRAY + StringUtils.capitalize(equip) + ChatColor.GRAY + " wurde gespeichert.");
            } else {
                player.sendMessage(PREFIX + "Du hast dir nichts equipt!");
            }
        }
        return true;
    }
}
