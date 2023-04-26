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
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
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

    public DeadSpace(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);

    }

    @Override
    public void onStart() {
        GameAreas.createRegions();
        Walls.createWalls();
        arena.setState(GameState.LIVE);

        DeadPlayerRegionUtil.reset();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : arena.getPlayers()) {
                    for (Map.Entry<Cuboid, String> entry : GameAreas.getCuboids().entrySet()) {
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
                    fillWalls();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(Minigame.getInstance(), 80L);
    }
    private void fillWalls() {
        Walls.fillWall("loadingDockWall1");
        Walls.fillWall("loadingDockWall2");
        Walls.fillWall("loadingDockWall3");
        Walls.fillWall("blockDoorLoading");
        Walls.fillWall("blockDoorComputer");
        Walls.fillWall("blockDoorInteractComputer");
    }
    private void loadInventory() throws IOException {
        Player player = Bukkit.getPlayer(arena.getPlayers().get(0));
        PersistentData persistentData = new PersistentData();
        String convertFrom = persistentData.deadPlayerGetCustomDataTag(Objects.requireNonNull(player), "deadInfoInventory");
        if (!convertFrom.equals("inventory") ) {
            ItemStack[] items = SerializeInventory.itemStackArrayFromBase64(persistentData.deadPlayerGetCustomDataTag(player, "deadInfoInventory"));
            int slotSpot = 9;
            for (ItemStack i : items) {
                if(i!=null) {
                    if(i.getItemMeta() != null && i.getItemMeta().getDisplayName().contains("BANK ACCOUNT")){
                        continue;
                    }
                    if(slotSpot >= 34){
                        player.sendMessage(ChatColor.RED + "Your inventory is full");
                    }
                    player.getInventory().setItem(slotSpot, i);
                    slotSpot++;
                }
            }
            player.getInventory().setItem(35, ItemsManager.createBankAccount(persistentData.deadPlayerGetCustomDataTag(Objects.requireNonNull(player), "deadInfoMoney")));
        }else{
            player.getInventory().setItem(35, ItemsManager.bankAccount);
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

        if (message.equals("boarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("boarding") && !DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")) {
            arena.sendMessage(ChatColor.YELLOW + "Welcome to the Ishamura.");
            new DeadTitlesUtil(ChatColor.BLUE + "Chapter 1; NEW ARRIVALS", ChatColor.YELLOW + "Objective: get to the Medical Bay", arena);
            ChestConfig.spawnChest(ChestConfig.boarding(), ChestConfig.boardingStack());
            ChestConfig.spawnChest(ChestConfig.enterIshamura1(), ChestConfig.enterIshamuraStack1());
            ChestConfig.spawnChest(ChestConfig.enterIshamura2(), ChestConfig.enterIshamuraStack2());
        }
        if (message.equals("enterBoarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")) {arena.sendMessage(ChatColor.WHITE + "Objective: " + ChatColor.BLUE + "Interact with computer");}
        if (message.equals("firstPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("firstPartHall")) {
            GameAreas.fillWall("c1Computer");
            EntityConfig.spawnEntityChapter1(EntityConfig.firstPartHallLocation(), EntityConfig.firstPartHallEntity());
        }
        if (message.equals("secondPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("secondPartHall")) {
            EntityConfig.spawnEntityChapter1(EntityConfig.secondPartHallLocation(), EntityConfig.secondPartHallEntity());}
        if (message.equals("thirdPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("thirdPartHall")) {
            EntityConfig.spawnEntityChapter1(EntityConfig.thirdPartHallLocation(), EntityConfig.thirdPartHallEntity());
        }
        if (message.equals("forthPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("forthPartHall")) {
            EntityConfig.spawnEntityChapter1(EntityConfig.forthPartHallLocation(), EntityConfig.forthPartHallEntity());
        }
        if (message.equals("stairsDown")) {GameAreas.fillWall("forthPartHall");}
    }



    @Override
    public void endGame() {
        DeadPlayerRegionUtil.reset();
        arena.reset();
    }

}
