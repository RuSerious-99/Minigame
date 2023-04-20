package com.ruserious99.minigame.listeners;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.managers.DataMgr;
import com.ruserious99.minigame.npc.LoadNpcs;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
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
        Player player = e.getPlayer();
        World world = e.getPlayer().getWorld();
        player.teleport(ConfigMgr.getLobbySpawn());
        showNPCs(world, player);

    }


    private void showNPCs(World world, Player player) {
        if (Objects.requireNonNull(world).getName().equals("world")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(DataMgr.getConfig().contains("data")) {
                        LoadNpcs load = new LoadNpcs(minigame, player);
                        if(minigame.getNPCs().isEmpty()){
                            load.loadOnServerStartNpc();
                        }
                        load.loadNPCs();
                    }
                }
            }.runTaskLater(minigame, 40);
        }
    }
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        World world = Objects.requireNonNull(e.getTo()).getWorld();
        showNPCs(world, player);
    }

    //some games have their own
    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws IOException {
        Arena arena = minigame.getArenaMgr().getArena(e.getPlayer());
        if (arena != null) {
            arena.removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onResourceStatus(PlayerResourcePackStatusEvent e){
        if(e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)){
           e.getPlayer().kickPlayer("You must accept pack to play");
        }
        if(e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)){
            e.getPlayer().kickPlayer("Oops! looks like the download failed");
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e){
        Player killed = e.getEntity();
        String world = killed.getWorld().getName();
        if ("arena1".equals(world) && minigame.getArenaMgr().getArena(0).equals(GameState.LIVE)) {
            killed.spigot().respawn();
            killed.teleport(ConfigMgr.getAfterDeathSpawn(0)); // 5 is arena id
        }
        if ("arena6".equals(world)) {
            AttributeInstance maxHealthAttribute = killed.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            maxHealthAttribute.setBaseValue(20.0);
            killed.spigot().respawn();
            killed.teleport(ConfigMgr.getAfterDeathSpawn(5)); // 5 is arena id
        }
    }


}
