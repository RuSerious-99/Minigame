package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PvpGame extends Game {

    private final HashMap<UUID, Integer> kills;

    public PvpGame(Minigame minigame, Arena arena) {
        super(minigame, arena);
        kills = new HashMap<>();
    }


    @Override
    public void onStart() {
        arena.sendMessage("GAME HAS STARTED! First Player to kill " + ConfigMgr.getPvpKillCountInt() + " players wins!");

        for (UUID uuid : arena.getPlayers()) {
            kills.put(uuid, 0);

            Objects.requireNonNull
                    (Bukkit.getPlayer(uuid)).getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
            Objects.requireNonNull
                    (Bukkit.getPlayer(uuid)).closeInventory();
        }
    }


    public void addKill(Player player) {
        arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! with " + kills.size() + " Thanks for playing.");
        arena.reset();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (arena.getGameName().equals("PVP")) {
            Player killer = e.getEntity().getKiller();
            if (arena.getPlayers().contains(e.getEntity().getUniqueId())
                    && arena.getPlayers().contains(Objects.requireNonNull(killer).getUniqueId())
                    && arena.getState().equals(GameState.LIVE)) {
                addKill(Objects.requireNonNull(e.getEntity().getKiller()));
            }
        }
    }

}
