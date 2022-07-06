package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement M", category = Category.MOVEMENT)
public class MovementM extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.LIQUID, ExemptType.SLIME, ExemptType.BLOCK_ABOVE, ExemptType.VELOCITY, ExemptType.FLYING, ExemptType.TELEPORT, ExemptType.GLIDE);
        if (!exempt) {
            if(!data.inAir) removeBuffer();
            if(System.currentTimeMillis() - data.lasthurt < 1200) maxBuffer = 2;
            if(((motionY - lastmotionY) * 8) > 0) {
                fail("Movement impossible répété", "my=" + ((motionY - lastmotionY) * 8));
            }
            if(((motionY - lastmotionY) * 8) <= 0) {
                removeBuffer();
            }
        } else {
            buffer = 0;
        }
    }
    
}
