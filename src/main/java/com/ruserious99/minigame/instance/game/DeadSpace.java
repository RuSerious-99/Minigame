package com.ruserious99.minigame.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.deadspace.deadEnums.ChapterEnum;
import com.ruserious99.minigame.instance.game.deadspace.deadEnums.GameProgress;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadTitlesUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.GameInitUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.SerializeInventoryutil;
import com.ruserious99.minigame.instance.game.deadspace.event.DeadBroadcastEvent;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems.AudioLogs;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems.SpawnItemsChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chestsConfig.Chest_DispenserConfigChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.entityConfig.EntityConfigChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import com.ruserious99.minigame.instance.game.deadspace.gameZones.GameAreas;
import com.ruserious99.minigame.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.npc.NpcGameStartUtil;
import com.ruserious99.minigame.utils.ActionBarMessage;
import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class DeadSpace extends Game {

    private boolean stasis;
    private boolean c1CompInteracted;
    private GameProgress progress;

    public DeadSpace(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
    }

    @Override
    public void onStart() {
        changeBlocks();
        arena.setState(GameState.LIVE);
        GameInitUtil.setupChapter();
        progress = GameInitUtil.progress;

        DeadPlayerRegionUtil.reset();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : arena.getPlayers()) {
                    for (Map.Entry<Cuboid, String> entry : GameAreas.cuboids.entrySet()) {
                        Cuboid cuboid = entry.getKey();
                        String value = entry.getValue();
                        if (cuboid.contains(Objects.requireNonNull(Bukkit.getPlayer(uuid)).getLocation())) {
                            DeadPlayerRegionUtil.deadRegionEvents(uuid, value);
                        }
                    }
                }
            }
        }.runTaskTimer(Minigame.getInstance(), 20L, 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    loadInventory();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(Minigame.getInstance(), 60L);
    }
    @Override
    public void endGame() {
        World world = Bukkit.getWorld("arena6");
        for (Entity entity : Objects.requireNonNull(world).getEntities()) {
            if (entity != null) entity.remove();
        }
        progress = GameProgress.START;
        c1CompInteracted = false;
        stasis = false;
        DeadPlayerRegionUtil.reset();
        arena.reset();

    }

    private void interactWithStasisBlock(Player player, Block block) {
        if (player.hasMetadata("stasis")) {
            ItemStack snowballs = new ItemStack(Material.SNOWBALL, 3);
            player.getInventory().setItemInOffHand(snowballs);
        }
    }

    private void interactWithDispenser(Player player, Block block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Inventory dispenserInv = ((Dispenser) block.getState()).getInventory();
                for (ItemStack item : dispenserInv) {
                    if (item != null) {
                        if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().contains("CREDIT")) {
                            addCreditsToBank(player, item);
                        }
                        if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().contains("HEALTH")) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.getInventory().remove(item);
                                }
                            }.runTaskLater(Minigame.getInstance(), 200L);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (int i = 9; i < 34; i++) {
                                        if (player.getInventory().getItem(i) == null) {
                                            player.getInventory().setItem(i, item);
                                            break;
                                        }
                                    }
                                }
                            }.runTaskLater(Minigame.getInstance(), 30L);

                            ActionBarMessage.actionMessage(player, ChatColor.GREEN + "you acquired " + item.getItemMeta().getDisplayName());
                            player.getInventory().addItem(item);
                        }
                    }
                    dispenserInv.clear();
                }
            }
        }.runTaskLater(Minigame.getInstance(), 40L);

    }

    private void stasisTramRoom(Player player) {
        if (progress == GameProgress.START) {
            EntityConfigChap1.spawnEntityCreeper(EntityConfigChap1.stasisTramAfterInteract());
            progress = GameProgress.TO_DATA_BOARD;
            PersistentData persistentData = new PersistentData();
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoGameProgress", "to_data_board");
            DeadPlayerRegionUtil.reset();
            new DeadTitlesUtil("Objective Added", "get the Data Board", arena);
            ActionBarMessage.actionMessage(player, "Auto save in progress");
            autoSave(player);
        }
    }

    private void c1ComputerInteract(Player player) {
        if (!c1CompInteracted) {
            GameAreas.fillWall("enterBoarding");
            GameAreas.removeWall("firstPartHall");
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.c1ComputerLocation());
            ActionBarMessage.actionMessage(player, ChatColor.GREEN + "Objective Complete: ");
            c1CompInteracted = true;
        }
    }

    private void dataBoardCheck(Player player) {
        Inventory inventory = player.getInventory();
        World world = player.getWorld();
        String name = "DATA_BOARD";

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().contains(name)) {
                player.sendMessage(ChatColor.GREEN + "Objective Complete Replaced DataBoard ");
                new DeadTitlesUtil("Objective Complete", "Replaced DataBoard", arena);
                Location location = new Location(world, 1787, 74, -59);
                location.setYaw(180);
                Block block = world.getBlockAt(location);
                block.setType(Material.JACK_O_LANTERN);
                BlockData blockData = block.getBlockData();
                Directional directional = (Directional) blockData;
                directional.setFacing(BlockFace.SOUTH);
                block.setBlockData(directional);
                inventory.remove(itemStack);
                break;
            }
            ActionBarMessage.actionMessage(player, "You haven't acquired DataBoard");
        }
    }

    private void startToggleDoor(Block block) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Door door) {
            new BukkitRunnable() {
                boolean isOpen = door.isOpen();

                public void run() {
                    isOpen = !isOpen;
                    door.setOpen(isOpen);
                    block.setBlockData(door);
                    if (stasis) {
                        cancel();
                    }
                }
            }.runTaskTimer(Minigame.getInstance(), 0L, 5L);
        }
    }

    private void playAudiofile(Player player) {
        Sound theAudioFile = null;
        for (Cuboid c : GameAreas.cuboids.keySet()) {
            if (c.contains(player.getLocation())) {
                String targetAudio = GameAreas.cuboids.get(c);
                System.out.println("Listeners: playAudioFile " + targetAudio);

                if(progress == GameProgress.START) {
                    switch (targetAudio) {
                        case ("secondPartHall") -> theAudioFile = Sound.ENTITY_BLAZE_DEATH; //run:
                        case ("hallAfterDataBoard") -> theAudioFile = Sound.ENTITY_CAT_HISS; //shoot the limbs:
                        case ("HallDownToFastDoor") -> theAudioFile = Sound.ENTITY_CAT_PURR; //dismemberment:
                        case ("beforeDataBoardComputer") -> theAudioFile = Sound.ENTITY_CAT_PURREOW; //Vent warning
                    }
                }
                if (progress == GameProgress.TO_DATA_BOARD) {
                    switch (targetAudio) {
                        case ("HallDownToFastDoor") -> theAudioFile = Sound.ENTITY_CHICKEN_STEP; //maintenance Bay Unlocked
                        case ("fastDoorTram") -> theAudioFile = Sound.ENTITY_CHICKEN_STEP; //stasis door
                        case ("elevatorDownToDataBoard") -> theAudioFile = Sound.ENTITY_CHICKEN_HURT; //locked_door
                    }
                }
            }
        }
        if (theAudioFile != null) {
            System.out.println("audio file playing " + theAudioFile);
            player.playSound(player.getLocation(), theAudioFile, SoundCategory.PLAYERS, 10f, 1f);
        } else {
            System.out.println("audio file is null");
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

    private void autoSave(Player player) {
        PersistentData persistentData = new PersistentData();
        for (Cuboid c : GameAreas.cuboids.keySet()) {
            if (c.contains(player.getLocation())) {
                String s = GameAreas.cuboids.get(c);
                if (s.equals("repairTramWithStasis")) {
                    System.out.println(" auto save" + s);
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSaveStation", s);

                    ItemStack[] items = player.getInventory().getContents();
                    String saveInventory = SerializeInventoryutil.itemStackArrayToBase64(items);
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoInventory", saveInventory);

                    ItemStack bankAccount = player.getInventory().getItem(35);
                    String money = persistentData.getCustomDataTag(bankAccount, "bank_account");
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoMoney", money);
                }
            }
        }

    }

    private void savePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        PersistentData persistentData = new PersistentData();

        if (DeadPlayerRegionUtil.deadEntered.containsKey(uuid)) {
            String value = DeadPlayerRegionUtil.deadEntered.get(uuid);
            System.out.println("save Station clicked in or auto save" + value);
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

    private void changeBlocks() {
        World world = Bukkit.getServer().getWorld("arena6");

        //remove 3 signs
        Block sign1 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1735, 86, -61));
        sign1.setType(Material.AIR);
        Block sign2 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1739, 86, -61));
        sign2.setType(Material.AIR);
        Block sign3 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1742, 86, -61));
        sign3.setType(Material.AIR);

        // adding stasis stations
        Block stasisFastDoor = Objects.requireNonNull(world).getBlockAt(new Location(world, 1782, 72, -69));
        stasisFastDoor.setType(Material.DIAMOND_ORE);

        Block stasisStasisRoom = Objects.requireNonNull(world).getBlockAt(new Location(world, 1757, 77, -82));
        stasisStasisRoom.setType(Material.DIAMOND_ORE);

        //removing saveStations
        Block noSave = Objects.requireNonNull(world).getBlockAt(new Location(world, 1790, 79, -37));
        noSave.setType(Material.IRON_BLOCK);
        Block noSave1 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1798, 75, -58));
        noSave1.setType(Material.IRON_BLOCK);

        //adding saveStations
        Block save2 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1785, 75, -52));
        save2.setType(Material.LIME_WOOL);

        Block save3 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1770, 75, -81));
        save3.setType(Material.LIME_WOOL);

        Block save4 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1773, 75, -46));
        save4.setType(Material.LIME_WOOL);

        // dispencer in DataBoard tram room
        Location location = new Location(world, 1799, 75, -55);
        location.setYaw(-90);
        Block block = world.getBlockAt(location);
        block.setType(Material.DISPENSER);
        BlockData blockData = block.getBlockData();
        Directional directional = (Directional) blockData;
        directional.setFacing(BlockFace.WEST);
        block.setBlockData(directional);

        // dispencer in tram stasis room
        Location location1 = new Location(world, 1749, 75, -78);
        location.setYaw(-90);
        Block block1 = world.getBlockAt(location1);
        block1.setType(Material.DISPENSER);
        BlockData blockData1 = block1.getBlockData();
        Directional directional1 = (Directional) blockData1;
        directional1.setFacing(BlockFace.EAST);
        block1.setBlockData(directional1);

        // dispencer lower bay
        Location locationBay = new Location(world, 1751, 65, -36);
        location.setYaw(-90);
        Block blockbay = world.getBlockAt(locationBay);
        blockbay.setType(Material.DISPENSER);
        BlockData blockDataBay = blockbay.getBlockData();
        Directional directionalBay = (Directional) blockDataBay;
        directionalBay.setFacing(BlockFace.NORTH);
        blockbay.setBlockData(directionalBay);

        //set to look like a computer monitor kinda
        //databoard room
        Block dataCardTram = Objects.requireNonNull(world).getBlockAt(new Location(world, 1787, 74, -59));
        dataCardTram.setType(Material.JACK_O_LANTERN);
        //stasis tram room
        Block stasisTramRoom = Objects.requireNonNull(world).getBlockAt(new Location(world, 1756, 76, -79));
        stasisTramRoom.setType(Material.JACK_O_LANTERN);

    }

    private void loadInventory() throws IOException {
        Player player = Bukkit.getPlayer(arena.getPlayers().get(0));

        PersistentData persistentData = new PersistentData();
        String convertFrom = persistentData.deadPlayerGetCustomDataTag(Objects.requireNonNull(player), "deadInfoInventory");
        if (!convertFrom.equals("inventory")) {
            ItemStack[] items = SerializeInventoryutil.itemStackArrayFromBase64(persistentData.deadPlayerGetCustomDataTag(player, "deadInfoInventory"));
            int slotSpot = 9;
            for (ItemStack i : items) {
                if (i != null) {
                    if (i.getType().equals(Material.SNOWBALL)) {
                        player.getInventory().setItemInOffHand(i);
                        continue;
                    }
                    if (i.getItemMeta() != null && i.getItemMeta().getDisplayName().contains("BANK ACCOUNT") || i.getItemMeta().getDisplayName().contains("LEAVE GAME")) {
                        continue;
                    }
                    if (slotSpot >= 34) {
                        player.sendMessage(ChatColor.RED + "Your inventory is full");
                    }
                    player.getInventory().setItem(slotSpot, i);
                    slotSpot++;
                }
            }
            player.getInventory().setItem(35, ItemsManager.createBankAccount(persistentData.deadPlayerGetCustomDataTag(Objects.requireNonNull(player), "deadInfoMoney")));
        } else {
            player.getInventory().setItem(35, ItemsManager.bankAccount);
        }
        player.getInventory().setItem(27, ItemsManager.leaveArena);

    }

    private void addCreditsToBank(Player player, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();
        PersistentData persistentData = new PersistentData();
        int bankAccountTotal = Integer.parseInt(persistentData.getCustomDataTag(player.getInventory().getItem(35), "bank_account"));
        int creditTotal = Integer.parseInt(persistentData.getCustomDataTag(itemStack, "credits"));

        player.sendMessage(ChatColor.BLUE + "Added " + ChatColor.YELLOW + creditTotal + ChatColor.BLUE + " to Bank Account");
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

    @EventHandler
    public void onResourceStatus(PlayerResourcePackStatusEvent e) throws IOException {
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
            arena.removePlayer(e.getPlayer());
        }
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            arena.removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!event.getEntity().getWorld().getName().equals("arena6")) {
            return;
        }
        // todo add 50 percent chance of a drop
        event.getDrops().clear();
        event.getDrops().add(Chest_DispenserConfigChap1.getItemStack());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.blockList().clear();
            event.setCancelled(true);
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
        if (clickedItem.getType().equals(Material.SNOWBALL)) {
            e.setCancelled(true);
            return;
        }

        if (e.getView().getTitle().equalsIgnoreCase("dispenser")) {
            e.setCancelled(true);
            return;
        }
        if (e.getView().getTitle().contains("Save Station")) {
            if (!e.isRightClick()) {
                e.setCancelled(true);
                saveStationClicked(player, e.getRawSlot());
            }
        }
        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("LEAVE GAME")) {
            Arena arena = Minigame.getInstance().getArenaMgr().getArena(player);
            arena.removePlayer(player);
        }

        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("BANK ACCOUNT")) {
            e.setCancelled(true);
        }
        if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().contains("HEALTH PACK") && e.isRightClick()) {
            setHealth(player, clickedItem, slot);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        endGame();
    }

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
                    inventory.setItemInOffHand(itemStack);
                    playAudiofile(player);
                }
            }.runTaskLater(Minigame.getInstance(), 2L);
        }
        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().contains("CREDITS")) {
            addCreditsToBank(player, itemStack);
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

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        Player player = Bukkit.getPlayer(arena.getPlayers().get(0));
        World world = Objects.requireNonNull(player).getWorld();
        if (!world.getName().contains("arena6")) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack offhandItem = inventory.getItemInOffHand();
        if (offhandItem.getType() != Material.SNOWBALL && !player.hasMetadata("stasis")) {
            return;
        }

        if (progress == GameProgress.START) {
            if (projectile.getType() == EntityType.SNOWBALL) {
                Entity hitEntity = event.getHitEntity();
                Block hitBlock = event.getHitBlock();

                // Check if the snowball hit an entity
                if (hitEntity instanceof LivingEntity livingEntity) {
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 3));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            livingEntity.removePotionEffect(PotionEffectType.SLOW);
                        }
                    }.runTaskLater(Minigame.getInstance(), 80);
                }

                // Check if the snowball hit a block
                if (hitBlock != null) {
                    if (Objects.requireNonNull(event.getHitBlock()).getLocation().equals(new Location(world, 1779, 72, -72))) {
                        stasis = true;
                        GameAreas.removeWall("hallToUpStairsAfterFastDoor");
                        slowMeDown(player, hitBlock);
                    }
                }
            }
        }

        if (progress == GameProgress.TO_DATA_BOARD) {
            if (projectile.getType() == EntityType.SNOWBALL) {
                Entity hitEntity = event.getHitEntity();
                Block hitBlock = event.getHitBlock();

                // Check if the snowball hit an entity
                if (hitEntity instanceof LivingEntity livingEntity) {
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 3));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            livingEntity.removePotionEffect(PotionEffectType.SLOW);
                        }
                    }.runTaskLater(Minigame.getInstance(), 80);
                }

                // Check if the snowball hit a block
                if (hitBlock != null) {
                    if (Objects.requireNonNull(event.getHitBlock()).getLocation().equals(new Location(world, 1779, 72, -72))) {
                        stasis = true;
                        GameAreas.removeWall("fastDoorTram");
                        slowMeDown(player, hitBlock);
                    }
                }
            }
        }
    }

    private void slowMeDown(Player player, Block block) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Door door) {
            new BukkitRunnable() {
                boolean isOpen = door.isOpen();

                public void run() {
                    if (!stasis) {
                        cancel();
                    }
                    isOpen = !isOpen;
                    door.setOpen(isOpen);
                    block.setBlockData(door);
                }
            }.runTaskTimer(Minigame.getInstance(), 0L, 20L);
            new BukkitRunnable() {
                @Override
                public void run() {
                    stasis = false;
                    String fillIt = DeadPlayerRegionUtil.deadEntered.get(player.getUniqueId());
                    startToggleDoor(block);
                    if (progress == GameProgress.START) {
                        if (!fillIt.equals("fastDoorTram")) {
                            GameAreas.fillWall("fastDoorTram");
                        }
                    } else {
                        if (!fillIt.equals("hallToUpStairsAfterFastDoor")) {
                            GameAreas.fillWall("hallToUpStairsAfterFastDoor");
                        }
                    }

                }
            }.runTaskLater(Minigame.getInstance(), 80L);
        }
    }

    @EventHandler
    public void onRegionEnter(DeadBroadcastEvent event) {
        if (GameInitUtil.chapterState != ChapterEnum.CHAPTER1) {
            return;
        }
        Player player = event.getPlayer();
        World world = Objects.requireNonNull(player).getWorld();
        String message = event.getMessage();

        if (progress == GameProgress.START) {
            startToStasisTram(player, message, world);
        }
        if (progress == GameProgress.TO_DATA_BOARD) {
            stasisToDataBoard(player, message, world);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (!event.getPlayer().getWorld().getName().equals("arena6") || GameInitUtil.chapterState != ChapterEnum.CHAPTER1) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        //interact with computer chapter1
        //boarding just entered ishimura
        if (block.getLocation().equals(new Location(Bukkit.getWorld("arena6"), 1783, 87, -54))) {
            c1ComputerInteract(player);
        }

        //stasis tram room
        if (block.getLocation().equals(new Location(Bukkit.getWorld("arena6"), 1756, 76, -79))) {
            stasisTramRoom(player);
        }

        //interact with dispensers
        if (block.getType() == Material.DISPENSER) {
            interactWithDispenser(player, block);
        }

        //interact with dataBoard repair
        if (block.getLocation().equals(new Location(Bukkit.getWorld("arena6"), 1797, 75, -58))) {
            dataBoardCheck(player);
        }

        //chests
        if (block.getType() == Material.CHEST) {
            breakChestAndDropItem(block);
        }

        //stasis blocks
        if (block.getType() == Material.DIAMOND_ORE && player.hasMetadata("stasis")) {
            interactWithStasisBlock(player, block);
        }

        //save stations
        if (Objects.requireNonNull(block).getType().equals(Material.LIME_WOOL) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            openCustomInventorySaveStation(player);
        }
    }

    private void startToStasisTram(Player player, String message, World world) {
        if (!DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("dataBoard")) {
            if (message.equals("boarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("boarding") && !DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")) {
                arena.sendMessage(ChatColor.YELLOW + "Welcome to the Ishamura.");
                new DeadTitlesUtil(ChatColor.BLUE + "Chapter 1; NEW ARRIVALS", ChatColor.YELLOW + "Objective: get to the Medical Bay", arena);
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.boarding());
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.enterIshamura1());
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.enterIshamura2());
            }
            if (message.equals("enterBoarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")) {
                GameAreas.fillWall("firstPartHall");
                arena.sendMessage(ChatColor.WHITE + "Objective: " + ChatColor.BLUE + "Interact with computer");
            }
            if (message.equals("firstPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("firstPartHall")) {
                GameAreas.fillWall("c1Computer");
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.firstPartHallLocation());
            }
            if (message.equals("secondPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("secondPartHall")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.secondPartHallLocation());
                playAudiofile(player);
            }
            if (message.equals("thirdPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("thirdPartHall")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.thirdPartHallLocation());
            }
            if (message.equals("forthPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("forthPartHall")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.forthPartHallLocation());
            }
            if (message.equals("stairsDown")) {
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.firstWeaponRoom1());
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.firstWeaponRoom2());
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.firstWeaponRoom3());
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.firstWeaponRoom4());
                SpawnItemsChap1.spawnItems(SpawnItemsChap1.smallHealthAfterFirstWeapon(), ItemsManager.smallHealthPack);
                SpawnItemsChap1.spawnItems(SpawnItemsChap1.firstWeaponLocation(), ItemsManager.iron_sword);
                GameAreas.fillWall("forthPartHall");
            }
            if (message.equals("preHallwayAfter")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.firstWeaponLocation());
            }
            if (message.equals("hallwayAfterFirstWeaponRoom2")) {
                AudioLogs.dropAudioLog(AudioLogs.ventWarning());
                Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.HallwayDownStairsLeft());
            }
        }
        if (message.equals("dataBoard")) {
            GameAreas.fillWall("elevatorDownToDataBoard");

            Chest_DispenserConfigChap1.spawnLockerItems(Chest_DispenserConfigChap1.dataBoardDispencer());
            Chest_DispenserConfigChap1.spawnLockerItems(Chest_DispenserConfigChap1.dataBoardLocker1());
            Chest_DispenserConfigChap1.spawnLockerItems(Chest_DispenserConfigChap1.dataBoardLocker2());
            Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.hallwayAfterDataBoard());


        }
        if (message.equals("hallAfterDataBoard")) {
            AudioLogs.dropAudioLog(AudioLogs.shoot_the_limbs());
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.hallwayAfterDataBoardLocation());
            Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.ByFastdoorSnowball());
        }
        if (message.equals("HallDownToFastDoor")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    playAudiofile(player);
                }
            }.runTaskLater(Minigame.getInstance(), 10L);

        }
        if (message.equals("fastDoorTram")) {
            SpawnItemsChap1.spawnItems(SpawnItemsChap1.stasisLocation(), ItemsManager.stasis);
            GameAreas.fillWall("hallToUpStairsAfterFastDoor");
            GameAreas.fillWall("HallDownToFastDoor");
            Block block = new Location(world, 1779, 72, -72).getBlock();
            startToggleDoor(block);
        }
        if (message.equals("hallToUpStairsAfterFastDoor") && !DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("mainHallTopOfStairsAfterFastDoor")) {
            GameAreas.fillWall("fastDoorTram");
            GameAreas.fillWall("flightDeckTramArea");
            GameAreas.fillWall("elevatorDownToCargoBay");
            SpawnItemsChap1.spawnItems(SpawnItemsChap1.mainHallwayToTramStationCredit(), ItemsManager.credits200);
            Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.infrontOfelevatorToCargoBay());
            //spawnItemsTramStasisRoom
            SpawnItemsChap1.spawnItems(SpawnItemsChap1.heathInStasisTramRoom(), ItemsManager.smallHealthPack);
        }
        if (message.equals("repairTramWithStasis")) {
            EntityConfigChap1.spawnEntityCreeper(EntityConfigChap1.stasisTramOnFirstEnter());
            Chest_DispenserConfigChap1.spawnLockerItems(Chest_DispenserConfigChap1.tramStasisRoom());
            new BukkitRunnable() {
                @Override
                public void run() {
                    EntityConfigChap1.spawnEntityCreeper(EntityConfigChap1.stasisTramSecondDelayed());
                }
            }.runTaskLater(Minigame.getInstance(), 20 * 20L);
        }
    }

    private void stasisToDataBoard(Player player, String message, World world) {
        player.setMetadata("stasis", new FixedMetadataValue(Minigame.getInstance(), "true"));
        if (message.contains("mainHallTopOfStairsAfterFastDoor")) {
            GameAreas.removeWall("elevatorDownToDataBoard");
            GameAreas.fillWall("elevatorShaft");
            GameAreas.fillWall("fastDoorTram");
            GameAreas.fillWall("flightDeckTramArea");
            GameAreas.fillWall("elevatorDownToCargoBay");
            Block block = new Location(world, 1779, 72, -72).getBlock();
            startToggleDoor(block);
        }
        if (message.contains("fastDoorTram")) {
            GameAreas.removeWall("HallDownToFastDoor");
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.fastDoortoDataBoardZombie());
            new BukkitRunnable() {
                @Override
                public void run() {
                    EntityConfigChap1.spawnEntitySpider(EntityConfigChap1.fastDoortoDataBoardSpider());
                }
            }.runTaskLater(Minigame.getInstance(), 40L);
        }
        if (message.contains("HallDownToFastDoor")) {
            playAudiofile(player);
        }
        if (message.contains("elevatorDownToDataBoard")) {
            playAudiofile(player);
            Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.bayEnter());
            Chest_DispenserConfigChap1.spawnChest(Chest_DispenserConfigChap1.bayEnterBackCorner());

        }
        if (message.contains("maintenanceBay")) {
            GameAreas.fillWall("dataBoardRoom");
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.bayEnterStraightAHead());
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.bayEnterRightBack());
            if(DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("dataBoardRoom")){
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.bayAfterBoardRoomEnter());
            }
        }
        if (message.contains("dataBoardRoomSpawnEnemiesAfterBoardMain")){
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.bayTurnRightAhead());
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.bayEnterRightLeft());
        }
    }

}


