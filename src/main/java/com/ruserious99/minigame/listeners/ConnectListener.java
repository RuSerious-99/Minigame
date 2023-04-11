package com.ruserious99.minigame.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.managers.DataMgr;
import com.ruserious99.minigame.npc.LoadNpcs;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
}
