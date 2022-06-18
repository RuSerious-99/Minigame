package com.ruserious99.minigame.managers;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaMgr {

    FileConfiguration config;
    private  Minigame minigame;

    private static List<Arena> arenas = new ArrayList<>();

    public ArenaMgr(Minigame minigame) {
        this.minigame = minigame;
        config = minigame.getConfig();
        populateArenas();
    }

    public void populateArenas() {
        for(String s : Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)){
            arenas.add(new Arena(minigame, Integer.parseInt(s), new Location(
                    Bukkit.getWorld(Objects.requireNonNull(config.getString("arenas." + s + ".world"))),
                    config.getDouble("arenas." + s + ".x"),
                    config.getDouble("arenas." + s + ".y"),
                    config.getDouble("arenas." + s + ".z"),
                    (float) config.getDouble("arenas." + s + ".yaw"),
                    (float) config.getDouble("arenas." + s + ".pitch")),
                    config.getString("arenas." + s + ".game")));
        }
    }

    public List<Arena> getArenas(){
        return arenas;
    }

    public Arena getArena(Player player){
        for (Arena arena : arenas){
            if(arena.getPlayers().contains(player.getUniqueId())){
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(int id){
        for (Arena arena : arenas){
            if(arena.getId() == id){
                return arena;
            }
        }
        return null;
    }

    public void clearArena(int id) {
        Arena putBack = null;
        for (Arena a : arenas) {
            if (a.getId() == id) {
                putBack = a;
            }
        }
        if (putBack != null) {
            arenas.remove(putBack);
            arenas.add(new Arena(minigame, id, new Location(
                    Bukkit.getWorld(Objects.requireNonNull(config.getString("arenas." + id + ".world"))),
                    config.getDouble("arenas." + id + ".x"),
                    config.getDouble("arenas." + id + ".y"),
                    config.getDouble("arenas." + id + ".z"),
                    (float) config.getDouble("arenas." + id + ".yaw"),
                    (float) config.getDouble("arenas." + id + ".pitch")),
                    config.getString("arenas." + id + ".game")));

            System.out.println("success reset arena " + id);
        }
    }
}
