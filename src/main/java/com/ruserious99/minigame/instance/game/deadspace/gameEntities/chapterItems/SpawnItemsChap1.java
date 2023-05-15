package com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;

public class SpawnItemsChap1 {

    private final static World world = Bukkit.getServer().getWorld("arena6");

    private SpawnItemsChap1() {
    }

    public static Location firstWeaponLocation(){return new Location(world, 1789, 79, -36);}
    public static Location stasisLocation(){return new Location(world, 1778.709, 71, -67.851);}
    public static Location smallHealthAfterFirstWeapon(){return new Location(world,1791, 76, -50);}
    public static Location mainHallwayToTramStationCredit(){return new Location(world,1780, 73, -79);}
    public static Location heathInStasisTramRoom(){return new Location(world,1751, 73, -78);}
    public static Location maintenanceKey(){return new Location(world,1765, 72, -51);}


    public static void spawnItems(Location location, ItemStack itemStack) {
        World world = Bukkit.getServer().getWorld("arena6");
        (Objects.requireNonNull(world)).dropItem(location, itemStack);
    }


}