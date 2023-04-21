package com.ruserious99.minigame;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PersistentData {

    public ItemStack setCustomDataTag(ItemStack itemStack, String key, String value) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(new NamespacedKey(Minigame.getInstance(), key), PersistentDataType.STRING)) {
            data.set(new NamespacedKey(Minigame.getInstance(), key), PersistentDataType.STRING, value);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public String getCustomDataTag(ItemStack itemStack, String key) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Minigame.getInstance(), key), PersistentDataType.STRING)) {
            return data.get(new NamespacedKey(Minigame.getInstance(), key), PersistentDataType.STRING);
        }
        return null;
    }

    public ItemStack removeCustomDataTag(ItemStack itemStack, String key) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return null;
        }
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(new NamespacedKey(Minigame.getInstance(), key), PersistentDataType.STRING)) {
            data.remove(new NamespacedKey(Minigame.getInstance(), key));
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    //Player methods
    public void deadPlayerSetCustomDataTags(Player player, String theKey, String tag) {
        NamespacedKey key = new NamespacedKey(Minigame.getInstance(), theKey);
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(key, PersistentDataType.STRING, tag);
    }

    public String deadPlayerGetCustomDataTag(Player player, String theKey){
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Minigame.getInstance(), theKey);
        if(container.has(key, PersistentDataType.STRING)){
            return container.get(key, PersistentDataType.STRING);
        }
        return null;
    }

    public boolean hasPlayerData(Player player, String theKey){
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Minigame.getInstance(), theKey);
        return container.has(key, PersistentDataType.STRING);
    }
}