package com.ruserious99.minigame.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.DataMgr;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class LoadNpcs {

    private final Minigame minigame;
    private final Player player;

    public LoadNpcs(Minigame minigame, Player player) {
        this.minigame = minigame;
        this.player = player;
    }

    public void loadNPCs() {
        for (ServerPlayer npc : minigame.getNPCs().values()) {
            NpcPacketMgr pm = new NpcPacketMgr(minigame, npc);
            pm.addNPCPackets();
        }
    }

    public void loadOnServerStartNpc() {
        FileConfiguration file = DataMgr.getConfig();
        Set<String> id = Objects.requireNonNull(file.getConfigurationSection("data")).getKeys(false);

        Objects.requireNonNull(file.getConfigurationSection("data")).getKeys(false).forEach(npc -> {

            String name = file.getString("data." + npc + ".name");
            String textures = file.getString("data." + npc + ".text");
            String signature = file.getString("data." + npc + ".signature");

            CraftPlayer craftPlayer = (CraftPlayer) player;
            ServerPlayer serverPlayer = craftPlayer.getHandle();

            MinecraftServer server = serverPlayer.getServer();
            ServerLevel level = serverPlayer.getLevel();

            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
            gameProfile.getProperties().put("textures", new Property("textures", textures, signature));

            ServerPlayer npcToAdd = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);
            npcToAdd.setPos(file.getDouble("data." + npc + ".x"), file.getDouble("data." + npc + ".y"), file.getDouble("data." + npc + ".z"));

            minigame.getNPCs().put(npcToAdd.getId(), npcToAdd);
            SaveNpcs saveNpcs = new SaveNpcs(npcToAdd, textures, signature);
            saveNpcs.saveNpcData();
        });

        for(String idRemove : id) {
            RemoveNpc r = new RemoveNpc(minigame);
            r.removeNpc(Integer.parseInt(idRemove));
        }

    }
}
