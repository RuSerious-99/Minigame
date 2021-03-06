package com.ruserious99.minigame.listeners.instance;

import com.google.common.collect.TreeMultimap;
import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.game.*;
import com.ruserious99.minigame.listeners.instance.kit.CodKit;
import com.ruserious99.minigame.listeners.instance.kit.Kit;
import com.ruserious99.minigame.listeners.instance.kit.enums.CodKitType;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import com.ruserious99.minigame.listeners.instance.kit.gui.CodKitUI;
import com.ruserious99.minigame.listeners.instance.kit.gui.KitUI;
import com.ruserious99.minigame.listeners.instance.kit.type.BlockFighterKit;
import com.ruserious99.minigame.listeners.instance.kit.type.BlockMinerKit;
import com.ruserious99.minigame.listeners.instance.kit.type.CodHeavyWeaponKit;
import com.ruserious99.minigame.listeners.instance.kit.type.CodSpeedKit;
import com.ruserious99.minigame.listeners.instance.team.Team;
import com.ruserious99.minigame.listeners.instance.timers.BlockTimer;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.util.*;

//TODO: MAKE KITS GENERIC HERE

public class Arena {

    private final Minigame minigame;
    private final String gameName;
    private final int id;
    private final Location spawn;
    private final BlockTimer timer;

    private final HashMap<UUID, Kit> kits;
    private final HashMap<UUID, CodKit> codKits;
    private final HashMap<UUID, Team> teams;
    private final List<UUID> players;

    private GameState state;
    private Countdown countdown;
    private Game game;


    public Arena(Minigame minigame, int id, Location spawn, String gameName) {
        this.minigame = minigame;
        this.gameName = gameName;
        this.id = id;
        this.spawn = spawn;

        this.timer = new BlockTimer();
        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.codKits = new HashMap<>();
        this.kits = new HashMap<>();
        this.teams = new HashMap<>();
        this.countdown = new Countdown(minigame, this);

        startNewGameType(id);

    }

    private void startNewGameType(int id) {
        System.out.println("start nrw game type " + id);
        game = null;
        switch (id) {
            case (0) -> this.game = new BlockGame(minigame, this, timer);
            case (1) -> this.game = new PvpGame(minigame, this, timer);
            case (2) -> this.game = new Wak_A_Block(minigame, this, timer);
            case (3) -> this.game = new CodStronghold(minigame, this, timer);
        }
    }

    public void start() {
        game.start();
    }

