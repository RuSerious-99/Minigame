package com.ruserious99.minigame.npc;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.DataMgr;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RemoveNpc {

    private final Minigame minigame;

    public RemoveNpc(Minigame minigame) {
        this.minigame = minigame;
    }

    public void removeNpc(int id,  Player player) {
        if (DataMgr.getConfig().contains("data")) {
            DataMgr.getConfig().set("data." + id, null);
            DataMgr.saveConfig();
        }
            minigame.getNPCs().remove(id);
            player.sendMessage(ChatColor.GREEN + "Success! Npc Has been removed");

    }

    public void removeNpc(int id) {
        if (DataMgr.getConfig().contains("data")) {
            DataMgr.getConfig().set("data." + id, null);
            DataMgr.saveConfig();
            minigame.getNPCs().remove(id);
        }
    }
}

