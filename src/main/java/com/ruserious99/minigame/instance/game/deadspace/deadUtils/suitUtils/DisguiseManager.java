package com.ruserious99.minigame.instance.game.deadspace.deadUtils.suitUtils;

import com.ruserious99.minigame.Minigame;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DisguiseManager {
    private final Minigame plugin;
    private final Map<UUID, Disguise> playerIdToDisguise = new HashMap<>();

    public DisguiseManager(Minigame plugin) {
        this.plugin = plugin;
    }


    public void applyDisguise (Player player, String name, String texture, String signature){
            if (hasDisguise(player)) {
                deleteDisguise(player);
            }

            Disguise disguise = new Disguise(name, texture, signature);
            playerIdToDisguise.put(player.getUniqueId(), disguise);
            disguise.apply(player);
        }

        public void deleteDisguise (Player player){
            if (!hasDisguise(player)) return;
            Disguise existing = getDisguise(player).get();
            existing.remove(player);
            playerIdToDisguise.remove(player.getUniqueId());
        }

        public Optional<Disguise> getDisguise (Player player){
            return Optional.ofNullable(
                    playerIdToDisguise.get(player.getUniqueId())
            );
        }

        public boolean hasDisguise (Player player){
            return playerIdToDisguise.containsKey(player.getUniqueId());
        }
    }
