package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement K", category = Category.MOVEMENT)
public class MovementK extends Check {

    int airticks;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.TELEPORT, ExemptType.BLOCK_ABOVE, ExemptType.LIQUID, ExemptType.CLIMBABLE, ExemptType.FLYING, ExemptType.GLIDE, ExemptType.PLACE, ExemptType.NEAR_VEHICLE, ExemptType.INSIDE_VEHICLE, ExemptType.FLYING, ExemptType.PISTON, ExemptType.STAIRS, ExemptType.SLAB);
        if(!exempt) {
            if (motionY > 0) {
                if (airticks < 4) {
                    airticks++;
                }
            } else {
                if (motionY == 0 && !data.inAir) {
                    if (airticks < 4 && airticks != 0 && airticks > 0) {
                        if(!data.onLowBlock || !isExempt(ExemptType.STAIRS) || !isExempt(ExemptType.SLAB)) fail("Missing air ticks", airticks); // 1
                    } else {
                        if (airticks != 0 && airticks > 0)
                            buffer = 0;
                    }
                    airticks = 0;
                }
            }
        }
        if(exempt || data.onLowBlock) {
            airticks -= 2;
            buffer = 0;
        }
    }

}
