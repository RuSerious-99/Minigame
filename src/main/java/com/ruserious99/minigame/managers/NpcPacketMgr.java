package com.ruserious99.minigame.managers;

import com.mojang.datafixers.util.Pair;
import com.ruserious99.minigame.Minigame;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NpcPacketMgr {

    private final Minigame minigame;
    private final ServerPlayer npc;

    public NpcPacketMgr(Minigame minigame, ServerPlayer npc) {
        this.minigame = minigame;
        this.npc = npc;
    }

    public void addNPCPackets(){
        for(Player player : Bukkit.getOnlinePlayers()) {
            ServerPlayerConnection playerConnection = ((CraftPlayer)player).getHandle().connection;

            playerConnection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
            playerConnection.send(new ClientboundAddPlayerPacket(npc));

            ItemStack itemInHand = new ItemStack(Material.DIAMOND_SWORD);
            playerConnection.send(new ClientboundSetEquipmentPacket(npc.getBukkitEntity().getEntityId(),
                    List.of(new Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(itemInHand)))));

            new BukkitRunnable() {
                @Override
                public void run() {
                    playerConnection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));
                }
            }.runTaskLater(minigame, 20);
        }
    }

    public void removePacket(Player player){
        ServerPlayerConnection playerConnection = ((CraftPlayer)player).getHandle().connection;
        playerConnection.send(new ClientboundRemoveEntitiesPacket(npc.getBukkitEntity().getEntityId()));
    }
}
