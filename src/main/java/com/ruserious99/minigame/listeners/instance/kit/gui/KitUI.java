package com.ruserious99.minigame.listeners.instance.kit.gui;

import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class KitUI {

    public KitUI(Player player){
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit Selection for Block game");

        for(KitType type : KitType.values()){
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            Objects.requireNonNull(isMeta).setDisplayName(type.getDisplay());
            isMeta.setLore(List.of(type.getDescription()));
            isMeta.setLocalizedName(type.name());
            is.setItemMeta(isMeta);

            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
