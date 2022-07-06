package com.xiii.libertycity.check.checks.movement.advanced;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement A", category = Category.MOVEMENT)
public class MovementA extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.FLYING, ExemptType.SLIME, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.VELOCITY, ExemptType.INSIDE_VEHICLE, ExemptType.NEAR_VEHICLE, ExemptType.CLIMBABLE);
        double diff = Math.abs(data.motionY - data.predymotion);
        debug("dc=" + (diff > 0.001) + " a=" + (data.inAir) + " e=" +  (!exempt) + " d=" + diff);
        if(diff > 0.001 && data.inAir && !exempt) fail("Predictions non suivis", "d=" + diff); else removeBuffer();
    }

}
