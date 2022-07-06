package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement D", category = Category.MOVEMENT)
public class MovementD extends Check {

    double speed;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.FLYING, ExemptType.INSIDE_VEHICLE, ExemptType.WEB, ExemptType.CLIMBABLE);
        speed = Math.sqrt(Math.pow(Math.abs(motionX), 2) + Math.pow(Math.abs(motionZ), 2));
        speed = Math.round(speed * 10000000);
        if(!exempt && String.valueOf(speed).contains("000")) fail("Movement trop arrondis", "cs=" + speed);
        if(!String.valueOf(speed).contains("000")) removeBuffer();
    }

}
