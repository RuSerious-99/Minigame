package com.ruserious99.minigame.instance.game.deadspace.deadUtils.gameZones;

import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class GameAreas {

    private static final HashMap<Cuboid, String> cuboids = new HashMap<>();

    private GameAreas(){
    }

    public static void createRegions() {

        //chapter1
        Cuboid boarding = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1667, 90, -36),
                new Location(Bukkit.getWorld(("arena6")), 1770, 86, -63));
        cuboids.put(boarding, "boarding");

        Cuboid enterBoarding = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1791, 89, -57),
                new Location(Bukkit.getWorld(("arena6")), 1777, 85, -65));
        cuboids.put(enterBoarding, "enterBoarding");

        Cuboid c1Computer = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1791, 88, -54),
                new Location(Bukkit.getWorld(("arena6")), 1782, 85, -53));
        cuboids.put(c1Computer, "c1Computer");

        Cuboid firstPartHall = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1788, 89, -49),
                new Location(Bukkit.getWorld(("arena6")), 1790, 85, -43));
        cuboids.put(firstPartHall, "firstPartHall");

        Cuboid secondPartHall = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1788, 85, -42),
                new Location(Bukkit.getWorld(("arena6")), 1792, 89, -40));
        cuboids.put(secondPartHall, "secondPartHall");

        Cuboid thirdPartHall = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1795, 89, -42),
                new Location(Bukkit.getWorld(("arena6")), 1793, 85, -32));
        cuboids.put(thirdPartHall, "thirdPartHall");

        Cuboid forthPartHall = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1792, 89, -34),
                new Location(Bukkit.getWorld(("arena6")), 1787, 85, -32));
        cuboids.put(forthPartHall, "forthPartHall");

        Cuboid stairsDown = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1785, 85, -31),
                new Location(Bukkit.getWorld(("arena6")), 1779, 89, -34));
        cuboids.put(stairsDown, "stairsDown");

    }
    public static HashMap<Cuboid, String> getCuboids() {return cuboids;}

    public static void removeWall(String cuboid) {
        for (Map.Entry<Cuboid, String> entry : cuboids.entrySet()) {
            Cuboid c = entry.getKey();
            String value = entry.getValue();
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
        for (Map.Entry<Cuboid, String> entry : cuboids.entrySet()) {
            Cuboid c = entry.getKey();
            String value = entry.getValue();
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
