package com.xiii.libertycity.check.checks.player.packet;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;

@CheckInfo(name = "Packet C", category = Category.PLAYER)
public class PacketC extends Check {

    long lastms;
    long lastdiff;
    long currentms;
    double bal;
    boolean wasfirst;
    boolean notmoving;
    int tickslower;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == -95 || packet.getPacketId() == -96 || packet.getPacketId() == -94) {
            this.lastms = this.currentms;
            this.currentms = System.currentTimeMillis();
            long diff = this.currentms - this.lastms;
            if (this.notmoving) {
                if (diff < 1100L && diff > 900L)
                    diff = 51L;
                this.tickslower = 0;
            } else {
                if (this.bal < -80.0D)
                    this.bal--;
                if (this.bal < -400.0D) {
                    this.bal -= 2.0D;
                    this.tickslower++;
                    this.buffer = 0.0D;
                    if (diff > 400L)
                        this.tickslower = 0;
                    if (this.tickslower > 30)
                        this.bal = -30.0D;
                } else {
                    this.tickslower = 0;
                }
            }
            if (!this.wasfirst) {
                this.wasfirst = true;
                diff = 100L;
            }
            this.bal += (50L - diff);
            this.lastdiff = diff;
            if (this.bal >= 3.0D) {
                fail("Trop de packets", "bal=" + this.bal);
                this.bal = 0.0D;
            } else {
                removeBuffer();
            }
        }
    }

    public void onPacketSend(PacketPlaySendEvent packet) {
        if (packet.getPacketId() == -13)
            this.bal -= 50.0D;
    }

    public void onMove(PacketPlayReceiveEvent packet, double motionX, double motionY, double motionZ, double lastmotionX, double lastmotionY, double lastmotionZ, float deltaYaw, float deltaPitch, float lastdeltaYaw, float lastdeltaPitch) {
        this.notmoving = (motionX == 0.0D && motionY == 0.0D && motionZ == 0.0D && deltaPitch == 0.0F && deltaYaw == 0.0F);
    }
}

