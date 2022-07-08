package com.xiii.libertycity.check.checks.player.packet;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

@CheckInfo(name = "Packet D", category = Category.PLAYER)
public class PacketD extends Check {

    boolean sentsneak;
    int sprintBuffer = 0;
    int sneakBuffer = 0;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {
            WrappedPacketInEntityAction action = new WrappedPacketInEntityAction(packet.getNMSPacket());
            if (action.getAction() == WrappedPacketInEntityAction.PlayerAction.STOP_SNEAKING) {
                if (!sentsneak) {
                    sneakBuffer += 1;
                    if (sneakBuffer > 3) fail("Sneak invalid", "Aucune");
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

        if (data.player.isSprinting()) {
            if (data.player.getFoodLevel() <= 3) {
                sprintBuffer += 1;
                if (sprintBuffer > 8) fail("Court invalidement", "Aucune");
            } else {
                sprintBuffer = 0;
            }
        }

        if (packet.getPacketId() == PacketType.Play.Client.FLYING) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getNMSPacket());

            if (wrapper.hasRotationChanged()) {
                final float pitch = Math.abs(wrapper.getPitch());

                /*
                 * Pitch will always be clamped between 90 and -90 (even when teleporting, etc). This threshold is here
                 * because of some PvP client which messed it up on climbables, however it has since been fixed.
                 */
                final float threshold = isExempt(ExemptType.CLIMBABLE) ? 91.11f : 90.f;

                if (pitch > threshold) {
                    fail("Rotations impossible", "Aucune");
                }
            }
        }
    }
}
