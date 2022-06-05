package com.ruserious99.minigame.kit;

import com.ruserious99.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.UUID;

public abstract class Kit implements Listener {

    protected KitType type;
    protected UUID uuid;

    public Kit(Minigame minigame, KitType type, UUID uuid){
        this.type = type;
        this.uuid = uuid;

        //register here so all kit classes have access to eventhandler ect
        Bukkit.getPluginManager().registerEvents(this, minigame);
    }

    public void remove(){
        HandlerList.unregisterAll(this);
    }
    public abstract void onStart(Player player);

    public KitType getType() {return type;}
    public UUID getUuid() {return uuid;}
}
