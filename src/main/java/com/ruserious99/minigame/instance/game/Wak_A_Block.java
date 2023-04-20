package com.ruserious99.minigame.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.kit.enums.KitType;
import com.ruserious99.minigame.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.TimeUtils;
import com.ruserious99.minigame.utils.WakABlockEntities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Wak_A_Block extends Game {

    public static HashMap<UUID, Integer> wakedBlocks;
    private static final BossBar gameScore = Bukkit.createBossBar("Player: 00 | Player: 00", BarColor.BLUE, BarStyle.SOLID);
    private static boolean cancelTimer;


    public Wak_A_Block(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        wakedBlocks = new HashMap<>();
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Player who Waks the most Blocks wins!");

        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            wakedBlocks.put(uuid, 0);
            addPlayerToGameScore(player);
            minigame.getScoreboards().updateScoreboard(arena, player);
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).closeInventory();
        }

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }

        startGameTimer();


        for (int i = 0; i < ConfigMgr.getEnemiesCount_Wak_a_Block(); i++) {
            WakABlockEntities.spawn();
        }
        spawnTheBlocks(arena.getPlayers().size());
    }

    private void spawnTheBlocks(int size) {
        for (int i = 0; i < size; i++) {
            int newX = ConfigMgr.getPiratesSpawn(arena.getId()).getBlockX() + new Random().nextInt(25 + 20) - 20;
            int newZ = ConfigMgr.getPiratesSpawn(arena.getId()).getBlockZ() + new Random().nextInt(35 + 28) - 28;
            int newY = ConfigMgr.getPiratesSpawn(arena.getId()).getBlockY();

            Location spawnLoc = new Location(ConfigMgr.getPiratesSpawn(arena.getId()).getWorld(), newX, newY, newZ);
            spawnLoc.getBlock().setType(Material.BLUE_WOOL);
        }
    }


    @EventHandler
    public void onBlockHit(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null) {
            e.setCancelled(true);
        }
        if (arena.getId() == 2) {
            if (arena.getState().equals(GameState.LIVE)) {
                if(e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.BLUE_WOOL)){
                    e.getPlayer().sendMessage(ChatColor.GOLD + "Congrats!!!" + ChatColor.GREEN + " you waked a block");
                    e.getClickedBlock().setType(Material.AIR);
                    addWak(e.getPlayer());
                    spawnTheBlocks(arena.getPlayers().size());
                }
            }
        }
    }

    private void addWak(Player player) {
        int playerpoints = wakedBlocks.get(player.getUniqueId()) + 1;
        player.sendMessage(ChatColor.GREEN + "+1 wak");
        wakedBlocks.replace(player.getUniqueId(), playerpoints);
        if (playerpoints == ConfigMgr.getBlock_Count_to_Win_Wak_A_BlockGame()) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! Thanks for playing.");
            arena.sendTitle(ChatColor.BLUE + "Winner is ", player.getName(), 0, 30, 10);

            new BukkitRunnable() {
                @Override
                public void run() {
                    endGame();
                }
            }.runTaskLater(minigame, 40);
        }
    }

    @Override
    public void endGame() {
        removeAllFromGameScore();
        cancelTimer = true;
        arena.reset();
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getWorld().getName().equals("arena3")) {
            Player player = (Player) event.getWhoClicked();

            if (event.getView().getTitle().contains("Kit Selection") && event.getCurrentItem() != null) {
                event.setCancelled(true);

                KitType type = KitType.valueOf(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLocalizedName());
                player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit!");
                arena.setKit(player.getUniqueId(), type);

                player.closeInventory();
            }
        }
    }

    public void addPlayerToGameScore(Player player) {
        gameScore.addPlayer(player);
    }

    public static void removePlayerGameScore(Player player) {
        gameScore.removePlayer(player);
        //cancelTimer = true;
    }

    public void removeAllFromGameScore() {
        for (UUID uuid : arena.getPlayers()) {
            gameScore.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
        }
        cancelTimer = true;

    }

    public void startGameTimer() {
        BukkitRunnable runGame = new BukkitRunnable() {
            int timeLeft = ConfigMgr.getGameTimeBlock();

            @Override
            public void run() {
                timeLeft += checkAddTimer(0);

                if (timeLeft == 0 || cancelTimer) {
                    cancelTimer = false;
                    if (timeLeft == 0) {
                        arena.sendMessage("Aww ran out of time. No clear winner. Thanks for playing");
                    }
                    this.cancel();
                    arena.reset();
                    return;
                }
                setGameScoreTitle(timeLeft);
                timeLeft--;
            }
        };
        runGame.runTaskTimer(arena.getMinigame(), 0L, 20L);
    }

    public int checkAddTimer(int addTime) {
        return addTime;
    }

    private void setGameScoreTitle(int timeLeft) {
        gameScore.setTitle(ChatColor.GOLD
                + " || " + ChatColor.BLUE + TimeUtils.getFormattedTime(timeLeft)
                + ChatColor.GOLD + " || ");
        gameScore.setProgress(TimeUtils.getProgress(timeLeft, ConfigMgr.getGameTimeWak()));
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e) {

        Player killed = e.getEntity();

        if (wakedBlocks.containsKey(killed.getUniqueId())) {
            killed.spigot().respawn();
            killed.teleport(ConfigMgr.getAfterDeathSpawn(arena.getId()));
        }
        if (arena.getId() == 2) {
            if (arena.getState().equals(GameState.COUNTDOWN)) {
                killed.spigot().respawn();
                killed.teleport(ConfigMgr.getAfterDeathSpawn(arena.getId()));
            }
        }
    }
}