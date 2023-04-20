package com.ruserious99.minigame.instance.game.dungeon;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class InGameTimer extends BukkitRunnable {

    private final Arena arena;
    private int seconds;

    public InGameTimer(Arena arena, int seconds) {
        this.arena = arena;
        this.seconds = seconds;
        runTaskTimer(arena.getMinigame(), 0, 20);
    }

    public void updateSeconds(int time){
        this.seconds = time;
    }

    @Override
    public void run() {
        if (seconds == 0) {
            cancel();
            if (arena.getState().equals(GameState.COMPLETE)) {
                arena.sendMessage(ChatColor.RED + "Dungeon has been completed");
            } else {
                arena.sendMessage(ChatColor.RED + "Dungeon failed!! time is up.");
            }
            arena.reset();
        }
        if (seconds == 480) {
            arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 8" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 360) {
            arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 6" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 240) {
            arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 4" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 120) {
            arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 2" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 60) {
            arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 1" + ChatColor.AQUA + " minute left.");
        } else if (seconds == 30) {
            arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 30" + ChatColor.AQUA + " seconds left.");
        } else if (seconds <= 10) {
            if (seconds == 1) {
                arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 1" + ChatColor.AQUA + " second left.");
            } else {
                arena.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + seconds + ChatColor.AQUA + " seconds! left");
            }
        }
        seconds--;
        if (arena.getPlayers().size() == 0) {
            cancel();
            arena.setState(GameState.RECRUITING);
            arena.reset();
        }
    }
}
