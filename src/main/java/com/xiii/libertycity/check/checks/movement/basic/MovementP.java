package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import org.bukkit.Bukkit;

@CheckInfo(name = "Movement P", category = Category.MOVEMENT)
public class MovementP extends Check {

    int airTicks;
    int maxTicks = 6;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {

        boolean exempt = isExempt(ExemptType.SLIME, ExemptType.SLAB, ExemptType.STAIRS, ExemptType.LIQUID, ExemptType.GLIDE, ExemptType.FLYING, ExemptType.NEAR_VEHICLE, ExemptType.INSIDE_VEHICLE, ExemptType.CLIMBABLE, ExemptType.PLACE);

        if(!data.playerGround && motionY > 0.05 && !exempt) airTicks++;
        if(motionY <= 0 && !data.inAir) airTicks = 0;

        if(isExempt(ExemptType.VELOCITY)) maxTicks = 9;
        if(!isExempt(ExemptType.VELOCITY)) maxTicks = 6;
        if(data.onLowBlock || exempt) airTicks = 0;

        if(airTicks >= maxTicks && !exempt && !data.onLowBlock) fail("Reste trop longtemps en l'air", "airTicks= " + airTicks + " maxTicks= " + maxTicks);


    }
}
