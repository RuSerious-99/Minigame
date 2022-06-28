package com.ruserious99.minigame.listeners.instance.game.abship;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.Game;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Objects;

public class AbandonedSpaceship extends Game {

    private InGameTimer inGameTimer;

    public AbandonedSpaceship(Minigame minigame, Arena arena) {
        super(minigame, arena);
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Defeat all enemies, Make your way through all mazes. Find the lever to teleport out ");
        removeBlocks();
        setInGameTimer(ConfigMgr.getHangerCountdownSeconds());
        arena.sendTitle(ChatColor.BLUE + "GAME HAS STARTED!", ChatColor.GRAY + "Get to the Hanger",10,100,10);
    }

    private void removeBlocks() {
        int z = 91;
        for (int x = 0; x >= -4; --x) {
            for (int y = 79; y <= 83; ++y) {
                Block b = Objects.requireNonNull(ConfigMgr.getAbandonedSpawn().getWorld()).getBlockAt(x, y, z);
                b.setType(Material.AIR);
            }
        }
        arena.sendMessage(ChatColor.GREEN + "The door has opened! Its Time to begin");
        arena.sendMessage(ChatColor.BLUE + "Air Lock will be Destroyed in " + ChatColor.RED
                + (ConfigMgr.getHangerCountdownSeconds() / 60) + ChatColor.BLUE + " minutes");
    }
    public void setInGameTimer(int time) {
        inGameTimer = new InGameTimer(arena, time);}
    }

