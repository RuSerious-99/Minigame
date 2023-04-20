package com.ruserious99.minigame.instance.game.dungeon.utilities;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Titles {

    private final Arena arena;
    protected final String first;
    private final long time;

    public Titles(String first, long time, String region, Arena arena) {
        this.first = first;
        this.time = time;
        this.arena = arena;

        new BukkitRunnable() {
            public void run() {
                long showTimeLeft = System.currentTimeMillis() - PlayerRegionUtil.regionFirstEnter.get(region);
                arena.sendTitle(ChatColor.BLUE +first, ChatColor.YELLOW + showTime(showTimeLeft), 10, 60,10);

            }
        }.runTaskLater(Minigame.getInstance(), 20L);
    }

    private String showTime(long milliseconds) {
        long timeleft = (time*1000) - milliseconds;
        int seconds = (int) ((timeleft / 1000) % 60);
        int minutes = (int) ((timeleft / 1000) / 60);
        return String.format("%d Minutes %d Seconds",minutes, seconds);
    }

}
