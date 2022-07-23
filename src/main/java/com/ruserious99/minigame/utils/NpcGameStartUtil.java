package com.ruserious99.minigame.utils;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NpcGameStartUtil {

    public static void joinGame(Minigame minigame, Player player, String game) {

        int joinArena = -1;

        //game is the display name of your NPC
        switch (game) {
            case ("Block Game") -> joinArena = 0;
            case ("PVP Game")   -> joinArena = 1;
            case ("Stronghold PVP")  -> joinArena = 3;
        }

        if (joinArena != -1) {
            Arena arena = minigame.getArenaMgr().getArena(joinArena);

            if (minigame.getArenaMgr().getArena(player) != null) {
                player.sendMessage(ChatColor.RED + " Hey im only an NPC give me a second");
                return;
            }
            if (arena.getState() == GameState.RECRUITING || arena.getState() == GameState.COUNTDOWN) {
                player.sendMessage(ChatColor.GOLD + "You are now playing in Arena " + joinArena);
                arena.addPlayer(player);
            } else {
                player.sendMessage(ChatColor.RED + "You cant join that arena right now.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Cant find Arena!");
        }
    }
}
