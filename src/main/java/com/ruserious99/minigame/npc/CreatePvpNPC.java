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

public class CreatePvpNPC {

    Minigame minigame;

    public CreatePvpNPC(Minigame minigame) {
        this.minigame = minigame;
    }

    public void CreateNpc(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "PVP Game");
        String signature = "Gp+3iFlzOZlb5hbWikdswyqvTQTsf+mJMzS6jr2WKuguJx/otUz2MUMPWCdT0d9cwKcOBH8entv75G/pRvdPtPaZ7B" +
                "BIEUhhirFOzb6eUEf8JHsuxirTEEMIOpsEvfbP7Q73a/wUwLPYJsZy27YIn/RdA5SlcPtqEkK/BMoFhu/P7eKlkQo2zJVic9GbjPJe" +
                "xhpmscQBtGiHfkuQnDgDb0j/NaimP6IV8lE97Kj9oBDZtxiq602doJGDBZF9V4YBT2/1tqfl2nNShnHzipCV5j/EwSdgU4cw+0TZ/I" +
                "k3PhS72wvpc09V45+i7vaaD1rWnnEc5blpCUWUdFukfeLAKIGw5UB5Y3A8VShXcn55rWtecovL6jOJzaS7BGjIlNhsDVXLUvglta3k" +
                "Yn3rffhLm2KdIHZ/EtNLcN4QNwXlUBxsFOD7r543N5e7XaTMZutsFPOet+rbQCI61fihEzxHtOuVdT5rN7Vre15WdzUFg+0YcpSwhq" +
                "avSvKDXOBtQ/h1X6rkmokCIOmW16hioN1R/5E1GbRRcnTGZl/zYl/hZjSEAdlb0ZU+rVhBUwBUuKsgQCMXXRBI5hHdaOvW6KZL8Jpr" +
                "VGzzXpUR0isw8rJMEpDIAkrzGsM0DGRm6R9VbFglpGx5QvkZcp80ghQsoNFl5Oz/i3IUpml3xcmlLDvEBqo=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY1NjE2OTU1MTgwNywKICAicHJvZmlsZUlkIiA6ICIyNTlmZTJhZDBiZDA0NTlhOTc5N" +
                "GI1Y2QxYTNmN2I2NSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsZW93ZW56ZWxsIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSw" +
                "KICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ld" +
                "C90ZXh0dXJlL2RiMzA2NzcwODM0ODYzMDM0MWM2MTc5NzBkM2YwZGViMDUxYmE2ZWNlZGRiYjMxOTQ5NjE2YjZiNDYyNjljYTIiCiA" +
                "gICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzN" +
                "DBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9";

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer pvpNPC = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);

        pvpNPC.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        NpcPacketMgr pm = new NpcPacketMgr(minigame, pvpNPC);
        pm.addNPCPackets();

        minigame.getNPCs().put(pvpNPC.getId(), pvpNPC);
        SaveNpcs saveNpcs = new SaveNpcs(pvpNPC, texture, signature);
        saveNpcs.saveNpcData();
    }
}
