package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.listeners.instance.team.Team;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
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
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! First Player to break " + ConfigMgr.getBlockGameBlocksToBreakInt() + " blocks wins!");


        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            points.put(uuid, 0);
            addPlayerToGameScore(player);
            minigame.getScoreboards().updateScoreboard(arena, player);
            Objects.requireNonNull(player).closeInventory();
        }

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }

        startGameTimer();
    }

    public void addPoint(Player player) {
        int playerpoints = points.get(player.getUniqueId()) + 1;
        minigame.getScoreboards().updateScoreboard(arena, player);

        if (playerpoints == ConfigMgr.getBlockGameBlocksToBreakInt()) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! Thanks for playing.");
            endGame();
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 point");
        points.replace(player.getUniqueId(), playerpoints);
    }

    @Override
    public void endGame() {
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
    public void onBlockBreak(BlockBreakEvent e) {
        if (arena.getId() == 0) {
            if (!arena.getState().equals(GameState.LIVE)) {
                e.setCancelled(true);
            } else {
                if (arena.getPlayers().contains(e.getPlayer().getUniqueId()))
                    addPoint(e.getPlayer());
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
        gameScore.setTitle(ChatColor.GOLD
                        + " | " + ChatColor.BLUE + TimeUtils.getFormattedTime(timeLeft)
                        + ChatColor.GOLD + " | ");
                gameScore.setProgress(TimeUtils.getProgress(timeLeft, ConfigMgr.getGameTimeBlock()));
    }
}
