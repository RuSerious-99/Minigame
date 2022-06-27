package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;

public class AbandonedSpaceship extends Game{


    public AbandonedSpaceship(Minigame minigame, Arena arena) {
        super(minigame, arena);
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Defeat all enemies, Make your way through all mazes. Find the lever to teleport out ");

    }
}
