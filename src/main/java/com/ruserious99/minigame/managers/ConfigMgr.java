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


    //NPCs
    public static int getAbandonedNpcX(){
        return config.getInt("abandonedNpc.x");
    }
    public static int getAbandonedNpcY(){
        return config.getInt("abandonedNpc.y");
    }
    public static int getAbandonedNpcZ(){
        return config.getInt("abandonedNpc.z");
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


//general game settings
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


    //ab_Spaceship
    public static Location getAbandonedSpawn(){
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("abandonedNpc.world"))),
                config.getDouble("abandonedNpc.x"),
                config.getDouble("abandonedNpc.y"),
                config.getDouble("abandonedNpc.z"),
                (float) config.getDouble("abandonedNpc.yaw"),
                (float) config.getDouble("abandonedNpc.pitch"));
    }
    public static int getHangerCountdownSeconds(){ return config.getInt("hanger-countdown-seconds"); }



    //COD Stronghold
    public static int getGameTime() {
        return config.getInt("game-time-seconds");
    }
    public static int getWinningKillCount() { return config.getInt("kills-to-win"); }

    public static Location getWaitingSpawn() {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("waiting.world"))),
                config.getDouble("waiting.x"),
                config.getDouble("waiting.y"),
                config.getDouble("waiting.z"),
                config.getInt("waiting.yaw"),
                config.getInt("waiting.pitch"));
    }
    public static Location getArenaSpawnBlue(int id) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn-blue." + id + ".world"))),
                config.getDouble("spawn-blue." + id + ".x"),
                config.getDouble("spawn-blue." + id + ".y"),
                config.getDouble("spawn-blue." + id + ".z"),
                config.getInt("spawn-blue." + id + ".yaw"),
                config.getInt("spawn-blue." + id + ".pitch"));
    }
    public static Location getArenaSpawnRed(int id) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn-red." + id + ".world"))),
                config.getDouble("spawn-red." + id + ".x"),
                config.getDouble("spawn-red." + id + ".y"),
                config.getDouble("spawn-red." + id + ".z"),
                config.getInt("spawn-red." + id + ".yaw"),
                config.getInt("spawn-red." + id + ".pitch"));
    }
    public static int getBlueSpawnCount(){
        return Objects.requireNonNull(config.getConfigurationSection("spawn-blue.")).getKeys(false).size();
    }
    public static int getRedSpawnCount(){
        return Objects.requireNonNull(config.getConfigurationSection("spawn-red.")).getKeys(false).size();
    }
}



