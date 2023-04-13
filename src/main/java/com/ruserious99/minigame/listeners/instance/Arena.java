package com.ruserious99.minigame.listeners.instance;

import com.google.common.collect.TreeMultimap;
import com.ruserious99.minigame.GameState;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.game.*;
import com.ruserious99.minigame.listeners.instance.game.deadspace.deadUtils.SuitsUtil;
import com.ruserious99.minigame.listeners.instance.game.dungeon.Dungeon;
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
import com.ruserious99.minigame.listeners.instance.scorboards.Scoreboards;
import com.ruserious99.minigame.listeners.instance.team.Team;
import com.ruserious99.minigame.managers.ConfigMgr;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.BiomeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class Arena {

    private final Minigame minigame;
    private final int id;
    private final Location spawn;
    private final Scoreboards scoreboards;

    private final HashMap<UUID, Kit> kits;
    private final HashMap<UUID, CodKit> codKits;
    private final HashMap<UUID, Team> teams;
    private final List<UUID> players;

    private GameState state;
    private Countdown countdown;
    private Game game;

    public Arena(Minigame minigame, int id, Location spawn, String gameName) {
        this.minigame = minigame;
        this.id = id;
        this.spawn = spawn;

        this.scoreboards = new Scoreboards();
        this.state = GameState.RECRUITING;
        this.players = new ArrayList<>();
        this.codKits = new HashMap<>();
        this.kits = new HashMap<>();
        this.teams = new HashMap<>();
        this.countdown = new Countdown(minigame, this);

        startNewGameType(id);
    }

    private void startNewGameType(int id) {
        game = null;
        switch (id) {
            case (0) -> this.game = new BlockGame(minigame, this, scoreboards);
            case (1) -> this.game = new PvpGame(minigame, this, scoreboards);
            case (2) -> this.game = new Wak_A_Block(minigame, this, scoreboards);
            case (3) -> this.game = new CodStronghold(minigame, this, scoreboards);
            case (4) -> this.game = new Dungeon(minigame, this, scoreboards);
            case (5) -> this.game = new DeadSpace(minigame, this, scoreboards);
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
                removeGameTimer(player);
                removeKit(player.getUniqueId());
                removeTeam(player);

            }
            players.clear();

            switch (Objects.requireNonNull(spawn.getWorld()).getName()) {
                case ("arena1") -> minigame.getGameMapArena1().restoreFromSource(); // block game
                case ("arena2") -> minigame.getGameMapArena2().restoreFromSource(); // 1vs1 pvp
                case ("arena3") -> minigame.getGameMapArena3().restoreFromSource(); // wak
                case ("arena4") -> minigame.getGameMapArena4().restoreFromSource(); // team pvp stronghold
                case ("arena5") -> minigame.getGameMapArena5().restoreFromSource(); // dungeon
                case ("arena6") -> minigame.getGameMapArena6().restoreFromSource(); // deadspace
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

        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena6")) {
            minigame.getDisguiseManager().applyDisguise(player,"Isac Clark", SuitsUtil.startSuitValue(), SuitsUtil.startSuitSignature());
            player.setResourcePack("https://sourceforge.net/projects/mcresoursepacks/files/last_days.zip/download");
        }

        player.getInventory().clear();
        playerKitSelect(player);
        countdown.start();
        player.teleport(spawn);

        new BukkitRunnable() {
            @Override
            public void run() {
                applySuit(player);
            }
        }.runTaskLater(Minigame.getInstance(), 40);
    }
    private void applySuit(Player player) {
        ServerPlayer sp = ((CraftPlayer)player).getHandle();
        sp.connection.send(new ClientboundRespawnPacket(
                sp.getLevel().dimensionTypeId(),
                sp.getLevel().dimension(),
                BiomeManager.obfuscateSeed(sp.getLevel().getSeed()),
                sp.gameMode.getGameModeForPlayer(),
                sp.gameMode.getPreviousGameModeForPlayer(),
                sp.getLevel().isDebug(),
                sp.getLevel().isFlat(),
                (byte) 1,
                Optional.of(GlobalPos.of(sp.getLevel().dimension(), sp.getOnPos()))
        ));
    }

    private void playerKitSelect(Player player) {
        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena1")) {
            new KitUI(player);
            ImplTeams(player);
        }
        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena3")) {
            new KitUI(player);
        }
        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena4")) {
            new CodKitUI(player);
            ImplTeams(player);
        }
        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena5")) {
            new KitUI(player);
        }
    }

    private int getRequiredPlayerCount() {
        return switch (Objects.requireNonNull(spawn.getWorld()).getName()) {
            case "arena1" -> ConfigMgr.getRequiredPlayersBlockGame();
            case "arena2" -> ConfigMgr.getRequiredPlayersPvpOneOnOne();
            case "arena3" -> ConfigMgr.getRequiredPlayersWak_A_Block();
            case "arena4" -> ConfigMgr.getRequiredPlayersStronghold();
            case "arena5" -> ConfigMgr.getRequiredPlayersDungeon();
            case "arena6" -> ConfigMgr.getRequiredPlayersDeadSpace();

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
        removeGameTimer(player);

        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena6")) {
            player.sendMessage("removing Map resource pack");
            player.setResourcePack("https://sourceforge.net/projects/mcresoursepacks/files/VanillaDefault.zip/download");
            minigame.getDisguiseManager().deleteDisguise(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    applySuit(player);
                }
            }.runTaskLater(Minigame.getInstance(), 40);
        }

        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena4")) {
            Objects.requireNonNull(player).setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());
        }

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

    private void removeGameTimer(Player player) {
        switch (Objects.requireNonNull(spawn.getWorld()).getName()) {
            case "arena1" -> BlockGame.removePlayerGameScore(player);
            case "arena2" -> PvpGame.removePlayerGameScore(player);
            case "arena3" -> Wak_A_Block.removePlayerGameScore(player);
            case "arena4" -> CodStronghold.removePlayerGameScore(player);
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
