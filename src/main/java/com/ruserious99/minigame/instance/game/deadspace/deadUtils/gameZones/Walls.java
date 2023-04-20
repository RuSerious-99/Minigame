package com.ruserious99.minigame.instance.game.deadspace.deadUtils.gameZones;

import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Walls {

    public Walls() {
    }

    public void createWalls() {

        //chapter 1
        Cuboid loadingDockWall1 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1666, 88, -63),
                new Location(Bukkit.getWorld(("arena6")), 1770, 87, -63));
        fillWall(loadingDockWall1);

        Cuboid loadingDockWall2 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1770, 88, -59),
                new Location(Bukkit.getWorld(("arena6")), 1733, 86, -56));
        fillWall(loadingDockWall2);

        Cuboid loadingDockWall3 = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1731, 86, -56),
                new Location(Bukkit.getWorld(("arena6")), 1666, 88, -59));
        fillWall(loadingDockWall3);

        Cuboid blockDoorLoading = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1775, 86, -66),
                new Location(Bukkit.getWorld(("arena6")), 1770, 88, -66));
        fillWall(blockDoorLoading);

        Cuboid blockDoorComputer = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1788, 85, -65),
                new Location(Bukkit.getWorld(("arena6")), 1786, 89, -66));
        fillWall(blockDoorComputer);


    }

    private void fillWall(Cuboid c) {
        for(Block block : c.getBlocks()){
            if(block.getType() == Material.AIR) {
                block.setType(Material.BARRIER);
            }
        }
    }
}
