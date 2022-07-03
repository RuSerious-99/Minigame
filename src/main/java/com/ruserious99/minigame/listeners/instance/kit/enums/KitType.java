package com.ruserious99.minigame.listeners.instance.kit.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum KitType {

    MINER(ChatColor.GOLD + "Miner", Material.DIAMOND_PICKAXE, "Best Mining kit"),
    FIGHTER(ChatColor.DARK_RED + "Fighter", Material.DIAMOND_SWORD, "best fighting kit");

    private final String display;
    private final String description;
    private final Material material;

    KitType(String display, Material material, String description){
        this.display = display;
        this.description = description;
        this.material = material;
    }

    public String getDisplay() {return display;}
    public String getDescription() {return description;}
    public Material getMaterial() {return material;}
}
