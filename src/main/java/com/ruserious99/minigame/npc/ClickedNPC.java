package com.ruserious99.minigame.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.utils.NpcGameStartUtil;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.scheduler.BukkitRunnable;


public class ClickedNPC {

    //todo handle left and centre mouse clicks

    public static void listeningForOurNPCs(Minigame minigame) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(minigame, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                String game = null;

                PacketContainer packetContainer = event.getPacket();

                int npcID = packetContainer.getIntegers().read(0);
                for (Integer clicked_Npc : minigame.getNPCs().keySet()) {
                    if (npcID == clicked_Npc) {
                        ServerPlayer sp = minigame.getNPCs().get(clicked_Npc);
                        game = sp.displayName;
                    }
                }
                if (game != null) {
                    EnumWrappers.Hand hand = packetContainer.getEnumEntityUseActions().read(0).getHand();
                    EnumWrappers.EntityUseAction action = packetContainer.getEnumEntityUseActions().read(0).getAction();

                    if (hand == EnumWrappers.Hand.MAIN_HAND && action == EnumWrappers.EntityUseAction.INTERACT) {
                        String finalGame = game;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                NpcGameStartUtil.joinGame((Minigame) plugin, event.getPlayer(), finalGame);
                            }
                        }.runTaskLater(minigame, 40);
                    }
                }
            }
        });
    }
}