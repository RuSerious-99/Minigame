package com.ruserious99.minigame.listeners.instance.game.dungeon;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.Arena;
import com.ruserious99.minigame.listeners.instance.game.dungeon.events.ServerBroadcastEvent;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.PlayerRegionUtil;
import com.ruserious99.minigame.listeners.instance.game.dungeon.utilities.Titles;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.utils.SpawnSpacePirate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects  ;

public class TeleportationRoom implements Listener {

    private boolean isStarted;
    private boolean displayTitle;
    private final Arena arena;

    private LinkedList<Location> buttonLocations;
    private LinkedList<Location> currentButtons;
    private Block teleportLever = null;
    private HashMap<Location, ChatColor> buttonColors;
    private ArmorStand holoSolve;
    private ArmorStand holoSolve1;


    public TeleportationRoom(Minigame minigame) {
        arena = minigame.getArenaMgr().getArena(4);

    }
    @EventHandler
    public void onRegionEnter(ServerBroadcastEvent event) {
        World world  = event.getPlayer().getWorld();

        if (event.getMessage().contains("teleportRoom") && !displayTitle) {
            new Titles(ChatColor.BLUE + "Objective: Solve Teleportation Room Puzzle " + ChatColor.YELLOW, ConfigMgr.getTeleportationRoom(), event.getMessage(), arena);
            displayTitle = true;
        }

        if (!isStarted && event.getMessage().contains("teleportRoom")) {
            isStarted = true;

            arena.sendMessage(ChatColor.GREEN + "Opperate the teleportation device and escape the ship before it falls apart!"
                    + "To use the teleportor, you must operate all 10 buttons in the correct order, then pull the lever in the center of the room.");
            arena.sendMessage(ChatColor.AQUA + "Hurry! you only have " + ChatColor.RED + (ConfigMgr.getTeleportationRoom()/ 60) + ChatColor.AQUA + " minutes to find it!");
            Dungeon.updateTimer(ConfigMgr.getMaze());

            holoSolve = (ArmorStand) world.spawnEntity(new Location(world, -235.0, 70.0, 190.0), EntityType.ARMOR_STAND);
            holoSolve.setVisible(false);
            holoSolve.setCustomNameVisible(true);
            holoSolve.setCustomName(ChatColor.RED + "Locked");
            holoSolve.setGravity(false);

            holoSolve1 = (ArmorStand) world.spawnEntity(new Location(world, -235.0, 69.75, 190.0), EntityType.ARMOR_STAND);
            holoSolve1.setVisible(false);
            holoSolve1.setCustomNameVisible(true);
            holoSolve1.setCustomName(ChatColor.GRAY + "Solve puzzle to complete Dungeon");
            holoSolve1.setGravity(false);

            ArmorStand holoSolve2 = (ArmorStand) world.spawnEntity(new Location(world, -235.0, 69.5, 190.0), EntityType.ARMOR_STAND);
            holoSolve2.setVisible(false);
            holoSolve2.setCustomNameVisible(true);
            holoSolve2.setCustomName(" ");
            holoSolve2.setGravity(false);

            addButtonLocations(world);
        }
    }

