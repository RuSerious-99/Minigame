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

    public static Location getAbandonedSpawn(){
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("abandonedNpc.world"))),
                config.getDouble("abandonedNpc.x"),
                config.getDouble("abandonedNpc.y"),
                config.getDouble("abandonedNpc.z"),
                (float) config.getDouble("abandonedNpc.yaw"),
                (float) config.getDouble("abandonedNpc.pitch"));
    }





    public static String getArenaWorld(){ return config.getString("arenas." + "2" + ".world"); }
    public static int getHangerCountdownSeconds()            { return config.getInt("hanger-countdown-seconds"); }
    public static int getFirstBossCountdownSeconds()         { return config.getInt("firstboss-countdown-seconds"); }
    public static int getMaxPlayerCount()                    { return config.getInt("maxPlayer_count-seconds"); }
    public static int getParkour_1()                         { return config.getInt("parkour_1-seconds"); }
    public static int getMaze()                              { return config.getInt("maze-seconds"); }
    public static int getBoss_2()                            { return config.getInt("boss_2-seconds"); }
    public static int getParkour_2()                         { return config.getInt("parkour_2-seconds"); }
    public static int getTeleportationRoom()                 { return config.getInt("teleportation-seconds"); }
    public static int getLeave_the_Dungeon()                 { return config.getInt("leave_the_dungeon-seconds"); }
    public static int getDungeOnCooldown()                   { return config.getInt("dungeon_cooldown-seconds"); }
    public static boolean getIskeyRequired()                 { return config.getBoolean("is_key_required"); }
    public static boolean getIsDungeonDisabled()             { return config.getBoolean("is_dungeon_disabled"); }

}



