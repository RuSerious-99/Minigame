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
import java.util.Random;

public class ChestConfig {

    private static final World world = Bukkit.getServer().getWorld("arena6");

    private ChestConfig() {
    }

    //Chapter1
    public static Location boarding() {return new Location(world, 1775, 86, -57);}
    public static ItemStack boardingStack() {return new ItemStack(Objects.requireNonNull(getItemStack()));}

    public static Location enterIshamura1() {return new Location(world, 1779, 86, -58);}
    public static ItemStack enterIshamuraStack1() {return new ItemStack(Objects.requireNonNull(getItemStack()));}

    public static Location enterIshamura2() {return new Location(world, 1789, 86, -66);}
    public static ItemStack enterIshamuraStack2() {return new ItemStack(Objects.requireNonNull(getItemStack()));}


    //utils
    public static void spawnChest(Location location, ItemStack item) {
        System.out.println("chestConfig: called ");

        World world = Bukkit.getServer().getWorld("arena6");
        Block block = Objects.requireNonNull(world).getBlockAt(location);
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        chest.getBlockInventory().clear();
        chest.getInventory().addItem(item);
    }
    public static ItemStack getItemStack() {
        //define
        String[] items = {"small health", "medium health", "large health", "credits", "diamond sword"};
        int[] chances = {20, 10, 5, 60, 5};

        //calc percent chance
        int totalChance = 0;
        for (int chance : chances) {
            totalChance += chance;
        }

        // Generate a random number between 1 and percentage chance
        Random random = new Random();
        int randomNumber = random.nextInt(totalChance) + 1;

        // Determine which item was selected based on the percentage chances
        int cumulativeChance = 0;
        String selectedItem = null;
        for (int i = 0; i < items.length; i++) {
            cumulativeChance += chances[i];
            if (randomNumber <= cumulativeChance) {
                selectedItem = items[i];
                System.out.println("ChestConfig in loop; " + selectedItem);
                break;
            }
        }

        //switch for ItemStack
        switch (Objects.requireNonNull(selectedItem)) {
            case "small health" -> {
                return ItemsManager.smallHealthPack;
            }
            case "medium health" -> {
                return ItemsManager.mediumHealthPack;
            }
            case "large health" -> {
                return ItemsManager.largeHealthPack;
            }
            case "credits" -> {
                Random randomCredit = new Random();
                int randomNumberCredit = randomCredit.nextInt(5) + 1;
                return ItemsManager.createCredits(randomNumberCredit * 100);
            }
            case "diamond sword" -> {
                return new ItemStack(Material.DIAMOND_SWORD);
            }
        }

        return null;
    }

}
