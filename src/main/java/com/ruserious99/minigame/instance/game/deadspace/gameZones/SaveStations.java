package com.ruserious99.minigame.instance.game.deadspace.gameZones;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SaveStations {


    private SaveStations() {
    }

    public static Location getSpawnLocation(String progress) {

        switch (progress) {
            case "spawn" -> {
                return new Location(Bukkit.getWorld("arena6"), 1739.177, 86, -49.566);
            }
            case "enterBoarding" -> {
                return new Location(Bukkit.getWorld("arena6"), 1789.914, 86, -63.475);
            }
            case "dataBoard" -> {
                return new Location(Bukkit.getWorld("arena6"),  1785.689, 74, -53.040);
            }
        }
        return null;
    }
}
