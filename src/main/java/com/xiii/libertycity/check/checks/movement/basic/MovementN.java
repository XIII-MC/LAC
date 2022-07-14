package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Movement N", category = Category.MOVEMENT)
public class MovementN extends Check {

    double maxSpeed = 1;
    double cSpeed = 0;
    int groundTicks = 0;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.FLYING, ExemptType.TELEPORT);
        cSpeed = data.getDistance(true);

        if(data.playerGround) groundTicks++;
        if(!data.playerGround) groundTicks = 0;

        // BASIC | Ground - Air
        if(groundTicks > 12) {
            maxSpeed = 0.2868198;
        } else maxSpeed = 0.338;

        // BASIC | HitHead
        if(data.blockabove) maxSpeed = 0.5;

        // ICE | Ground - Air
        if(System.currentTimeMillis() - data.lastice <  1200) {
            if (groundTicks > 22) {
                if(System.currentTimeMillis() - data.lastice < 50) maxSpeed = 0.2757;
            } else {
                maxSpeed = 0.48;
                if(data.blockabove) maxSpeed = 0.9;
            }
        }

        // SLIME | Ground - Air
        if(isExempt(ExemptType.SLIME)) {
            if(groundTicks > 14) {
                if(System.currentTimeMillis() - data.lastslime < 50) maxSpeed = 0.09;
            } else {
                maxSpeed = 0.45;
                if(data.blockabove) maxSpeed = 0.74;
            }
        }

        // COBWEB | Ground - Air
        if(isExempt(ExemptType.WEB)) {
            if(groundTicks > 2) {
                maxSpeed = 0.1;
            } else {
                maxSpeed = 0.1004;
                if(data.blockabove) maxSpeed = 0.108;
            }
        }

        // SPEED | Ground - Air
        if(data.player.hasPotionEffect(PotionEffectType.SPEED)) {
            if(groundTicks > 2) {
                if(data.getPotionEffectAmplifier(PotionEffectType.SPEED) == 1) maxSpeed = 0.34362;
                if(data.getPotionEffectAmplifier(PotionEffectType.SPEED) >= 2) maxSpeed = (data.getPotionEffectAmplifier(PotionEffectType.SPEED) * 0.21);
            } else {
                maxSpeed = (data.getPotionEffectAmplifier(PotionEffectType.SPEED) * 0.38);
            }
        }

        // WATER | Ground - Air
        if(data.inLiquid) {
            maxSpeed = 0.21;
            if(data.getDepthStriderLevel() > 0) {
                if(data.getDepthStriderLevel() == 1) maxSpeed = 0.24;
                if(data.getDepthStriderLevel() == 2) maxSpeed = 0.26;
                if(data.getDepthStriderLevel() == 3) maxSpeed = 0.28;
                if(data.getDepthStriderLevel() > 3) maxSpeed = (0.08 * data.getDepthStriderLevel());
            }
        }

        // DAMAGE | Air
        if(isExempt(ExemptType.VELOCITY)) {
            maxSpeed = 0.8;
            if(data.kblevel > 0) {
                maxSpeed = (data.kblevel * 0.95);
            }
        }

        if(cSpeed > maxSpeed  && !exempt) fail("Bouge trop vite", "cs=" + cSpeed + "ms=" + maxSpeed);
        if(cSpeed <= maxSpeed) removeBuffer();
    }

}
