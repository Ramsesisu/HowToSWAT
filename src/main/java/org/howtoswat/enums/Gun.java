package org.howtoswat.enums;

public enum Gun {
    TS19(Items.TS19, 7, 3.5, 21, 400, 400, 3000, 0.7F, false),
    SCATTER3(Items.SCATTER3, 4, 5, 25, 500, 300, 2000, 1.0F, false),
    P69(Items.P69, 10, 3, 15, 150, 1500, 4000, 0.5F, false),
    VIPER9(Items.VIPER9, 16, 5, 5, 30, 5000, 10000, 0.0F, false),
    EXTENSO18(Items.EXTENSO18, 12, 4.5, 5, 50, 3000, 6000, 0.55F, false),
    ALPHA7(Items.ALPHA7, 12, 2.5, 1, 1, 3000,75000, 1.0F, true);

    private final Items item;
    private final double damage;
    private final double velocity;
    private final int ammo;
    private final int maxammo;
    private final int cooldown;
    private final int reloadcooldown;
    private final float pitch;
    private final boolean explosive;

    Gun(Items item, double damage, double velocity, int ammo, int maxammo, int cooldown, int reloadcooldown, float pitch, boolean explosive) {
        this.item = item;
        this.damage = damage;
        this.velocity = velocity;
        this.ammo = ammo;
        this.maxammo = maxammo;
        this.cooldown = cooldown;
        this.reloadcooldown = reloadcooldown;
        this.pitch = pitch;
        this.explosive = explosive;
    }

    public Items getItem() {
        return item;
    }

    public double getDamage() {
        return damage;
    }

    public double getVelocity() {
        return velocity;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxammo;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getReloadCooldown() {
        return reloadcooldown;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isExplosive() {
        return explosive;
    }
}
