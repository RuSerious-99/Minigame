package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener {

    protected final Minigame minigame;
    protected final Arena arena;
    protected final Scoreboards scoreboards;

    public Game(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        this.minigame = minigame;
        this.arena = arena;
        this.scoreboards = scoreboards;
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

