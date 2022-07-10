package com.ruserious99.minigame.listeners;

import com.mojang.datafixers.util.Pair;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.managers.DataMgr;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import com.ruserious99.minigame.npc.LoadNpcs;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConnectListener implements Listener {

    private final Minigame minigame;

    public ConnectListener(Minigame minigame) {
        this.minigame = minigame;
    }

   @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(ConfigMgr.getLobbySpawn());
        if (DataMgr.getConfig().contains("data")) {

            for (Player player : Bukkit.getOnlinePlayers()) {
                for (ServerPlayer p : minigame.getNPCs().values()) {
                    NpcPacketMgr mgr = new NpcPacketMgr(minigame, p);
                    mgr.removePacket(player);
                }
            }
            LoadNpcs load = new LoadNpcs(minigame, e.getPlayer());
            load.loadNpc();
        }
}
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        World world = Objects.requireNonNull(e.getTo()).getWorld();

        if (Objects.requireNonNull(world).getName().equals("world")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (ServerPlayer p : minigame.getNPCs().values()) {
                            NpcPacketMgr mgr = new NpcPacketMgr(minigame, p);
                            mgr.removePacket(player);
                        }
                        LoadNpcs load = new LoadNpcs(minigame, player);
                        load.loadNpc();
                    }
                }
            }.runTaskLater(minigame, 20);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws IOException {
        Arena arena = minigame.getArenaMgr().getArena(e.getPlayer());
        if (arena != null) {
            arena.removePlayer(e.getPlayer());
        }
    }

}
