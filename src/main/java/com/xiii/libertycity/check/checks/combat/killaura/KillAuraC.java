package com.xiii.libertycity.check.checks.combat.killaura;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.utils.MathUtils;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

@CheckInfo(name = "KillAura", category = Category.COMBAT)
public class KillAuraC extends Check {

    private double lastPosX = 0.0d, lastPosZ = 0.0d, lastHorizontalDistance = 0.0d;
    private float lastYaw = 0L, lastPitch = 0L;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.FLYING) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getNMSPacket());

            if (!wrapper.hasRotationChanged() || !wrapper.hasPositionChanged()) return;

            final double posX = wrapper.getX();
            final double posZ = wrapper.getZ();

            final float yaw = wrapper.getYaw();
            final float pitch = wrapper.getPitch();

            final double horizontalDistance = MathUtils.magnitude(posX - lastPosX, posZ - lastPosZ);

            // Player moved
            if (horizontalDistance > 0.0) {
                final float deltaYaw = Math.abs(yaw - lastYaw);
                final float deltaPitch = Math.abs(pitch - lastPitch);

                boolean attacking = false;
                if (System.currentTimeMillis() - data.lastAttack < 200) {
                    attacking = true;
                }
                final double acceleration = Math.abs(horizontalDistance - lastHorizontalDistance);

                // Player made a large head rotation and didn't accelerate / decelerate which is impossible
                if (acceleration < 1e-02 && deltaYaw > 30.f && deltaPitch > 15.f && attacking) {
                    fail("Rotations robotique", "acl=" + acceleration + " dY=" + deltaYaw + " dP=" + deltaPitch);
                }
            }

            this.lastHorizontalDistance = horizontalDistance;
            this.lastYaw = yaw;
            this.lastPitch = pitch;
            this.lastPosX = posX;
            this.lastPosZ = posZ;
        }
    }

}
