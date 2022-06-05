package com.ruserious99.minigame.listeners;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.kit.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GameListener implements Listener {


    private final Minigame minigame;

    public GameListener(Minigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("Kit Selection") && event.getInventory() != null && event.getCurrentItem() != null) {
            event.setCancelled(true);

            KitType type = KitType.valueOf(event.getCurrentItem().getItemMeta().getLocalizedName());

            Arena arena = minigame.getArenaMgr().getArena(player);
            if (arena != null) {
                KitType activated = arena.getKitType(player);
                if (activated != null && activated == type) {
                    player.sendMessage(ChatColor.RED + "You already have this kit equipped");
                } else {
                    player.sendMessage(ChatColor.RED + "You have equipped the " + type.getDisplay() + ChatColor.GREEN + " kit!");
                    arena.setKit(player.getUniqueId(), type);
                }
                player.closeInventory();
            }
        }
    }

    /* MiniGame Specific Event */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Arena arena = minigame.getArenaMgr().getArena(e.getPlayer());
        if(arena != null && arena.getState().equals(GameState.LIVE)){
            if(arena.getState().equals(GameState.LIVE)){
                arena.getgame().addPoint(e.getPlayer());
            }
        }
    }

}
