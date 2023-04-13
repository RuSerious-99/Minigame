package com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils;

import com.ruserious99.minigame.listeners.instance.game.deadspace.playerskin.DisguiseManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class DisguiseListener implements Listener {
    private final DisguiseManager disguiseManager;

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        disguiseManager.deleteDisguise(event.getPlayer());
    }
}
