package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.timers.BlockTimer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener {

    protected final Minigame minigame;
    protected final Arena arena;
    protected final BlockTimer timer;

    public Game(Minigame minigame, Arena arena, BlockTimer timer) {
        this.minigame = minigame;
        this.arena = arena;
        this.timer = timer;
        Bukkit.getPluginManager().registerEvents(this, minigame);

    }
    public void start() {
        onStart();
    }

    public abstract void onStart();

    public void unregister(){
        HandlerList.unregisterAll(this);
    }
}

