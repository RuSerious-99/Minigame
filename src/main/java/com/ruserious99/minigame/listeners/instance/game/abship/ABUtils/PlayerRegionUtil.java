package com.ruserious99.minigame.listeners.instance.game.abship.ABUtils;

import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.HashMap;

public class PlayerRegionUtil implements Listener {

    public static HashMap<String, Long> regionFirstEnter = new HashMap<>();
    public static HashMap<Player, String> entered = new HashMap<>();
    public static HashMap<Player, String> exit    = new HashMap<>();

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        System.out.println("World loading; " + event.getWorld().getName());
    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        entered.remove(player);
        exit.remove(player);
    }

    @EventHandler
    public void quitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        entered.remove(player);
        exit.remove(player);
    }

    @EventHandler
    public void onRegionEnter(RegionEnteredEvent event) {
        System.out.println("onRegionEnter: " + event.getRegion());
        Player player = event.getPlayer();

        if(!regionFirstEnter.containsKey(event.getRegion().toString())){
            regionFirstEnter.put(event.getRegion().toString(), System.currentTimeMillis());
        }

        if (entered.containsKey(player)) {
            if(exit.containsKey(player)) {
                exit.replace(player, entered.get(player));
            }else {
                exit.put(player, entered.get(player));
            }
            entered.replace(player, event.getRegion().toString());
        } else {
            entered.put(player, event.getRegion().toString());
        }
    }

}


