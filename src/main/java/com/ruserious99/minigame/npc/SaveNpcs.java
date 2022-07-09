package com.ruserious99.minigame.npc;

import com.ruserious99.minigame.Minigame;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SaveNpcs {

    private final Minigame minigame;
    private final Player player;
    private final String texture;
    private final String signature;

    public SaveNpcs(Minigame minigame, Player player, String texture, String signature) {
        this.minigame = minigame;
        this.player = player;
        this.texture = texture;
        this.signature = signature;
    }

    public void saveNpcData(){
        int var = 1;
            if (minigame.getNpcData().contains("data")) {
                var = Objects.requireNonNull
                                (minigame.getNpcData().getConfigurationSection("data"))
                        .getKeys(false).size() + 1;
            }

        minigame.getNpcData().set("data." + var + ".x", player.getLocation().getX());
        minigame.getNpcData().set("data." + var + ".y", player.getLocation().getY());
        minigame.getNpcData().set("data." + var + ".z", player.getLocation().getZ());
        minigame.getNpcData().set("data." + var + ".p", player.getLocation().getPitch());
        minigame.getNpcData().set("data." + var + ".yaw", player.getLocation().getYaw());
        minigame.getNpcData().set("data." + var + ".world", Objects.requireNonNull(player.getLocation().getWorld()).getName());
        minigame.getNpcData().set("data." + var + ".name", player.getName());
        minigame.getNpcData().set("data." + var + ".text", texture);  //did not work from npc try later
        minigame.getNpcData().set("data." + var + ".text", signature); //same as above

        minigame.saveData();
    }
}
