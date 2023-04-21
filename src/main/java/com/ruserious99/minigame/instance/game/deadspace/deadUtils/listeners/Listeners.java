package com.ruserious99.minigame.instance.game.deadspace.deadUtils.listeners;

import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.SerializeInventory;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.utils.ActionBarMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class Listeners implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int slot = e.getSlot();

        if (!player.getWorld().getName().equals("arena6")) {
            return;
        }
        if (e.getSlotType() == InventoryType.SlotType.ARMOR || clickedItem == null || !e.isRightClick()) {
            return;
        }

        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("HEALTH PACK")) {
            setHealth(player, clickedItem, slot);
        }
    }

    private void setHealth(Player player, ItemStack clickedItem, int slot) {
        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("SMALL HEALTH PACK")) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            if (currentHealth  < maxHealth -4) {
                System.out.println(" small new health " );
                double newHealth = currentHealth + 4.0;
                player.setHealth(newHealth);
            } else {
                player.setHealth(player.getMaxHealth());
            }
        }

        if (clickedItem.getItemMeta().getDisplayName().contains("MEDIUM HEALTH PACK")) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            if (currentHealth < maxHealth - 8 ){
                double newHealth = currentHealth + 8.0;
                player.setHealth(newHealth);
            } else {
                player.setHealth(player.getMaxHealth());
            }
        }

        if (clickedItem.getItemMeta().getDisplayName().contains("LARGE HEALTH PACK")) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            if (currentHealth < maxHealth - 14) {
                double newHealth = currentHealth + 14.0;
                player.setHealth(newHealth);
            } else {
                player.setHealth(player.getMaxHealth());
            }
        }
        player.getInventory().clear(slot);
        ActionBarMessage.actionMessage(player, "Successfully used Health Pack");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.getPlayer().getWorld().getName().equals("arena6")) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        //chests
        if (block != null && block.getType() == Material.CHEST) {
            breakChestAndDropItem(block);
        }

        //save station
        if (Objects.requireNonNull(block).getType().equals(Material.LIME_WOOL) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            savePlayer(player);
        }

    }

    private void breakChestAndDropItem(Block block) {
        BlockState state = block.getState();
        Chest chest = (Chest) state;
        for (ItemStack item : chest.getInventory().getContents()) {
            if (item != null) {
                block.getWorld().dropItemNaturally(block.getLocation(), item);
            }
            state.update();
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1);
            block.setType(Material.AIR);
        }
    }

    private void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        PersistentData persistentData = new PersistentData();
        if (DeadPlayerRegionUtil.deadEntered.containsKey(uuid)) {
            String value = DeadPlayerRegionUtil.deadEntered.get(uuid);
            System.out.println("save player " + value);
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSaveStation", value);

            ItemStack[] items = player.getInventory().getContents();
            String saveInventory = SerializeInventory.itemStackArrayToBase64(items);
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoInventory", saveInventory);
        }
    }
}
    




