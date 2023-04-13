package com.ruserious99.minigame;

import com.ruserious99.minigame.command.ArenaCommand;
import com.ruserious99.minigame.command.ArenaTab;
import com.ruserious99.minigame.listeners.ConnectListener;
import com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils.DisguiseListener;
import com.ruserious99.minigame.listeners.instance.game.deadspace.playerskin.DisguiseManager;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.managers.ArenaMgr;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.managers.DataMgr;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import com.ruserious99.minigame.npc.ClickedNPC;
import com.ruserious99.minigame.npc.NpcPlayerMoveEvent;
import com.ruserious99.minigame.utils.GameMap;
import com.ruserious99.minigame.utils.LocalGameMap;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/*create new Mini game
* make new folder(arena#) with your game world and copy it to resetWorlds folder for restoring
* Create a new game class in: listeners, instance, game
* in main class(MiniGame) make an instance of Gamemap with getter to restore worlds
* add game info to config and ConfigMgr
* in Arena class add your new game to 3 switch statements
* write your game in the class you made:)
*
* make a new npc for access to new game
* create a new class in the npc folder <Create<game>> copy and paste from other Create class and change name and game
* *** dont forget new texture and signature strings for npc (in Create class)
* add to command : ArenaCommand and ArenaTab
* add to NpcGameStartUtil

* */


public final class Minigame extends JavaPlugin implements Listener {

    private static Minigame instance;
    private DisguiseManager disguiseManager;

    private GameMap gameMapArena1;
    private GameMap gameMapArena2;
    private GameMap gameMapArena3;
    private GameMap gameMapArena4;
    private GameMap gameMapArena5;
    private GameMap gameMapArena6;


    private Scoreboards scoreboards;
    private ArenaMgr arenaMgr;
    private Plugin plugin;

    private final HashMap<Integer, ServerPlayer> NPCs = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigMgr.setupConfig(this);
        Minigame.instance = this;
        plugin = this;

        this.disguiseManager = new DisguiseManager(this);

        DataMgr.setupConfig();
        DataMgr.getConfig().options().copyDefaults(true);

        File worldResetsFolder = new File(ConfigMgr.getWorldArenasSource(), "worldResets");

        gameMapArena1 = new LocalGameMap(worldResetsFolder, "arena1", true);
        gameMapArena2 = new LocalGameMap(worldResetsFolder, "arena2", true);
        gameMapArena3 = new LocalGameMap(worldResetsFolder, "arena3", true);
        gameMapArena4 = new LocalGameMap(worldResetsFolder, "arena4", true);
        gameMapArena5 = new LocalGameMap(worldResetsFolder, "arena5", true);
        gameMapArena6 = new LocalGameMap(worldResetsFolder, "arena6", true);


        new BukkitRunnable() {
            @Override
            public void run() {
                arenaMgr    = new ArenaMgr((Minigame) plugin);
                scoreboards = new Scoreboards();
            }
        }.runTaskLater(plugin, 20);

        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getPluginManager().registerEvents(new DisguiseListener(disguiseManager), this);
        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new NpcPlayerMoveEvent(this), this);
        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommand(this));
        Objects.requireNonNull(getCommand("arena")).setTabCompleter(new ArenaTab());

        ClickedNPC.listeningForOurNPCs(this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (ServerPlayer p : NPCs.values()) {
                NpcPacketMgr mgr = new NpcPacketMgr(this, p);
                mgr.removePacket(player);
            }
        }
        getLogger().info("Plugin disabled!");
    }

    public void releaseLoadArena(int id) {
        arenaMgr.clearArena(id);
    }

    public HashMap<Integer, ServerPlayer> getNPCs() {return NPCs;}
    public GameMap getGameMapArena1() {return gameMapArena1;} //block
    public GameMap getGameMapArena2() {return gameMapArena2; } // pvp
    public GameMap getGameMapArena3() {return gameMapArena3; } // wakAblock
    public GameMap getGameMapArena4() {return gameMapArena4;} // cod stronghold
    public GameMap getGameMapArena5() {return gameMapArena5;} // dungeon
    public GameMap getGameMapArena6() {return gameMapArena6;} // deadspace
    public ArenaMgr getArenaMgr() {return arenaMgr;}
    public Scoreboards getScoreboards() {return scoreboards;}
    public Plugin getPlugin() {return plugin;}
    public static Minigame getInstance() {return instance;}

    public DisguiseManager getDisguiseManager() {
        return disguiseManager;
    }
}
