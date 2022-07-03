package com.ruserious99.minigame.listeners.instance.kit.type;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.kit.CodKit;
import com.ruserious99.minigame.listeners.instance.kit.enums.CodKitType;
import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class CodSpeedKit extends CodKit {

    public CodSpeedKit(Minigame minigame, UUID uuid) {
        super(minigame, CodKitType.SPEED, uuid);
    }

    @Override
    public void atStart(Player p) {

        p.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
        p.getInventory().setItem(1, new ItemStack(Material.BOW));
        p.getInventory().setItem(2, new ItemStack(Material.ARROW, 32));
        p.getInventory().setItem(3, new ItemStack(Material.MUSHROOM_STEW, 2));
        p.getInventory().setItem(4, new ItemStack(Material.APPLE));

        p.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        p.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ConfigMgr.getGameTime(), 1));

        p.updateInventory();
    }
}

