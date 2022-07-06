package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement H", category = Category.MOVEMENT)
public class MovementH extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.FLYING, ExemptType.SLIME, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.INSIDE_VEHICLE, ExemptType.NEAR_VEHICLE, ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.STAIRS, ExemptType.SLAB);
        if(lastmotionY - motionY < 0.01)  {
            if(!exempt && motionY != 0 && !data.onLowBlock) fail("Difference de movement impossible", "lmY=" + lastmotionY + " mY=" + motionY);
        }
        if(lastmotionY - motionY > 0.01) removeBuffer();
    }

}
