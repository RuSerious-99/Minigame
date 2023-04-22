package com.ruserious99.minigame.instance.game.deadspace.gameEntities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class EntityConfig {


    private EntityConfig() {
    }

    //chapter1
    public static Location c1ComputerLocation(){return new Location(Bukkit.getWorld("arena6"), 1790.700,86,-52.863);}
    public static EntityType c1ComputerEntity(){return EntityType.ZOMBIE;}

    public static Location firstPartHallLocation(){return new Location(Bukkit.getWorld("arena6"), 1789.566,86,-48.831);}
    public static EntityType firstPartHallEntity(){return EntityType.ZOMBIE;}


    public static void spawnEntity(Location location, EntityType entity) {
        Objects.requireNonNull(Bukkit.getWorld("arena6")).spawnEntity(location, entity);
    }
}
