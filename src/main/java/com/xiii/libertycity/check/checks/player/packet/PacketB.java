package com.xiii.libertycity.check.checks.player.packet;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.exempt.ExemptType;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

import static io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig.PlayerDigType.RELEASE_USE_ITEM;

@CheckInfo(name = "Packet B", category = Category.PLAYER)
public class PacketB extends Check {

    private int lastSlot = -1;
    private int count = 0;
    long placetime;
    private int countH = 0;
    private int streak = 0;
    private WrappedPacketInEntityAction.PlayerAction lastAction;
    private float lastYaw = 0.0f, lastPitch = 0.0f;
    private boolean swung;
    boolean sent;
    private long lastId = -1;

    public void onPacket(PacketPlayReceiveEvent packet) {
        if (packet.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail("Mauvais ordre des packets", "ARM_ANIMATION");
            if (isPost) packet.setCancelled(true);
        }

        if (packet.getPacketId() == PacketType.Play.Client.BLOCK_DIG) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail("Mauvais ordre des packets", "BLOCK_DIG");
            if (isPost) packet.setCancelled(true);
        }

        if (packet.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail("Mauvais ordre des packets", "ENTITY_ACTION");
            if (isPost) packet.setCancelled(true);
        }

        if (packet.getPacketId() == PacketType.Play.Client.CUSTOM_PAYLOAD) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail("Mauvais ordre des packets", "CUSTOM_PAYLOAD");
            if (isPost) packet.setCancelled(true);
        }

        if (packet.getPacketId() == PacketType.Play.Client.HELD_ITEM_SLOT) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail("Mauvais ordre des packets", "HELD_ITEM_SLOT");
            if (isPost) packet.setCancelled(true);
        }

        if (packet.getPacketId() == PacketType.Play.Client.HELD_ITEM_SLOT) {
            WrappedPacketInHeldItemSlot p = new WrappedPacketInHeldItemSlot(packet.getNMSPacket());
            if (p.getCurrentSelectedSlot() == lastSlot) fail("Mauvais ordre des packets", "HELD_ITEM_SLOT_REPEATED");
            lastSlot = p.getCurrentSelectedSlot();
        }

        if (packet.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {
            WrappedPacketInEntityAction p = new WrappedPacketInEntityAction(packet.getNMSPacket());
            final boolean invalid = ++count > 1 && p.getAction() == lastAction;
            if (invalid) fail("Mauvais ordre des packets", "ENTITY_ACTION_REPEATED");
            this.lastAction = p.getAction();
        } else if (packet.getPacketId() == PacketType.Play.Client.FLYING) count = 0;

        final boolean digging = packet.getPacketId() == PacketType.Play.Client.BLOCK_DIG;
        final boolean flying = packet.getPacketId() == PacketType.Play.Client.FLYING;
        if (digging) {
            final WrappedPacketInBlockDig p = new WrappedPacketInBlockDig(packet.getNMSPacket());

            handle:
            {
                if (p.getDigType() != RELEASE_USE_ITEM) break handle;

                final boolean invalid = ++countH > 3;

                if (invalid) fail("Mauvais ordre des packets", "BLOCK_IN_DIG_REPEATED");
            }
        } else if (flying) {
            countH = 0;
        }

        if (packet.getPacketId() == PacketType.Play.Client.FLYING) {
            WrappedPacketInFlying p = new WrappedPacketInFlying(packet.getNMSPacket());
            if (!p.hasRotationChanged()) return;

            final float yaw = p.getYaw();
            final float pitch = p.getPitch();

            final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.TPS, ExemptType.INSIDE_VEHICLE);

            if (yaw == lastYaw && pitch == lastPitch && !exempt) {
                fail("Mauvais ordre des packets", "FLYING");
            }

            this.lastYaw = yaw;
            this.lastPitch = pitch;
        }

        if (packet.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity p = new WrappedPacketInUseEntity(packet.getNMSPacket());
            handle:
            {
                if (p.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) break handle;

                boolean placing = false;
                if (System.currentTimeMillis() - data.lastblockplace < 1) {
                    placing = true;
                }

                if (placing) fail("Mauvais ordre des packets", "USE_ENTITY");
            }
        }

        if (packet.getPacketId() == PacketType.Play.Client.STEER_VEHICLE) {
            boolean exempt = isExempt(ExemptType.INSIDE_VEHICLE);
            if (exempt) {
                fail("Mauvais ordre des packets", "STREE_VEHICULE");
            }
        }

        if (packet.getPacketId() == PacketType.Play.Client.FLYING) {
            WrappedPacketInFlying p = new WrappedPacketInFlying(packet.getNMSPacket());
            if (!p.hasPositionChanged() && packet.getPlayer().getVehicle() == null) {
                // There must be a position update by the client every 20 ticks
                if (++streak > 20) {
                    fail("Mauvais ordre des packets", "FLYING_VEHICULE");
                }
            } else {
                streak = 0;
            }

        } else if (packet.getPacketId() == PacketType.Play.Client.STEER_VEHICLE) {
            streak = 0;
        }

        if (packet.getPacketId() == PacketType.Play.Client.WINDOW_CLICK) {
            boolean isPost = isPost(packet.getPacketId(), (byte) -100);
            if (isPost) fail("Mauvais ordre des packets", "WINDOW_CLICK");
            if (isPost) packet.setCancelled(true);
        }

        //no swing

        if (packet.getPacketId() == PacketType.Play.Client.STEER_VEHICLE) {
            WrappedPacketInSteerVehicle p = new WrappedPacketInSteerVehicle(packet.getNMSPacket());
            final float forward = Math.abs(p.getForwardValue());
            final float side = Math.abs(p.getSideValue());

            // The max forward/side value is .98 or -.98
            final boolean invalid = side > .98F || forward > .98F;

            if (invalid) {
                fail("Mauvais ordre des packets", "STREE_VEHICULE_IMPOSSIBLE");
            }
        }

        if (packet.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
            WrappedPacketInKeepAlive p = new WrappedPacketInKeepAlive(packet.getNMSPacket());
            if (p.getId() == lastId) {
                fail("Mauvais ordre des packets", "KEEP_ALIVE");
            }

            lastId = p.getId();
        }

        if (packet.getPacketId() == PacketType.Play.Client.BLOCK_DIG) {
            WrappedPacketInBlockDig dig = new WrappedPacketInBlockDig(packet.getNMSPacket());
            if (dig.getDigType().toString().contains("START_DESTROY_BLOCK")) {
                if (sent) {
                    fail("Mauvais ordre des packets", "START_DESTROY_BLOCK");
                    sent = true;
                }
            }
            if (dig.getDigType().toString().contains("STOP_DESTROY_BLOCK")) {
                if (!sent) {
                    fail("Mauvais ordre des packets", "STOP_DESTROY_BLOCK");
                    sent = false;
                }
            }
        }
        if (packet.getPacketId() == PacketType.Play.Client.BLOCK_PLACE) {
            placetime = System.currentTimeMillis();
        }
        if (packet.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            long delay = (System.currentTimeMillis() - placetime);
            if (delay > 10 && delay < 50) {
                fail("Mauvais ordre des packets", "BLOCK_PLACE - USE_ENTITY");
           }
       }
    }
}
