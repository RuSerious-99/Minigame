package com.ruserious99.minigame.listeners.instance;

import com.google.common.collect.TreeMultimap;
import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.kit.KitUI_Blockgame;
import com.ruserious99.minigame.listeners.instance.game.BlockGame;
import com.ruserious99.minigame.listeners.instance.game.Game;
import com.ruserious99.minigame.listeners.instance.game.PvpGame;
import com.ruserious99.minigame.listeners.instance.kit.Kit_Blockgame;
import com.ruserious99.minigame.listeners.instance.kit.KitTypeBlockgame;
import com.ruserious99.minigame.listeners.instance.kit.type.FighterKit;
import com.ruserious99.minigame.listeners.instance.kit.type.MinerKit;
import com.ruserious99.minigame.managers.ConfigMgr;
import com.ruserious99.minigame.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.util.*;

public class Arena {

    private final Minigame minigame;
    private final String gameName;
    private final int id;
    private final Location spawn;

    private GameState state;
    private final HashMap<UUID, Kit_Blockgame> kits;
    private final HashMap<UUID, Team> teams;
    private final List<UUID> players;
    private Countdown countdown;
    private Game game;


    public Arena(Minigame minigame, int id, Location spawn, String gameName) {
        this.minigame = minigame;
        this.gameName = gameName;
        this.id = id;
        this.spawn = spawn;


        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.kits = new HashMap<>();
        this.teams = new HashMap<>();
        this.countdown = new Countdown(minigame, this);

        startNewGameType(id);

    }

    private void startNewGameType(int id) {
        game = null;
        switch(id){
            case(0):
                this.game = new BlockGame(minigame, this);
                break;

            case(1):
                this.game = new PvpGame(minigame, this);
                break;
        }
    }


    public void start() {
        game.start();
    }

    public void reset() {

        if(state == GameState.LIVE){
            Location location = ConfigMgr.getLobbySpawn();

            for(UUID uuid : players){
                Player player = Bukkit.getPlayer(uuid);
                Objects.requireNonNull(player).teleport(location);
                removeKit(player.getUniqueId());
            }
            players.clear();

            switch(Objects.requireNonNull(spawn.getWorld()).getName()){
                case("arena1"):{
                    minigame.getGameMapArena1().restoreFromSource();
                    break;
                }
                case("arena2"):{
                    minigame.getGameMapArena2().restoreFromSource();
                    break;
                }
            }
            minigame.releaseLoadArena(id);
        }

        sendTitle("", "");
        state = GameState.RECRUITING;
        countdown.cancel();
        countdown = new Countdown(minigame, this);

        game.unregister();

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
        player.getInventory().clear();

        if(Objects.requireNonNull(spawn.getWorld()).getName().equals("arena1")){
            new KitUI_Blockgame(player);
        }


        //Team lowest = getLowestTeamCount();

       // setTeam(player, lowest);
        //player.sendMessage(ChatColor.GOLD + "You are on team " + lowest.getDisplay() + ChatColor.GOLD + " Team");

        if(state.equals(GameState.RECRUITING) && players.size() >= ConfigMgr.getRequiredPlayers()){
            countdown.start();
        }
    }

    private Team getLowestTeamCount() {
        TreeMultimap<Integer, Team> count = TreeMultimap.create();
        for(Team t : Team.values()){
            count.put(getTeamCount(t), t);
        }
        return (Team) count.values().toArray()[0];
    }

    public void removePlayer(Player player) throws IOException {
        players.remove(player.getUniqueId());

        player.getInventory().clear();
        player.teleport(ConfigMgr.getLobbySpawn());
        player.sendTitle("", "", 0,0,0);

        removeKit(player.getUniqueId());
        removeTeam(player);

        if(state == GameState.COUNTDOWN && players.size()  < ConfigMgr.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "There are not enough players countdown has stopped");
            reset();
            return;
        }

        if(state == GameState.LIVE && players.size() < ConfigMgr.getRequiredPlayers()){
            sendMessage(ChatColor.RED + "AWW! Too many players have left. Game stopping.");
            reset();
        }
    }

    public void removeKit(UUID uuid){
      if(kits.containsKey(uuid)){
          kits.get(uuid).remove();
          kits.remove(uuid);
      }
    }

    public void setKit(UUID uuid, KitTypeBlockgame type){
        removeKit(uuid);
        System.out.println("set kit = " + type.name());
        switch(type.name()){
            case "FIGHTER": {
                kits.put(uuid, new FighterKit(minigame, uuid));
            }
            case "MINER": {
                kits.put(uuid, new MinerKit(minigame, uuid));
            }
        }
    }

    public KitTypeBlockgame getKitType(Player player){
        if(kits.containsKey(player.getUniqueId())){
            System.out.println("get KitType = " + kits.get(player.getUniqueId()).getType());
            return kits.get(player.getUniqueId()).getType();
        }else{
            return null;
        }
    }


    public void setTeam(Player player, Team team){
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
    }

    public void removeTeam(Player player){
        teams.remove(player.getUniqueId());
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

    public String getGameName() {return gameName;}
    public HashMap<UUID, Kit_Blockgame> getKits() {return kits;}
    public int getId() {return id;}
    public GameState getState() {return state;}
    public List<UUID> getPlayers() {return players;}
    public Game getGame(){return game;}
    public void setState(GameState gameState){this.state = gameState;}


}