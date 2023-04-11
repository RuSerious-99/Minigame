package com.ruserious99.minigame.listeners.instance.game.dungeon;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.dungeon.events.ServerBroadcastEvent;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.PlayerRegionUtil;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.Titles;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class ParkourTwo implements Listener {

    private boolean isStarted;
    private boolean displayTitle;
    private final Arena arena;

    public ParkourTwo(Minigame minigame) {
        arena = minigame.getArenaMgr().getArena(4);

    }
    @EventHandler
    public void onRegionEnter(ServerBroadcastEvent event) {

        if (event.getMessage().contains("parkourTwo") && !displayTitle) {
            new Titles(ChatColor.BLUE + "Objective: Get to the teleport room " + ChatColor.YELLOW, ConfigMgr.getParkour_2(), event.getMessage(), arena);
            displayTitle = true;
        }

        if (!isStarted && event.getMessage().contains("parkourTwo")) {
            isStarted = true;
            arena.sendMessage(ChatColor.BLUE + "You Have " + ChatColor.RED + (ConfigMgr.getParkour_2() / 60) + ChatColor.BLUE + " minutes to exit the bridge and reach the Teleportation room!");
            arena.sendMessage(ChatColor.AQUA + "Hurry! you only have " + ChatColor.RED + (ConfigMgr.getParkour_2() / 60) + ChatColor.AQUA + " minutes to find it!");
            Dungeon.updateTimer(ConfigMgr.getParkour_2());
        }
    }
    @EventHandler
    public void onPlayerMoveInParkour(final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getFrom().getBlockX() == Objects.requireNonNull(event.getTo()).getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        if (PlayerRegionUtil.entered.get(player.getUniqueId()) != null) {
            if (PlayerRegionUtil.entered.get(player.getUniqueId()).contains("parkourTwo")) {
                final Block b = event.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (b.getLocation().getY() < 74.0) {
                    if (b.getType() != Material.LEGACY_STEP && b.getType() != Material.LEGACY_DOUBLE_STEP
                            && b.getType() != Material.LEGACY_STAINED_CLAY) {
                        return;
                    }
                    b.setType(Material.END_PORTAL);
                }
            }
        }
    }
}
