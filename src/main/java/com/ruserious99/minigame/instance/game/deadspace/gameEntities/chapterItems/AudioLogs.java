package com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems;

import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class AudioLogs {


    private final static World world = Bukkit.getServer().getWorld("arena6");

    private AudioLogs() {
    }

    public static Location ventWarning(){return new Location(world,1797.993, 74, -49.046);}
    public static Location shoot_the_limbs(){return new Location(world,1774.874, 74, -43.142);}// after tram hall

    public static void dropAudioLog(Location location) {
        World world = Bukkit.getServer().getWorld("arena6");
        (Objects.requireNonNull(world)).dropItem(location, ItemsManager.audioLog);
    }


}
