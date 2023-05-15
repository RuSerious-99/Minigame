package com.ruserious99.minigame.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarMessage {

    public static void actionMessage(Player player, String text){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text, ChatColor.YELLOW));
    }
}
