package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Movement B", category = Category.MOVEMENT)
public class MovementB extends Check {

    boolean touchedSlime;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.FLYING, ExemptType.SLIME, ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.VELOCITY, ExemptType.SLAB, ExemptType.STAIRS, ExemptType.NEAR_VEHICLE);
        if(isExempt(ExemptType.SLIME)) {
            touchedSlime = true;
        }
        if(motionY > .6 + (data.player.hasPotionEffect(PotionEffectType.JUMP) ? ((data.getPotionEffectAmplifier(PotionEffectType.JUMP) * 0.1) + 1) : 0) && !exempt && !touchedSlime) fail("Téléportation verticale impossible", motionY);
        if(motionY <= 0) {
            touchedSlime = false;
        }
    }

}
