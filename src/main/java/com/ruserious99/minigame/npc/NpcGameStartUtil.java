package com.ruserious99.minigame.npc;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;

public class NpcGameStartUtil {

    public static void joinGame(Minigame minigame, Player player, String game) throws IOException {

        int joinArena = -1;

        //game is the display name of your NPC
        switch (game) {
            case ("Block Game") -> joinArena = 0;
            case ("PVP Game")   -> joinArena = 1;
            case ("Wak_A_Block Game")   -> joinArena = 2;
            case ("Stronghold PVP")  -> joinArena = 3;
            case ("Dungeon")  -> joinArena = 4;

            //todo check player persistant data for chapter start
            case ("DeadSpace")  -> joinArena = 5;


        }
        if (joinArena != -1) {

            Arena arena = minigame.getArenaMgr().getArena(joinArena);
            if (minigame.getArenaMgr().getArena(player) != null) {
                return;
            }
            if (arena.getState() == GameState.RECRUITING || arena.getState() == GameState.COUNTDOWN) {
                arena.addPlayer(player);
            } else {
                player.sendMessage(ChatColor.RED + "You cant join that arena right now.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Cant find Arena!");
        }
    }
}
