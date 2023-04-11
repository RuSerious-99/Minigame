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
        String signature = "hS6o3CE4P4T4BWHX5NcERq5335DDQ51ticxEladqqaSa0FZXRpoMwE3EJQDNePW7Mc8uDwo3ZAYMzlAsDsko2DZfQXB8vnvEdKJKNYrVJuCZM0R2BYqEB5j8ilFtijIu2H14PkmIr9rSzFwPRsS/w6UxKQ8e3NxBLGK67Up6RkSycCKR+6mCKTE5HnwJ70etXCMqm/v+zTTOmBbdWd55ORnrdO9318C5BGaSTSPeM/gQmi5qIO7BulYTUrebe6+EVeaktDvDgJhoqKclH1oJvrxCa/vcIJuMeY/mND1uUSxiFHXSkJ2wClu9EUwPUByx8m8d+orDYqJTxscATfUhvl0xfHz/hN8rYomuwtbKDbaPzWTEnXnWJTklRZWI5hjb+4qnvYo2CztTmI0WjTcK9cWD8SdZYEJHlXScFhtaYwYJ56NWcBZcTG+MkbhzGGimypvk1zD1ZxFqzOJqw8DbBGb1FzLicLw4EgJARq25NdHSSMBGk5xHK7dt5xXMCiF21FREZdMjLcFM+u5ic/FQzBZpMb2IA8OPdtFDR6cyfmhGZLpWv98jr/7PXPO+lkvtHX+M5lsx3iRR3yQf8ex1863FW1kZRDNl4NV/yjt6Hc3i15YPx7r4uDpWZmPnIKOBkQ76DttTWNbP908soesZn7jgZ5xj2QfC10vDEUUZSZ4=";
        String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY4MDM1Mjg1Njg2NiwKICAicHJvZmlsZUlkIiA6ICIwZWQ2MDFlMDhjZTM0YjRkYWUxZmI4MDljZmEwNTM5NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJOZWVkTW9yZUFjY291bnRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY1MzhhYjAzMjdjYWQzNmMwNWYyOTkzNmI5ZGM3MDg3ODJhM2I4NDc0MGNlNGExYmM5YzM3NWNkMzc1OTBhMSIKICAgIH0KICB9Cn0=";

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