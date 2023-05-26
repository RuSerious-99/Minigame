package com.ruserious99.minigame.instance.game.deadspace.gameZones;

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

    public static void createWallsChapter1() {
        Cuboid loadingDockWall1 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1666, 88, -63),
                new Location(Bukkit.getWorld(("arena6")), 1770, 87, -63));
        walls.put("loadingDockWall1", loadingDockWall1);
        fillWall("loadingDockWall1");

        Cuboid loadingDockWall2 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1770, 88, -59),
                new Location(Bukkit.getWorld(("arena6")), 1733, 86, -56));
        walls.put("loadingDockWall2", loadingDockWall2);
        fillWall("loadingDockWall2");

        Cuboid loadingDockWall3 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1731, 86, -56),
                new Location(Bukkit.getWorld(("arena6")), 1666, 88, -59));
        walls.put("loadingDockWall3", loadingDockWall3);
        fillWall("loadingDockWall3");

        Cuboid blockDoorLoading = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1775, 86, -66),
                new Location(Bukkit.getWorld(("arena6")), 1770, 88, -66));
        walls.put("blockDoorLoading", blockDoorLoading);
        fillWall("blockDoorLoading");

        Cuboid blockDoorComputer = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1788, 85, -65),
                new Location(Bukkit.getWorld(("arena6")), 1786, 89, -66));
        walls.put("blockDoorComputer", blockDoorComputer);
        fillWall("blockDoorComputer");

        Cuboid dataBoardWall = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1793, 74, -60),
                new Location(Bukkit.getWorld(("arena6")), 1781, 76, -60));
        walls.put("dataBoardWall", dataBoardWall);
        fillWall("dataBoardWall");

        Cuboid fastDoor = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1782, 70, -70),
                new Location(Bukkit.getWorld(("arena6")), 1782, 74, -60));
        walls.put("fastDoor", fastDoor);
        fillWall("fastDoor");

        Cuboid fastDoor1 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1764, 70, -61),
                new Location(Bukkit.getWorld(("arena6")), 1764, 74, -71));
        walls.put("fastDoor1", fastDoor1);
        fillWall("fastDoor1");

        Cuboid repairTramWithStasisWall = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1753, 78, -78),
                new Location(Bukkit.getWorld(("arena6")), 1760, 70, -68));
        walls.put("repairTramWithStasisWall", repairTramWithStasisWall);
        fillWall("repairTramWithStasisWall");

        //// DATABOARD ROOM entry point front left
        Cuboid data1 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1762, 69, -43),
                new Location(Bukkit.getWorld(("arena6")), 1756, 76, -43));
        walls.put("data1", data1);
        fillWall("data1");

        Cuboid data2 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1762, 69, -43),
                new Location(Bukkit.getWorld(("arena6")), 1762, 76, -45));
        walls.put("data2", data2);
        fillWall("data2");

        Cuboid data3 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1762, 68, -45),
                new Location(Bukkit.getWorld(("arena6")), 1764, 72, -46));
        walls.put("data3", data3);
        fillWall("data3");

        Cuboid data4 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1764, 68, -45),
                new Location(Bukkit.getWorld(("arena6")), 1764, 72, -49));
        walls.put("data4", data4);
        fillWall("data4");

        Cuboid data5 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1764, 72, -49),
                new Location(Bukkit.getWorld(("arena6")), 1759, 66, -49));
        walls.put("data5", data5);
        fillWall("data5");

        Cuboid data6 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1759, 66, -49),
                new Location(Bukkit.getWorld(("arena6")), 1759, 70, -47));
        walls.put("data6", data6);
        fillWall("data6");

        Cuboid data7 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1757, 66, -47),
                new Location(Bukkit.getWorld(("arena6")), 1759, 70, -47));
        walls.put("data7", data7);
        fillWall("data7");

        Cuboid data8 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1757, 66, -47),
                new Location(Bukkit.getWorld(("arena6")), 1757, 72, -49));
        walls.put("data8", data8);
        fillWall("data8");

        Cuboid data9 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1753, 66, -49),
                new Location(Bukkit.getWorld(("arena6")), 1757, 72, -49));
        walls.put("data9", data9);
        fillWall("data9");

        Cuboid data10 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1753, 66, -49),
                new Location(Bukkit.getWorld(("arena6")), 1753, 72, -44));
        walls.put("data10", data10);
        fillWall("data10");

        Cuboid data11 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1751, 66, -44),
                new Location(Bukkit.getWorld(("arena6")), 1753, 72, -44));
        walls.put("data11", data11);
        fillWall("data11");

        Cuboid data12 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1752, 72, -43),
                new Location(Bukkit.getWorld(("arena6")), 1752, 63, -40));
        walls.put("data12", data12);
        fillWall("data12");

        Cuboid data13 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1752, 63, -40),
                new Location(Bukkit.getWorld(("arena6")), 1752, 72, -43));
        walls.put("data13", data13);
        fillWall("data13");

        Cuboid data14 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1754, 63, -37),
                new Location(Bukkit.getWorld(("arena6")), 1754, 69, -39));
        walls.put("data14", data14);
        fillWall("data14");

        Cuboid data15 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1747, 63, -40),
                new Location(Bukkit.getWorld(("arena6")), 1748, 72, -43));
        walls.put("data15", data15);
        fillWall("data15");

        Cuboid data16 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1752, 63, -40),
                new Location(Bukkit.getWorld(("arena6")), 1754, 69, -40));
        walls.put("data16", data16);
        fillWall("data16");


        Cuboid data19 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1749, 72, -44),
                new Location(Bukkit.getWorld(("arena6")), 1747, 66, -44));
        walls.put("data19", data19);
        fillWall("data19");

        Cuboid data20 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1756, 76, -39),
                new Location(Bukkit.getWorld(("arena6")), 1756, 69, -43));
        walls.put("data20", data20);
        fillWall("data20");

        Cuboid data21 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1744, 66, -44),
                new Location(Bukkit.getWorld(("arena6")), 1744, 76, -41));
        walls.put("data21", data21);
        fillWall("data21");

        Cuboid data22 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1744, 66, -44),
                new Location(Bukkit.getWorld(("arena6")), 1743, 76, -44));
        walls.put("data22", data22);
        fillWall("data22");

        Cuboid data23 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1763, 67, -53),
                new Location(Bukkit.getWorld(("arena6")), 1754, 76, -52));
        walls.put("data23", data23);
        fillWall("data23");

        Cuboid data24 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1766, 72, -46),
                new Location(Bukkit.getWorld(("arena6")), 1764, 76, -41));
        walls.put("data24", data24);
        fillWall("data24");

        Cuboid data25 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1763, 72, -48),
                new Location(Bukkit.getWorld(("arena6")), 1753, 75, -36));
        walls.put("data25", data25);
        fillWall("data25");

        Cuboid data26 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1753, 72, -43),
                new Location(Bukkit.getWorld(("arena6")), 1743, 76, -37));
        walls.put("data26", data26);
        fillWall("data26");

        Cuboid data27 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1746, 72, -44),
                new Location(Bukkit.getWorld(("arena6")), 1746, 76, -46));
        walls.put("data27", data27);
        fillWall("data27");


    }
    public static void createWallsChapter2() {
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
