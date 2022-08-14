package com.ruserious99.minigame.listeners.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.listeners.instance.timers.BlockTimer;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.WakABlockEntities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Wak_A_Block extends Game {

    private final HashMap<UUID, Integer> wakedBlocks;

    public Wak_A_Block(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
        this.wakedBlocks = new HashMap<>();
    }

    @Override
    public void onStart() {
        arena.setState(GameState.LIVE);
        arena.sendMessage("GAME HAS STARTED! Player who Waks the most Blocks wins!");

        BlockTimer timer = new BlockTimer(arena);

        for (UUID uuid : arena.getPlayers()) {
            wakedBlocks.put(uuid, 0);
            timer.addPlayer(Bukkit.getPlayer(uuid));
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).closeInventory();
        }

        for (UUID uuid1 : arena.getKits().keySet()) {
            Player player = Bukkit.getPlayer(uuid1);
            arena.getKits().get(uuid1).atStart(player);
        }

        timer.startGameTimer(arena);
        for(int i = 0; i<5; i++) {
            WakABlockEntities.spawn();
        }
    }

    private void endGame() {
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

}