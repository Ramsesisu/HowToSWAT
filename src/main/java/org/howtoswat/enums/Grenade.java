package org.howtoswat.enums;

public enum Grenade {
    FLASH(Items.FLASHES, 7, 3000);

    private final Items item;
    private final int amount;
    private final int cooldown;

    Grenade(Items item, int amount, int cooldown) {
        this.item = item;
        this.amount = amount;
        this.cooldown = cooldown;
    }

    public Items getItem() {
        return item;
    }

    public double getAmout() {
        return amount;
    }

    public int getCooldown() {
        return cooldown;
    }
}
