package com.xiii.libertycity.check.checks.combat.killaura;

import com.google.common.collect.Lists;
import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.utils.MathUtils;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;

import java.util.Deque;

@CheckInfo(name = "KillAura A", category = Category.COMBAT)
public class KillAuraA extends Check {

    private final Deque<Float> samplesYaw = Lists.newLinkedList();
    private final Deque<Float> samplesPitch = Lists.newLinkedList();

    private double buffer = 0.0d, lastAverage = 0.0d;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == PacketType.Play.Client.LOOK || packet.getPacketId() == PacketType.Play.Client.POSITION_LOOK || packet.getPacketId() == PacketType.Play.Client.POSITION) {
            final float deltaYaw = data.deltaYaw;
            final float deltaPitch = data.deltaPitch;

            final boolean attacking = System.currentTimeMillis() - data.lastAttack < 500;

            if (deltaYaw > 0.0 && deltaPitch > 0.0 && attacking) {
                samplesYaw.add(deltaYaw);
                samplesPitch.add(deltaPitch);
            }

            if (samplesPitch.size() == 20 && samplesYaw.size() == 20) {
                final double averageYaw = samplesYaw.stream().mapToDouble(d -> d).average().orElse(0.0);
                final double averagePitch = samplesPitch.stream().mapToDouble(d -> d).average().orElse(0.0);

                final double deviation = MathUtils.getStandardDeviation(samplesPitch);
                final double averageDelta = Math.abs(averagePitch - lastAverage);

                if (deviation > 6.f && averageDelta > 1.5f && averageYaw < 30.d) {
                    buffer += 0.5;

                    if (buffer > 2.0) fail("Rotations robotique", "dv=" + deviation + " aY=" + averageYaw + " aP=" + averagePitch);
                } else {
                    buffer = Math.max(buffer - 0.125, 0);
                }

                samplesYaw.clear();
                samplesPitch.clear();
                lastAverage = averagePitch;
            }
        }
    }
}
