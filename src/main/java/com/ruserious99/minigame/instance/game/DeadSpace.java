package com.ruserious99.minigame.instance.game;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.deadspace.ChapterEnum.ChapterEnum;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadPlayerRegionUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.DeadTitlesUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.GameInitUtil;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.SerializeInventoryutil;
import com.ruserious99.minigame.instance.game.deadspace.event.DeadBroadcastEvent;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems.AudioLogs;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chapterItems.SpawnItemsChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.chestsConfig.ChestConfigChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameEntities.entityConfig.EntityConfigChap1;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import com.ruserious99.minigame.instance.game.deadspace.gameZones.GameAreas;
import com.ruserious99.minigame.instance.game.deadspace.listeners.Listeners;
import com.ruserious99.minigame.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.utils.ActionBarMessage;
import com.ruserious99.minigame.utils.Cuboid;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class DeadSpace extends Game {

    private boolean stasis;
    private boolean c1CompInteractedWith;

    public DeadSpace(Minigame minigame, Arena arena, Scoreboards scoreboards) {
        super(minigame, arena, scoreboards);
    }

    @Override
    public void onStart() {
        changeBlocks();
        arena.setState(GameState.LIVE);
        registerListeners();
        GameInitUtil.setupChapter();

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

    private void changeBlocks() {
        World world = Bukkit.getServer().getWorld("arena6");

        //3 signs
        Block sign1 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1735, 86, -61));
        sign1.setType(Material.AIR);
        Block sign2 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1739, 86, -61));
        sign2.setType(Material.AIR);
        Block sign3 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1742, 86, -61));
        sign3.setType(Material.AIR);

        //removing saveStations
        Block noSave = Objects.requireNonNull(world).getBlockAt(new Location(world, 1790, 79, -37));
        noSave.setType(Material.IRON_BLOCK);
        Block noSave1 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1798, 75, -58));
        noSave1.setType(Material.IRON_BLOCK);

        //adding saveStations
        Block save2 = Objects.requireNonNull(world).getBlockAt(new Location(world, 1785, 75, -52));
        save2.setType(Material.LIME_WOOL);


        // dispencer in DataBoard tram room
        Location location = new Location(world, 1799, 75, -55);
        location.setYaw(-90);
        Block block = world.getBlockAt(location);
        block.setType(Material.DISPENSER);
        BlockData blockData = block.getBlockData();
        Directional directional = (Directional) blockData;
        directional.setFacing(BlockFace.WEST);
        block.setBlockData(directional);

        Block dataCardTram = Objects.requireNonNull(world).getBlockAt(new Location(world, 1787, 74, -59));
        dataCardTram.setType(Material.JACK_O_LANTERN);

        // iron door 1779 72 -72
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new Listeners(), Minigame.getInstance());
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

    @EventHandler
    public void onResourceStatus(PlayerResourcePackStatusEvent e) throws IOException {
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED)) {
            arena.removePlayer(e.getPlayer());
        }
        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            arena.removePlayer(e.getPlayer());
        }
    }

    @Override
    public void endGame() {
        c1CompInteractedWith = false;
        stasis = false;
        DeadPlayerRegionUtil.reset();
        arena.reset();
    }

    @EventHandler
    public void onRegionEnter(DeadBroadcastEvent event) {
        if (GameInitUtil.chapterState != ChapterEnum.CHAPTER1) {
            return;
        }
        Player player = event.getPlayer();
        World world = Objects.requireNonNull(player).getWorld();
        Arena arena = Minigame.getInstance().getArenaMgr().getArena(5);
        String message = event.getMessage();

        if (!DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("dataBoard")) {
            if (message.equals("boarding") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("boarding") && !DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("enterBoarding")) {
                arena.sendMessage(ChatColor.YELLOW + "Welcome to the Ishamura.");
                new DeadTitlesUtil(ChatColor.BLUE + "Chapter 1; NEW ARRIVALS", ChatColor.YELLOW + "Objective: get to the Medical Bay", arena);
                ChestConfigChap1.spawnChest(ChestConfigChap1.boarding());
                ChestConfigChap1.spawnChest(ChestConfigChap1.enterIshamura1());
                ChestConfigChap1.spawnChest(ChestConfigChap1.enterIshamura2());
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
            }
            if (message.equals("thirdPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("thirdPartHall")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.thirdPartHallLocation());
            }
            if (message.equals("forthPartHall") && DeadPlayerRegionUtil.deadRegionFirstEnter.containsKey("forthPartHall")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.forthPartHallLocation());
            }
            if (message.equals("stairsDown")) {
                ChestConfigChap1.spawnChest(ChestConfigChap1.firstWeaponRoom1());
                ChestConfigChap1.spawnChest(ChestConfigChap1.firstWeaponRoom2());
                ChestConfigChap1.spawnChest(ChestConfigChap1.firstWeaponRoom3());
                ChestConfigChap1.spawnChest(ChestConfigChap1.firstWeaponRoom4());

                SpawnItemsChap1.spawnItems(SpawnItemsChap1.firstWeaponLocation(), ItemsManager.iron_sword);
                GameAreas.fillWall("forthPartHall");
            }
            if (message.equals("preHallwayAfter")) {
                EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.firstWeaponLocation());
            }
            if (message.equals("hallwayAfterFirstWeaponRoom2")) {
                ChestConfigChap1.spawnChest(ChestConfigChap1.HallwayDownStairsLeft());
            }
        }

        if (message.equals("dataBoard")) {
            GameAreas.fillWall("elevatorDownToDataBoard");

            new BukkitRunnable() {
                @Override
                public void run() {
                    ChestConfigChap1.spawnLockerItems(ChestConfigChap1.dataBoardDispencer());
                    ChestConfigChap1.spawnLockerItems(ChestConfigChap1.dataBoardLocker1());
                    ChestConfigChap1.spawnLockerItems(ChestConfigChap1.dataBoardLocker2());
                }
            }.runTaskLater(Minigame.getInstance(), 20L);

        }
        if (message.equals("hallAfterDataBoard")) {
            AudioLogs.audioLog(AudioLogs.bensonAudioLog(), ItemsManager.audioLog);
            SpawnItemsChap1.spawnItems(SpawnItemsChap1.smallHealthAfterFirstWeapon(), ItemsManager.smallHealthPack);
            ChestConfigChap1.spawnChest(ChestConfigChap1.hallwayAfterDataBoard());
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.hallwayAfterDataBoardLocation());
        }
        if (message.equals("fastDoorTram")) {
            SpawnItemsChap1.spawnItems(SpawnItemsChap1.stasisLocation(), ItemsManager.stasis);
            GameAreas.fillWall("blockFastDoorGoing");
            Block block = new Location(world, 1779, 72, -72).getBlock();
            startToggleDoor(block);
        }
    }

    @EventHandler
    private void playerInteractEventChap1(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().equals("arena6") || GameInitUtil.chapterState != ChapterEnum.CHAPTER1) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        //interact with computer chapter1
        if (block.getLocation().equals(new Location(Bukkit.getWorld("arena6"), 1783, 87, -54))) {
            c1ComputerInteract();
        }

        //interact with dataBoard repair
        if (block.getLocation().equals(new Location(Bukkit.getWorld("arena6"), 1797, 75, -58))) {
            dataBoardCheck(player);
        }

    }

    private void c1ComputerInteract() {
        if (!c1CompInteractedWith) {
            GameAreas.fillWall("enterBoarding");
            GameAreas.removeWall("firstPartHall");
            EntityConfigChap1.spawnEntityZombie(EntityConfigChap1.c1ComputerLocation());
            c1CompInteractedWith = true;
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
                    if(stasis){cancel();}
                }
            }.runTaskTimer(Minigame.getInstance(), 0L, 5L);
        }
    }
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        Player player = Bukkit.getPlayer(arena.getPlayers().get(0));
        World world = Objects.requireNonNull(player).getWorld();

        if(!world.getName().contains("arena6")){return;}

        PlayerInventory inventory = player.getInventory();
        ItemStack offhandItem = inventory.getItemInOffHand();
        if(offhandItem.getType() != Material.SNOWBALL && !player.hasMetadata("stasis")){return;}

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
                    GameAreas.removeWall("blockFastDoorGoing");
                    slowMeDown(hitBlock);
                }
            }
        }
    }

    private void slowMeDown(Block block) {
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Door door) {
            new BukkitRunnable() {
                boolean isOpen = door.isOpen();
                public void run() {
                    if(!stasis){cancel();}
                    isOpen = !isOpen;
                    door.setOpen(isOpen);
                    block.setBlockData(door);
                }
            }.runTaskTimer(Minigame.getInstance(), 0L, 20L);
            new BukkitRunnable() {
                @Override
                public void run() {
                    stasis = false;
                    startToggleDoor(block);
                    GameAreas.fillWall("blockFastDoorGoing");
                }
            }.runTaskLater(Minigame.getInstance(), 80L);
        }
    }

}


