package com.ruserious99.minigame.utils;

import com.ruserious99.minigame.managers.ConfigMgr;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;

public class SpawnSpacePirate {


    public static void spawnTeleportPirates(Location location) {
        final String s = ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString()
                + "M" + ChatColor.GREEN
                + ChatColor.BOLD + "L"
                + ChatColor.AQUA + " POLICE";
        LivingEntity  et = (LivingEntity) Objects.requireNonNull(Bukkit.getWorld("arena5")).spawnEntity(location, getEntity());
        et.setCustomName(s);
        et.setCustomNameVisible(true);
        et.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        et.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, new Random().nextInt(3)));
        et.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, new Random().nextInt(2)));
        final ItemStack i = new ItemStack(Material.DIAMOND_SWORD);
        i.addEnchantment(Enchantment.KNOCKBACK, new Random().nextBoolean() ? 1 : 2);
        et.getEquipment().setItemInHand(i);
        et.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        et.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        et.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        et.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        et.setCanPickupItems(false);
    }

    private static EntityType getEntity() {
        EntityType et = null;
        Random rand = new Random();
        int n = rand.nextInt((1) + 1) ;
        switch (n) {
            //case 0 -> et = EntityType.GHAST;
            //case 1 -> et = EntityType.IRON_GOLEM;
            case 0 -> et = EntityType.SKELETON;
            case 1 -> et = EntityType.ZOMBIFIED_PIGLIN;
        }
        return et;
    }
}
