package org.howtoswat.enums;

public enum Melee {
    MESSER(Items.MESSER, 7, 10000, 500),
    BASEBALLSCHLAEGER(Items.BASEBALLSCHLAEGER, 5, 8000, 500);

    private final Items item;
    private final double damage;
    private final int cooldown;
    private final int durability;

    Melee(Items item, double damage, int cooldown, int durability) {
        this.item = item;
        this.damage = damage;
        this.cooldown = cooldown;
        this.durability = durability;
    }

    public Items getItem() {
        return item;
    }

    public double getDamage() {
        return damage;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDurability() {
        return durability;
    }
}
