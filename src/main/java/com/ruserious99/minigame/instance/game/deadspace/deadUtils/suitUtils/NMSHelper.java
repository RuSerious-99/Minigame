package com.ruserious99.minigame.instance.game.deadspace.deadUtils.suitUtils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class NMSHelper {
    @Getter
    private static final NMSHelper instance = new NMSHelper();
    private NMSHelper() {}

    public Object getHandle(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return player.getClass().getMethod("getHandle").invoke(player);
    }

    public GameProfile getGameProfile(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ServerPlayer sp = ((CraftPlayer)player).getHandle();
        return sp.getBukkitEntity().getProfile();
    }

    public Property getTexturesProperty(GameProfile profile) {
        Optional<Property> texturesProperty = profile.getProperties().get("textures").stream().findFirst();
        return texturesProperty.orElse(null);
    }
}
