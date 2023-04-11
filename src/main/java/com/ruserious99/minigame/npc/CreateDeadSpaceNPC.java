package com.ruserious99.minigame.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class CreateDeadSpaceNPC {

    Minigame minigame;

    public CreateDeadSpaceNPC(Minigame minigame) {
        this.minigame = minigame;
    }

    public void createNpc(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "DeadSpace");
        String signature = "PyFK5I2yD3sJlp2JFpy0i8Ios2hdcfFZArayTU8N0jbnUlw0lT94gSUhTg4AGAYrd0AgvJRekbbrwHjxLsZs9ylQCg2AW12vdGt3EyLw2RZgySB64JeluRtmXXiyz+GMAmBB3nGIw9xWSEvwJl3Jq/Spe+dXDZTTnh5vQYtw0Aw94QXbCZVl5OcriHPHz7Z+hxTgtFo7JvzgegQyVESMXYAtrElqHNKmMEn2fj4BMih1uGC73DqSe7x30zoAlKd6p5Cv3ghWMd8rHTatMndYEYYlliyv+sT5lTutg6z2jzttmfCeQdDGVKFfbgJlSqC3p37clKDeDrKRgLEv0Fx220FGS9iqyWSgkxskTErMEcrPeXupq9TVfpqqWG6Etssp2AEL1arY7b7PMO53r1Am9ILxN9W8R5VmTnoEeh2iQ4z+g/ArL/ykmhz29RsKrjOTHAukNPybc/xqaO2gwAA9a/xb7pslSY0GkAY8BbItQlBoHihO5Ca+FkbfSL5sD8ixjdTF72BBQDUp27c6H7UJONsaGOpcEN944ngG8eDXFLYAjp7JHEZUYFKU1DXvklmD35bFktNOAgUKUv2JOxlnq9lwxCw+IPmQk86RNfHJNnl2limowxME/s2j0EzmcdDCQb/xuHJN7CPH/Mbh15jA2TGihp51McgMqrPZwzhVEvc=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxMTM0NTU0MTUyNCwKICAicHJvZmlsZUlkIiA6ICI2OTBkMDM2OGM2NTE0OGM5ODZjMzEwN2FjMmRjNjFlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ5emZyXzciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzcyZjEwMGEzOTk2OThkYWJhZTg3MGY4ODNhMDkyYjIwYTUxY2YyYjg5ZjRiMDNkYTEyYTNkY2EyZjg1ZmQxZiIKICAgIH0KICB9Cn0=";

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer deadSpaceNPC = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);

        deadSpaceNPC.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        NpcPacketMgr pm = new NpcPacketMgr(minigame, deadSpaceNPC);
        pm.addNPCPackets();

        minigame.getNPCs().put(deadSpaceNPC.getId(), deadSpaceNPC);
        SaveNpcs saveNpcs = new SaveNpcs(deadSpaceNPC, texture, signature);
        saveNpcs.saveNpcData();

    }

}
