package com.ruserious99.minigame.listeners.instance.kit;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.kit.enums.CodKitType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class CodKit implements Listener {

    protected CodKitType type;
    protected UUID uuid;

    public CodKit(Minigame minigame, CodKitType type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;

        Bukkit.getPluginManager().registerEvents(this, minigame);
    }

    public void remove() {
        HandlerList.unregisterAll(this);
    }

    public abstract void atStart(Player player);
}