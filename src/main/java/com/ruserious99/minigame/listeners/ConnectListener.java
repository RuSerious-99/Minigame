package com.ruserious99.minigame.listeners;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.npc.Stronghold;
import com.ruserious99.minigame.npc.CreateBlockNPC;
import com.ruserious99.minigame.npc.CreatePvpNPC;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Objects;

public class ConnectListener implements Listener {

    private final Minigame minigame;

    public ConnectListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(ConfigMgr.getLobbySpawn());
    }

/*
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        World world = Objects.requireNonNull(e.getTo()).getWorld();

        if (Objects.requireNonNull(world).getName().equals("world")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    CreateBlockNPC.execute(e.getPlayer());
                    CreatePvpNPC.execute(e.getPlayer());
                    Stronghold.execute(e.getPlayer());
                }
            }.runTaskLater(minigame, 20);
        }
    }*/

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws IOException {
        Arena arena = minigame.getArenaMgr().getArena(e.getPlayer());
        if (arena != null) {
            arena.removePlayer(e.getPlayer());
        }
    }

}
