package com.ruserious99.minigame.instance.game.dungeon;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.dungeon.events.ServerBroadcastEvent;
import com.ruserious99.minigame.instance.game.dungeon.utilities.PlayerRegionUtil;
import com.ruserious99.minigame.instance.game.dungeon.utilities.Titles;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class ParkourOne implements Listener {

    private boolean isStarted;
    private boolean displayTitle;
    private final Arena arena;

    public ParkourOne(Minigame minigame) {
        this.arena = minigame.getArenaMgr().getArena(4);
    }

    @EventHandler
    public void onRegionEnter(ServerBroadcastEvent event) {
        Player player = event.getPlayer();

        if(event.getMessage().equals("parkourOne")) {
            System.out.println("recieved maze message " + player + " " + event.getMessage());
        }

        if (event.getMessage().contains("parkourOne") && !displayTitle) {
            new Titles(ChatColor.BLUE + "Objective: Get to the Maze: " + ChatColor.YELLOW, ConfigMgr.getParkour_1(), event.getMessage(), arena);
            displayTitle = true;
        }

        if (!isStarted && event.getMessage().contains("parkourOne")) {
            arena.sendMessage(ChatColor.GREEN + "Make it through the vent shaft: ");
            arena.sendMessage(ChatColor.AQUA + "Hurry! you only have " + ChatColor.RED + (ConfigMgr.getParkour_1() / 60) + ChatColor.AQUA + " minutes to get to the Maze!");
            isStarted = true;

            Dungeon.updateTimer(ConfigMgr.getParkour_1());
        }
    }

    @EventHandler
    public void onPlayerMoveInParkour(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (PlayerRegionUtil.entered.get(player.getUniqueId()) == null) {
            return;
        }

        if (event.getFrom().getBlockX() == Objects.requireNonNull(event.getTo()).getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        if (PlayerRegionUtil.entered.get(player.getUniqueId()).contains("parkourOne")) {
            final Block b = event.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
            if (b.getLocation().getY() < 74.0) {
                if (b.getType() != Material.LEGACY_STEP && b.getType() != Material.LEGACY_DOUBLE_STEP
                        && b.getType() != Material.LEGACY_STAINED_CLAY) {
                    return;
                }
                b.setType(Material.LEGACY_ENDER_PORTAL);
            }
        }
    }

}

