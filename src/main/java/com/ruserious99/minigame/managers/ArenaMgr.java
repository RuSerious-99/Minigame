package com.ruserious99.minigame.managers;

import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.Minigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaMgr {

    private final List<Arena> arenas = new ArrayList<>();

    public ArenaMgr(Minigame minigame) {
        FileConfiguration config = minigame.getConfig();
        for(String s : Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)){
            arenas.add(new Arena(minigame, Integer.parseInt(s), new Location(
                    Bukkit.getWorld(Objects.requireNonNull(config.getString("arenas." + s + ".world"))),
                    config.getDouble("arenas." + s + ".x"),
                    config.getDouble("arenas." + s + ".y"),
                    config.getDouble("arenas." + s + ".z"),
                    (float) config.getDouble("arenas." + s + ".yaw"),
                    (float) config.getDouble("arenas." + s + ".pitch"))));
        }
        for(Arena arena : arenas){
            System.out.println("888888 " + arena.getId());
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
}
