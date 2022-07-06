package com.xiii.libertycity.check.checks.player.packet;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;

@CheckInfo(name = "Packet D", category = Category.PLAYER)
public class PacketD extends Check {

    boolean sentsneak;
    int sprintBuffer = 0;
    int sneakBuffer = 0;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {
            WrappedPacketInEntityAction action = new WrappedPacketInEntityAction(packet.getNMSPacket());
            if (action.getAction() == WrappedPacketInEntityAction.PlayerAction.STOP_SNEAKING) {
                if (!sentsneak) {
                    sneakBuffer += 1;
                    if(sneakBuffer > 3) fail("Sneak invalid", "Aucune");
                } else {
                    sentsneak = false;
                    sneakBuffer -= 1;
                }
            }
            if (action.getAction() == WrappedPacketInEntityAction.PlayerAction.START_SNEAKING) {
                if (sentsneak) {
                    fail("Sneak invalid", "Aucune");
                } else {
                    sentsneak = true;
                    sneakBuffer -= 1;
                }
            }
        }

        if(data.player.isSprinting()) {
            if(data.player.getFoodLevel() <= 3) {
                sprintBuffer += 1;
                if(sprintBuffer > 8) fail("Court invalidement", "Aucune");
            } else {
                sprintBuffer = 0;
            }
        }
    }
}
