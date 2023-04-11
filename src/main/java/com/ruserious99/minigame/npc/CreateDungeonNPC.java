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

public class CreateDungeonNPC {

    Minigame minigame;

    public CreateDungeonNPC(Minigame minigame) {
        this.minigame = minigame;
    }

    public void createNpc(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();
        ServerLevel level = serverPlayer.getLevel();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Dungeon");
        String signature = "h9/+O53OMa4ZnYIAHFLUtSlZsfPtG8oGklVEdEP1Bm3nNlVArymUOSNivNjg0p/tcHIyzGInQYNgmhzFq5tDhQtcdvJ1b5Vqkc4wzfEddHexaOYTGrTPlOZQsJV/JypyCiRBYNNTE/D31GSr5vfABGlCxymywZs9z6OyoAkGw3/54t0ZsGNeHM7E+S7YswlNz/0wRm/B2KgdmjGagyerJUuiv8yKYgBf6xlpXJHK6FxgIHVWFUQCg0PU372ad3yOeUGNeTSGRudqpqn/+xZigmS6OdCiWc2UPrRUiuxju4+k891bf96L1WeOcHpvSf/A937NDxe6+kbEq7BadQWYapq4rE3Ec0ItwhMwDt/9kOPbhJdzJRo/e19iSvYd0gDVCEd+smQnAav3GJlRsfXqw6LV1W4bu6zayyWyHkp12FSO14fzBDWa+WT+2eDc42IceSQZU2+634Qx6ekqS44uF0qjVoeZWTPhqKHFcpXFWID7ZfaJP8S5fwjmTimoydHaEQs8Lmc9aHsw3H2voXA/hx8zlwFFqFub1pIiE/O7WOS8/kJyL88cY7hFyBfpB4TssHZBU/v/U2iYNH5ryq6otT/WxzJ/06OqRcD57PO5hSfurIP+52J0xFAIaE3ojq8mzXX+myCzf49odw5/ava65kbsvo9qkMyZbvP+UjhoKXE=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxMDA3Nzc2OTkwNSwKICAicHJvZmlsZUlkIiA6ICJiYzRlZGZiNWYzNmM0OGE3YWM5ZjFhMzlkYzIzZjRmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YWNhNjgwYjIyNDYxMzQwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJkYTg4M2FkNmE4NTcxZDYwMWRiODAzYzdhYTdiMGQyOGExZmNmZmFkZWYxNzg5ZmEyNzdlZjE1MTRhZjIxYzYiCiAgICB9CiAgfQp9";

        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer dungeonNPC = new ServerPlayer(Objects.requireNonNull(server), level, gameProfile);

        dungeonNPC.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        NpcPacketMgr pm = new NpcPacketMgr(minigame, dungeonNPC);
        pm.addNPCPackets();

        minigame.getNPCs().put(dungeonNPC.getId(), dungeonNPC);
        SaveNpcs saveNpcs = new SaveNpcs(dungeonNPC, texture, signature);
        saveNpcs.saveNpcData();

    }
}
