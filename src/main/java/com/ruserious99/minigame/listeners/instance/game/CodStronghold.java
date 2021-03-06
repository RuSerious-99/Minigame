package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.kit.enums.CodKitType;
import com.ruserious99.minigame.listeners.instance.team.Team;
import com.ruserious99.minigame.listeners.instance.timers.BlockTimer;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CodStronghold extends Game {

    public HashMap<UUID, Integer> kills;
    public HashMap<UUID, Integer> deaths;

    public static int redScore;
    public static int blueScore;
    private final int KillsToWin = ConfigMgr.getWinningKillCount();

    public CodStronghold(Minigame minigame, Arena arena, BlockTimer timer) {
        super(minigame, arena, timer);
        this.deaths = new HashMap<>();
        this.kills = new HashMap<>();
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage(ChatColor.GREEN + "Game has started!!" + ChatColor.WHITE + " Objective... " +
                ChatColor.RED + "Seek and destroy! First Team to " + ChatColor.WHITE + ConfigMgr.getWinningKillCount() + ChatColor.RED + " kills Wins!!!");

        for (UUID uuid1 : arena.getCodKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            minigame.getTimer().addPlayer(Bukkit.getPlayer(uuid1));
            arena.getCodKits().get(uuid1).atStart(player);
        }
        redScore = 0;
        blueScore = 0;

        for (UUID uuid : arena.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);

            Objects.requireNonNull(player).setFoodLevel(20);
            Objects.requireNonNull(player).setHealth(20D);

            deaths.put(uuid, 0);
            kills.put(uuid, 0);

            if ((arena.getTeam(player).getDisplay().contains("RED"))) {
                int ranTeleport = getRand(ConfigMgr.getRedSpawnCount());
                player.teleport(ConfigMgr.getArenaSpawnRed(ranTeleport));
            } else {
                int ranTeleport = getRand(ConfigMgr.getBlueSpawnCount());
                player.teleport(ConfigMgr.getArenaSpawnBlue(ranTeleport));
            }
            updateScoreboard(player);
            player.closeInventory();
        }

        minigame.getTimer().startGameTimer(arena);
    }

    private int getRand(int max) {
        int range = (max) - 1;
        return (int) (Math.random() * range);
    }

    public void addkill(Player player) {
        System.out.println("addkillCalled " + player);
        System.out.println("team is " + arena.getTeam(player).getDisplay());

        int p = kills.get(player.getUniqueId()) + 1;
        kills.replace(player.getUniqueId(), p);

        if (arena.getTeam(player).getDisplay().contains("RED")) {
            redScore++;
            player.sendMessage("RED team plus one point");
        } else {
            blueScore++;
            player.sendMessage("BLUE team plus one point");
        }
        updateScoreboard(player);

    }

    public void addDeath(Player player) {
        int p = this.deaths.get(player.getUniqueId()) + 1;
        deaths.replace(player.getUniqueId(), p);
        updateScoreboard(player);

    }

    public void gameEnd() {
        if (redScore < KillsToWin && blueScore < KillsToWin) {
            arena.sendMessage("Aww No clear winner. must be a tie. ");
        }
        if (redScore == KillsToWin) {
            arena.sendMessage("RED team WINS!!!!");
        }
        if (blueScore == KillsToWin) {
            arena.sendMessage("BLUE team WINS!!!!");
        }

        minigame.getTimer().removeAll();
        kills.clear();
        deaths.clear();
        redScore = 0;
        blueScore = 0;

        arena.reset();
    }

    private void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = Objects.requireNonNull(manager).getNewScoreboard();
        Objective obj = board.registerNewObjective("HubScoreboard-1", "dummy",
                ChatColor.translateAlternateColorCodes('&', "&a&l<< &2&lTeam DeathMatch &a&l>>"));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null || !player.isOnline() || !player.getWorld().getName().equals("arena4")) {
                    Objects.requireNonNull(player).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    cancel();
                    return;
                }
                Set<String> entries;
                entries = board.getEntries();
                for (String entry : entries) {
                    board.resetScores(entry);
                }
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                obj.getScore("??6??lName:").setScore(5);
                obj.getScore(player.getName()).setScore(4);
                obj.getScore(" ").setScore(3);
                obj.getScore("??6??lPlayers Online: " + ChatColor.WHITE + (Bukkit.getOnlinePlayers().size() + " ")).setScore(2);
                obj.getScore("??6??lKills: " + ChatColor.WHITE + kills.get(player.getUniqueId())).setScore(1);
                obj.getScore("??6??lDeaths: " + ChatColor.WHITE + deaths.get(player.getUniqueId())).setScore(0);

                player.setScoreboard(board);
            }
        }.runTaskTimer(arena.getMinigame(), 20, 20);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && !(event.getDamager() instanceof Projectile))) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player attacker, defender;
        attacker = event.getDamager() instanceof Player ? (Player) event.getDamager() : (Player) ((Projectile) event.getDamager()).getShooter();
        defender = (Player) event.getEntity();

        if (attacker == null || !arena.getPlayers().contains(attacker.getUniqueId()) || !arena.getPlayers().contains(defender.getUniqueId())) {
            return;
        }

        if (areFriendly(attacker, defender)) {
            event.setCancelled(true);
            attacker.sendMessage(ChatColor.RED + "Try not to  hit your teammates!");
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player killed = e.getEntity();
        Player killer = null;
        EntityDamageEvent damageCause = killed.getLastDamageCause();
        if (damageCause != null) {
            if (damageCause instanceof EntityDamageByEntityEvent entityDamageCause) {
                killer = Bukkit.getPlayer(entityDamageCause.getDamager().getUniqueId());
            }
        }
        if (killed.getWorld().getName().equals("arena4")) {
            addDeath(killed);
            addkill(killer);
        }
        killed.spigot().respawn();

        for (Integer count : kills.values()) {
            if (count == KillsToWin) {
                gameEnd();
                return;
            }
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(arena.getMinigame(), () -> {

            if (arena.getState().equals(GameState.LIVE)) {
                killed.teleport(ConfigMgr.getArenaSpawnRed(getRand(4)));
            } else {
                killed.teleport(ConfigMgr.getArenaSpawnBlue(getRand(4)));
            }
            killed.setFoodLevel(20);
            killed.setHealth(20D);

            if (arena.getCodKits().containsKey(killed.getUniqueId())) {
                arena.getCodKits().get(killed.getUniqueId()).atStart((Bukkit.getPlayer(killed.getUniqueId())));
            }
        }, 20L);
    }



    public boolean areFriendly(@NotNull Player one, @NotNull Player two) {
        if ((arena.getTeam(Objects.requireNonNull(one.getPlayer())).getDisplay().equals("RED") && arena.getTeam(Objects.requireNonNull(two.getPlayer())).getDisplay().equals("RED"))) {
            return true;
        }
        return (arena.getTeam(one.getPlayer()).getDisplay().equals("BLUE") && arena.getTeam(Objects.requireNonNull(two.getPlayer())).getDisplay().equals("BLUE"));
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (arena.getId() == 3) {
            Player player = (Player) event.getWhoClicked();

            if (event.getView().getTitle().contains("Stronghold") && event.getCurrentItem() != null) {
                event.setCancelled(true);

                CodKitType type = CodKitType.valueOf(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLocalizedName());
                player.sendMessage(ChatColor.GREEN + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit!");
                arena.setCodKit(player.getUniqueId(), type);

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

}
