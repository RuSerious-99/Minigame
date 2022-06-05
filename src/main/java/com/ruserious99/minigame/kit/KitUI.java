package com.ruserious99.minigame.kit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

public class KitUI {

    public KitUI(Player player){
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit Selection");

        for(KitType type : KitType.values()){
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            Objects.requireNonNull(isMeta).setDisplayName(type.getDisplay());
            isMeta.setLore(Arrays.asList(type.getDescription()));
            isMeta.setLocalizedName(type.name());
            is.setItemMeta(isMeta);

            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
