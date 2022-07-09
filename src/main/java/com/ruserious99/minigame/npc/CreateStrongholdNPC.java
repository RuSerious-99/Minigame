package com.ruserious99.minigame.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateStrongholdNPC {

    Minigame minigame;

    public CreateStrongholdNPC(Minigame minigame) {
        this.minigame = minigame;
    }

    public void createNpc(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Stronghold PVP");
        String signature = "ovldz0K8YDr+gNiNhYFj8YhrKc1Qwxg+zPfGDcfJWB66GKF7ndQPaf0PD7SER/O+EScdbMxYIfWGBo0XzoW4366k+5t" +
                "3x1OyaaaYHqGHrY0rwdguUywTaR6+/vnLrq41MIN5UHv7UNaUnCzPV4ruuRsC48SQvq88X8qRy4+BVpJeHtpyzmo8fAO6jyk5Z6vtQt" +
                "f5JTzwoQT/0KIxSPtAGzgS3QkvawaVSbUDfj6N5LfHwQWBUFR4hz0s4pIhUiIwavX2StEYzzKRkHbiDtzjgzajoOUbKYFeVr12rPqVH" +
                "0MN+KCtMR7HrEz8ggrl3FiwVSTBp6/RHpX4ZRtbJYojB2d6dmbgNuDw/Ui61AftL6Nmoi90ZkbFsDQJEdS+UVdA5yZsaCkmc2T983h" +
                "J2WEjpbwwMT2OqPasbqlRv2WwHht/wl5yfcbkUjI0X/5fEKn7hagn7MvCumoVYPfhzp8c1xwBc+OSST09U+/c7C61CQ/f/lnFEU04F" +
                "/qWT2zOBbUxhZyZFvI48OHx0PcqchNPx6LzyRUovlQoBD3X1bQQXI6rIGB4zkMpRp393WCi9ynuhPujQiF4XyfV1nNisRTSmK36KKS" +
                "T6Xe1p2aSKFJgPLN1BnryMReFvOqeHfIMmfLyYlxHsmJ5FySgz3q735NpzzlmZYoOgf9Zm+NsDUyqCUk=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY1NjM0MjY3MzMxMywKICAicHJvZmlsZUlkIiA6ICIwYjQ2ZDViYzYzOGE0MjZlYmNiZm" +
                "QyNjhiMWM1Yjc4YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTcGFjZW1hbjAzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKI" +
                "CAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC9" +
                "0ZXh0dXJlLzYzN2Q2N2VmYjg5YmFkMDE3YjUyZmExZTE2YmRlN2RkNzQ0ODIzOWEzM2NiNWY3MDM5YmFlMDI5MmNmZWRkN2UiCiAgICB9CiAgfQp9";

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer Stronghold = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);

        Stronghold.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        NpcPacketMgr pm = new NpcPacketMgr(minigame, Stronghold);
        pm.addNPCPackets();

        minigame.getNPCs().put(Stronghold.getId(), Stronghold);
        SaveNpcs saveNpcs = new SaveNpcs(minigame, player, texture, signature);
        saveNpcs.saveNpcData();
    }
}
