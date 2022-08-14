package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.listeners.instance.timers.BlockTimer;
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

    public static final HashMap<UUID, Integer> kills = new HashMap<>();

    public PvpGame(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);

        arena.sendMessage("GAME HAS STARTED! First Player to kill " + ConfigMgr.getPvpKillCountInt() + " players wins!");

        BlockTimer timer = new BlockTimer(arena);


        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            minigame.getScoreboards().updateScoreboard(arena, player);
            timer.addPlayer(Bukkit.getPlayer(uuid));
            kills.put(uuid, 0);

            Objects.requireNonNull
                    (Bukkit.getPlayer(uuid)).getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));

            if (player != null) {
                player.teleport(ConfigMgr.getPvpExtraSpawnlocations(getRand()));
            }

            Objects.requireNonNull
                    (Bukkit.getPlayer(uuid)).closeInventory();

        }
        timer.startGameTimer(arena);
    }

    private void endGame() {
        kills.clear();
        arena.reset();
    }

    private int getRand() {
        int range = (4) - 1;
        return (int) (Math.random() * range);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();

        if (killed.getWorld().getName().equals("arena2")) {
            if (arena.getPlayers().contains(e.getEntity().getUniqueId())
                    && arena.getPlayers().contains(Objects.requireNonNull(killer).getUniqueId())
                    && arena.getState().equals(GameState.LIVE)) {
                for (UUID uuid : kills.keySet()) {
                    if (uuid.equals(killer.getUniqueId())) {
                        int v = kills.get(killer.getUniqueId());
                        kills.put(uuid, v + 1);

                        if (v == ConfigMgr.getPvpKillCountInt()) {
                            arena.sendMessage(ChatColor.GOLD + Objects.requireNonNull(Bukkit.getEntity(uuid)).getName()
                                    + " WINS!!! with " + ConfigMgr.getPvpKillCountInt() + " kills,  Thanks for playing.");
                            killed.spigot().respawn();
                            endGame();
                            return;
                        }
                    }
                }
            }
        }
        killed.spigot().respawn();

        Bukkit.getScheduler().scheduleSyncDelayedTask(minigame, () -> {
            if (arena.getState().equals(GameState.LIVE)) {
                killed.teleport(ConfigMgr.getPvpExtraSpawnlocations(getRand()));
                killed.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
            }
        }, 20L);
    }
}
