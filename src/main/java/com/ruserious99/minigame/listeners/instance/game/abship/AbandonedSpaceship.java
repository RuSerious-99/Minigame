package com.ruserious99.minigame.listeners.instance.game.abship;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.Game;
import com.ruserious99.minigame.listeners.instance.game.abship.ABUtils.PlayerRegionUtil;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Objects;

public class AbandonedSpaceship extends Game {

    private final Minigame minigame;
    public WorldGuardPlugin worldGuardPlugin;

    public static HashMap<String, Long> regionFirstEnter = new HashMap<>();
    public static HashMap<Player, String> entered = new HashMap<>();
    public static HashMap<Player, String> exit    = new HashMap<>();

    private InGameTimer inGameTimer;

    public AbandonedSpaceship(Minigame minigame, Arena arena) {
        super(minigame, arena);
        this.minigame = minigame;
    }

    @Override
    public void onStart() {
        worldGuardPlugin = getWorldGuard();
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Defeat all enemies, Make your way through all mazes. Find the lever to teleport out ");
        removeBlocks();
        setInGameTimer(ConfigMgr.getHangerCountdownSeconds());
        arena.sendTitle(ChatColor.BLUE + "GAME HAS STARTED!", ChatColor.GRAY + "Get to the Hanger",10,100,10);

        Bukkit.getPluginManager().registerEvents(new PlayerRegionUtil(), minigame);
    }

    private void removeBlocks() {
        int z = 91;
        for (int x = 0; x >= -4; --x) {
            for (int y = 79; y <= 83; ++y) {
                Block b = Objects.requireNonNull(ConfigMgr.getAbandonedSpawn().getWorld()).getBlockAt(x, y, z);
                b.setType(Material.AIR);
            }
        }
        arena.sendMessage(ChatColor.GREEN + "The door has opened! Its Time to begin");
        arena.sendMessage(ChatColor.BLUE + "Air Lock will be Destroyed in " + ChatColor.RED
                + (ConfigMgr.getHangerCountdownSeconds() / 60) + ChatColor.BLUE + " minutes");
    }
    public void setInGameTimer(int time) {
        inGameTimer = new InGameTimer(arena, time);}


    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }


    @EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        entered.remove(player);
        exit.remove(player);
    }

    @EventHandler
    public void quitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        entered.remove(player);
        exit.remove(player);
    }

    @EventHandler
    public void onRegionEnter(RegionEnteredEvent event) {
        System.out.println("onRegionEnter: " + event.getRegion());
        Player player = event.getPlayer();

        if(!regionFirstEnter.containsKey(event.getRegion().toString())){
            regionFirstEnter.put(event.getRegion().toString(), System.currentTimeMillis());
        }

        if (entered.containsKey(player)) {
            if(exit.containsKey(player)) {
                exit.replace(player, entered.get(player));
            }else {
                exit.put(player, entered.get(player));
            }
            entered.replace(player, event.getRegion().toString());
        } else {
            entered.put(player, event.getRegion().toString());
        }
    }
}