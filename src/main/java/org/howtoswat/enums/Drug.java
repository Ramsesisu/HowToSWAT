package org.howtoswat.enums;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Drug {
    KOKAIN(Arrays.asList("pulver", "kokain", "koks"), Arrays.asList(new PotionEffect(PotionEffectType.ABSORPTION, 3000, 4), new PotionEffect(PotionEffectType.REGENERATION, 320, 0), new PotionEffect(PotionEffectType.SPEED, 200, 1))),
    MARIHUANA(Arrays.asList("kräuter", "marihuana", "gras"), Arrays.asList(new PotionEffect(PotionEffectType.ABSORPTION, 5000, 3), new PotionEffect(PotionEffectType.REGENERATION, 400, 1))),
    METHAMPHETAMIN(Arrays.asList("kristalle", "methamphetamin", "meth"), Arrays.asList(new PotionEffect(PotionEffectType.ABSORPTION, 4400, 4), new PotionEffect(PotionEffectType.REGENERATION, 320, 1), new PotionEffect(PotionEffectType.SPEED, 200, 1), new PotionEffect(PotionEffectType.NIGHT_VISION, 320, 0))),
    SCHMERZMITTEL(Collections.singletonList("schmerzmittel"), Arrays.asList(new PotionEffect(PotionEffectType.ABSORPTION, 5000, 3), new PotionEffect(PotionEffectType.REGENERATION, 400, 1), new PotionEffect(PotionEffectType.CONFUSION, 160, 0)));

    private final List<String> names;
    private final List<PotionEffect> effects;

    Drug(List<String> names, List<PotionEffect> effects) {
        this.names = names;
        this.effects = effects;
    }

    public List<String> getNames() {
        return names;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }
}
