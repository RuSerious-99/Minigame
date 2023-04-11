package com.ruserious99.minigame.listeners.instance;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable implements Listener {


    private final Minigame minigame;
    private final Arena arena;
    private int countdownSeconds;

    public Countdown(Minigame miniGame, Arena arena) {
        this.minigame = miniGame;
        this.arena = arena;
        this.countdownSeconds = ConfigMgr.getCountdownSeconds();
    }

    public void start() {
        arena.setState(GameState.COUNTDOWN);
        runTaskTimer(minigame, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            arena.start();
            return;
        }
        if (countdownSeconds <= 10 || countdownSeconds % 15 == 0) {
            arena.sendMessage(ChatColor.GREEN + "Game starting in " + ChatColor.YELLOW + countdownSeconds
                    + ChatColor.GREEN + " second" + (countdownSeconds == 1 ? "" : "s") + ".");
        }

        arena.sendTitle(ChatColor.BLUE + " Starts in " + ChatColor.YELLOW + countdownSeconds
                + ChatColor.BLUE + " second" + (countdownSeconds == 1 ? "" : "s"), ChatColor.GRAY + "Get Ready!!", 10, 10, 10);
        countdownSeconds--;
    }

    public Arena getArena() {
        return arena;
    }
}
