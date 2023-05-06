package com.ruserious99.minigame.instance.game.deadspace.deadUtils;

import com.ruserious99.minigame.instance.game.deadspace.event.DeadBroadcastEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class DeadPlayerRegionUtil implements Listener {

    public static HashMap<String, Long> deadRegionFirstEnter = new HashMap<>();
    public static HashMap<UUID, String> deadEntered = new HashMap<>();
    public static HashMap<UUID, String> deadExit = new HashMap<>();

    public static void reset() {
        deadEntered.clear();
        deadExit.clear();
        deadRegionFirstEnter.clear();
    }

    public static void deadRegionEvents(UUID uuid, String cuboidName) {

        //System.out.println("dead cubiod name = " + cuboidName);
        if (!deadRegionFirstEnter.containsKey(cuboidName)) {
            deadRegionFirstEnter.put(cuboidName, System.currentTimeMillis());
            sendRegionEnter(uuid, cuboidName);
           // System.out.println("dead Succesfully added  " + cuboidName);
        }

        if (deadEntered.containsKey(uuid)) {
            if (deadExit.containsKey(uuid)) {
                deadExit.replace(uuid, deadEntered.get(uuid));
                //System.out.println("dead Succesfully added exit  " + cuboidName);
            } else {
                deadExit.put(uuid, deadEntered.get(uuid));
               // System.out.println("dead Succesfully added to exit  " + cuboidName);
            }
            deadEntered.replace(uuid, cuboidName);
           //System.out.println("dead Succesfully replaced entered  " + cuboidName);

        } else {
            deadEntered.put(uuid, cuboidName);
             //System.out.println("dead Succesfully added to entered  " + cuboidName);
        }
    }

    private static void sendRegionEnter(UUID uuid, String cuboidName) {
        Player player = Bukkit.getPlayer(uuid);

        DeadBroadcastEvent event = new DeadBroadcastEvent(player, cuboidName);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            Bukkit.broadcastMessage(cuboidName);
        }
    }


}
