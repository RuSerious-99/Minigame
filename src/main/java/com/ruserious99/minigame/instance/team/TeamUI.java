package com.ruserious99.minigame.instance.team;

import com.ruserious99.minigame.instance.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Objects;

public class TeamUI {

    public TeamUI(Arena arena, Player player){
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Team Selection");

        for(Team team : Team.values()){
            ItemStack is = new ItemStack(team.getMaterial());
            ItemMeta isMeta = is.getItemMeta();
            Objects.requireNonNull(isMeta).setDisplayName(team.getDisplay() + " " + ChatColor.GRAY + "(" + arena.getTeamCount(team) + " players)");
            isMeta.setLocalizedName(team.name());
            is.setItemMeta(isMeta);

            gui.addItem(is);
        }

        player.openInventory(gui);
    }
}
