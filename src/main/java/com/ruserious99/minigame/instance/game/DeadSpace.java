package com.ruserious99.minigame.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadTitlesUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.SerializeInventory;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.gameZones.GameAreas;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.gameZones.Walls;
import com.ruserious99.minigame.instance.game.deadspace.event.DeadBroadcastEvent;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.ChestConfig;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.EntityConfig;
import com.ruserious99.minigame.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class DeadSpace extends Game {

    private final GameAreas gameAreas;
    private final Walls walls;

    public DeadSpace(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        this.gameAreas = new GameAreas();
        this.walls = new Walls();
    }

    @Override
    public void onStart() {
        gameAreas.createRegions();
        walls.createWalls();
        arena.setState(GameState.LIVE);



        DeadPlayerRegionUtil.reset();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : arena.getPlayers()) {
                    for (Map.Entry<Cuboid, String> entry : gameAreas.getCuboids().entrySet()) {
                        Cuboid cuboid = entry.getKey();
                        String value = entry.getValue();
                        if (cuboid.contains(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getLocation())) {
                            DeadPlayerRegionUtil.deadRegionEvents(uuid, value);
                        }
                    }
                }
            }
        }.runTaskTimer(Minigame.getInstance(), 20L, 40L);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    loadInventory();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(Minigame.getInstance(), 80L);

    }

    private void loadInventory() throws IOException {
        Player player = Bukkit.getPlayer(arena.getPlayers().get(0));
        PersistentData persistentData = new PersistentData();
        String convertFrom = persistentData.deadPlayerGetCustomDataTag(Objects.requireNonNull(player), "deadInfoInventory");
        if (!convertFrom.equals("inventory") ) {
            ItemStack[] items = SerializeInventory.itemStackArrayFromBase64(persistentData.deadPlayerGetCustomDataTag(player, "deadInfoInventory"));
            for (ItemStack i : items) {
                if(i!=null) {
                    System.out.println("loadPlayer " + i.getType());
                    player.getInventory().addItem(i);
                }
            }
        }
    }


    @EventHandler
    public void onResourceStatus(PlayerResourcePackStatusEvent e) throws IOException {
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
            arena.removePlayer(e.getPlayer());
        }
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            arena.removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onRegionEnter(DeadBroadcastEvent event) {
        String message = event.getMessage();

        //this is why you are placing chests as you progress rather than all at start
        //todo: implement randon system to fill chests to balance game
        if(message.equals("boarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("boarding") && !DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")){
            arena.sendMessage("Welcome to the Ishamura.");
            new DeadTitlesUtil(ChatColor.BLUE + "Chapter 1; NEW ARRIVALS", ChatColor.YELLOW + "Objective: get to the Medical Bay", arena);
            ChestConfig.spawnChest(ChestConfig.boarding(), ChestConfig.boardingStack());
        }

        if(message.equals("enterBoarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")){
            ChestConfig.spawnChest(ChestConfig.enterIshamura1(), ChestConfig.enterIshamuraStack1());
            ChestConfig.spawnChest(ChestConfig.enterIshamura2(), ChestConfig.enterIshamuraStack2());
        }

        if(message.equals("c1Computer") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("c1Computer")){
            EntityConfig.spawnEntity(EntityConfig.c1ComputerLocation(), EntityConfig.c1ComputerEntity());
        }
    }

    @Override
    public void endGame() {
        DeadPlayerRegionUtil.reset();
        arena.reset();
    }

}
