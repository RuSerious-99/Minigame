package com.ruserious99.minigame.listeners.instance.game.dungeon;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.dungeon.events.ServerBroadcastEvent;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.PlayerRegionUtil;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.Titles;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Maze implements Listener {

    private boolean isStarted;
    private boolean displayTitle;
    private final Minigame minigame;
    private final Arena arena;

    private Block mazeLever;
    private final List<Location> mazeLeverLocations = new ArrayList<>();

    public Maze(Minigame minigame) {
        this.minigame = minigame;
        arena = minigame.getArenaMgr().getArena(4);
    }

    @EventHandler
    public void onRegionEnter(ServerBroadcastEvent event) {
        Player player = event.getPlayer();

        if (event.getMessage().contains("maze") && !displayTitle) {
            new Titles(ChatColor.BLUE + "Objective: Complete the maze and get to the Bridge " + ChatColor.YELLOW, ConfigMgr.getMaze(), event.getMessage(), arena);
            displayTitle = true;
        }

        if (!isStarted && event.getMessage().contains("maze")) {
            isStarted = true;
            addLeverLocations(player);
            mazeLever = getRandomMazeLever(player);
            arena.sendMessage(ChatColor.GREEN
                    + "This is the ship's generator room. The exit seems to have been sealed in the chaos,"
                    + " you will need to locate the correct lever to switch the power back on and continue. Be careful! The floor seems extremely unstable.");
            arena.sendMessage(ChatColor.AQUA + "Hurry! you only have " + ChatColor.RED + (ConfigMgr.getMaze() / 60) + ChatColor.AQUA + " minutes to find it!");
            Dungeon.updateTimer(ConfigMgr.getMaze());
        }
    }

    private void addLeverLocations(Player player) {
        mazeLeverLocations.add(new Location(player.getWorld(), -59.0, 79.0, 183.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -61.0, 79.0, 171.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -74.0, 79.0, 180.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -77.0, 79.0, 208.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -75.0, 79.0, 208.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -57.0, 79.0, 166.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -60.0, 79.0, 176.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -81.0, 79.0, 189.0));
        mazeLeverLocations.add(new Location(player.getWorld(), -81.0, 79.0, 181.0));

    }

    private Block getRandomMazeLever(Player player) {
        return player.getWorld().getBlockAt(mazeLeverLocations.get(new Random().nextInt(mazeLeverLocations.size())));
    }

    @EventHandler
    public void interactMazeLever(final PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!player.getWorld().getName().contains("arena5")) {
            return;
        }
        if (PlayerRegionUtil.entered.get(player.getUniqueId()).contains("maze")) {

            if (mazeLever != null) {
                if (event.getClickedBlock().equals(mazeLever)) {
                    player.getWorld().getBlockAt(-76, 79, 172).setType(Material.AIR);
                    player.getWorld().getBlockAt(-76, 80, 172).setType(Material.AIR);
                    mazeLever = null;
                    arena.sendMessage(ChatColor.GREEN + "As "
                            + event.getPlayer().getName()
                            + " pulls the lever, you hear the generator hum to life. The exit hatch is unlocked!");
                } else {
                    Block lever = event.getClickedBlock();
                    if (lever.getType() == Material.LEVER) {
                        arena.sendMessage(ChatColor.RED + event.getPlayer().getName()
                                + " has pulled the lever at " + lever.getLocation().getBlockX() + "x, "
                                + lever.getLocation().getBlockY() + "y, " + lever.getLocation().getBlockZ()
                                + "z, but nothing seems to happen.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMoveInMaze(PlayerMoveEvent event) throws IOException {
        Player mazePlayer = event.getPlayer();

        if (PlayerRegionUtil.entered.get(mazePlayer.getUniqueId()).equals("maze")) {
            if (event.getFrom().getBlockX() == Objects.requireNonNull(event.getTo()).getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY()
                    && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
                return;
            }

            Block b = event.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock();

            if (b.getType() == Material.END_PORTAL) {
                mazePlayer.sendMessage(ChatColor.RED + "Dungeon has Failed!!");
                arena.removePlayer(mazePlayer);
            }
            if (b.getType() == Material.QUARTZ_BLOCK) {
                b.setType(Material.LIGHT_BLUE_STAINED_GLASS);
                return;
            }
            if (b.getType() == Material.LIGHT_BLUE_STAINED_GLASS) {
                b.setType(Material.BLACK_STAINED_GLASS);
                return;
            }
            if (b.getType() == Material.BLACK_STAINED_GLASS) {
                b.setType(Material.LEGACY_ENDER_PORTAL);
            }
        }
    }
}
