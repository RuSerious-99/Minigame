package com.ruserious99.minigame.instance.kit.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum CodKitType {

    SPEED(ChatColor.GOLD + "Speed", Material.BOW, new String[]{ChatColor.GRAY + "Built for speed!!!!" }),
    HEAVYWEAPON(ChatColor.GOLD + "Heavy Weapon", Material.DIAMOND_SWORD, new String[]{ChatColor.GRAY + "Feel the power!!!!" });

    private final String display;
    private final Material material;
    private final String[] description;

    public String getDisplay() {
        return display;
    }

    public Material getMaterial() {
        return material;
    }

    public String[] getDescription() {
        return description;
    }

    CodKitType(String display, Material material, String[] description) {
        this.display = display;
        this.description = description;
        this.material = material;

    }
}
