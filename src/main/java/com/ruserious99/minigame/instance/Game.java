package com.ruserious99.minigame.instance;

import com.ruserious99.minigame.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Game {

    private final Arena arena;
    private final HashMap<UUID, Integer> points;   /* game specifc */

    public Game(Arena arena) {
        this.arena = arena;
        points = new HashMap<>();    /* game specifc */
    }

    public void start() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Fill in objective of minigame");

        for (UUID uuid : arena.getKits().keySet()) {
            arena.getKits().get(uuid).onStart(Bukkit.getPlayer(uuid));
        }

        /* game specifc */
        for (UUID uuid : arena.getPlayers()) {
            points.put(uuid, 0);
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).closeInventory();
        }
    }

    /* game specific functionality */
    public void addPoint(Player player) {

        int playerpoints = points.get(player.getUniqueId()) + 1;

        if (playerpoints == 20) {
            /* end game */
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! Thanks for playing.");
            arena.reset(true);
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 point");
        points.replace(player.getUniqueId(), playerpoints);

    }
}

