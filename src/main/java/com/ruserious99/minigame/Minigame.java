package com.ruserious99.minigame;

import com.ruserious99.minigame.command.ArenaCommand;
import com.ruserious99.minigame.listeners.ClickedNPC;
import com.ruserious99.minigame.listeners.ConnectListener;
import com.ruserious99.minigame.listeners.NpcPlayerMoveEvent;
import com.ruserious99.minigame.managers.ArenaMgr;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.npc.CreateBlockNPC;
import com.ruserious99.minigame.npc.CreatePvpNPC;
import com.ruserious99.minigame.npc.Stronghold;
import com.ruserious99.minigame.utils.GameMap;
import com.ruserious99.minigame.utils.LocalGameMap;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*create new Mini game
* make new folder(arena#) with your game world and copy it to resetWorlds folder for restoring

* Create a new game class in: listeners, instance, game
* in main class(MiniGame) make an instance of Gamemap with getter to restore worlds
* in Arena class add your new game class to 2 switch statements
*
* make a new npc for access to new game
 TODO make instructions to add an npc
* */

public final class Minigame extends JavaPlugin {

    private GameMap gameMapArena1;
    private GameMap gameMapArena2;
    private GameMap gameMapArena4;

    private ArenaMgr arenaMgr;
    private Plugin plugin;

    private final HashMap<Integer, ServerPlayer> NPCs = new HashMap<>();

    @Override
    public void onEnable() {
        ConfigMgr.setupConfig(this);
        plugin = this;

        File worldResetsFolder = new File(ConfigMgr.getWorldArenasSource(), "worldResets");

        gameMapArena1 = new LocalGameMap(worldResetsFolder, "arena1", true);
        gameMapArena2 = new LocalGameMap(worldResetsFolder, "arena2", true);
        gameMapArena4 = new LocalGameMap(worldResetsFolder, "arena4", true);

        new BukkitRunnable() {
            @Override
            public void run() {
                arenaMgr = new ArenaMgr((Minigame) plugin);
            }
        }.runTaskLater(plugin, 20);


        Bukkit.getPluginManager().registerEvents(new ConnectListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new NpcPlayerMoveEvent(this), this);

        Objects.requireNonNull(getCommand("arena")).setExecutor(new ArenaCommand(this));

        ClickedNPC.listeningForOurNPCs(this);

    }

    public void releaseLoadArena(int id){
       arenaMgr.clearArena(id);
    }
    public HashMap<Integer, ServerPlayer> getNPCs() {return NPCs;}
    public GameMap  getGameMapArena1() {return gameMapArena1;} //block
    public GameMap  getGameMapArena2() {return gameMapArena2;} // pvp
    public GameMap  getGameMapArena4() {return gameMapArena4;} // cod stronghold
    public ArenaMgr getArenaMgr()      {return arenaMgr;}

}
