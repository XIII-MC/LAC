package com.xiii.libertycity.check.checks.combat.killaura;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.utils.MathUtils;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

@CheckInfo(name = "KillAura E", category = Category.COMBAT)
public class KillAuraE extends Check {

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() != PacketType.Play.Client.BLOCK_DIG) {
            boolean isPost = isPost(packet.getPacketId(), PacketType.Play.Client.USE_ENTITY);
            if (isPost) fail("Mauvais ordre des packets", "Aucune");
            if(isPost) packet.setCancelled(true);

        }
    }

}
