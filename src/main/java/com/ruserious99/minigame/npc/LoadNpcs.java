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
import java.util.UUID;

public class LoadNpcs {

    private final Minigame minigame;
    private final Player player;


    public LoadNpcs(Minigame minigame, Player player) {
        this.minigame = minigame;
        this.player = player;
    }
    public void loadNPCs(Location location, GameProfile profile){
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();

        ServerPlayer npc = new ServerPlayer(Objects.requireNonNull(server), level, profile);
        npc.setPos(location.getX(), location.getY(), location.getZ());

        NpcPacketMgr pm = new NpcPacketMgr(minigame, npc);
        pm.addNPCPackets();

        minigame.getNPCs().put(npc.getId(), npc);
    }

    public void loadNpc(){
        FileConfiguration file = DataMgr.getConfig();
        Objects.requireNonNull(file.getConfigurationSection("data")).getKeys(false).forEach(npc ->{
            Location location = new Location(Bukkit.getWorld(Objects.requireNonNull(file.getString("data." + npc + ".world"))),
                    file.getInt("data." + npc + ".x"), file.getInt("data." + npc + ".y"), file.getInt("data." + npc + ".z"));
            location.setPitch((float) file.getDouble("data." + npc + ".p"));
            location.setYaw((float) file.getDouble("data." + npc + ".yaw"));

            String name = file.getString("data." + npc + ".name");
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
            gameProfile.getProperties().put("textures", new Property("textures", file.getString("data." + npc + ".text"), file.getString("data." + npc + ".signature")));

            loadNPCs(location, gameProfile);

        });
    }
}
