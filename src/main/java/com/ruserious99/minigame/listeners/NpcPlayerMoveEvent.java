package com.ruserious99.minigame.listeners;

import com.ruserious99.minigame.Minigame;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class NpcPlayerMoveEvent implements Listener {

    Minigame minigame;

    public NpcPlayerMoveEvent(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public  void onPlayerMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        World world = e.getPlayer().getWorld();

        if (world.getName().equalsIgnoreCase("world")) {
            for (Integer Npc : minigame.getNPCs().keySet()) {
                ServerPlayer sp = minigame.getNPCs().get(Npc);

                Location l = sp.getBukkitEntity().getLocation();
                l.setDirection(e.getPlayer().getLocation().subtract(l).toVector());
                float yaw = l.getYaw();
                float pitch = l.getPitch();

                ServerGamePacketListenerImpl ps = ((CraftPlayer) player).getHandle().connection;

                ps.send(new ClientboundRotateHeadPacket(sp, (byte) ((yaw % 360) * 256 / 360)));
                ps.send(new ClientboundMoveEntityPacket.Rot(sp.getBukkitEntity().getEntityId(), (byte) ((yaw % 360.) * 256 / 360), (byte) ((pitch % 360.) * 256 / 360), false));
            }
        }
    }
}