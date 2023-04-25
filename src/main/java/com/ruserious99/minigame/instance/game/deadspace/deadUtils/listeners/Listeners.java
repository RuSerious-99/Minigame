package com.ruserious99.minigame.instance.game.deadspace.deadUtils.listeners;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.SerializeInventory;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.ChestConfig;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.EntityConfig;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import com.ruserious99.minigame.utils.ActionBarMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("arena6")) {
            return;
        }
        Inventory inventory = player.getInventory();

        ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack.getItemMeta().getDisplayName().contains("CREDITS")) {
            PersistentData persistentData = new PersistentData();
            int bankAccountTotal = Integer.parseInt(persistentData.getCustomDataTag(player.getInventory().getItem(35), "bank_account"));
            int creditTotal = Integer.parseInt(persistentData.getCustomDataTag(itemStack, "credits"));

            ActionBarMessage.actionMessage(player, ChatColor.BLUE + "Added " + ChatColor.YELLOW  + creditTotal + ChatColor.BLUE + " to Bank Account");

            inventory.setItem(35, null);
            new BukkitRunnable(){
                @Override
                public void run() {
                    inventory.remove(itemStack);
                }
            }.runTaskLater(Minigame.getInstance(), 20L);
            player.getInventory().setItem(35, ItemsManager.createBankAccount(String.valueOf((bankAccountTotal + creditTotal))));
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int slot = e.getSlot();
        if (!player.getWorld().getName().equals("arena6")) {
            return;
        }
        if (clickedItem == null) {
            return;
        }
        if (clickedItem.getItemMeta() == null) {
            return;
        }

        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("BANK ACCOUNT")) {
            e.setCancelled(true);
        }
        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("HEALTH PACK") && e.isRightClick()) {
            setHealth(player, clickedItem, slot);
        }
    }
    private void setHealth(Player player, ItemStack clickedItem, int slot) {
        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("SMALL HEALTH PACK")) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            if (currentHealth < maxHealth - 4) {
                double newHealth = currentHealth + 4.0;
                player.setHealth(newHealth);
            } else {
                player.setHealth(player.getMaxHealth());
            }
        }

        if (clickedItem.getItemMeta().getDisplayName().contains("MEDIUM HEALTH PACK")) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getMaxHealth();
            if (currentHealth < maxHealth - 8) {
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
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSaveStation", value);

            ItemStack[] items = player.getInventory().getContents();
            String saveInventory = SerializeInventory.itemStackArrayToBase64(items);
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoInventory", saveInventory);

            ItemStack bankAccount = player.getInventory().getItem(35);
            String money = persistentData.getCustomDataTag(bankAccount, "bank_account");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoMoney", money);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() == EntityConfig.chapter1_zombie) {
            event.getDrops().clear();
            event.getDrops().add(ChestConfig.getItemStack());
        }
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

        //after interact with computer chap1
        if (block.getLocation().equals(new Location(Bukkit.getWorld("arena6"), 1783, 87, -54))) {
            EntityConfig.spawnEntityChapter1(EntityConfig.c1ComputerLocation(), EntityConfig.c1ComputerEntity());
        }

        //chests
        if (block.getType() == Material.CHEST) {
            breakChestAndDropItem(block);
        }
        //save station
        if (Objects.requireNonNull(block).getType().equals(Material.LIME_WOOL) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            savePlayer(player);
        }

    }
}
    




