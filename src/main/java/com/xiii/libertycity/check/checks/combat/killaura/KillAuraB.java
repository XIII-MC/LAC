package com.xiii.libertycity.check.checks.combat.killaura;

import com.google.common.collect.Lists;
import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

@CheckInfo(name = "KillAura B", category = Category.COMBAT)
public class KillAuraB extends Check {

    private final Deque<Float> samplesYaw = Lists.newLinkedList();
    private final Deque<Float> samplesPitch = Lists.newLinkedList();

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == PacketType.Play.Client.LOOK || packet.getPacketId() == PacketType.Play.Client.POSITION_LOOK || packet.getPacketId() == PacketType.Play.Client.POSITION) {
            final float deltaYaw = data.deltaYaw;
            final float deltaPitch = data.deltaPitch;

            final boolean exempt = this.isExempt(ExemptType.TELEPORT);

            if (deltaYaw > 0.0 && deltaPitch > 0.0 && !exempt) {
                samplesPitch.add(deltaPitch);
                samplesYaw.add(deltaYaw);
            }

            if (samplesPitch.size() == 10 && samplesYaw.size() == 10) {
                final AtomicInteger level = new AtomicInteger(0);

                final double averageYaw = samplesYaw.stream().mapToDouble(d -> d).average().orElse(0.0);
                final double averagePitch = samplesPitch.stream().mapToDouble(d -> d).average().orElse(0.0);

                samplesYaw.stream().filter(delta -> delta % 1.0 == 0.0).forEach(delta -> level.incrementAndGet());
                samplesPitch.stream().filter(delta -> delta % 1.0 == 0.0).forEach(delta -> level.incrementAndGet());

                if (level.get() >= 8 && averageYaw > 1.d && averagePitch > 1.d) {
                    fail("Rotations robotique", "lvl=" + level.get() + " aY=" + averageYaw + " aP=" + averagePitch);
                }
            }
        }
    }

}
