package com.ruserious99.minigame.instance.game.deadspace.gameZones;

import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class GameAreas {

    public static final HashMap<Cuboid, String> cuboids = new HashMap<>();

    private GameAreas(){
    }

    public static void createRegionsChapter1() {
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

        Cuboid elevatorShaft = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1779, 76, -34),
                new Location(Bukkit.getWorld(("arena6")), 1781, 89, -32));
        cuboids.put(elevatorShaft, "elevatorShaft");

        Cuboid firstWeaponRoom = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1781, 76, -32),
                new Location(Bukkit.getWorld(("arena6")), 1789, 81, -38));
        cuboids.put(firstWeaponRoom, "firstWeaponRoom");

        Cuboid preHallwayAfter = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1787, 76, -34),
                new Location(Bukkit.getWorld(("arena6")), 1784, 81, -38));
        cuboids.put(preHallwayAfter, "preHallwayAfter");

        Cuboid hallwayAfterFirstWeaponRoom = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1793, 79, -32),
                new Location(Bukkit.getWorld(("arena6")), 1792, 78, -43));
        cuboids.put(hallwayAfterFirstWeaponRoom, "hallwayAfterFirstWeaponRoom");

        Cuboid hallwayAfterFirstWeaponRoom2 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1791, 76, -39),
                new Location(Bukkit.getWorld(("arena6")), 1788, 79, -46));
        cuboids.put(hallwayAfterFirstWeaponRoom2, "hallwayAfterFirstWeaponRoom2");

        Cuboid dataBoard = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1798, 73, -53),
                new Location(Bukkit.getWorld(("arena6")), 1781, 77, -59));
        cuboids.put(dataBoard, "dataBoard");

        Cuboid hallAfterDataBoard = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1778, 77, -58),
                new Location(Bukkit.getWorld(("arena6")), 1773, 73, -42));
        cuboids.put(hallAfterDataBoard, "hallAfterDataBoard");

        Cuboid fastDoorTram = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1765, 70, -61),
                new Location(Bukkit.getWorld(("arena6")), 1781, 75, -71));
        cuboids.put(fastDoorTram, "fastDoorTram");

        Cuboid blockFastDoorGoing = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1779, 70, -73),
                new Location(Bukkit.getWorld(("arena6")), 1779, 73, -73));
        cuboids.put(blockFastDoorGoing, "blockFastDoorGoing");

        Cuboid blockFastDoorReturning = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1779, 70, -71),
                new Location(Bukkit.getWorld(("arena6")), 1779, 73, -71));
        cuboids.put(blockFastDoorReturning, "blockFastDoorReturning");

        Cuboid elevatorDownToDataBoard = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1770, 76, -44),
                new Location(Bukkit.getWorld(("arena6")), 1768, 68, -42));
        cuboids.put(elevatorDownToDataBoard, "elevatorDownToDataBoard");

    }
    public static void createRegionsChapter2() {}
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
