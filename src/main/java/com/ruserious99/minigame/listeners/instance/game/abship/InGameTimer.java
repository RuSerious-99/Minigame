package com.ruserious99.minigame.listeners.instance.game.abship;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class InGameTimer extends BukkitRunnable {

    Arena abandonedSpaceship;
    public int seconds;

    public InGameTimer(Arena arena, int seconds) {
        this.abandonedSpaceship = arena;
        this.seconds = seconds;
        runTaskTimer(Minigame.getPlugin(), 0, 20);
    }

    @Override
    public void run() {
        if (seconds == 0) {
            abandonedSpaceship.sendMessage(ChatColor.RED + "Dungeon failed!! time is up.");
            abandonedSpaceship.sendTitle(ChatColor.RED + "Dungeon failed!!", ChatColor.GRAY + "time is up.",10, 60, 10);
            abandonedSpaceship.reset();
            cancel();
            return;
        }
        if (seconds == 480) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 8" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 360) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 6" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 240) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 4" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 120) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 2" + ChatColor.AQUA + " minutes left.");
        } else if (seconds == 60) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 1" + ChatColor.AQUA + " minute left.");
        } else if (seconds == 30) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 30" + ChatColor.AQUA + " seconds left.");
        } else if (seconds <= 10) {
            if (seconds == 1) {
                abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + " 1" + ChatColor.AQUA + " second left.");
            } else {
                abandonedSpaceship.sendMessage(ChatColor.AQUA + "Hurry! Only " + ChatColor.RED + seconds + ChatColor.AQUA + " seconds! left");
            }
        }
        seconds--;
        if (abandonedSpaceship.getPlayers().size() < ConfigMgr.getRequiredPlayers()) {
            abandonedSpaceship.sendMessage(ChatColor.AQUA + "Too many players left Resetting game");
            abandonedSpaceship.reset();
            cancel();
        }

    }
    public void updateSeconds(int time){
        this.seconds = time;
    }
}