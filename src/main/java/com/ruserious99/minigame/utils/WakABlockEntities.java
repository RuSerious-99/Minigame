package com.ruserious99.minigame.utils;

import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import java.util.Objects;
import java.util.Random;

public class WakABlockEntities {

    public static void spawn() {
        final String s = ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString()
                + "M" + ChatColor.GREEN
                + ChatColor.BOLD + "L"
                + ChatColor.AQUA + " POLICE";
        Entity et = Objects.requireNonNull(Bukkit.getWorld("arena3")).spawnEntity(ConfigMgr.getPiratesSpawn(2), getEntity());
        et.setCustomName(s);
        et.setCustomNameVisible(true);
    }



    private static EntityType getEntity() {
        EntityType et = null;
        Random rand = new Random();
        int n = rand.nextInt((4) + 1) ;
        switch (n) {
            case 0 -> et = EntityType.BLAZE;
            case 1 -> et = EntityType.BAT;
            case 2 -> et = EntityType.SPIDER;
            case 3 -> et = EntityType.SKELETON;
            case 4 -> et = EntityType.CAVE_SPIDER;

        }
        return et;
    }
}

