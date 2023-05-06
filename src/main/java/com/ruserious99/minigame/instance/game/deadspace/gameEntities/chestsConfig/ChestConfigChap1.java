package com.ruserious99.minigame.instance.game.deadspace.gameEntities.chestsConfig;

import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;
import java.util.Random;

public class ChestConfigChap1 {

    private static final World world = Bukkit.getServer().getWorld("arena6");

    private ChestConfigChap1() {
    }

    //Chapter1

    public static Location boarding() {return new Location(world, 1775, 86, -57);}
    public static Location enterIshamura1() {return new Location(world, 1779, 86, -58);}
    public static Location enterIshamura2() {return new Location(world, 1789, 86, -66);}
    public static Location firstWeaponRoom1() {return new Location(world, 1789, 78, -36);}
    public static Location firstWeaponRoom2() {return new Location(world, 1789, 78, -38);}
    public static Location firstWeaponRoom3() {return new Location(world, 1783, 77, -38);}
    public static Location firstWeaponRoom4() {return new Location(world, 1783, 77, -37);}
    public static Location HallwayDownStairsLeft() {return new Location(world, 1782, 73, -50);}
    public static Location dataBoardDispencer() {return new Location(world, 1799, 75, -55);}
    public static Location dataBoardLocker1() {return new Location(world, 1781, 75, -52);}
    public static Location dataBoardLocker2() {return new Location(world, 1781, 74, -52);}
    public static Location hallwayAfterDataBoard() {return new Location(world, 1777, 74, -50);}

    //utils
    public static void spawnLockerItems(Location location){
        Block dispenser = Objects.requireNonNull(world).getBlockAt(location);
        if (dispenser.getType() == Material.DISPENSER) {
            Inventory dispenserInventory = ((Dispenser) dispenser.getState()).getInventory();
            dispenserInventory.addItem(getItemStack());
        }
    }
    public static void spawnChest(Location location) {
        World world = Bukkit.getServer().getWorld("arena6");
        Block block = Objects.requireNonNull(world).getBlockAt(location);
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();
        chest.getBlockInventory().clear();
        chest.getInventory().addItem(getItemStack());
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
                //System.out.println("ChestConfig in loop; " + selectedItem);
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
                switch (randomNumberCredit) {
                    case 1 -> {
                        return ItemsManager.credits100;
                    }
                    case 2 -> {
                        return ItemsManager.credits200;
                    }
                    case 3 -> {
                        return ItemsManager.credits300;
                    }
                    case 4 -> {
                        return ItemsManager.credits400;
                    }
                    case 5 -> {
                        return ItemsManager.credits500;
                    }
                }

            }
            case "diamond sword" -> {
                return new ItemStack(Material.DIAMOND_SWORD);
            }
        }

        return null;
    }

}
