package org.howtoswat.enums;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public enum Items {
    AIR("air", new ItemStack(Material.AIR), false, false),
    ELYTRA("elytra", getElytra(), false, false),
    LKEV("lkev", getLKev(), false, false),
    SKEV("skev", getSKev(), false, false),
    SPRENGGUERTEL("sprenggürtel", getSprengguertel(), false, true),
    SCHILD("schild", getSchild(), false, false),
    FLASHES("flashes", getFlashes(), true, false),
    TS19("ts19", getTS19(), false, false),
    SCATTER3("scatter3", getScatter3(), false, false),
    P69("p69", getP69(), false, false),
    VIPER9("viper9", getViper9(), false, false),
    EXTENSO18("extenso18", getExtenso18(), false, false),
    ALPHA7("alpha7", getAlpha7(), false, true),
    MESSER("messer", getMesser(), false, false),
    BASEBALLSCHLAEGER("baseballschläger", getBaseballschlaeger(), false, false),
    TAZER("tazer", getTazer(), false, false),
    FLAMMENWERFER("flammenwerfer", getFlammenwerfer(), false, false);

    private final String name;
    private final ItemStack content;
    private final boolean droppable;
    private final boolean verify;

    Items(String name, ItemStack content, boolean droppable, boolean verify) {
        this.name = name;
        this.content = content;
        this.droppable = droppable;
        this.verify = verify;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return content;
    }
    public boolean isDroppable() {
        return droppable;
    }
    public boolean needsVerify() {
        return verify;
    }


    private static ItemStack getElytra() {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraname = elytra.getItemMeta();
        elytraname.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3Wingsuit"));
        elytra.setItemMeta(elytraname);
        return elytra;
    }

    private static ItemStack getLKev() {
        ItemStack kev = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta kevcolor = (LeatherArmorMeta) kev.getItemMeta();
        kevcolor.setColor(Color.fromRGB(80, 92, 90));
        kev.setItemMeta(kevcolor);
        ItemMeta kevname = kev.getItemMeta();
        kevname.setDisplayName(ChatColor.GRAY + "Kevlar");
        kevname.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        kev.setItemMeta(kevname);
        kev.setDurability((short) 50);
        return kev;
    }

    private static ItemStack getSKev() {
        ItemStack kev = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta kevcolor = (LeatherArmorMeta) kev.getItemMeta();
        kevcolor.setColor(Color.fromRGB(55, 54, 61));
        kev.setItemMeta(kevcolor);
        ItemMeta kevname = kev.getItemMeta();
        kevname.setDisplayName(ChatColor.DARK_GRAY + "Kevlar");
        kevname.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        kev.setItemMeta(kevname);
        kev.setDurability((short) 30);
        return kev;
    }

    private static ItemStack getSprengguertel() {
        ItemStack sprengi = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta kevname = sprengi.getItemMeta();
        kevname.setDisplayName(ChatColor.RED + "Sprenggürtel");
        kevname.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        sprengi.setItemMeta(kevname);
        return sprengi;
    }

    private static ItemStack getSchild() {
        ItemStack schild = new ItemStack(Material.SHIELD);
        schild.setDurability((short) 176);
        ItemMeta schildname = schild.getItemMeta();
        schildname.setDisplayName(ChatColor.GRAY + "Einsatzschild");
        schild.setItemMeta(schildname);
        return schild;
    }

    private static ItemStack getFlashes() {
        ItemStack flashes = new ItemStack(Material.SLIME_BALL, 6);
        ItemMeta flashmeta = flashes.getItemMeta();
        flashmeta.setDisplayName(ChatColor.GRAY + "Blendgranate");
        flashes.setItemMeta(flashmeta);
        return flashes;
    }

    private static ItemStack getTS19() {
        ItemStack m4 = new ItemStack(Material.DIAMOND_BARDING);
        ItemMeta m4meta = m4.getItemMeta();
        m4meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lTS-19"));
        m4.setItemMeta(m4meta);
        return m4;
    }

    private static ItemStack getScatter3() {
        ItemStack mp5 = new ItemStack(Material.GOLD_BARDING);
        ItemMeta mp5meta = mp5.getItemMeta();
        mp5meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lScatter-3"));
        mp5.setItemMeta(mp5meta);
        return mp5;
    }

    private static ItemStack getP69() {
        ItemStack pistole = new ItemStack(Material.IRON_BARDING);
        ItemMeta pistolemeta = pistole.getItemMeta();
        pistolemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lP-69"));
        pistole.setItemMeta(pistolemeta);
        return pistole;
    }

    private static ItemStack getViper9() {
        ItemStack sniper = new ItemStack(Material.STONE_HOE);
        ItemMeta snipermeta = sniper.getItemMeta();
        snipermeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lViper-9"));
        snipermeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        sniper.setItemMeta(snipermeta);
        return sniper;
    }

    private static ItemStack getExtenso18() {
        ItemStack jagdflinte = new ItemStack(Material.GOLD_HOE);
        ItemMeta jagdflintemeta = jagdflinte.getItemMeta();
        jagdflintemeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lExtenso-18"));
        jagdflintemeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        jagdflinte.setItemMeta(jagdflintemeta);
        return jagdflinte;
    }

    private static ItemStack getAlpha7() {
        ItemStack rpg = new ItemStack(Material.GOLD_AXE);
        ItemMeta rpgmeta = rpg.getItemMeta();
        rpgmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lAlpha-7"));
        rpgmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        rpg.setItemMeta(rpgmeta);
        return rpg;
    }

    private static ItemStack getMesser() {
        ItemStack messer = new ItemStack(Material.FEATHER);
        ItemMeta messermeta = messer.getItemMeta();
        messermeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8Messer"));
        messer.setItemMeta(messermeta);
        return messer;
    }

    private static ItemStack getBaseballschlaeger() {
        ItemStack basey = new ItemStack(Material.BONE);
        ItemMeta baseymeta = basey.getItemMeta();
        baseymeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8Baseballschläger"));
        basey.setItemMeta(baseymeta);
        return basey;
    }

    private static ItemStack getTazer() {
        ItemStack tazer = new ItemStack(Material.WOOD_HOE);
        ItemMeta tazermeta = tazer.getItemMeta();
        tazermeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eTazer"));
        tazermeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        tazer.setItemMeta(tazermeta);
        return tazer;
    }

    private static ItemStack getFlammenwerfer() {
        ItemStack flammenwerfer = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta flammenwerfermeta = flammenwerfer.getItemMeta();
        flammenwerfermeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cFlammenwerfer"));
        flammenwerfermeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        flammenwerfer.setItemMeta(flammenwerfermeta);
        return flammenwerfer;
    }
}
