package com.ruserious99.minigame.command;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ArenaCommand implements CommandExecutor {

    private final Minigame minigame;

    public ArenaCommand(Minigame minigame) {
        this.minigame = minigame;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;

            if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
                player.sendMessage(ChatColor.GREEN + "These are your available arenas:");
                for(Arena arena : minigame.getArenaMgr().getArenas()){
                    System.out.println("****** in for loop list " + arena.getId());
                    player.sendMessage(ChatColor.GREEN + "- " + arena.getId()
                            + "("
                            + arena.getState().name()
                            + ")");
                }

            }else if(args.length == 1 && args[0].equalsIgnoreCase("leave")){
                Arena arena = minigame.getArenaMgr().getArena(player);
                if(arena != null){
                    player.sendMessage(ChatColor.RED + " you have left the arena");
                    arena.removePlayer(player);
                }else{
                    player.sendMessage(ChatColor.RED + "You are not in an arena");
                }

            }else if(args.length == 2 && args[0].equalsIgnoreCase("join")){
                if(minigame.getArenaMgr().getArena(player) != null){
                    player.sendMessage(ChatColor.RED + " you are already in an arena");
                    return false;
                }
                int id = -1;
                try{
                    id = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "Invalid id given");
                }
                if(id >= 0 && id < minigame.getArenaMgr().getArenas().size()){
                    Arena arena = minigame.getArenaMgr().getArena(id);
                    if(arena.getState() == GameState.RECRUITING ||
                       arena.getState() == GameState.COUNTDOWN){
                        player.sendMessage(ChatColor.RED + "You are now playing in Arena " + id + ".");
                        arena.addPlayer(player);
                    }else{
                        player.sendMessage(ChatColor.RED + "You cant join that arena right now.");
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Invalid id given");
                }

            }else{
                player.sendMessage(ChatColor.RED + "Invalid usage! These are your Options:");
                player.sendMessage(ChatColor.RED + "/arena list");
                player.sendMessage(ChatColor.RED + "/arena leave");
                player.sendMessage(ChatColor.RED + "/arena join <1d>");

            }
        }


        return false;
    }
}
