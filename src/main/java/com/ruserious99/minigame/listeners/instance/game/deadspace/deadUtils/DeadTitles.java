package com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class DeadTitles {


    private final Arena arena;
    protected final String first;
    private String sub;

    public DeadTitles(String first, String sub , Arena arena) {
        this.first = first;
        this.sub = sub;
        this.arena = arena;

        new BukkitRunnable() {
            public void run() {
                arena.sendTitle(ChatColor.BLUE +first, ChatColor.YELLOW + sub, 10, 60,10);

            }
        }.runTaskLater(Minigame.getInstance(), 20L);
    }
}
