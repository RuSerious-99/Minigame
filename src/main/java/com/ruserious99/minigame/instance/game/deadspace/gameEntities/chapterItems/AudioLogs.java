package com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;

public class AudioLogs {


    private final static World world = Bukkit.getServer().getWorld("arena6");

    private AudioLogs() {
    }

    public static void audioLog(Location location, ItemStack itemStack) {
        World world = Bukkit.getServer().getWorld("arena6");
        (Objects.requireNonNull(world)).dropItem(location, itemStack);
    }

    public static Location bensonAudioLog(){return new Location(world,1775, 73, -42);}// after tram hall

}
