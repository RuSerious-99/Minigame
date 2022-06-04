package com.ruserious99.minigame.listeners;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GameListener implements Listener {


    private final Minigame minigame;

    public GameListener(Minigame minigame) {
        this.minigame = minigame;
    }


    /* MiniGame Specific Event */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Arena arena = minigame.getArenaMgr().getArena(e.getPlayer());
        if(arena != null && arena.getState().equals(GameState.LIVE)){
            if(arena.getState().equals(GameState.LIVE)){
                arena.getgame().addPoint(e.getPlayer());
            }
        }
    }

}
