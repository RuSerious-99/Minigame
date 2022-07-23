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
    public void atStart(Player p) {

        p.getInventory().clear();
        p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
        p.getInventory().setItem(1, new ItemStack(Material.FISHING_ROD));
        p.getInventory().setItem(3, new ItemStack(Material.MUSHROOM_STEW, 2));
        p.getInventory().setItem(4, new ItemStack(Material.GOLDEN_APPLE));
        p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ConfigMgr.getGameTimeCod(), 0));

        p.updateInventory();
    }


}
