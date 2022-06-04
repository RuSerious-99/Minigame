package com.ruserious99.minigame.instance;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {


    private Minigame minigame;
    private Arena arena;
    private int countdownSeconds;

    public Countdown(Minigame miniGame, Arena arena) {
        this.minigame= miniGame;
        this.arena = arena;
        this.countdownSeconds = ConfigMgr.getCountdownSeconds();
    }

    public void start(){
        arena.setState(GameState.COUNTDOWN);
        runTaskTimer(minigame, 0, 20);
    }

    @Override
    public void run() {
        if(countdownSeconds == 0){
            cancel();
            arena.start();
            return;
        }
        if(countdownSeconds <= 10 || countdownSeconds % 15 == 0){
            arena.sendMessage(ChatColor.GREEN + "Game starting in " + countdownSeconds
            + "seconds " + (countdownSeconds == 1 ? "" : "s") + ".");
        }

        arena.sendTitle(ChatColor.GREEN + "Game starting in " + countdownSeconds
                + "seconds " + (countdownSeconds == 1 ? "" : "s"), ChatColor.GRAY + "Until Game starts");
        countdownSeconds--;
    }
}
