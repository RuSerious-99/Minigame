package com.ruserious99.minigame.instance.game.deadspace.gameItems;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

    public static void init() {
        gameItems = new HashMap<>();
        persistentData = new PersistentData();

        createSmallHealthPack();
        createMediumHealthPack();
        createLargeHealthPack();
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
        lore.add(ChatColor.GRAY + "Double right click to use");
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
        lore.add(ChatColor.GRAY + "Double right click to use");
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
        lore.add(ChatColor.GRAY + "right click to use");
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

    public static HashMap<ItemStack, Boolean> getGameItems() {
        return gameItems;
    }

    public static PersistentData getPersistentData() {
        return persistentData;
    }
}
