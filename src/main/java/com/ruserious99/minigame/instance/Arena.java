package com.ruserious99.minigame.instance;

import com.google.common.collect.TreeMultimap;
import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.kit.Kit;
import com.ruserious99.minigame.kit.KitType;
import com.ruserious99.minigame.kit.type.FighterKit;
import com.ruserious99.minigame.kit.type.MinerKit;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Arena {

    private final Minigame minigame;

    private final int id;
    private final Location spawn;

    private GameState state;
    private final HashMap<UUID, Kit> kits;
    private final HashMap<UUID, Team> teams;
    private final List<UUID> players;
    private Countdown countdown;
    private Game game;


    public Arena(Minigame minigame,int id, Location spawn) {
        this.minigame = minigame;

        this.id = id;
        this.spawn = spawn;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.kits = new HashMap<>();
        this.teams = new HashMap<>();
        this.countdown = new Countdown(minigame, this);
        this.game = new Game(this);
    }

    //Game
    public void start() {
        game.start();
    }

    public void reset(Boolean resetPlayers){
        if(resetPlayers){
            Location location = ConfigMgr.getLobbySpawn();
            for(UUID uuid : players){
                Player player = Bukkit.getPlayer(uuid);
                Objects.requireNonNull(player).teleport(location);
                removeKit(player.getUniqueId());
            }
            players.clear();
            teams.clear();
            kits.clear();
        }

        sendTitle("", "");

        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(minigame, this);
        game = new Game(this);
    }

    //tools
    public void sendMessage(String message){
        for(UUID uuid : players){
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(message);
        }
    }
    public void sendTitle(String title, String subTitle){
        for(UUID uuid : players){
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendTitle(title, subTitle, 10,10,10);
        }
    }

    //players
    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        player.teleport(spawn);

        TreeMultimap<Integer, Team> count = TreeMultimap.create();
        for(Team t : teams.values()){
           count.put(getTeamCount(t), t);
        }
        Team lowest = (Team) count.values().toArray()[0];
        setTeam(player, lowest);
        player.sendMessage(ChatColor.GOLD + "You are on team " + lowest.getDisplay() + ChatColor.GOLD + " Team");

        player.sendMessage("Don't forget your kit /arena kit");

        if(state.equals(GameState.RECRUITING) && players.size() >= ConfigMgr.getRequiredPlayers()){
            countdown.start();
        }
    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(ConfigMgr.getLobbySpawn());
        player.sendTitle("", "", 0,0,0);

        removeKit(player.getUniqueId());
        removeTeam(player);

        if(state == GameState.COUNTDOWN && players.size()  < ConfigMgr.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "There are not enough players countdown has stopped");
            reset(false);
            return;
        }

        if(state == GameState.LIVE && players.size() < ConfigMgr.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "AWWW! Too many players have left. Game stopping.");
            reset(false);
        }
    }

    public void removeKit(UUID uuid){
      if(kits.containsKey(uuid)){
          kits.get(uuid).remove();
          kits.remove(uuid);
      }
    }

    public void setKit(UUID uuid, KitType type){
        removeKit(uuid);
        switch(type){
            case FIGHTER: {
                kits.put(uuid, new FighterKit(minigame, uuid));
            }
            case MINER: {
                kits.put(uuid, new MinerKit(minigame, uuid));
            }
        }
    }

    public KitType getKitType(Player player){
        return kits.containsKey(player.getUniqueId()) ? kits.get(player.getUniqueId()).getType() : null;
    }


    public void setTeam(Player player, Team team){
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
    }

    public void removeTeam(Player player){
        if(teams.containsKey(player.getUniqueId())){
            teams.remove(player.getUniqueId());
        }
    }

    public int getTeamCount(Team team){
        int count = 0;
        for(Team t : teams.values()){
            if(t == team){
                count++;
            }
        }
        return count;
    }
    public Team getTeam(Player player){
        return teams.get(player.getUniqueId());
    }

    //getters
    public HashMap<UUID, Kit> getKits() {return kits;}
    public int getId() {return id;}
    public GameState getState() {return state;}
    public List<UUID> getPlayers() {return players;}
    public Game getgame(){return game;}
    public void setState(GameState gameState){this.state = gameState;}


}
