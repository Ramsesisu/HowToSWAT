package org.howtoswat.enums;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public enum Equip {
    SWAT("swat", ChatColor.translateAlternateColorCodes('&', "&8&l[&1SWAT&8&l]"), Arrays.asList(Items.SCHILD, Items.SCHWERE_KEVLAR, Items.TS19, Items.VIPER9, Items.FLASHES, Items.AIR, Items.AIR, Items.AIR, Items.ELYTRA)),
    POLIZEI("polizei", ChatColor.translateAlternateColorCodes('&', "&8&l[&9UCPD&8&l]"), Arrays.asList(Items.LEICHTE_KEVLAR, Items.TS19, Items.SCATTER3, Items.TAZER, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR)),
    BALLAS("ballas", ChatColor.translateAlternateColorCodes('&', "&8&l[&5Ballas&8&l]"), Arrays.asList(Items.LEICHTE_KEVLAR, Items.TS19, Items.EXTENSO18, Items.MESSER, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR)),
    TERROR("terror", ChatColor.translateAlternateColorCodes('&', "&8&l[&eTerror&8&l]"), Arrays.asList(Items.SPRENGGUERTEL, Items.TS19, Items.ALPHA7, Items.MESSER, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR)),
    ZIVILIST("zivilist", ChatColor.translateAlternateColorCodes('&', "&8&l[&7Zivi&8&l]"), Arrays.asList(Items.SCATTER3, Items.P69, Items.BASEBALLSCHLAEGER, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR)),
    FLAMMENWERFER("flammenwerfer", ChatColor.translateAlternateColorCodes('&', "&8&l[&cFlammi&8&l]"), Arrays.asList(Items.LEICHTE_KEVLAR, Items.FLAMMENWERFER, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR)),
    ELYTRA("elytra", ChatColor.translateAlternateColorCodes('&', "&8&l[&3Elytra&8&l]"), Arrays.asList(Items.ELYTRA, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR)),
    NONE("none", "", Arrays.asList(Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR, Items.AIR));

    private final String name;
    private final String prefix;
    private final List<Items> contents;

    Equip(String name, String prefix, List<Items> contents) {
        this.name = name;
        this.prefix = prefix;
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<Items> getContents() {
        return contents;
    }
}
