package com.xiii.libertycity.check.checks.movement.advanced;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement O", category = Category.MOVEMENT)
public class MovementO extends Check {

    double nf;
    double nf2;
    double nf3;
    double nf4;
    double nf5;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {

        boolean exempt = isExempt(ExemptType.LIQUID, ExemptType.FLYING, ExemptType.INSIDE_VEHICLE, ExemptType.NEAR_VEHICLE, ExemptType.TELEPORT, ExemptType.PLACE, ExemptType.GLIDE, ExemptType.SLIME);

        nf = data.player.getLocation().getY();
        nf3 = (nf - nf2);
        if (nf3 > 0) nf4 = data.player.getLocation().getY();
        if (nf3 < 0) nf5 = data.player.getLocation().getY();

        if (data.inAir && !data.ground2() && !exempt) {
            if ((Math.round((nf4 - nf5))) - (Math.round(data.player.getFallDistance())) != 0 && (Math.round((nf4 - nf5)) > 3))
                fail("Prediction non suivis", "fd=" + Math.round(data.player.getFallDistance()) + " pfd=" + Math.round((nf4 - nf5)));
        }

        if (nf3 == 0) nf4 = data.player.getLocation().getY();
        nf2 = data.player.getLocation().getY();
        if ((Math.round(nf4 - nf5)) - (Math.round(data.player.getFallDistance())) == 0) buffer = 0;

    }
}
