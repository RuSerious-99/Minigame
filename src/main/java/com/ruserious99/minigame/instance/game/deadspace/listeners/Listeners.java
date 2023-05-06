package com.ruserious99.minigame.instance.game.deadspace.listeners;

import com.mysql.cj.x.protobuf.MysqlxExpr;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.GameInitUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.SerializeInventoryutil;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems.AudioLogs;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chestsConfig.ChestConfigChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import com.ruserious99.minigame.instance.game.deadspace.gameZones.GameAreas;
import com.ruserious99.minigame.npc.NpcGameStartUtil;
import com.ruserious99.minigame.utils.ActionBarMessage;
import com.ruserious99.minigame.utils.Cuboid;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static java.lang.System.getLogger;

public class Listeners implements Listener {

    private final String SOUND_ID = "your_plugin_name.your_sound_id";

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equals("arena6")) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = event.getItem().getItemStack();

        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().contains("HEALTH")) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.remove(itemStack);
                }
            }.runTaskLater(Minigame.getInstance(), 20L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 9; i < 34; i++) {
                        if (inventory.getItem(i) == null) {
                            inventory.setItem(i, itemStack);
                            break;
                        }
                    }
                }
            }.runTaskLater(Minigame.getInstance(), 30L);
        }

        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().contains("STASIS")) {
            player.sendMessage(ChatColor.BLUE + "STASIS Mod " + ChatColor.YELLOW + "Use in left hand");
            ActionBarMessage.actionMessage(player, ChatColor.BLUE + "STASIS Mod " + ChatColor.YELLOW + "Use in left hand");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.setMetadata("stasis", new FixedMetadataValue(Minigame.getInstance(), "true"));
                    inventory.remove(itemStack);
                    ItemStack itemStack = new ItemStack(Material.SNOWBALL, 3);
                    inventory.addItem(itemStack);
                }
            }.runTaskLater(Minigame.getInstance(), 2L);
        }

        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().contains("CREDITS")) {
            PersistentData persistentData = new PersistentData();
            int bankAccountTotal = Integer.parseInt(persistentData.getCustomDataTag(player.getInventory().getItem(35), "bank_account"));
            int creditTotal = Integer.parseInt(persistentData.getCustomDataTag(itemStack, "credits"));

            ActionBarMessage.actionMessage(player, ChatColor.BLUE + "Added " + ChatColor.YELLOW + creditTotal + ChatColor.BLUE + " to Bank Account");

            inventory.setItem(35, null);
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.remove(itemStack);
                }
            }.runTaskLater(Minigame.getInstance(), 2L);
            player.getInventory().setItem(35, ItemsManager.createBankAccount(String.valueOf((bankAccountTotal + creditTotal))));
        }

        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().contains("AUDIO LOG")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.remove(itemStack);
                }
            }.runTaskLater(Minigame.getInstance(), 2L);
            playAudiofile(player);
        }
    }

    private void playAudiofile(Player player) {
        Sound theAudioFile = null;
        for (Cuboid c : GameAreas.cuboids.keySet()) {
            if (c.contains(player.getLocation())) {
                String targetAudio = GameAreas.cuboids.get(c);
                switch (targetAudio) {
                    case ("hallAfterDataBoard") -> theAudioFile = Sound.ENTITY_CAT_HISS; //benson
                    case ("fterDataBoard") -> theAudioFile = Sound.ENTITY_CAT_HISS;
                }
            }
        }
        if(theAudioFile!=null){
            System.out.println("audio file playing "  + theAudioFile);
            player.playSound(player.getLocation(), theAudioFile, SoundCategory.PLAYERS,1f, 1f);
        }else{
            System.out.println("audio file is null");
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int slot = e.getSlot();
        if (!player.getWorld().getName().equals("arena6")) {
            return;
        }
        if (clickedItem == null || e.isShiftClick()) {
            return;
        }
        if (clickedItem.getItemMeta() == null) {
            return;
        }

        if (e.getView().getTitle().contains("Save Station")) {
            if (!e.isRightClick()) {
                e.setCancelled(true);
                saveStationClicked(player, e.getRawSlot());
            }
        }
        if(Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("LEAVE GAME")){
            Arena arena = Minigame.getInstance().getArenaMgr().getArena(player);
            arena.removePlayer(player);
        }

        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("BANK ACCOUNT")){
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
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!event.getEntity().getWorld().getName().equals("arena6")) {
            return;
        }
        // todo add 50 percent chance of a drop
        event.getDrops().clear();
        event.getDrops().add(ChestConfigChap1.getItemStack());
    }
    private void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        PersistentData persistentData = new PersistentData();
        if (DeadPlayerRegionUtil.deadEntered.containsKey(uuid)) {
            String value = DeadPlayerRegionUtil.deadEntered.get(uuid);
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSaveStation", value);

            ItemStack[] items = player.getInventory().getContents();
            String saveInventory = SerializeInventoryutil.itemStackArrayToBase64(items);
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoInventory", saveInventory);

            ItemStack bankAccount = player.getInventory().getItem(35);
            String money = persistentData.getCustomDataTag(bankAccount, "bank_account");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoMoney", money);

            ActionBarMessage.actionMessage(player, ChatColor.GREEN + "Successfully Saved game!!");
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


        //chests
        if (block.getType() == Material.CHEST) {breakChestAndDropItem(block);}

        //save stations
        if (Objects.requireNonNull(block).getType().equals(Material.LIME_WOOL) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {openCustomInventorySaveStation(player);}
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
    private void openCustomInventorySaveStation(Player player) {
        Inventory customInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Save Station");
        customInventory.setItem(0, ItemsManager.exit);
        customInventory.setItem(8, ItemsManager.leaveArena);
        customInventory.setItem(11, ItemsManager.restartChapter);
        customInventory.setItem(15, ItemsManager.save);
        player.openInventory(customInventory);
    }
    private void saveStationClicked(Player player, int rawSlot) {
        switch (rawSlot) {
            case 0 -> {
            }
            case 8 -> {
                Arena arena = Minigame.getInstance().getArenaMgr().getArena(player);
                GameInitUtil.removePlayer(player);
                arena.reset();
            }
            case 11 -> {
                Arena arena = Minigame.getInstance().getArenaMgr().getArena(player);

                player.getInventory().clear();
                player.sendMessage(ChatColor.GREEN + "Restarting game");
                GameInitUtil.removePlayer(player);
                player.setHealth(0.0);
                arena.reset();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Minigame.getInstance(),
                        () -> {
                            try {
                                NpcGameStartUtil.joinGame(Minigame.getInstance(), player, "DeadSpace");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }, 1L);
            }
            case 15 -> savePlayer(player);
            default -> {
                return;
            }
        }
        player.closeInventory();
    }



}





