package com.ruserious99.minigame.listeners.instance.timers;

import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.CodStronghold;
import com.ruserious99.minigame.listeners.instance.game.PvpGame;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Objects;
import java.util.UUID;

public class BlockTimer {

    private boolean cancelTimer;
    private Arena arena;
    private final BossBar gameScore = Bukkit.createBossBar("Player: 00 | Player: 00", BarColor.BLUE, BarStyle.SOLID);

    public void addPlayer(Player player) {
        gameScore.addPlayer(player);
    }

    public void removePlayer(Player player) {
        gameScore.removePlayer(player);
    }

    public void removeAll() {
        for (UUID uuid : arena.getPlayers()) {
            gameScore.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
        }
    }

    public void startGameTimer(Arena arena) {
        this.arena = arena;
        BukkitRunnable runGame = new BukkitRunnable() {
            int timeLeft = gameTime();

            @Override
            public void run() {
                timeLeft += checkAddTimer(0);
                if (timeLeft == 0 || cancelTimer) {
                    cancelTimer = false;
                    if (timeLeft == 0) {
                        arena.sendMessage("Aww ran out of time. No clear winner. Thanks for playing");
                    }
                    this.cancel();
                    arena.reset();
                    return;
                }
                setGameScoreTitle(timeLeft);
                timeLeft--;
            }
        };
        runGame.runTaskTimer(arena.getMinigame(), 0L, 20L);

    }

    public int checkAddTimer(int addTime) {
      return addTime;
    }

    private int gameTime() {
        switch (arena.getId()) {
            case (0) -> {
                return ConfigMgr.getGameTimeBlock();
            } // block game
            case (1) -> {
                return ConfigMgr.getGameTimePvp();
            } // 1vs1 pvp
            case (2) -> {
                return ConfigMgr.getGameTimeWak();
            } // 1vs1 pvp
            case (3) -> {
                return ConfigMgr.getGameTimeCod();
            } // team pvp stronghold
        }
        return -1;
    }

    private void setGameScoreTitle(int timeLeft) {
        switch (arena.getId()) {
            case 0, 2 -> {
                gameScore.setTitle(ChatColor.GOLD
                        + " | " + ChatColor.BLUE + getFormattedTime(timeLeft)
                        + ChatColor.GOLD + " | ");
                gameScore.setProgress(getProgress(timeLeft, gameTime()));
            }
            case 1 -> {
                Player player0 = Bukkit.getPlayer(arena.getPlayers().get(0));
                Player player1 = Bukkit.getPlayer(arena.getPlayers().get(1));
                int killsP0 = PvpGame.kills.get(Objects.requireNonNull(player0).getUniqueId());
                int killsP1 = PvpGame.kills.get(Objects.requireNonNull(player1).getUniqueId());
                gameScore.setTitle(Objects.requireNonNull(player0).getName() + " kills = " + ChatColor.RED + killsP0 + ChatColor.GOLD
                        + " | " + ChatColor.GREEN + getFormattedTime(timeLeft)
                        + ChatColor.GOLD + " | " + ChatColor.WHITE + Objects.requireNonNull(player1).getName() + " kills = " + ChatColor.RED + killsP1);
                gameScore.setProgress(getProgress(timeLeft, ConfigMgr.getGameTimePvp()));
            }
            case 3 -> {
                gameScore.setTitle(ChatColor.RED + "RED: " + CodStronghold.redScore + ChatColor.GOLD
                        + " | " + ChatColor.GREEN + getFormattedTime(timeLeft)
                        + ChatColor.GOLD + " | " + ChatColor.BLUE + "BLUE: " + CodStronghold.blueScore);
                gameScore.setProgress(getProgress(timeLeft, ConfigMgr.getGameTimeCod()));
            }
        }
    }

    private String getFormattedTime(int time) {
        int seconds;
        int minutes;
        minutes = time / 60;
        seconds = time - (minutes * 60);

        String minutesString, secondsString;
        minutesString = minutes < 10 ? "0" + minutes : minutes + "";
        secondsString = seconds < 10 ? "0" + seconds : seconds + "";

        return minutesString + " : " + secondsString;
    }

    private double getProgress(int timeLeft, int totalTime) {
        return (double) timeLeft / (double) totalTime;
    }

    public void cancelTimer() {
        cancelTimer = true;
    }
}
