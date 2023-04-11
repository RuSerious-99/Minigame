package com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class SpawnChests {

    private final Location location;
    private final ItemStack item;

    public SpawnChests(Location location, ItemStack item) {
        this.location = location;
        this.item = item;
    }

    public void spawnChest() {
        Block block = Objects.requireNonNull(Bukkit.getServer().getWorld("arena6")).getBlockAt(location);
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        chest.getInventory().addItem(item);
    }
}
