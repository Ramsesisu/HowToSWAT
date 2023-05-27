package org.howtoswat.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.howtoswat.enums.Equip;
import org.howtoswat.enums.Gun;
import org.howtoswat.enums.Items;
import org.howtoswat.enums.Melee;
import org.howtoswat.utils.DataUtils;

import java.io.File;
import java.util.*;

public class EquipCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "" + ChatColor.BOLD + "EQUIP" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    public static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public static final HashMap<UUID, String> equipments = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();

            if (args.length > 0) {
                int cooldownTime = 30;
                if (cooldowns.containsKey(player.getUniqueId())) {
                    long secondsLeft = cooldowns.get(player.getUniqueId()) / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                    if (secondsLeft > 0L) {
                        player.sendMessage(PREFIX + "Du kannst dich erst in " + ChatColor.DARK_GRAY + secondsLeft + ChatColor.GRAY + " Sekunden equippen!");
                        return true;
                    }
                }

                equip(player, args[0]);
            } else {
                player.sendMessage(PREFIX + "Gib ein Equip an!");
            }
        }

        return true;
    }

    public static void equip(Player player, String equipname) {
        player.getInventory().clear();

        String listname = player.getPlayerListName();
        for (Equip equip : Equip.values()) if (listname.contains(equip.getPrefix()) && !equip.getPrefix().equals("")) listname = listname.replace(equip.getPrefix() + " ", "");

        for (Equip equip : Equip.values()) {
            if (Objects.equals(equip.getName(), equipname.toLowerCase())) {
                List<Object> equiplist = new ArrayList<>();

                File save = new File("data" + File.separator + "equipment" + File.separator + player.getUniqueId().toString() + ".yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(save);
                DataUtils.checkFile(save, config, equip.getName(), equiplist);

                List<Object> equipstandard = new ArrayList<>();
                for (Items item : equip.getContents()) equipstandard.add(item.getName());
                try {
                    equiplist = DataUtils.getValues(config, equip.getName());
                } catch (NullPointerException e) {
                    equiplist = equipstandard;
                }
                if (equiplist.size() != 9) equiplist = equipstandard;

                DataUtils.saveValues(save, config, equip.getName(), equiplist);

                List<String> checklist1 = new ArrayList<>();
                List<String> checklist2 = new ArrayList<>();
                for (int i = 0; i < 9; i++) for (Items item : Items.values()) if (Objects.equals(item.getName(), equiplist.get(i).toString())) if (item != Items.AIR) checklist1.add(item.getName());
                for (Items item : equip.getContents()) if (item != Items.AIR) checklist2.add(item.getName());

                if (checklist1.size() != checklist2.size()) {
                    equiplist = equipstandard;
                }

                for (int i = 0; i < 9; i++) {
                    for (Items item : Items.values()) {
                        if (Objects.equals(item.getName(), equiplist.get(i).toString())) {
                            ItemStack itemstack = item.getItem();

                            ItemMeta meta = itemstack.getItemMeta();
                            ArrayList<String> lore = new ArrayList<>();
                            for (Gun gun : Gun.values()) {
                                if (gun.getItem() == item) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', "&6" + gun.getAmmo() + "&8/&6" + gun.getMaxAmmo()));
                                }
                            }
                            for (Melee melee : Melee.values()) {
                                if (melee.getItem() == item) {
                                    lore.add(ChatColor.translateAlternateColorCodes('&', "&6" + melee.getDurability() + "&8/&6" + melee.getDurability()));
                                }
                            }
                            if (!lore.isEmpty()) {
                                meta.setLore(lore);
                                itemstack.setItemMeta(meta);
                            }

                            player.getInventory().setItem(i, itemstack);
                        }
                    }
                }

                String space = " ";
                if (Objects.equals(equip.getPrefix(), "")) {
                    space = "";
                }
                player.setPlayerListName(equip.getPrefix() + space + ChatColor.RESET + listname);

                equipments.put(player.getUniqueId(), equip.getName());

                player.sendMessage(PREFIX + "Du hast dich mit " + ChatColor.DARK_GRAY + StringUtils.capitalize(equip.getName()) + ChatColor.GRAY + " equippt.");

                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

                return;
            }
        }

        player.setPlayerListName(listname);

        equipments.put(player.getUniqueId(), Equip.NONE.getName());

        player.sendMessage(PREFIX + "Das Equip " + ChatColor.DARK_GRAY + equipname + ChatColor.GRAY + " exisitert nicht!");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        List<String> targets = new ArrayList<>();
        for (Equip equip : Equip.values()) targets.add(StringUtils.capitalize(equip.getName()));
        if (args.length == 1) for (String target : targets) if (target.toUpperCase().startsWith(args[0].toUpperCase())) list.add(target);
        return list;
    }
}
