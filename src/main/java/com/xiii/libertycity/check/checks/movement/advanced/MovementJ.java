package com.xiii.libertycity.check.checks.movement.advanced;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement J", category = Category.MOVEMENT)
public class MovementJ extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.WEB, ExemptType.FLYING, ExemptType.SLIME, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.INSIDE_VEHICLE, ExemptType.NEAR_VEHICLE, ExemptType.PISTON, ExemptType.PLACE, ExemptType.VELOCITY);
        if (data.inAir) {
            if (Math.abs(data.predymotion - motionY) > 0.005 && !exempt)
                fail("Predictions non suivis", "my=" + motionY + " py=" + data.predymotion);
        } else buffer = 0;
    }

}
