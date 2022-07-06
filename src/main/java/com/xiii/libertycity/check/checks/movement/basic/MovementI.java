package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement I", category = Category.MOVEMENT)
public class MovementI extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.LIQUID, ExemptType.SLAB, ExemptType.STAIRS, ExemptType.SLIME, ExemptType.VELOCITY);
        if(data.player.isSprinting()) {
            if(data.getAngle() > 90 && !data.inAir && !exempt && !data.onLowBlock) {
                fail("Court invalidement","angle=" + data.getAngle());
            } else {
                removeBuffer();
            }

        } else {
            removeBuffer();
        }


    }

}
