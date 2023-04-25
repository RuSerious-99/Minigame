package com.ruserious99.minigame.instance.game.deadspace.gameEntities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class EntityConfig {

    public static Entity chapter1_zombie;

    private EntityConfig() {
    }

    //chapter1 computer room
    public static Location c1ComputerLocation(){return new Location(Bukkit.getWorld("arena6"), 1790.700,86,-52.863);}
    public static EntityType c1ComputerEntity(){return EntityType.ZOMBIE;}

    //hallway to the elevator(stairs)
    public static Location firstPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1789.566,86,-48.831);}
    public static EntityType firstPartHallEntity(){return EntityType.ZOMBIE;}
    public static Location secondPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1789.132,86,-41.995);}
    public static EntityType secondPartHallEntity(){return EntityType.ZOMBIE;}
    public static Location thirdPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1794.417,86,-40.853);}
    public static EntityType thirdPartHallEntity(){return EntityType.ZOMBIE;}
    public static Location forthPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1794.126,86,-33.728);}
    public static EntityType forthPartHallEntity(){return EntityType.ZOMBIE;}


    public static void spawnEntityChapter1(Location location, EntityType entity) {
        chapter1_zombie = (Objects.requireNonNull(Bukkit.getWorld("arena6"))).spawnEntity(location, entity);
        chapter1_zombie.setCustomNameVisible(false);
        chapter1_zombie.setCustomName("chapter1_zombie");
    }

}
