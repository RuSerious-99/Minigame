package com.ruserious99.minigame.instance.kit.type;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.kit.CodKit;
import com.ruserious99.minigame.instance.kit.enums.CodKitType;
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
    public void atStart(Player player) {

        player.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.BOW));
        player.getInventory().setItem(2, new ItemStack(Material.ARROW, 32));
        player.getInventory().setItem(3, new ItemStack(Material.MUSHROOM_STEW, 2));
        player.getInventory().setItem(4, new ItemStack(Material.APPLE));

        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ConfigMgr.getGameTimeCod(), 1));

        player.updateInventory();
    }
}

