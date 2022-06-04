package com.ruserious99.minigame.instance;

import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Arena {

    private final Minigame minigame;

    private final int id;
    private final Location spawn;

    private GameState state;
    private final List<UUID> players;
    private Countdown countdown;
    private Game game;


    public Arena(Minigame minigame,int id, Location spawn) {
        this.minigame = minigame;

        this.id = id;
        this.spawn = spawn;

        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
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
                Objects.requireNonNull(Bukkit.getPlayer(uuid)).teleport(location);
            }
            players.clear();
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

        if(state.equals(GameState.RECRUITING) && players.size() >= ConfigMgr.getRequiredPlayers()){
            countdown.start();
        }
    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.teleport(ConfigMgr.getLobbySpawn());
        player.sendTitle("", "", 0,0,0);

        if(state == GameState.COUNTDOWN && players.size()  < ConfigMgr.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "There are not enough players countdown has stopped");
            reset(false);
            return;
        }

        if(state == GameState.LIVE && players.size() < ConfigMgr.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "AWWW! Too many player have left. Game stopping.");
            reset(false);
        }
    }



    //getters
    public int getId() {return id;}
    public GameState getState() {return state;}
    public List<UUID> getPlayers() {return players;}
    public Game getgame(){return game;}
    public void setState(GameState gameState){this.state = gameState;}


}
