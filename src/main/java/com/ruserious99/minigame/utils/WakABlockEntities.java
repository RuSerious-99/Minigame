package com.ruserious99.minigame.utils;

import com.ruserious99.minigame.Minigame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class WakABlockEntities {

    private Minigame minigame;

    public static void spawn(Minigame minigame,Location l) {
        EntityType et = null;
        final double rand = Math.random();
        if (rand <= 0.1) {
            et = EntityType.GHAST;
        }
        if (rand <= 0.35) {
            et = EntityType.IRON_GOLEM;
        }
        if (rand <= 0.7) {
            et = EntityType.SKELETON;
        }
        if (et == null) {
            et = EntityType.PHANTOM;
        }
        LivingEntity le = null;
        final String s = ChatColor.AQUA + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Pirate";
        if (et == EntityType.PHANTOM) {
            final Phantom b = (Phantom)l.getWorld().spawnEntity(l, EntityType.PHANTOM);
            b.setMaxHealth(b.getMaxHealth() * 2.0);
            b.setHealth(b.getMaxHealth());
            b.setCustomName(s);
            b.setCustomNameVisible(true);
            b.setAware(true);
            le = b;
        }
        else if (et == EntityType.IRON_GOLEM) {
            final IronGolem b2 = (IronGolem)l.getWorld().spawnEntity(l, EntityType.IRON_GOLEM);
            b2.setMaxHealth(b2.getMaxHealth() * 2.0);
            b2.setHealth(b2.getMaxHealth());
            b2.setPlayerCreated(false);
            b2.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Golem");
            b2.setCustomNameVisible(true);
            le = b2;
        }
        else if (et == EntityType.SKELETON) {
            final Skeleton b3 = (Skeleton)l.getWorld().spawnEntity(l, EntityType.SKELETON);
            b3.setSkeletonType(Skeleton.SkeletonType.WITHER);
            b3.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
            b3.setMaxHealth(b3.getMaxHealth() * 2.0);
            b3.setHealth(b3.getMaxHealth());
            b3.setCustomName(s);
            b3.setCustomNameVisible(true);
            le = b3;
        }
        else if (et == EntityType.GHAST) {
            final Ghast b4 = (Ghast)l.getWorld().spawnEntity(l, EntityType.GHAST);
            b4.setMaxHealth(b4.getMaxHealth() * 5.0);
            b4.setHealth(b4.getMaxHealth());
            b4.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Space " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Phantom");
            b4.setCustomNameVisible(true);
            le = b4;
        }
        le.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        le.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, new Random().nextInt(3)));
        le.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, new Random().nextInt(2)));
        final ItemStack i = new ItemStack(Material.DIAMOND_SWORD);
        i.addEnchantment(Enchantment.KNOCKBACK, new Random().nextBoolean() ? 1 : 2);
        le.getEquipment().setItemInHand(i);
        le.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        le.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        le.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        le.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        le.setCanPickupItems(false);
        le.setMetadata("spacePirate", new FixedMetadataValue(minigame, Boolean.TRUE));
    }
}

