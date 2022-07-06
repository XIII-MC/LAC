package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Movement E", category = Category.MOVEMENT)
public class MovementE extends Check {

    int invalidA;
    double maxSpeed;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.FLYING, ExemptType.INSIDE_VEHICLE);
        if(data.playerGround) invalidA++;
        if(!data.playerGround) invalidA = 0;
        if(invalidA >= 8) maxSpeed = 0.28804;
        if(invalidA < 8) maxSpeed = 0.62;
        if(data.onLowBlock || isExempt(ExemptType.STAIRS) || isExempt(ExemptType.SLAB)) maxSpeed += 0.2;
        if(isExempt(ExemptType.VELOCITY)) {
            maxSpeed += data.kblevel;
            maxSpeed += 0.45;
        }
        if(System.currentTimeMillis() - data.lastice < 1800) maxSpeed += 0.25;
        if(data.getDeltaXZ() >= maxSpeed && !exempt) fail("Movement de téléportation impossible", "cs=" + data.getDeltaXZ() + " ms=" + maxSpeed);

    }

}
