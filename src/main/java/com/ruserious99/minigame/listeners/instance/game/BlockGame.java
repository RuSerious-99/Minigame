package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import com.ruserious99.minigame.listeners.instance.team.Team;
import com.ruserious99.minigame.listeners.instance.timers.BlockTimer;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BlockGame extends Game {

    private final HashMap<UUID, Integer> points;

    public BlockGame(Minigame minigame, Arena arena, BlockTimer timer) {
        super(minigame, arena, timer);
        points = new HashMap<>();
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! First Player to break " + ConfigMgr.getBlockGameBlocksToBreakInt() + " blocks wins!");

        for (UUID uuid : arena.getPlayers()) {
            points.put(uuid, 0);
            minigame.getTimer().addPlayer(Bukkit.getPlayer(uuid));
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).closeInventory();
        }

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }

        minigame.getTimer().startGameTimer(arena);
    }

    public void addPoint(Player player) {
        int playerpoints = points.get(player.getUniqueId()) + 1;

        if (playerpoints == ConfigMgr.getBlockGameBlocksToBreakInt()) {
            arena.sendMessage(ChatColor.GOLD + player.getName() + " WINS!!! Thanks for playing.");
            endGame();
            return;
        }

        player.sendMessage(ChatColor.GREEN + "+1 point");
        points.replace(player.getUniqueId(), playerpoints);
    }

    private void endGame() {
        minigame.getTimer().removeAll();
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
}
