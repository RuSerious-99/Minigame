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

public class CodHeavyWeaponKit extends CodKit {

    public CodHeavyWeaponKit(Minigame minigame, UUID uuid) {
        super(minigame, CodKitType.HEAVYWEAPON, uuid);
    }

    @Override
    public void atStart(Player player) {

        player.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.FISHING_ROD));
        player.getInventory().setItem(3, new ItemStack(Material.MUSHROOM_STEW, 2));
        player.getInventory().setItem(4, new ItemStack(Material.GOLDEN_APPLE));

        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ConfigMgr.getGameTime(), 0));


    }


}
