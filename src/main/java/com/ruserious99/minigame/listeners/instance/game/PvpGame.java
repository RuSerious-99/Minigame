package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PvpGame extends Game {

    public static final HashMap<UUID, Integer> kills = new HashMap<>();
    private static final BossBar gameScore = Bukkit.createBossBar("Player: 00 | Player: 00", BarColor.BLUE, BarStyle.SOLID);
    private static boolean cancelTimer;

    public PvpGame(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);

        arena.sendMessage("GAME HAS STARTED! First Player to kill " + ConfigMgr.getPvpKillCountInt() + " players wins!");



        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            minigame.getScoreboards().updateScoreboard(arena, player);
            addPlayerToGameScore(player);
            kills.put(uuid, 0);

            Objects.requireNonNull
                    (Bukkit.getPlayer(uuid)).getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));

            if (player != null) {
                player.teleport(ConfigMgr.getPvpExtraSpawnlocations(getRand()));
            }

            Objects.requireNonNull
                    (Bukkit.getPlayer(uuid)).closeInventory();

        }
        startGameTimer();
    }

    @Override
    public void endGame() {
        removeAllFromGameScore();
        kills.clear();
        arena.reset();
    }

    private int getRand() {
        int range = (4) - 1;
        return (int) (Math.random() * range);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.getDrops().clear();
        Player killed = e.getEntity();
        Player killer = e.getEntity().getKiller();

        if (killed.getWorld().getName().equals("arena2")) {
            if (arena.getPlayers().contains(e.getEntity().getUniqueId())
                    && arena.getPlayers().contains(Objects.requireNonNull(killer).getUniqueId())
                    && arena.getState().equals(GameState.LIVE)) {
                for (UUID uuid : kills.keySet()) {
                    if (uuid.equals(killer.getUniqueId())) {
                        int v = kills.get(killer.getUniqueId());
                        kills.put(uuid, v + 1);

                        if (v == ConfigMgr.getPvpKillCountInt()) {
                            arena.sendMessage(ChatColor.GOLD + Objects.requireNonNull(Bukkit.getEntity(uuid)).getName()
                                    + " WINS!!! with " + ConfigMgr.getPvpKillCountInt() + " kills,  Thanks for playing.");
                            killed.spigot().respawn();
                            endGame();
                            return;
                        }
                    }
                }
            }
        }
        killed.spigot().respawn();

        Bukkit.getScheduler().scheduleSyncDelayedTask(minigame, () -> {
            if (arena.getState().equals(GameState.LIVE)) {
                killed.teleport(ConfigMgr.getPvpExtraSpawnlocations(getRand()));
                killed.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
            }
        }, 20L);
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
                timeLeft += checkAddTimer(0);
                System.out.println("cancel timer = in run " + cancelTimer);

                if (timeLeft == 0 || cancelTimer) {
                    System.out.println("cancel timer = in if statement " + cancelTimer);
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
        Player player0 = Bukkit.getPlayer(arena.getPlayers().get(0));
        Player player1 = Bukkit.getPlayer(arena.getPlayers().get(1));
        int killsP0 = PvpGame.kills.get(Objects.requireNonNull(player0).getUniqueId());
        int killsP1 = PvpGame.kills.get(Objects.requireNonNull(player1).getUniqueId());
        gameScore.setTitle(Objects.requireNonNull(player0).getName() + " kills = " + ChatColor.RED + killsP0 + ChatColor.GOLD
                + " | " + ChatColor.GREEN + TimeUtils.getFormattedTime(timeLeft)
                + ChatColor.GOLD + " | " + ChatColor.WHITE + Objects.requireNonNull(player1).getName() + " kills = " + ChatColor.RED + killsP1);
        gameScore.setProgress(TimeUtils.getProgress(timeLeft, ConfigMgr.getGameTimePvp()));

    }
}
