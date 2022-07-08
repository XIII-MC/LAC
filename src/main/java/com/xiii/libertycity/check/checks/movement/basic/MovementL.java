package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement L", category = Category.MOVEMENT)
public class MovementL extends Check {

    int AirTicks;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.GLIDE, ExemptType.FLYING, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.PLACE, ExemptType.NEAR_VEHICLE, ExemptType.INSIDE_VEHICLE, ExemptType.PISTON, ExemptType.SLIME, ExemptType.ICE);
        if (!exempt) {
            if (!data.playerGround) {
                AirTicks++;
            } else {
                AirTicks = 0;
            }
            if (AirTicks > 3 && !data.blockabove) {
                double value = ((Math.pow((motionX + motionZ), 8)) - (Math.pow(data.getMotionX(3) + data.getMotionZ(3), 8)) * 0.91);
                if (value > 0.000086 || value < -0.00086) {
                    fail("Strafed In Air", value); // 3.5 and add 1
                } else {
                    removeBuffer(); // 0.35
                }
            }
        }
    }

}
