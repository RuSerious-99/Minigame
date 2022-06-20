package com.ruserious99.minigame.listeners.instance.kit.type;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.listeners.instance.kit.Kit_Blockgame;
import com.ruserious99.minigame.listeners.instance.kit.KitTypeBlockgame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FighterKit extends Kit_Blockgame {

    public FighterKit(Minigame minigame, UUID uuid) {
        super(minigame, KitTypeBlockgame.FIGHTER, uuid);
    }

    @Override
    public void onStart(Player player) {
        player.getInventory().addItem((new ItemStack(Material.DIAMOND_SWORD)));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100,3));
    }
}
