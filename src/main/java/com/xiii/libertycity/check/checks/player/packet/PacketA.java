package com.xiii.libertycity.check.checks.player.packet;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.utils.SampleList;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;

@CheckInfo(name = "Packet A", category = Category.PLAYER)
public class PacketA extends Check {

    int pCount;
    int pCount2;
    int pCount3;
    double lastWindowTime;
    double lastUseEntityTime;
    int packetD;
    double packetB;
    double packetC;
    double lastUse;

    long lastMs = System.currentTimeMillis();
    long lastPosition = System.currentTimeMillis();
    double bal;
    double lastBal;
    SampleList<Double> balances = new SampleList(4);

    public void onPacket(PacketPlayReceiveEvent packet) {

        if (packet.getPacketId() == -93) {
            this.pCount2++;
        } else {
            this.pCount2 = 0;
        }
        if (this.pCount2 > 42)
            fail("Spam de packets", "pCount2=" + this.pCount2);
        if (this.pCount2 > 42)
            packet.setCancelled(true);


        if (packet.getPacketId() == -105)
            this.lastWindowTime = System.currentTimeMillis();
        if (packet.getPacketId() == -100)
            this.lastUseEntityTime = System.currentTimeMillis();
        double delay = this.lastUseEntityTime - this.lastWindowTime;
        if (packet.getPacketId() == -100 &&
                delay < 50.0D)
            fail("Clique son inventaire trop vite", "delay=" + delay);


        if (this.data.eatDelay < 1300.0D) {
            fail("A manger trop vite", "delay=" + this.data.eatDelay);
            this.data.eatDelay = 1300.0D;
        }
        if (this.data.lastShootDelay < 99.0D) {
            fail("Tire trop vite", "delay=" + this.data.lastShootDelay);
            this.data.lastShootDelay = 99.0D;
        }
        if (this.data.shootDelay < 299.0D) {
            fail("Tire trop vite", "delay=" + this.data.shootDelay);
            this.data.shootDelay = 299.0D;
        }


        if (packet.getPacketId() == -68) {
            if (this.lastUse - System.currentTimeMillis() > -50.0D && this.lastUse - System.currentTimeMillis() < -1.0D)
                packetD += 1;
                if(packetD > 3) fail("Utilise un item trop vite", "delay=" + (this.lastUse - System.currentTimeMillis()));
            if (packetD > 3)
                packet.setCancelled(true);
            if (this.lastUse - System.currentTimeMillis() < -50.0D)
                if(packetD >= 1) packetD -= 1;
            this.lastUse = System.currentTimeMillis();
        }


        if (packet.getPacketId() == -96 || packet.getPacketId() == -94 || packet.getPacketId() == -95) {
            this.pCount3++;
            if (this.pCount3 > 0)
                packetB = 0.0;
            if (this.data.getDistance(false) <= 0.1D)
                packetB = 0.0;
        } else if (packet.getPacketId() == -93) {
            this.pCount3 = -1;
        }
        if (this.pCount3 <= 0 && this.data.getDistance(false) > 0.1D)
            packetB += 0.5;
            if(packetB > 40) fail("Spam de packets", "pCount3=" + this.pCount3);



        if (packet.getPacketId() == -93) {
            long diff = System.currentTimeMillis() - this.lastMs;
            long diff2 = System.currentTimeMillis() - this.lastPosition;
            this.lastBal = this.bal;
            this.bal += (50L - diff);
            if (this.bal >= 5.0D && diff2 < 400L) {
                this.balances.add(Double.valueOf(this.bal));
                if (this.balances.isCollected())
                    if (this.balances.getAverageDouble(this.balances) >= 50.0D) {
                        packetC += 1;
                        if(packetC > 2) fail("Spam de packets", "bal=" + this.bal);
                    } else {
                        if(packetC >= 0.1) packetC -= 0.1;
                    }
                this.bal = 0.0D;
            }
            this.lastMs = System.currentTimeMillis();
        } else if (packet.getPacketId() == -96 || packet.getPacketId() == -95 || packet.getPacketId() == -94) {
            if (this.bal < -100.0D)
                this.bal = -20.0D;
            this.lastPosition = System.currentTimeMillis();
        }

    }
}
