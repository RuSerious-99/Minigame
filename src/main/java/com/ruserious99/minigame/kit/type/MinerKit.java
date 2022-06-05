package com.ruserious99.minigame.kit.type;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.kit.Kit;
import com.ruserious99.minigame.kit.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class MinerKit extends Kit {

    public MinerKit(Minigame minigame, UUID uuid) {
        super(minigame, KitType.MINER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem((new ItemStack(Material.DIAMOND_PICKAXE)));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100,3));
    }
}
