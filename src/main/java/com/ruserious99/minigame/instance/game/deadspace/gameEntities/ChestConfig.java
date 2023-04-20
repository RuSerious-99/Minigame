package com.ruserious99.minigame.instance.game.deadspace.gameEntities;

import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ChestConfig {

    private static final World world = Bukkit.getServer().getWorld("arena6");

    private ChestConfig() {
    }

    //Chapter1
    public static Location boarding() {return new Location(world, 1775, 86, -57);}
    public static ItemStack boardingStack() {return new ItemStack(Material.DIAMOND_SWORD);}

    public static Location enterIshamura1() {return new Location(world, 1779, 86, -58);}
    public static ItemStack enterIshamuraStack1() {return new ItemStack(ItemsManager.smallHealthPack);}

    public static Location enterIshamura2() {return new Location(world, 1789, 86, -66);}
    public static ItemStack enterIshamuraStack2() {return new ItemStack(ItemsManager.mediumHealthPack);}


    public static void spawnChest(Location location, ItemStack item) {
        System.out.println("chestConfig: called ");

        World world = Bukkit.getServer().getWorld("arena6");
        Block block = Objects.requireNonNull(world).getBlockAt(location);
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        chest.getBlockInventory().clear();
        chest.getInventory().addItem(item);
    }
}
