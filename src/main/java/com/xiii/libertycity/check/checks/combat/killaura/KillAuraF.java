package com.xiii.libertycity.check.checks.combat.killaura;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.entity.Entity;

@CheckInfo(name = "KillAura F", category = Category.COMBAT)
public class KillAuraF extends Check {

    Entity lastTarget;
    Entity target;
    int targetedEntities;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity ue = new WrappedPacketInUseEntity(packet.getNMSPacket());
            lastTarget = target == null ? ue.getEntity() : target;
            target = ue.getEntity();
            if(target != lastTarget) {
                targetedEntities++;
            }
            if(targetedEntities > 1)
                fail("Attaque trop d'entitées", "tE=" + targetedEntities);

        }
        if(packet.getPacketId() == PacketType.Play.Client.POSITION || packet.getPacketId() == PacketType.Play.Client.POSITION_LOOK || packet.getPacketId() == PacketType.Play.Client.LOOK) {
            targetedEntities = 0;
        }
    }

}
