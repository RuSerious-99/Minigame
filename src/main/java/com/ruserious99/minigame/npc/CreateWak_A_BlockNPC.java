package com.ruserious99.minigame.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class CreateWak_A_BlockNPC {
    Minigame minigame;

    public CreateWak_A_BlockNPC(Minigame minigame) {
        this.minigame = minigame;
    }

    public void createNpc(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Wak_A_Block Game");
        String signature = "wJPswoHZEuhiicWCZ6gZ1fqnOiHY1MbJ6PPA+lBgMX6IJ94eVx/OwOj6KRXW9EZP3XE/tkp15ymuuxNuufhDGGrtacnVPVsPSfB8RVhGHuPz/ttCaFLO2KmP+iG0P99PTpp9HvcD/HxDSL1zuc/wK2TRDflOmR2b8DD1pjw156Y0rpe1IPCVtTDzw4T8KQ4eJ0GQPCXI7QCNNbeIWlRktixMd/RFPJvKGh6VEX+UP32sF1VZhVVrotZliueFzuYG4Oz6Xr6qBuejd97TbgoMjubB8Ym59kFIKgIr7vBJ4MF9KLn30L3k0FIg8Ab6qjiD8cMo50br83eLUu/L2kYQK708nUvkGhjypy3/fqgPDMZldpq+OKxYJpTPyHsoby6AJ8HOWWKbnj35X4G50+bG1TwmZLPHPC7+jgGfrNJpar/rkf2Rnirm+jfOMMUaaO5fJo25rFEQExpEUboKtYgoHeXVDJqWR+lpmoTggdTRCR9duPLoGmbVhVaiLCe8HxiRPdtzlDOFbL75Wscg4L2zbFTd76/sBrNd+SgBuDUvBh8S2/uLsrKOe5ioBbtj6jKmRR+QoRS6VWqw+hAQzPoVXzC3vstgzi0mFVt/A0RyQ+1HXZBD4vn7GhaacBXWeQ1RSok6k8Cfu1CxE+Ou/XySyXB+t2ZYmAOT8OxFg37J52M=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY1NTYxNzk2MzQzMiwKICAicHJvZmlsZUlkIiA6ICJlOTUzODllY2MzZWE0MTRkYjVmNzYxZjEyZWY1Mzg2NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSdVNlcmlvdXM5OSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYTNiOWEyMDgyNTdjMTE3OWY1MTc0MDdjOWNhOGVlMGMxZjczZjczODg5YjNlMzY1NzNmZjU2ZDIwNjc2MTNiIgogICAgfQogIH0KfQ==";

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer WakABlockNPC = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);

        WakABlockNPC.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        NpcPacketMgr pm = new NpcPacketMgr(minigame, WakABlockNPC);
        pm.addNPCPackets();

        minigame.getNPCs().put(WakABlockNPC.getId(), WakABlockNPC);
        SaveNpcs saveNpcs = new SaveNpcs(WakABlockNPC, texture, signature);
        saveNpcs.saveNpcData();
    }
}