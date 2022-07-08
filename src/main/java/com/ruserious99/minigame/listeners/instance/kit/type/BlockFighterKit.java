package com.ruserious99.minigame.listeners.instance.kit.type;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.kit.Kit;
import com.ruserious99.minigame.listeners.instance.kit.enums.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class BlockFighterKit extends Kit {

    public BlockFighterKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.FIGHTER, uuid);
    }

    @Override
    public void atStart(Player player) {
        player.getInventory().addItem((new ItemStack(Material.DIAMOND_SWORD)));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000,3));
    }
}
