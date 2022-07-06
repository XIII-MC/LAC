package com.xiii.libertycity.check.checks.movement.advanced;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import org.bukkit.Bukkit;

@CheckInfo(name = "Movement C", category = Category.MOVEMENT)
public class MovementC extends Check {

    double prediction;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        WrappedPacketInFlying flying = new WrappedPacketInFlying(packet.getNMSPacket());
        if(flying.isMoving()) {
            prediction = data.getLastDeltaXZ() * (double) 0.91F + (data.player.isSprinting() ? 0.026 : 0.02);
            double diff = data.getDeltaXZ() - prediction;
            boolean exempt = isExempt(ExemptType.NEAR_VEHICLE, ExemptType.FLYING, ExemptType.TELEPORT, ExemptType.VELOCITY);
            debug("d=" + diff);
            if(data.airticks > 2 && !exempt) {
                if(diff > 0.006) {
                    fail("Predictions non suivis", "d=" + diff);
                } else {
                    removeBuffer();
                }
            }
        }
    }

}
