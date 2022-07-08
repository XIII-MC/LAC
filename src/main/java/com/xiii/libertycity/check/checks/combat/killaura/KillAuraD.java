package com.xiii.libertycity.check.checks.combat.killaura;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

@CheckInfo(name = "KillAura D", category = Category.COMBAT)
public class KillAuraD extends Check {

    public boolean cSwing = true;
    private int streak = 0;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getNMSPacket());

            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                final boolean invalid = !cSwing;

                // Player swung and attacked
                if (invalid) {
                    if (++streak > 2) {
                        fail("Attaque invalide", "Aucune");
                    }
                } else {
                    streak = 0;
                }
            }
            cSwing = false;
        }
        if (packet.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {
            cSwing = true;
        }
    }
}
