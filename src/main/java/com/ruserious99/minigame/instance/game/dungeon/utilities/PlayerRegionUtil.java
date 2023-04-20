package com.ruserious99.minigame.instance.game.dungeon.utilities;

import com.ruserious99.minigame.instance.game.dungeon.events.ServerBroadcastEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerRegionUtil implements Listener{

    public static HashMap<String, Long> regionFirstEnter = new HashMap<>();
    public static HashMap<UUID, String> entered = new HashMap<>();
    public static HashMap<UUID, String> exit = new HashMap<>();

    public static void reset() {
        entered.clear();
        exit.clear();
        regionFirstEnter.clear();
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        UUID player = Objects.requireNonNull(event.getEntity().getPlayer()).getUniqueId();
        entered.remove(player);
        exit.remove(player);
    }

    @EventHandler
    public void quitEvent(PlayerQuitEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        entered.remove(player);
        exit.remove(player);
    }

    public static void regionEvents(UUID uuid, String cuboidName) {

       // System.out.println("playerregionUtil cubiod name = " + cuboidName);
        if (!regionFirstEnter.containsKey(cuboidName)) {
            regionFirstEnter.put(cuboidName, System.currentTimeMillis());
            sendRegionEnter(uuid, cuboidName);
            //System.out.println("playerregionUtil Succesfully added  " + cuboidName);
        }

        if (entered.containsKey(uuid)) {
            if (exit.containsKey(uuid)) {
                exit.replace(uuid, entered.get(uuid));
               // System.out.println("playerregionUtil Succesfully added exit  " + cuboidName);
            } else {
                exit.put(uuid, entered.get(uuid));
               // System.out.println("playerregionUtil Succesfully added to exit  " + cuboidName);
            }
            entered.replace(uuid, cuboidName);
            //System.out.println("playerregionUtil Succesfully replaced entered  " + cuboidName);

        } else {
            entered.put(uuid, cuboidName);
           // System.out.println("playerregionUtil Succesfully added to entered  " + cuboidName);
        }
    }

    private static void sendRegionEnter(UUID uuid, String cuboidName) {
        Player player = Bukkit.getPlayer(uuid);

        ServerBroadcastEvent event = new ServerBroadcastEvent(player, cuboidName);
        Bukkit.getPluginManager().callEvent(event);

        if(!event.isCancelled()){
            Bukkit.broadcastMessage(cuboidName);
        }
    }

}