package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils.DeadTitles;
import com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils.event.DeadBroadcastEvent;
import com.ruserious99.minigame.listeners.instance.game.deadspace.playerskin.DisguiseManager;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.PlayerRegionUtil;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class DeadSpace extends Game {

    private final HashMap<Cuboid, String> cuboids;
    private boolean displayTitle;
    private final DisguiseManager disguiseManager;

    public DeadSpace(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        this.disguiseManager = new DisguiseManager(minigame);
        this.cuboids = new HashMap<>();
    }

    @Override
    public void onStart() {
        createRegions();
        arena.setState(GameState.LIVE);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : arena.getPlayers()) {
                    for (Map.Entry<Cuboid, String> entry : cuboids.entrySet()) {
                        Cuboid cuboid = entry.getKey();
                        String value = entry.getValue();
                        if(cuboid.contains(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getLocation())) {
                            DeadPlayerRegionUtil.deadRegionEvents(uuid, value);
                        }
                    }
                }
            }
        }.runTaskTimer(Minigame.getInstance(), 0L, 10L);
    }


    private void createRegions() {
        Cuboid boarding = new Cuboid(
                new Location(Bukkit.getWorld("arena6"), 1770, 79, -35),
                new Location(Bukkit.getWorld(("arena6")), 1666, 99, -99));
        cuboids.put(boarding, "boarding");
    }

    @Override
    public void endGame() {
        UUID player = arena.getPlayers().get(0);
        disguiseManager.deleteDisguise(Bukkit.getPlayer(player));
        DeadPlayerRegionUtil.reset();
        arena.reset();
    }

    @EventHandler
    public void onResourceStatus(PlayerResourcePackStatusEvent e) throws IOException {
        if(e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)){
            arena.removePlayer(e.getPlayer());
        }
        if(e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)){
            arena.removePlayer(e.getPlayer());
        }
    }
    @EventHandler
    public void onRegionEnter(DeadBroadcastEvent event) {
        if (event.getMessage().contains("boarding") && !displayTitle) {
            arena.sendMessage("Welcome to the Ishamura.");
            new DeadTitles(ChatColor.BLUE + "Chapter 1; NEW ARRIVALS", ChatColor.YELLOW + "Objective: get to the Medical Bay", arena);
            displayTitle = true;
        }
    }
}
