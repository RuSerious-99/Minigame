package com.ruserious99.minigame.instance.game.dungeon;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.dungeon.events.ServerBroadcastEvent;
import com.ruserious99.minigame.instance.game.dungeon.utilities.Titles;
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

public class BossBridge implements Listener {

    private boolean isStarted;
    private boolean displayTitle;
    private final Minigame minigame;
    private final Arena arena;

    private Wither witherBoss2;
    private boolean witherLoc2;
    private boolean witherLoc3;

    public BossBridge(Minigame minigame) {
        this.minigame = minigame;
        arena = minigame.getArenaMgr().getArena(4);

    }

    @EventHandler
    public void onRegionEnter(ServerBroadcastEvent event) {
        Player player = event.getPlayer();
        World world = event.getPlayer().getWorld();

        if (event.getMessage().contains("bossTwo") && !displayTitle) {
            new Titles(ChatColor.BLUE + "Objective: Yijki has destroyed the ship's main computer - defeat her! " + ChatColor.YELLOW, ConfigMgr.getBoss_2(), event.getMessage(), arena);
            displayTitle = true;
        }

        if (!isStarted && event.getMessage().contains("bossTwo")) {
            arena.sendMessage(ChatColor.GREEN + "You have discovered the ship's bridge, perhaps now you can finally find some answers...");
            arena.sendMessage(ChatColor.RED + "No! Police have the ship's main computer - retrieve it!");
            arena.sendMessage(ChatColor.AQUA + "Hurry! you only have " + ChatColor.RED + (ConfigMgr.getBoss_2() / 60) + ChatColor.AQUA + " minutes.");
            isStarted = true;
            Dungeon.updateTimer(ConfigMgr.getBoss_2());
            Location loc = new Location(world, -137.0, 92.0, 172.0);
            loc.setYaw(player.getLocation().getYaw() - 180);

            witherBoss2 = event.getPlayer().getWorld().spawn(loc, Wither.class);
            witherBoss2.setMaxHealth(witherBoss2.getMaxHealth()/10);
            witherBoss2.setCustomName("ML Police");
            witherBoss2.setCustomNameVisible(false);
            witherBoss2.setMetadata("witherBoss2", new FixedMetadataValue(minigame.getPlugin(), "witherBoss2"));
            for (int x = -128; x <= -113; ++x) {
                for (int z = 189; z <= 202; ++z) {
                    for (int y = 80; y <= 97; ++y) {
                        final Block b = world.getBlockAt(x, y, z);
                        b.setType(Material.AIR);
                    }
                }
            }
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wither && event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            World world = event.getDamager().getWorld();
            event.setDamage(event.getDamage()*10);
            if (event.getEntity().hasMetadata("witherBoss2")) {
                if (witherBoss2.getHealth() <= witherBoss2.getMaxHealth() - (witherBoss2.getHealth() * .33)
                        && !witherLoc2) {
                    witherBoss2.teleport(new Location(world, -136.0, 80.0, 210.0));
                    witherLoc2 = true;
                }
                if (witherBoss2.getHealth() <= witherBoss2.getMaxHealth() - (witherBoss2.getHealth() * .66)
                        && !witherLoc3) {
                    witherBoss2.teleport(new Location(world, -138.0, 80.0, 189.0));
                    witherLoc3 = true;
                }
                if (Math.random() <= .1D) {
                    event.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 10, 10);
                    player.sendMessage(ChatColor.RED + "Mother of Yijki Blocked attack!");
                }
            }
        }
        if (event.getDamager() instanceof Wither && event.getEntity() instanceof Player) {
            if (event.getDamager().hasMetadata("witherBoss2")) {
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

    @EventHandler
    public void onKillBoss(EntityDeathEvent event) {
        if (event.getEntity() instanceof Wither) {
            if (event.getEntity().hasMetadata("witherBoss2")) {
                for (int x = -132; x < -126; ++x) {
                    for (int y2 = 79; y2 <= 85; ++y2) {
                        final Block b2 = event.getEntity().getWorld().getBlockAt(x, y2, 163);
                        b2.setType(Material.AIR);
                    }
                }
                arena.sendMessage(ChatColor.GREEN + "The ship is falling apart! Get off the bridge!!!");
            }
        }
    }
}
