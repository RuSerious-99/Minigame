package com.ruserious99.minigame;

import com.ruserious99.minigame.command.ArenaCommand;
import com.ruserious99.minigame.listeners.ClickedNPC;
import com.ruserious99.minigame.listeners.ConnectListener;
import com.ruserious99.minigame.listeners.NpcPlayerMoveEvent;
import com.ruserious99.minigame.managers.ArenaMgr;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.GameMap;
import com.ruserious99.minigame.utils.LocalGameMap;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Minigame extends JavaPlugin {

    private GameMap gameMapArena1;
    private GameMap gameMapArena2;

    private ArenaMgr arenaMgr;
    private Plugin plugin;

    public static final List<ServerPlayer> NPCs = new ArrayList<>();

    @Override
    public void onEnable() {
        ConfigMgr.setupConfig(this);
        plugin = this;

        File worldResetsFolder = new File(ConfigMgr.getWorldArenasSource(), "worldResets");

        gameMapArena1 = new LocalGameMap(worldResetsFolder, "arena1", true);
        gameMapArena2 = new LocalGameMap(worldResetsFolder, "arena2", true);

        System.out.println("[MiniGames] - Plugin by RuSerious99 Enabled!");
        new BukkitRunnable() {

            @Override
            public void run() {
                System.out.println("Delayed start of ArenaMgr");
                arenaMgr = new ArenaMgr((Minigame) plugin);
            }

        }.runTaskLater(this.plugin, 20);

        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new NpcPlayerMoveEvent(), this);

        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommand(this));

        ClickedNPC.listeningForOurNPCs(this);
    }

    public void releaseLoadArena(int id){
       arenaMgr.clearArena(id);
    }

    public GameMap getGameMapArena1() {return gameMapArena1;} //block
    public GameMap getGameMapArena2() {return gameMapArena2;} // pvp
    public ArenaMgr getArenaMgr() {return arenaMgr;}

}
