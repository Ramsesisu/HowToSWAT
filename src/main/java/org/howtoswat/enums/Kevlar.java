package org.howtoswat.enums;

public enum Kevlar {
    ELYTRA(Items.ELYTRA, 432, false),
    LKEV(Items.LKEV, 30, false),
    SKEV(Items.SKEV, 50, false),
    SPRENGGUERTEL(Items.SPRENGGUERTEL, 80, true);

    private final Items item;
    private final int durability;
    private final boolean explosive;

    Kevlar(Items item, int durability, boolean explosive) {
        this.item = item;
        this.durability = durability;
        this.explosive = explosive;
    }

    public Items getItem() {
        return item;
    }

    public int getDurability() {
        return durability;
    }
    public boolean isExplosive() {
        return explosive;
    }
}
