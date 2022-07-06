package com.xiii.libertycity.check.checks.movement.basic;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Movement N", category = Category.MOVEMENT)
public class MovementN extends Check {

    double maxSpeed;
    double cSpeed;
    int groundTicks;

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        boolean exempt = isExempt(ExemptType.FLYING, ExemptType.TELEPORT);

        if(data.playerGround) {
            groundTicks++;
        } else {
            groundTicks = 0;
        }

        if(System.currentTimeMillis() - data.lasthurt < 1200 || System.currentTimeMillis() - data.lasthurtother < 1400) {
            maxSpeed = 0.5;
        }

        if(groundTicks > 8) {
            maxSpeed = 0.2868198;
        } else {
            if(System.currentTimeMillis() - data.lasthurt > 1200 || System.currentTimeMillis() - data.lasthurtother > 1400) {
                maxSpeed = 0.333;
            }
        }
        if(data.blockabove) {
            maxSpeed = 0.5;
        }

        if(System.currentTimeMillis() - data.lastice < 1800) {
            if(groundTicks > 22) {
                if(System.currentTimeMillis() - data.lastice < 50) {
                    maxSpeed = 0.2757;
                }
            } else {
                maxSpeed = 0.58;
                if(data.blockabove) {
                    maxSpeed = 0.9;
                }
            }
        }

        if(System.currentTimeMillis() - data.lastslime < 1800) {
            if(groundTicks > 14) {
                if(System.currentTimeMillis() - data.lastice < 50) {
                    maxSpeed = 0.09;
                }
            } else {
                maxSpeed = 0.45;
                if(data.blockabove) {
                    maxSpeed = 0.74;
                }
            }
        }

        if(data.onLowBlock || isExempt(ExemptType.STAIRS) || isExempt(ExemptType.SLAB)) maxSpeed = 0.61;

        cSpeed = data.getDistance(true);

        if(cSpeed > maxSpeed + ((data.player.hasPotionEffect(PotionEffectType.SPEED) ? ((data.getPotionEffectAmplifier(PotionEffectType.SPEED) * 0.37) + 1) : 0)) && !exempt) fail("Bouge trop vite", "cs=" + cSpeed + "ms=" + maxSpeed);
        if(cSpeed <= maxSpeed) removeBuffer();
    }

}
