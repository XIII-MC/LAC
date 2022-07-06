package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement G", category = Category.MOVEMENT)
public class MovementG extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.TELEPORT, ExemptType.SLIME, ExemptType.LIQUID, ExemptType.WEB, ExemptType.GROUND, ExemptType.JOINED, ExemptType.INSIDE_VEHICLE, ExemptType.FLYING);
        if(!exempt && !data.onLowBlock) {
            if (Math.abs(motionY - data.predymotion) > 1.45 && (System.currentTimeMillis() - data.lasthurt > 1400)) {
                fail("Téléportation verticale impossible", Math.abs(motionY - data.predymotion));
            } else removeBuffer();
        } else removeBuffer();
    }

}
