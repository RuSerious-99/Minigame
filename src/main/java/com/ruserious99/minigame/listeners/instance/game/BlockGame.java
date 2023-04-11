package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.listeners.instance.team.Team;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.npc.NpcGameStartUtil;
import com.ruserious99.minigame.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BlockGame extends Game {

    public static HashMap<UUID, Integer> points;
    private static final BossBar gameScore = Bukkit.createBossBar("Player: 00 | Player: 00", BarColor.BLUE, BarStyle.SOLID);
    private static boolean cancelTimer;


    public BlockGame(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        points = new HashMap<>();
    }

    @Override
    public void onStart() {

        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            points.put(uuid, 0);
            addPlayerToGameScore(player);
            minigame.getScoreboards().updateScoreboard(arena, player);
            //Objects.requireNonNull(player).closeInventory();
        }

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }

        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! First Player to break " + ConfigMgr.getBlockGameBlocksToBreakInt() + " blocks wins!");
        startGameTimer();
    }

    public void addPoint(Player player) {
        int playerpoints = points.get(player.getUniqueId()) + 1;
        minigame.getScoreboards().updateScoreboard(arena, player);
        if (playerpoints == ConfigMgr.getBlockGameBlocksToBreakInt()) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! Thanks for playing.");
            arena.sendTitle(ChatColor.BLUE + "Winner is ", player.getName(), 0, 30, 10);

            new BukkitRunnable() {
                @Override
                public void run() {
                    endGame();
                }
            }.runTaskLater(minigame, 40);
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 point");
        points.replace(player.getUniqueId(), playerpoints);
    }

    @Override
    public void endGame() {
        points.clear();
        removeAllFromGameScore();
        cancelTimer = true;
        arena.reset();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (arena.getId() == 0) {
            Player player = (Player) event.getWhoClicked();

            if (event.getView().getTitle().contains("Kit Selection") && event.getCurrentItem() != null) {
                event.setCancelled(true);

                KitType type = KitType.valueOf(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLocalizedName());
                player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit!");
                arena.setKit(player.getUniqueId(), type);

                player.closeInventory();
            } else {
                if (event.getView().getTitle().contains("Team Selection") && event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    Team team = Team.valueOf(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLocalizedName());
                    if (arena.getTeam(player) == team) {
                        player.sendMessage(ChatColor.RED + "You are already on this team");
                    } else {
                        player.sendMessage(ChatColor.AQUA + "You are now on the " + team.getDisplay() + ChatColor.AQUA + " team!");
                        arena.setTeam(player, team);
                    }
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onBlocKBreak(BlockBreakEvent e) {
        if (e.getPlayer().getWorld().getName().equals("arena1")) {
            if (arena.getState().equals(GameState.LIVE)) {
                if (arena.getPlayers().contains(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage(ChatColor.GOLD + "Congrats!!!" + ChatColor.GREEN + " You Broke a block");
                    addPoint(e.getPlayer());
                }
            }else{
                e.setCancelled(true);
            }
        }
    }
    public void addPlayerToGameScore(Player player) {
        gameScore.addPlayer(player);
    }

    public static void removePlayerGameScore(Player player) {
        gameScore.removePlayer(player);
        cancelTimer = true;
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

    private void setGameScoreTitle(int timeLeft) {
        gameScore.setTitle(ChatColor.GOLD
                        + " | " + ChatColor.BLUE + TimeUtils.getFormattedTime(timeLeft)
                        + ChatColor.GOLD + " | ");
                gameScore.setProgress(TimeUtils.getProgress(timeLeft, ConfigMgr.getGameTimeBlock()));
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e){

        Player killed = e.getEntity();

        if (points.containsKey(killed.getUniqueId())){
            killed.spigot().respawn();
            killed.teleport(ConfigMgr.getAfterDeathSpawn(arena.getId()));
        }
        if(arena.getId() == 0){
            if(arena.getState().equals(GameState.COUNTDOWN)){
                killed.spigot().respawn();
                killed.teleport(ConfigMgr.getAfterDeathSpawn(arena.getId()));
            }
        }
    }

}
