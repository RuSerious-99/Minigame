package com.ruserious99.minigame.instance.game.deadspace.deadUtils.gameZones;

import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Walls {

    private static  final HashMap<String, Cuboid> walls = new HashMap<>();

    private Walls() {
    }

    public static void createWalls() {
        //chapter 1
        Cuboid loadingDockWall1 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1666, 88, -63),
                new Location(Bukkit.getWorld(("arena6")), 1770, 87, -63));
        walls.put("loadingDockWall1", loadingDockWall1);

        Cuboid loadingDockWall2 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1770, 88, -59),
                new Location(Bukkit.getWorld(("arena6")), 1733, 86, -56));
        walls.put("loadingDockWall2", loadingDockWall2);

        Cuboid loadingDockWall3 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1731, 86, -56),
                new Location(Bukkit.getWorld(("arena6")), 1666, 88, -59));
        walls.put("loadingDockWall3", loadingDockWall3);

        Cuboid blockDoorLoading = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1775, 86, -66),
                new Location(Bukkit.getWorld(("arena6")), 1770, 88, -66));
        walls.put("blockDoorLoading", blockDoorLoading);

        Cuboid blockDoorComputer = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1788, 85, -65),
                new Location(Bukkit.getWorld(("arena6")), 1786, 89, -66));
        walls.put("blockDoorComputer", blockDoorComputer);

    }

    public static void removeWall(String cuboid) {
        for (Map.Entry<String, Cuboid> entry : walls.entrySet()) {
            Cuboid c = entry.getValue();
            String value = entry.getKey();
            if (value.equals(cuboid)) {
                for (Block block : c.getBlocks()) {
                    if (block.getType() == Material.BARRIER) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public static void fillWall(String cuboid) {
        for (Map.Entry<String, Cuboid> entry : walls.entrySet()) {
            Cuboid c = entry.getValue();
            String value = entry.getKey();
            if (value.equals(cuboid)) {
                for (Block block : c.getBlocks()) {
                    if (block.getType() == Material.AIR) {
                        block.setType(Material.BARRIER);
                    }
                }
            }
        }
    }
}
