package com.xiii.libertycity.check.checks.movement.advanced;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Movement S", category = Category.MOVEMENT)
public class MovementS extends Check {

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {

        boolean step = mathOnGround(motionY) && mathOnGround(data.from.getY());
        boolean jumped = motionY > 0 && data.from.getY() % (1D/64) == 0 && !data.playerGround && !step;
        double expectedJumpMotion = 0.42F + (double)(data.player.hasPotionEffect(PotionEffectType.JUMP) ? (data.player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F : 0);
        boolean exempt = isExempt(ExemptType.INSIDE_VEHICLE, ExemptType.VELOCITY, ExemptType.CLIMBABLE, ExemptType.FLYING, ExemptType.SLIME, ExemptType.BLOCK_ABOVE, ExemptType.PISTON, ExemptType.LIQUID, ExemptType.NEAR_VEHICLE, ExemptType.TELEPORT, ExemptType.WEB, ExemptType.TRAPDOOR);

        if (jumped && !exempt) {
            if (motionY < expectedJumpMotion) {
                fail("Saut trop bas", "my=" + motionY);
            }
        }
    }

}
