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
    public static String   getWorldName() {return config.getString("main-world-name"); }
    public static int      getPvpKillCountInt(){return config.getInt("pvpgame-player-kill-count"); }
    public static int      getBlockGameBlocksToBreakInt(){return config.getInt("blockgame-blocks-to-break-count"); }
    public static String   getWorldArenasSource(){return config.getString("world-source-arenas"); }
    public static int      getRequiredPlayersBlockGame(){
        return config.getInt("required-players-block-game");
    }
    public static int      getRequiredPlayersPvpOneOnOne(){
        return config.getInt("required-players-pvp-vs");
    }
    public static int      getRequiredPlayersWak_A_Block(){
        return config.getInt("required-players-wak_a_block");
    }
    public static int      getRequiredPlayersStronghold(){
        return config.getInt("required-players-stronghold");
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
    public static Location getPvpExtraSpawnlocations(int id) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("pvp-1on1." + id + ".world"))),
                config.getDouble("pvp-1on1." + id + ".x"),
                config.getDouble("pvp-1on1." + id + ".y"),
                config.getDouble("pvp-1on1." + id + ".z"),
                config.getInt("pvp-1on1." + id + ".yaw"),
                config.getInt("pvp-1on1." + id + ".pitch"));
    }

    //game times
    public static int getGameTimeBlock() {return config.getInt("game-time-block");}
    public static int getGameTimeCod() {return config.getInt("game-time-cod");}
    public static int getGameTimeWak() {return config.getInt("game-time-wak");}
    public static int getGameTimePvp() {return config.getInt("game-time-pvp");}


    //COD Stronghold

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



