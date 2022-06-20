package com.ruserious99.minigame.listeners.instance.kit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Objects;

public class KitUI_Blockgame {

    public KitUI_Blockgame(Player player){
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Kit Selection fo Block game");

        for(KitTypeBlockgame type : KitTypeBlockgame.values()){
            ItemStack is = new ItemStack(type.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            Objects.requireNonNull(isMeta).setDisplayName(type.getDisplay());
            isMeta.setLore(Collections.singletonList(type.getDescription()));
            isMeta.setLocalizedName(type.name());
            is.setItemMeta(isMeta);

            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
