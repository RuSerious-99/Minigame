package com.ruserious99.minigame.listeners;

import com.mojang.datafixers.util.Pair;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.EquipmentSlot;
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
        if (minigame.getNpcData() != null) {
            if (minigame.getNpcData().contains("data")) {
                addJoinPacket(e.getPlayer());
            }
        }
    }

    public void addJoinPacket(Player player) {
        for (ServerPlayer npcplayer : minigame.getNPCs().values()) {
            ServerPlayerConnection playerConnection = ((CraftPlayer) player).getHandle().connection;
            playerConnection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npcplayer));
            playerConnection.send(new ClientboundAddPlayerPacket(npcplayer));
            ItemStack itemInHand = new ItemStack(Material.DIAMOND_SWORD);
            playerConnection.send(new ClientboundSetEquipmentPacket(npcplayer.getBukkitEntity().getEntityId(),
                    List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(itemInHand)))));

        }

}
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        World world = Objects.requireNonNull(e.getTo()).getWorld();

        if (Objects.requireNonNull(world).getName().equals("world")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (minigame.getNpcData() != null) {
                        if (minigame.getNpcData().contains("data")) {
                            addJoinPacket(e.getPlayer());
                        }
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
