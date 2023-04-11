package com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils;

import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

public class SpawnEntities {

    private final EntityType entity;

    public SpawnEntities(Location location, EntityType entity) {
        this.entity = entity;
    }

    public void spawnEntity() {
        LivingEntity et = (LivingEntity) Objects.requireNonNull(Bukkit.getWorld("arena3")).spawnEntity(ConfigMgr.getPiratesSpawn(2), entity);
       et.setHealth(10.00);


    }
}
