package com.ruserious99.minigame.instance.kit.gui;

import com.ruserious99.minigame.instance.kit.enums.CodKitType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CodKitUI {

    public CodKitUI(Player player){

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE +"KIT Selection for Stronghold");
        for(CodKitType type: CodKitType.values()){
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            assert isMeta != null;
            isMeta.setDisplayName(type.getDisplay());
            isMeta.setLore(Arrays.asList(type.getDescription()));
            isMeta.setLocalizedName(type.name());
            is.setItemMeta(isMeta);

            gui.addItem(is);
        }
        player.openInventory(gui);
    }
}
