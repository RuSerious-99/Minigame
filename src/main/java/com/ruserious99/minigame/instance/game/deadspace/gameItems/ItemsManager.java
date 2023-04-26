package com.ruserious99.minigame.instance.game.deadspace.gameItems;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ruserious99.minigame.PersistentData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemsManager {

    private ItemsManager() {
    }

    private static HashMap<ItemStack, Boolean> gameItems;
    private static PersistentData persistentData;

    public static ItemStack smallHealthPack;
    public static ItemStack mediumHealthPack;
    public static ItemStack largeHealthPack;
    public static ItemStack save;
    public static ItemStack restartChapter;
    public static ItemStack exit;

    public static int creditAdded = 100;
    public static ItemStack credits;

    public static String balance = "0";
    public static ItemStack bankAccount;

    public static void init() {
        gameItems = new HashMap<>();
        persistentData = new PersistentData();

        createSmallHealthPack();
        createMediumHealthPack();
        createLargeHealthPack();
        createSave();
        createExit();
        createRestartChapter();
        createCredits();
        createBankAccount("0");
    }

    private static void createCredits() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "" + creditAdded + "CREDITS");

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GRAY + "Added to bank Account");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjdlMmFlYWJjOTBhNWU2ODRiYzU0ZTAxOWZmMjkxOTk5OTE5ZmRmNDc1OTg4ZTIyYTc0YmFjYTU5N2YyMGZkZSJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);

        credits = tempSkull;
        credits = persistentData.setCustomDataTag(tempSkull, "credits", String.valueOf(creditAdded));
        gameItems.put(credits, false);
    }
    public static ItemStack createBankAccount(String balance) {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_PURPLE + "BANK ACCOUNT");


        List<String> lore = new ArrayList<>();
        lore.add("============");
        lore.add("");
        lore.add(ChatColor.WHITE + "Balance");
        lore.add(ChatColor.GREEN + balance);
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDRkYjI0YTAwYWZkMTFjNTQyYjVmZWVlMDBjN2I0MTZkNzhhNjhiNmYwMzY3YTllMTAyNWYwYTJmY2FkMTNhNiJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        bankAccount = tempSkull;
        bankAccount = persistentData.setCustomDataTag(tempSkull, "bank_account", balance);

        gameItems.put(bankAccount, false);
        return bankAccount;
    }
    private static void createSmallHealthPack() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "SMALL HEALTH PACK");

        List<String> lore = new ArrayList<>();
        lore.add("ReGain the upper Hand");
        lore.add("");

        lore.add("");
        lore.add(ChatColor.GRAY + "Replenishes you 2 hearts");
        lore.add(ChatColor.GRAY + "Right click to use");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhkZjhkN2U0Y2EwN2E2NTVlYzI0ODEzNzgxZDFhMjc3NTEzOWM2ODUyMDgwZjgxM2Q0NjNhNjFmMjAyNzY5OSJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        smallHealthPack = tempSkull;
        smallHealthPack = persistentData.setCustomDataTag(tempSkull, "healthPack", "small");

        gameItems.put(smallHealthPack, false);
    }
    private static void createMediumHealthPack() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "MEDIUM HEALTH PACK");

        List<String> lore = new ArrayList<>();
        lore.add("ReGain the upper Hand");
        lore.add("");

        lore.add("");
        lore.add(ChatColor.GRAY + "Replenishes you 4 hearts");
        lore.add(ChatColor.GRAY + "Right click to use");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGMzODRmODNiODk3YTI2ODkxYzVhZDc3OWRmYTE4ZDUyZmMwMzM3YmQ4MWQ5OWFjMmViYjZkZTI1NzYxNzk3NyJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        mediumHealthPack = tempSkull;
        mediumHealthPack = persistentData.setCustomDataTag(tempSkull, "healthPack", "medium");

        gameItems.put(mediumHealthPack, false);
    }
    private static void createLargeHealthPack() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "LARGE HEALTH PACK");

        List<String> lore = new ArrayList<>();
        lore.add("ReGain the upper Hand");
        lore.add("");

        lore.add("");
        lore.add(ChatColor.GRAY + "Replenishes you 6 hearts");
        lore.add(ChatColor.GRAY + "Right click to use");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE5ZTUyYmNhYjc2NzU3ZjY5NDU4ZjYzNWYzMjIzOWVmZDUzOTZlNTVkYzRmZWRkNzAxMjA1NzNkNmM1YjY2NCJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        largeHealthPack = tempSkull;
        largeHealthPack = persistentData.setCustomDataTag(tempSkull, "healthPack", "large");

        gameItems.put(largeHealthPack, false);
    }
    private static void createSave() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "SAVE PROGRESS");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "=============");
        lore.add("");
        lore.add(ChatColor.GRAY + "saves location and inventory");
        lore.add(ChatColor.GRAY + "Right click to use");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODk0MjgyN2Q0MzRmYWE4MWU4NGZkMDY4ODJmOWExNTUzY2I3YzIwYTExM2Q1NzM0ZjM3ZWY0ODBlMGMyYTU5YSJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        save = tempSkull;
        save = persistentData.setCustomDataTag(tempSkull, "save", "progress");

        gameItems.put(save, false);
    }
    private static void createRestartChapter() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "RESTART");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "=============");
        lore.add("");
        lore.add(ChatColor.GRAY + "Restarts location and inventory");
        lore.add(ChatColor.GRAY + "Right click to use");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc3NDIwMzRmNTlkYjg5MGM4MDA0MTU2YjcyN2M3N2NhNjk1YzQzOTlkOGUwZGE1Y2U5MjI3Y2Y4MzZiYjhlMiJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        restartChapter = tempSkull;
        restartChapter = persistentData.setCustomDataTag(tempSkull, "restart", "progress");

        gameItems.put(restartChapter, false);
    }
    private static void createExit() {
        ItemStack tempSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) tempSkull.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + "EMERGENCY EXIT");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "=============");
        lore.add("");
        lore.add(ChatColor.GRAY + "Exit this menu inventory");
        lore.add(ChatColor.GRAY + "Right click to use");
        lore.add("");

        meta.setLore(lore);

        tempSkull.setItemMeta(meta);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGYwMzQwYTc4YzA2ZjNjZTg4ZjRkNDdiYTgwYzQ4MDEwNjMyMzY3MDQ0MjQ3ZGZkYmNmZmRhYzc3ODllZTZhMyJ9fX0="));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException x){
            x.printStackTrace();
        }
        tempSkull.setAmount(1);
        tempSkull.setItemMeta(meta);
        exit = tempSkull;
        exit = persistentData.setCustomDataTag(tempSkull, "exit", "menu");

        gameItems.put(exit, false);
    }
}