    public void reset() {

        if (state == GameState.LIVE) {
            Location location = ConfigMgr.getLobbySpawn();

            for (UUID uuid : players) {
                Objects.requireNonNull(Bukkit.getPlayer(uuid)).teleport(location);
            }

            for (UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);
                Objects.requireNonNull(player).getInventory().clear();
                removeKit(player.getUniqueId());
                removeTeam(player);
                minigame.getTimer().removePlayer(player);
            }
            minigame.getTimer().cancelTimer();
            players.clear();


            switch (Objects.requireNonNull(spawn.getWorld()).getName()) {
                case ("arena1") -> minigame.getGameMapArena1().restoreFromSource(); // block game
                case ("arena2") -> minigame.getGameMapArena2().restoreFromSource(); // 1vs1 pvp
                case ("arena3") -> minigame.getGameMapArena3().restoreFromSource(); //
                case ("arena4") -> minigame.getGameMapArena4().restoreFromSource(); // team pvp stronghold

            }
            minigame.releaseLoadArena(id);
            game.unregister();
        }
        if (state.equals(GameState.COUNTDOWN)) {
            countdown.cancel();
            countdown = new Countdown(minigame, this);
        }
        sendTitle("", "", 0, 0, 0);
        state = GameState.RECRUITING;


    }

    public void sendMessage(String message) {
        for (UUID uuid : players) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(message);
        }
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        for (UUID uuid : players) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        }
    }

    //players
    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        player.teleport(spawn);
        player.getInventory().clear();

        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena1")) {
            new KitUI(player);
            ImplTeams(player);
        } else if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena4")) {
            new CodKitUI(player);
            ImplTeams(player);
        }

        if (state.equals(GameState.RECRUITING) && players.size() >= getRequiredPlayerCount()) {
            countdown.start();
        }
    }

    private int getRequiredPlayerCount() {
        return switch (Objects.requireNonNull(spawn.getWorld()).getName()) {
            case "arena1" -> ConfigMgr.getRequiredPlayersBlockGame();
            case "arena2" -> ConfigMgr.getRequiredPlayersPvpOneOnOne();
            case "arena3" -> ConfigMgr.getRequiredPlayersWak_A_Block();
            case "arena4" -> ConfigMgr.getRequiredPlayersStronghold();
            default -> -1;
        };
    }

    private void ImplTeams(Player player) {
        Team lowest = getLowestTeamCount();
        setTeam(player, lowest);
        player.sendMessage(ChatColor.GOLD + "You are on team " + lowest.getDisplay() + ChatColor.GOLD + " Team");
    }

    private Team getLowestTeamCount() {
        TreeMultimap<Integer, Team> count = TreeMultimap.create();
        for (Team t : Team.values()) {
            count.put(getTeamCount(t), t);
        }
        return (Team) count.values().toArray()[0];
    }

    public void removePlayer(Player player) throws IOException {
        players.remove(player.getUniqueId());

        player.getInventory().clear();
        player.teleport(ConfigMgr.getLobbySpawn());
        player.sendTitle("", "", 0, 0, 0);

        removeKit(player.getUniqueId());
        removeTeam(player);

        minigame.getTimer().removePlayer(player);

        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena4")) {
            Objects.requireNonNull(player).setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());        }

        if (state == GameState.COUNTDOWN && players.size() < getRequiredPlayerCount()) {
            sendMessage(ChatColor.RED + "There are not enough players: Countdown has stopped");
            reset();
            return;
        }

        if (state == GameState.LIVE && players.size() < getRequiredPlayerCount()) {
            sendMessage(ChatColor.RED + "AWW! Too many players have left. Game stopping.");
            reset();
        }
    }

    public void removeKit(UUID uuid) {
        if (codKits.containsKey(uuid)) {
            codKits.get(uuid).remove();
            codKits.remove(uuid);
        }
        if (kits.containsKey(uuid)) {
            kits.get(uuid).remove();
            kits.remove(uuid);
        }
    }

    public void setCodKit(UUID uuid, CodKitType type) {
        removeKit(uuid);

        switch (type) {
            case HEAVYWEAPON -> codKits.put(uuid, new CodHeavyWeaponKit(minigame, uuid));
            case SPEED -> codKits.put(uuid, new CodSpeedKit(minigame, uuid));
        }
    }

    public void setKit(UUID uuid, KitType type) {
        removeKit(uuid);

        switch (type) {
            case FIGHTER -> kits.put(uuid, new BlockFighterKit(minigame, uuid));
            case MINER -> kits.put(uuid, new BlockMinerKit(minigame, uuid));
        }
    }

    public void setTeam(Player player, Team team) {
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
    }

    public void removeTeam(Player player) {
        teams.remove(player.getUniqueId());
    }

    public int getTeamCount(Team team) {
        int count = 0;
        for (Team t : teams.values()) {
            if (t == team) {
                count++;
            }
        }
        return count;
    }


    public Team getTeam(Player player) {
        return teams.get(player.getUniqueId());
    }

    public HashMap<UUID, Kit> getKits() {
        return kits;
    }

    public HashMap<UUID, CodKit> getCodKits() {
        return codKits;
    }

    public int getId() {
        return id;
    }

    public GameState getState() {
        return state;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setState(GameState gameState) {
        this.state = gameState;
    }

    public Minigame getMinigame() {
        return minigame;
    }


}
