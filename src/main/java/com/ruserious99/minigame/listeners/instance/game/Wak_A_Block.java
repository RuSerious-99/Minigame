package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.WakABlockEntities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Wak_A_Block extends Game {

    private final HashMap<UUID, Integer> wakedBlocks;
    private static final BossBar gameScore = Bukkit.createBossBar("Player: 00 | Player: 00", BarColor.BLUE, BarStyle.SOLID);
    private boolean cancelTimer;


    public Wak_A_Block(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        this.wakedBlocks = new HashMap<>();
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Player who Waks the most Blocks wins!");

        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            wakedBlocks.put(uuid, 0);
            addPlayerToGameScore(player);
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).closeInventory();
        }

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }

        startGameTimer();
        for(int i = 0; i<5; i++) {
            WakABlockEntities.spawn();
        }
    }

    private void endGame() {
        removeAllFromGameScore();
        arena.reset();
    }

    @EventHandler
    public void onBlockHit(BlockDamageEvent e) {
        if (arena.getId() == 3) {
            if (!arena.getState().equals(GameState.LIVE)) {
                e.setCancelled(true);
            } else {
                if (arena.getPlayers().contains(e.getPlayer().getUniqueId()))
                    addWak(e.getPlayer());
            }
        }
    }

    private void addWak(Player player) {
        int playerpoints = wakedBlocks.get(player.getUniqueId()) + 1;

        if (playerpoints == ConfigMgr.getBlockGameBlocksToBreakInt()) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! Thanks for playing.");
            endGame();
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 point");
        wakedBlocks.replace(player.getUniqueId(), playerpoints);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (arena.getId() == 3) {
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
    }

    public void removeAllFromGameScore() {
        for (UUID uuid : arena.getPlayers()) {
            gameScore.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
        }
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
                + " || " + ChatColor.BLUE + getFormattedTime(timeLeft)
                + ChatColor.GOLD + " || ");
        gameScore.setProgress(getProgress(timeLeft, ConfigMgr.getGameTimeWak()));
    }

    private String getFormattedTime(int time) {
        int seconds;
        int minutes;
        minutes = time / 60;
        seconds = time - (minutes * 60);

        String minutesString, secondsString;
        minutesString = minutes < 10 ? "0" + minutes : minutes + "";
        secondsString = seconds < 10 ? "0" + seconds : seconds + "";

        return minutesString + " : " + secondsString;
    }

    private double getProgress(int timeLeft, int totalTime) {
        return (double) timeLeft / (double) totalTime;
    }
}