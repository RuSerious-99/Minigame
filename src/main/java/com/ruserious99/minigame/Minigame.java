package com.ruserious99.minigame;

import com.ruserious99.minigame.command.ArenaCommand;
import com.ruserious99.minigame.listeners.ConnectListener;
import com.ruserious99.minigame.listeners.GameListener;
import com.ruserious99.minigame.managers.ArenaMgr;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Minigame extends JavaPlugin {

    private ArenaMgr arenaMgr;


    @Override
    public void onEnable() {
        ConfigMgr.setupConfig(this);
        arenaMgr = new ArenaMgr(this);

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);

        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommand(this));
    }

    public ArenaMgr getArenaMgr() {return arenaMgr;}
}
