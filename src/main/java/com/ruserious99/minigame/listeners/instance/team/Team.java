package com.ruserious99.minigame.listeners.instance.team;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Team {

    GREEN(ChatColor.GREEN + "Green", Material.GREEN_WOOL),
    BLUE(ChatColor.BLUE + "Blue", Material.BLUE_WOOL),
    RED(ChatColor.RED + "Red", Material.RED_WOOL);

    private final String display;
    private final Material material;

    Team(String display, Material material) {
        this.display = display;
        this.material = material;
    }

    public String getDisplay() {return display;}
    public Material getMaterial() {return material;}
}
