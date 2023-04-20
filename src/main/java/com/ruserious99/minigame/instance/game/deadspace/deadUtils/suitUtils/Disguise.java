package com.ruserious99.minigame.instance.game.deadspace.deadUtils.suitUtils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ruserious99.minigame.Minigame;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Disguise {
    private String name;
    private String texture;
    private String signature;

    private String originalName;
    private String originalTexture;
    private String originalSignature;

    public Disguise(String name, String texture, String signature) {
        this.name = name;
        this.texture = texture;
        this.signature = signature;
    }


    public boolean apply(Player player) {
        GameProfile gameProfile = null;

        try {
            gameProfile = NMSHelper.getInstance().getGameProfile(player);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (gameProfile == null) {
            return false;
        }

        this.originalName = player.getName();
        Property originalTextures = NMSHelper.getInstance().getTexturesProperty(gameProfile);
        if (originalTextures != null) {
            this.originalTexture = originalTextures.getValue();
            this.originalSignature = originalTextures.getSignature();
        }

        gameProfile.getProperties().clear();
        gameProfile.getProperties()
                .put("textures",
                        new Property(
                                "textures",
                                texture,
                                signature
                        ));


        //broadcast new player profile info
        ServerPlayer sp = ((CraftPlayer)player).getHandle();
        new BukkitRunnable() {
            @Override
            public void run() {
                MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
                server.getPlayerList().broadcastAll(new ClientboundPlayerInfoRemovePacket(List.of(sp.getUUID())));
                server.getPlayerList().broadcastAll(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, sp));
                server.getPlayerList().broadcastAll(new ClientboundAddPlayerPacket(sp));
            }
        }.runTaskLater(Minigame.getInstance(), 20);


        //apply temporary name
        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(gameProfile, name);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (name != null) {
            player.setDisplayName(name);
        }

        return true;
    }

    public boolean remove(Player player) {
        this.name = originalName;
        this.texture = originalTexture;
        this.signature = originalSignature;
        return apply(player);
    }

}