    private void addButtonLocations(World world) {
        this.teleportLever = null;
        this.currentButtons = new LinkedList<>();
        this.buttonLocations = new LinkedList<>();
        this.buttonColors = new HashMap<>();
        this.buttonLocations.add(new Location(world, -235.0, 70.0, 199.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -237.0, 70.0, 199.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -239.0, 70.0, 199.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -240.0, 70.0, 198.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -240.0, 70.0, 196.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -240.0, 70.0, 185.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -240.0, 70.0, 183.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -239.0, 70.0, 182.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -237.0, 70.0, 182.0).getBlock().getLocation());
        this.buttonLocations.add(new Location(world, -235.0, 70.0, 182.0).getBlock().getLocation());
        Collections.shuffle(this.buttonLocations);
        this.buttonColors.put(new Location(world, -235.0, 70.0, 199.0).getBlock().getLocation(), ChatColor.DARK_GRAY);
        this.buttonColors.put(new Location(world, -237.0, 70.0, 199.0).getBlock().getLocation(), ChatColor.LIGHT_PURPLE);
        this.buttonColors.put(new Location(world, -239.0, 70.0, 199.0).getBlock().getLocation(), ChatColor.BLUE);
        this.buttonColors.put(new Location(world, -240.0, 70.0, 198.0).getBlock().getLocation(), ChatColor.GRAY);
        this.buttonColors.put(new Location(world, -240.0, 70.0, 196.0).getBlock().getLocation(), ChatColor.RED);
        this.buttonColors.put(new Location(world, -240.0, 70.0, 185.0).getBlock().getLocation(), ChatColor.GOLD);
        this.buttonColors.put(new Location(world, -240.0, 70.0, 183.0).getBlock().getLocation(), ChatColor.DARK_PURPLE);
        this.buttonColors.put(new Location(world, -239.0, 70.0, 182.0).getBlock().getLocation(), ChatColor.AQUA);
        this.buttonColors.put(new Location(world, -237.0, 70.0, 182.0).getBlock().getLocation(), ChatColor.YELLOW);
        this.buttonColors.put(new Location(world, -235.0, 70.0, 182.0).getBlock().getLocation(), ChatColor.GREEN);
        this.teleportLever = new Location(world, -236.0, 71.0, 191.0).getBlock();

        teleportLever.setType(Material.LEGACY_LEVER);

        for (Location loc : buttonLocations) {
            if (buttonColors.containsKey(loc)) {
                Bukkit.getConsoleSender().sendMessage((buttonColors.get(loc) + "colour"));
            }
        }
    }

    @EventHandler
    public void onPlayerInteractTeleportLever(final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getLocation().getWorld();


        if (!player.getWorld().getName().contains("arena5")) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!PlayerRegionUtil.entered.get(player.getUniqueId()).contains("teleportRoom")) {
            return;
        }
        if (buttonLocations != null) {
            if (buttonLocations.contains(Objects.requireNonNull(event.getClickedBlock()).getLocation())) {
                final ChatColor buttonColor = buttonColors.getOrDefault(event.getClickedBlock().getLocation(), ChatColor.WHITE);
                currentButtons.add(event.getClickedBlock().getLocation());
                arena.sendMessage(buttonColor + event.getPlayer().getName() + " pressed the button at " + event.getClickedBlock().getX() + "x, " + event.getClickedBlock().getZ() + "z " + buttonColor + "(#" + currentButtons.size() + ")");
            }
        }

        if (teleportLever != null && teleportLever.equals(event.getClickedBlock())) {
            if (currentButtons.size() == 0) {
                arena.sendMessage("Nothing happens.");
                return;
            }

            for (int x = 0; x < 9; ++x) {
                if (x >= currentButtons.size()) {
                    arena.sendMessage(ChatColor.RED + "ERROR: Not enough input data!");
                    return;
                }

                if (!(currentButtons.get(x)).equals(buttonLocations.get(x))) {
                    arena.sendMessage("#" + (x + 1) + " - " + ChatColor.RED + "INVALID, reset!");
                    currentButtons.clear();
                    if (Math.random() < .20) {
                        arena.sendMessage("Space monsters are attacking the ship!");
                        for (int i = 5; i > 0; --i) {
                            SpawnSpacePirate.spawnTeleportPirates(new Location(world, -226.0, 75.0, 190.0));
                        }
                    }
                    return;
                }
                arena.sendMessage("#" + (x + 1) + " - " + ChatColor.GREEN + "CORRECT");
            }

            Location l = player.getLocation();
            l.add(0, 5.0, 15.0);
            spawnFireworks(l, 5 );
            holoSolve.setCustomName(ChatColor.GREEN + "CONGRATULATIONS");
            holoSolve1.setCustomName(ChatColor.GRAY + "You have cleared the Dungeon!");
        }

    }

    public void spawnFireworks(Location location, int amount){
        Firework fw = (Firework) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }
}
