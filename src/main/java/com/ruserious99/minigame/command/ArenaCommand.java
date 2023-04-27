package com.ruserious99.minigame.command;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.managers.DataMgr;
import com.ruserious99.minigame.managers.NpcPacketMgr;
import com.ruserious99.minigame.npc.*;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ArenaCommand implements CommandExecutor {

    private final Minigame minigame;

    public ArenaCommand(Minigame minigame) {
        this.minigame = minigame;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

        if (sender instanceof Player player) {
            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.GREEN + "These are your available arenas:");
                for (Arena arena : minigame.getArenaMgr().getArenas()) {

                    player.sendMessage(ChatColor.GREEN + "- " + arena.getId()
                            + "("
                            + arena.getState().name()
                            + ")");
                }

            }else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
                DataMgr.reload();

            } else if (args.length == 2 && args[0].equalsIgnoreCase("createNpc")) {
                if (player.isOp()) {
                    if (args[1].equalsIgnoreCase("Stronghold")) {
                        CreateStrongholdNPC c = new CreateStrongholdNPC(minigame);
                        c.createNpc(player);
                    } else if (args[1].equalsIgnoreCase("Block")) {
                        CreateBlockNPC c = new CreateBlockNPC(minigame);
                        c.createNpc(player);
                    } else if (args[1].equalsIgnoreCase("Pvp")) {
                        CreatePvpNPC c = new CreatePvpNPC(minigame);
                        c.createNpc(player);
                    } else if (args[1].equalsIgnoreCase("Wak_A_Block")) {
                        CreateWak_A_BlockNPC c = new CreateWak_A_BlockNPC(minigame);
                        c.createNpc(player);
                    } else if (args[1].equalsIgnoreCase("Dungeon")) {
                        CreateDungeonNPC c = new CreateDungeonNPC(minigame);
                        c.createNpc(player);
                    } else if (args[1].equalsIgnoreCase("DeadSpace")) {
                        CreateDeadSpaceNPC c = new CreateDeadSpaceNPC(minigame);
                        c.createNpc(player);
                    } else {
                        player.sendMessage("NPC not found");
                        player.sendMessage("usage = /arena createNpc <NPC name>");
                    }
                } else {
                    player.sendMessage("Only Ops can perform this task");
                }



            } else if (args.length == 2 && args[0].equalsIgnoreCase("removeNpc")) {

                int id = -1;
                ServerPlayer sp = null;

                for (ServerPlayer p : minigame.getNPCs().values()) {
                    if (p.getBukkitEntity().getDisplayName().toLowerCase().contains(args[1].toLowerCase())) {
                        id = p.getId();
                        sp = p;
                        break;
                    }
                }
                if (id != -1) {
                    RemoveNpc r = new RemoveNpc(minigame);
                    r.removeNpc(id, player);
                }

                if (sp != null) {
                    for (Player playerR : Bukkit.getOnlinePlayers()) {
                        NpcPacketMgr mgr = new NpcPacketMgr(minigame, sp);
                        mgr.removePacket(playerR);
                    }
                }

            }else if(args.length == 1 && args[0].equalsIgnoreCase("deadspace_reset")) {
                PersistentData persistentData = new PersistentData();
                if(persistentData.hasPlayerData(player, "deadInfoChapter")){
                    System.out.println("resetting player basic info");
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoChapter", "chapter1");
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSuit", "startSuit");
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoMoney", "0");
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSaveStation", "spawn");
                    persistentData.deadPlayerSetCustomDataTags(player, "deadInfoInventory", "inventory");
                }

        } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
            Arena arena = minigame.getArenaMgr().getArena(player);
            if (arena != null) {
                player.sendMessage(ChatColor.RED + " you have left the arena");
                try {
                    arena.removePlayer(player);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are not in an arena");
            }



        } else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            if (minigame.getArenaMgr().getArena(player) != null) {
                player.sendMessage(ChatColor.RED + " you are already in an arena");
                return false;
            }
            int id = -1;
            try {
                id = Integer.parseInt(args[1]);
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Invalid id given");
            }
            System.out.println("arenas************** # " + minigame.getArenaMgr().getArenas().size());
            if (id >= 0 && id < minigame.getArenaMgr().getArenas().size()) {
                Arena arena = minigame.getArenaMgr().getArena(id);
                if (arena.getState() == GameState.RECRUITING ||
                        arena.getState() == GameState.COUNTDOWN) {
                    player.sendMessage(ChatColor.GOLD + "You are now playing in Arena " + id + ".");
                    try {
                        arena.addPlayer(player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You cant join that arena right now.");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Invalid id given");
            }



        } else {
            if (player.isOp()) {
                player.sendMessage(ChatColor.RED + "Invalid usage! These are your Options:");
                player.sendMessage(ChatColor.RED + "/arena createNpc");
                player.sendMessage(ChatColor.RED + "/arena list");
                player.sendMessage(ChatColor.RED + "/arena leave");
                player.sendMessage(ChatColor.RED + "/arena join <1d>");
            } else {
                player.sendMessage(ChatColor.RED + "Invalid usage! These are your Options:");
                player.sendMessage(ChatColor.RED + "/arena list");
                player.sendMessage(ChatColor.RED + "/arena leave");
                player.sendMessage(ChatColor.RED + "/arena join <1d>");
            }
        }
    }
        return false;
}
}
