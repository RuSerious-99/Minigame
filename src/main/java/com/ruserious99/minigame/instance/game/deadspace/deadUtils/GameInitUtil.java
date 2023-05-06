package com.ruserious99.minigame.instance.game.deadspace.deadUtils;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.PersistentData;
import com.ruserious99.minigame.instance.Arena;
import com.ruserious99.minigame.instance.game.deadspace.ChapterEnum.ChapterEnum;
import com.ruserious99.minigame.instance.game.deadspace.deadUtils.suitUtils.SuitsConfig;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.ItemsManager;
import com.ruserious99.minigame.instance.game.deadspace.gameZones.GameAreas;
import com.ruserious99.minigame.instance.game.deadspace.gameZones.SaveStations;
import com.ruserious99.minigame.instance.game.deadspace.gameZones.Walls;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.BiomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Optional;

public class GameInitUtil {

    public static ChapterEnum chapterState;

    public static void setupChapter() {
        Arena arena = Minigame.getInstance().getArenaMgr().getArena(5);
        Player player = Bukkit.getPlayer(arena.getPlayers().get(0));

        PersistentData persistentData = new PersistentData();
        assert player != null;
        String chap = persistentData.deadPlayerGetCustomDataTag(player, "deadInfoChapter");
        switch (chap) {
            case "chapter1" -> {
                chapterState = ChapterEnum.CHAPTER1;
                GameAreas.createRegionsChapter1();
                Walls.createWallsChapter1();
            }
            case "chapter2" -> {
                GameAreas.createRegionsChapter2();
                Walls.createWallsChapter2();
                chapterState = ChapterEnum.CHAPTER2;
            }
        }
    }
    public static void removePlayer(Player player){
        Minigame minigame = Minigame.getInstance();
       // player.setResourcePack("https://sourceforge.net/projects/mcresoursepacks/files/VanillaDefault.zip/download");
        minigame.getDisguiseManager().deleteDisguise(player);
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert maxHealthAttribute != null;
        maxHealthAttribute.setBaseValue(20.0);
        new BukkitRunnable() {
            @Override
            public void run() {
                applySuit(player);
            }
        }.runTaskLater(Minigame.getInstance(), 40);
    }
    public static void addPlayerToDeadSpace(Player player, Location spawn){
        if (Objects.requireNonNull(spawn.getWorld()).getName().equals("arena6")) {
            ItemsManager.init();
            savePlayerBasicInfo(player);
            SuitsConfig.setSuitparams(player);
           // player.setResourcePack("https://sourceforge.net/projects/mcresoursepacks/files/sounddead.zip/download");
            new BukkitRunnable() {
                @Override
                public void run() {
                    applySuit(player);
                }
            }.runTaskLater(Minigame.getInstance(), 40);
        }
    }
    public static Location getSpawnPoint(Player player) {
        PersistentData persistentData = new PersistentData();
        if (persistentData.hasPlayerData(player, "deadInfoSaveStation")) {
            String spawnLocation = persistentData.deadPlayerGetCustomDataTag(player, "deadInfoSaveStation");
            return SaveStations.getSpawnLocation(spawnLocation);
        }
        return null;
    }
    private static void savePlayerBasicInfo(Player player) {
        PersistentData persistentData = new PersistentData();
        if(!persistentData.hasPlayerData(player, "deadInfoChapter")){
            System.out.println("saving player basic info");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoChapter", "chapter1");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSuit", "startSuit");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoMoney", "0");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoSaveStation", "spawn");
            persistentData.deadPlayerSetCustomDataTags(player, "deadInfoInventory", "inventory");
        }
    }
    private static void applySuit(Player player) {
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
}
