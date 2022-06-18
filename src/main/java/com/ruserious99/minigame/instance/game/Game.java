package com.ruserious99.minigame.instance.game;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener {

    protected final Arena arena;

    public Game(Minigame minigame, Arena arena) {
        this.arena = arena;
        Bukkit.getPluginManager().registerEvents(this, minigame);

    }

    public void start() {
        onStart();
    }

    public abstract void onStart();

    public void unregistar(){
        HandlerList.unregisterAll(this);
    }
}

