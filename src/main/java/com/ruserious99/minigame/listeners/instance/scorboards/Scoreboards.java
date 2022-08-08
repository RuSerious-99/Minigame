package com.ruserious99.minigame.listeners.instance.scorboards;

import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.BlockGame;
import com.ruserious99.minigame.listeners.instance.game.CodStronghold;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import java.util.Objects;
import java.util.Set;

public class Scoreboards {

    public  void updateScoreboard(Arena arena, Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = Objects.requireNonNull(manager).getNewScoreboard();
        Objective obj = getObjectiveArena(board, player);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.getWorld().getName().equals(ConfigMgr.getWorldName())) {
                    Objects.requireNonNull(player).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    cancel();
                    return;
                }
                Set<String> entries;
                entries = board.getEntries();
                for (String entry : entries) {
                    board.resetScores(entry);
                }

                switch (player.getWorld().getName()) {
                    case "arena1" ->  setObjBlock(obj, player, board);
                    case "arena2" ->  setObjPvp(obj, player, board);
                    case "arena3" ->  setObjWak(obj, player, board);
                    case "arena4" ->  setObjStronghold(obj, player, board);
                }


            }
        }.runTaskTimer(arena.getMinigame(), 20, 20);
    }

    private  void setObjBlock(Objective obj, Player player, Scoreboard board) {
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.YELLOW + "Name:").setScore(5);
        obj.getScore(player.getName()).setScore(4);
        obj.getScore(" ").setScore(3);
        obj.getScore(ChatColor.DARK_PURPLE + "Players Online: " + ChatColor.GREEN + (Bukkit.getOnlinePlayers().size() + " ")).setScore(2);
        obj.getScore(ChatColor.DARK_PURPLE + "         Points: " + ChatColor.GREEN + BlockGame.points.get(player.getUniqueId())).setScore(1);

        player.setScoreboard(board);
    }

    private  void setObjPvp(Objective obj, Player player, Scoreboard board) {
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.YELLOW + "Name:").setScore(5);
        obj.getScore(player.getName()).setScore(4);
        obj.getScore(" ").setScore(3);
        obj.getScore(ChatColor.DARK_PURPLE + "Players Online: " + ChatColor.GREEN + (Bukkit.getOnlinePlayers().size() + " ")).setScore(2);
    }

    private  void setObjWak(Objective obj, Player player, Scoreboard board) {
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.YELLOW + "Name:").setScore(5);
        obj.getScore(player.getName()).setScore(4);
        obj.getScore(" ").setScore(3);
        obj.getScore(ChatColor.DARK_PURPLE + "Players Online: " + ChatColor.GREEN + (Bukkit.getOnlinePlayers().size() + " ")).setScore(2);
    }

    private  void setObjStronghold(Objective obj, Player player, Scoreboard board) {
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.YELLOW + "Name:").setScore(5);
        obj.getScore(player.getName()).setScore(4);
        obj.getScore(" ").setScore(3);
        obj.getScore(ChatColor.DARK_PURPLE + "Players Online: " + ChatColor.GREEN + (Bukkit.getOnlinePlayers().size() + " ")).setScore(2);
        obj.getScore(ChatColor.DARK_PURPLE + "         Kills: " + ChatColor.GREEN + CodStronghold.kills.get(player.getUniqueId())).setScore(1);
        obj.getScore(ChatColor.DARK_PURPLE + "      Deaths: " + ChatColor.GREEN + CodStronghold.deaths.get(player.getUniqueId())).setScore(0);

        player.setScoreboard(board);
    }

    private  Objective getObjectiveArena(Scoreboard board, Player player) {
        return switch (player.getWorld().getName()) {
            case "arena1" -> board.registerNewObjective("HubScoreboard-1", "dummy",
                    ChatColor.translateAlternateColorCodes('&', getHeaderString(player)));
            case "arena2" -> board.registerNewObjective("HubScoreboard-2", "dummy",
                    ChatColor.translateAlternateColorCodes('&', getHeaderString(player)));
            case "arena3" -> board.registerNewObjective("HubScoreboard-3", "dummy",
                    ChatColor.translateAlternateColorCodes('&', getHeaderString(player)));
            case "arena4" -> board.registerNewObjective("HubScoreboard-4", "dummy",
                    ChatColor.translateAlternateColorCodes('&', getHeaderString(player)));
            default -> null;
        };
    }

    private  String getHeaderString(Player player) {
        return switch (player.getWorld().getName()) {
            case "arena1" -> "&a&l<< &2&lTeam BlockGame &a&l>>";
            case "arena2" -> "&a&l<< &2&lOne On One PVP &a&l>>";
            case "arena3" -> "&a&l<< &2&lTeam Death-match &a&l>>";
            case "arena4" -> ChatColor.GOLD +"<<" + ChatColor.WHITE + " Team DeathMatch " + ChatColor.GOLD +">>";
            default -> null;
        };

    }

}
