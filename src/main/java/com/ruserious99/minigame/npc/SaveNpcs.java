package com.ruserious99.minigame.npc;

import com.ruserious99.minigame.managers.DataMgr;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class SaveNpcs {

    private final ServerPlayer player;
    private final String texture;
    private final String signature;

    public SaveNpcs(ServerPlayer player, String texture, String signature) {

        this.player = player;
        this.texture = texture;
        this.signature = signature;
    }

    public void saveNpcData() {
        int var = player.getId();

            DataMgr.getConfig().set("data." + var + ".x", player.getBukkitEntity().getLocation().getX());
            DataMgr.getConfig().set("data." + var + ".y", player.getBukkitEntity().getLocation().getY());
            DataMgr.getConfig().set("data." + var + ".z", player.getBukkitEntity().getLocation().getZ());
            DataMgr.getConfig().set("data." + var + ".p", player.getBukkitEntity().getLocation().getPitch());
            DataMgr.getConfig().set("data." + var + ".yaw", player.getBukkitEntity().getLocation().getYaw());
            DataMgr.getConfig().set("data." + var + ".world", Objects.requireNonNull(player.getBukkitEntity().getLocation().getWorld()).getName());
            DataMgr.getConfig().set("data." + var + ".name", player.getBukkitEntity().getName());
            DataMgr.getConfig().set("data." + var + ".text", texture);
            DataMgr.getConfig().set("data." + var + ".signature", signature);

            DataMgr.saveConfig();
            DataMgr.reload();

    }
}