package com.ruserious99.minigame.listeners.instance.game.dungeon;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.Game;
import com.ruserious99.minigame.listeners.instance.game.dungeon.*;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.PlayerRegionUtil;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.Titles;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Dungeon extends Game {

    private static InGameTimer inGameTimer;
    private final HashMap<Cuboid, String> cuboids;

    public Dungeon(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        cuboids = new HashMap<>();
    }

    @Override
    public void onStart() {
        createRegions();
        setInGameTimer(ConfigMgr.getHangerCountdownSeconds());
        registerListeners();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : arena.getPlayers()) {
                    for (Map.Entry<Cuboid, String> entry : cuboids.entrySet()) {
                        Cuboid cuboid = entry.getKey();
                        String value = entry.getValue();
                        if(cuboid.contains(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getLocation())) {
                            PlayerRegionUtil.regionEvents(uuid, value);
                        }
                    }
                }
            }
        }.runTaskTimer(Minigame.getInstance(), 0L, 60L);

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }
        arena.setState(GameState.LIVE);
        removeBlocks();
    }

    private void createRegions() {
       Cuboid startGame = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), -41, 76, 53),
                new Location(Bukkit.getWorld(("arena5")), 21, 88, 91));
        cuboids.put(startGame, "startGame");

        Cuboid hallway = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), 1, 77, 98),
                new Location(Bukkit.getWorld(("arena5")), -4, 83, 149));
        cuboids.put(hallway, "hallway");

        Cuboid hanger = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), -45, 119, 157),
                new Location(Bukkit.getWorld(("arena5")), 28, 79, 229));
        cuboids.put(hanger, "hanger");

        Cuboid parkourOne = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), 25, 88, 232),
                new Location(Bukkit.getWorld(("arena5")), -56, 94, 245));
        cuboids.put(parkourOne, "parkourOne");

        Cuboid maze = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), -50, 78, 209),
                new Location(Bukkit.getWorld(("arena5")), -82, 87, 166));
        cuboids.put(maze, "maze");

        Cuboid bossTwo = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), -89, 78, 163),
                new Location(Bukkit.getWorld(("arena5")), -150, 100, 230));
        cuboids.put(bossTwo, "bossTwo");

        Cuboid parkourTwo = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), -208, 100, 144),
                new Location(Bukkit.getWorld(("arena5")), -168, 73, 239));
        cuboids.put(parkourTwo, "parkourTwo");

        Cuboid teleportRoom = new Cuboid(
                new Location(Bukkit.getWorld("arena5"), -269, 106, 215),
                new Location(Bukkit.getWorld(("arena5")), -218, 70, 167));
        cuboids.put(teleportRoom, "teleportRoom");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BossOne(minigame), minigame.getPlugin());
        Bukkit.getPluginManager().registerEvents(new ParkourOne(minigame), minigame.getPlugin());
        Bukkit.getPluginManager().registerEvents(new Maze(minigame), minigame.getPlugin());
        Bukkit.getPluginManager().registerEvents(new BossBridge(minigame), minigame.getPlugin());
        Bukkit.getPluginManager().registerEvents(new ParkourTwo(minigame), minigame.getPlugin());
        Bukkit.getPluginManager().registerEvents(new TeleportationRoom(minigame), minigame.getPlugin());
    }

    @Override
    public void endGame() {
        PlayerRegionUtil.reset();
        arena.reset();
    }

    private void removeBlocks() {
        int z = 91;
        for (int x = 0; x >= -4; --x) {
            for (int y = 79; y <= 83; ++y) {
                Block b = (Objects.requireNonNull(Bukkit.getWorld("arena5")).getBlockAt(x, y, z));
                b.setType(Material.AIR);
            }
        }
        new Titles("Get to the Hanger in ", ConfigMgr.getHangerCountdownSeconds(), "startGame", arena);

        arena.sendMessage(ChatColor.GREEN + "The door has opened! Its Time to begin");
        arena.sendMessage(ChatColor.BLUE + "Air Lock will be Destroyed in " + ChatColor.RED
                + (ConfigMgr.getHangerCountdownSeconds() / 60) + ChatColor.BLUE + " minutes");
    }

    public void setInGameTimer(int time) {
        inGameTimer = new InGameTimer(arena, time);
    }

    public static void updateTimer(int time) {
        inGameTimer.updateSeconds(time);
    }

}
