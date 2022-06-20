package com.ruserious99.minigame.listeners;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class ConnectListener implements Listener {

    private final Minigame minigame;

    public ConnectListener(Minigame minigame) {
        this.minigame = minigame;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        System.out.println("on join connect listener");
        e.getPlayer().getInventory().clear();
        e.getPlayer().teleport(ConfigMgr.getLobbySpawn());
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws IOException {
        Arena arena = minigame.getArenaMgr().getArena(e.getPlayer());
        if(arena != null){
            arena.removePlayer(e.getPlayer());
        }
    }
}
