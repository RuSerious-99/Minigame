package com.ruserious99.minigame.instance.game.deadspace.deadUtils.gameZones;

import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.HashMap;

public class GameAreas {

    private final HashMap<Cuboid, String> cuboids;

    public GameAreas() {
        this.cuboids = new HashMap<>();
    }

    public void createRegions() {

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

    }
    public HashMap<Cuboid, String> getCuboids() {return cuboids;}


}
