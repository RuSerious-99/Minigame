package com.ruserious99.minigame.instance.game.deadspace.gameEntities.entityConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class EntityConfigChap1 {

    private EntityConfigChap1() {
    }
    //chapter1 computer room
    public static Location c1ComputerLocation(){return new Location(Bukkit.getWorld("arena6"), 1790.700,86,-52.863);}

    //hallway to the elevator(stairs)
    public static Location firstPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1789.566,86,-48.831);}
    public static Location secondPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1789.132,86,-41.995);}
    public static Location thirdPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1794.417,86,-40.853);}
    public static Location forthPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1794.126,86,-33.728);}

    // after first weapon
    public static Location firstWeaponLocation(){return new Location(Bukkit.getWorld("arena6"), 1791.217,77,-32.466);}

    //hallway after dataBoard
    public static Location hallwayAfterDataBoardLocation(){return new Location(Bukkit.getWorld("arena6"), 1776.817,74,-45.642);}

    //Stasis tram room
    public static Location stasisTramOnFirstEnter(){return new Location(Bukkit.getWorld("arena6"), 1751.155,74,-78.835);}
    public static Location stasisTramSecondDelayed(){return new Location(Bukkit.getWorld("arena6"), 1751.928,74,-75.723);}
    public static Location stasisTramAfterInteract(){return new Location(Bukkit.getWorld("arena6"), 1762.393,74,-79.799);}

    //fast door to dataBoard
    public static Location fastDoortoDataBoardZombie(){return new Location(Bukkit.getWorld("arena6"), 1767.470,71,-63.968);}
    public static Location fastDoortoDataBoardSpider(){return new Location(Bukkit.getWorld("arena6"), 1766.056,71,-67.339);}

    public static Location bayEnterStraightAHead(){return new Location(Bukkit.getWorld("arena6"), 1757.449,70,-39.334);}
    public static Location bayEnterRightBack(){return new Location(Bukkit.getWorld("arena6"), 1758.264,67,-49.595);}
    public static Location bayAfterBoardRoomEnter(){return new Location(Bukkit.getWorld("arena6"), 1757.581,70,-37.392);}
    public static Location bayTurnRightAhead(){return new Location(Bukkit.getWorld("arena6"), 1765.661,68,-52.700);}
    public static Location bayEnterRightLeft(){return new Location(Bukkit.getWorld("arena6"), 1756.973,67,-49.448);}


    public static void spawnEntityZombie(Location location) {
        (Objects.requireNonNull(Bukkit.getWorld("arena6"))).spawnEntity(location, EntityType.ZOMBIE);
    }
    public static void spawnEntityCreeper(Location location) {
        (Objects.requireNonNull(Bukkit.getWorld("arena6"))).spawnEntity(location, EntityType.CREEPER);
    }
    public static void spawnEntitySpider(Location location) {
        (Objects.requireNonNull(Bukkit.getWorld("arena6"))).spawnEntity(location, EntityType.SPIDER);
    }
}
