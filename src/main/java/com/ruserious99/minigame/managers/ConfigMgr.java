package com.ruserious99.minigame.managers;

import com.ruserious99.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigMgr {

    private static FileConfiguration config;

    public static void setupConfig(Minigame minigame){
        ConfigMgr.config = minigame.getConfig();
        minigame.saveDefaultConfig();
    }

    public static int getBlockNpcX(){
        return config.getInt("blockgameNpc.x");
    }
    public static int getBlockNpcY(){
        return config.getInt("blockgameNpc.y");
    }
    public static int getBlockNpcZ(){
        return config.getInt("blockgameNpc.z");
    }

    public static int getPvpNpcX(){
        return config.getInt("pvpNpc.x");
    }
    public static int getPvpNpcY(){
        return config.getInt("pvpNpc.y");
    }
    public static int getPvpNpcZ(){
        return config.getInt("pvpNpc.z");
    }

    public static int      getPvpKillCountInt(){return config.getInt("pvpgame-player-kill-count"); }
    public static int      getBlockGameBlocksToBreakInt(){return config.getInt("blockgame-blocks-to-break-count"); }
    public static String   getWorldArenasSource(){return config.getString("world-source-arenas"); }
    public static int      getRequiredPlayers(){
        return config.getInt("required-players");
    }
    public static int      getCountdownSeconds(){
        return config.getInt("countdown-seconds");
    }
    public static Location getLobbySpawn(){
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("lobby-spawn.world"))),
                config.getDouble("lobby-spawn.x"),
                config.getDouble("lobby-spawn.y"),
                config.getDouble("lobby-spawn.z"),
                (float) config.getDouble("lobby-spawn.yaw"),
                (float) config.getDouble("lobby-spawn.pitch"));
    }
}



