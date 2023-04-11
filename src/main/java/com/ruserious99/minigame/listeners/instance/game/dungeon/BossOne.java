package com.ruserious99.minigame.listeners.instance.game.dungeon;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.dungeon.events.ServerBroadcastEvent;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.Titles;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class BossOne implements Listener {

    private boolean isStarted;
    private boolean displayTitle;
    private final Minigame minigame;
    private final Arena arena;

    public BossOne(Minigame minigame) {
        this.minigame = minigame;
        arena = minigame.getArenaMgr().getArena(4);
    }

    @EventHandler
    public void onRegionEnter(ServerBroadcastEvent event) {

        Player player = event.getPlayer();

        if (event.getMessage().contains("hanger") && !displayTitle) {
            new Titles(ChatColor.BLUE + "Objective: Defeat Yijki and Exit the hanger" + ChatColor.YELLOW, ConfigMgr.getFirstBossCountdownSeconds(), event.getMessage(), arena);
            displayTitle = true;
        }

        if (!isStarted && event.getMessage().contains("hanger")) {
            arena.sendMessage(ChatColor.GREEN + "Defeat Mother_of_Yijki and escape the Hanger ");
            arena.sendMessage(ChatColor.AQUA + "Hurry! you only have " + ChatColor.RED + (ConfigMgr.getFirstBossCountdownSeconds() / 60) + ChatColor.AQUA + " minutes to defeat Her! and exit the hanger");
            isStarted = true;

            Dungeon.updateTimer(ConfigMgr.getFirstBossCountdownSeconds());

            Location l = player.getLocation();
            l.add(0, 5.0, 25.0);

            Wither witherBoss = event.getPlayer().getWorld().spawn(l, Wither.class);
            witherBoss.setCustomName("Mother_of_Yijki");
            witherBoss.setMaxHealth(witherBoss.getMaxHealth()/10);
            witherBoss.setCustomNameVisible(false);
            witherBoss.setMetadata("witherBoss1", new FixedMetadataValue(minigame.getPlugin(), "witherBoss1"));
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wither && event.getDamager() instanceof Player) {
            if (event.getEntity().hasMetadata("witherBoss1")) {
                event.setDamage(event.getDamage()*10);
                if (Math.random() <= .1D) {
                    event.setCancelled(true);
                    Player player = (Player) event.getDamager();
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 10);
                    player.sendMessage(ChatColor.RED + "Mother of Yijki Blocked attack!");
                }
            }
            if (event.getDamager() instanceof Wither && event.getEntity() instanceof Player) {
                if (event.getDamager().hasMetadata("witherBoss1")) {
                    if (Math.random() <= .5D) {
                        event.setCancelled(true);
                        Player player = (Player) event.getEntity();
                        player.setVelocity(new Vector(0, 2, 0));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 10, 10);
                        player.sendMessage(ChatColor.RED + "You got punched!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKillBoss(EntityDeathEvent event) {
            if (event.getEntity().hasMetadata("witherBoss1")) {
                for (int x = 21; x <= 24; ++x) {
                    for (int z = 230; z <= 235; ++z) {
                        for (int y = 89; y <= 92; ++y) {
                            Block b = event.getEntity().getWorld().getBlockAt(x, y, z);
                            b.setType(Material.AIR);
                        }
                    }
            }
                arena.sendMessage(ChatColor.GREEN + "Mother of Yijiki has been defeated!  Well Done!");
                arena.sendMessage(ChatColor.GREEN + "get to the Ventilation shaft, HURRY!!! ");
        }
    }



}


