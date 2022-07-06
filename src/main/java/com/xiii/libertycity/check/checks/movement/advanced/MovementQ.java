package com.xiii.libertycity.check.checks.movement.advanced;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import org.bukkit.Bukkit;

@CheckInfo(name = "Movement Q", category = Category.MOVEMENT)
public class MovementQ extends Check {

    double predymotion = 999999999;
    double motionYD;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {

        boolean exempt = isExempt(ExemptType.SLIME, ExemptType.SLAB, ExemptType.STAIRS, ExemptType.LIQUID, ExemptType.GLIDE, ExemptType.FLYING, ExemptType.NEAR_VEHICLE, ExemptType.INSIDE_VEHICLE, ExemptType.CLIMBABLE);

        predymotion = (motionYD - 0.08) * 0.9800000190734863;
        motionYD = motionY;
        if(data.inAir && !exempt && (predymotion - motionYD > 0.000000000001)) fail("Predictions non suivis", "lmy=" + motionYD + " py=" + predymotion + " result=" + (predymotion - motionYD));
        if(!data.inAir) buffer = 0;
    }

}
