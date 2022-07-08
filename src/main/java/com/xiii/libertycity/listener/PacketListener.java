package com.xiii.libertycity.listener;

import com.xiii.libertycity.LAC;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.data.Data;
import com.xiii.libertycity.data.PlayerData;
import com.xiii.libertycity.utils.BoundingBox;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.function.Predicate;

public class PacketListener extends PacketListenerAbstract {
    private final List<Block> blocks = new ArrayList<>();
    private final ArrayDeque<Vector> teleports = new ArrayDeque<>();
    private int tpBandaidFixTicks;
    private int ticks;
    public PacketListener() {
        super(PacketListenerPriority.HIGH);
    }


    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player p = event.getPlayer();
        Data.data.registerUser(p);
        PlayerData data = Data.data.getUserData(p);
        NMSPacket packet = event.getNMSPacket();

        // Bukkit.broadcastMessage("s");
        if(data != null) {
            if (data.join++ > 50) {
                if (event.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
                    data.ping = (int) (System.currentTimeMillis() - data.serverkeepalive);
                }
                if (p.isDead()) {
                    data.isDead = true;
                    data.wasDead = System.currentTimeMillis();
                } else {
                    if (data.isDead) {
                        data.wasDead = System.currentTimeMillis();
                    }
                    data.isDead = false;
                }
                data.lasthealth = p.getHealth();
                data.lastFalldistance = p.getFallDistance();
                if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
                    WrappedPacketInUseEntity ue = new WrappedPacketInUseEntity(packet);
                    data.target = ue.getEntity();
                    data.useAction = ue.getAction();
                }
                for (Check c : data.checks) {
                    c.data = data;
                    if (c.data != null && data != null) {
                        c.onPacket(event);
                    }
                }

                if (event.getPacketId() == PacketType.Play.Client.BLOCK_DIG) {
                    WrappedPacketInBlockDig dig = new WrappedPacketInBlockDig(packet);
                    if (dig.getDigType().toString().contains("START_DESTROY_BLOCK")) {
                        data.sentstartdestroy = true;
                    }
                    if (dig.getDigType().toString().contains("ABORT_DESTROY_BLOCK")) {
                        data.sentstartdestroy = false;
                    }
                    if (dig.getDigType().toString().contains("STOP_DESTROY_BLOCK")) {
                        if (data.sentstartdestroy) {
                            if (dig.getDirection().toString().contains("UP")) {
                                data.brokeblock = System.currentTimeMillis();
                            }
                        }
                        data.sentstartdestroy = false;
                    }
                }
                if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK) {
                    WrappedPacketInFlying ps = new WrappedPacketInFlying(packet);
                    Location from = new Location(p.getWorld(), ps.getPosition().getX(), ps.getPosition().getY(), ps.getPosition().getZ());
                    if (ps.isRotating()) {
                        from.setYaw(ps.getYaw());
                        from.setPitch(ps.getPitch());
                    } else {
                        from.setYaw(p.getLocation().getYaw());
                        from.setPitch(p.getLocation().getPitch());
                    }
                    if (teleports.size() > 0) {
                        tpBandaidFixTicks = 2;
                        data.isTeleporting = true;
                    }

                    if (teleports.size() == 0) {
                        if (--tpBandaidFixTicks < 0) {
                            data.isTeleporting = false;
                        }
                    }
                    if(data.isTeleporting) {
                        ticks++;
                    } else {
                        ticks = 0;
                    }
                    if(data.onSlime) {
                        data.sinceSlimeTicks = 0;
                    } else {
                        data.sinceSlimeTicks++;
                    }
                    data.lastplayerGround = data.playerGround;
                    data.playerGround = ps.isOnGround();
                    data.from = data.to;
                    data.to = from;
                    data.sfrom = data.sto;
                    data.sto = from;
                    data.domove();
                    handleBlocks(data);
                    if(data.player.isFlying()) {
                        data.lastflyingtime = System.currentTimeMillis();
                    }
                    if (teleports.size() > 150) {
                        teleports.remove(0);
                    }
                    if(ticks > 50) {
                        teleports.clear();
                    }
                    for (Vector vector : teleports) {
                        final double dx = Math.abs(from.getX() - vector.getX());
                        final double dy = Math.abs(from.getY() - vector.getY());
                        final double dz = Math.abs(from.getZ() - vector.getZ());

                        if (dx == 0 && dy == 0 && dz == 0) {
                            teleports.remove(vector);
                        }
                    }
                    for (Check c : data.checks) {
                        if (data != null) {
                            c.data = data;
                            if (c.data != null) {
                                c.onMove(event, data.motionX, data.motionY, data.motionZ, data.lastmotionX, data.lastmotionY, data.lastmotionZ, data.deltaYaw, data.deltaPitch, data.lastdeltaYaw, data.lastdeltaPitch);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        Player p = event.getPlayer();
        if(p != null) {
            Data.data.registerUser(p);
            PlayerData data = Data.data.getUserData(p);
            if(data != null) {
                if(event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
                    data.serverkeepalive = System.currentTimeMillis();
                }
                if(event.getPacketId() == PacketType.Play.Server.POSITION) {
                    WrappedPacketOutPosition ps = new WrappedPacketOutPosition(event.getNMSPacket());
                    final Vector teleportVector = new Vector(
                            ps.getPosition().getX(),
                            ps.getPosition().getY(),
                            ps.getPosition().getZ()
                    );

                    teleports.add(teleportVector);
                }
                if(event.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
                    WrappedPacketOutEntityVelocity velo = new WrappedPacketOutEntityVelocity(event.getNMSPacket());
                    if(velo.getEntityId() == p.getEntityId())
                        data.lastvelocity = System.currentTimeMillis();
                }
                for (Check c : data.checks) {
                    if(data != null) {
                        c.data = data;
                        if(c.data != null) {
                            c.onPacketSend(event);
                        }
                    }
                }

            }
        }
    }

    private void handleBlocks(PlayerData data) {
        blocks.clear();
        final BoundingBox boundingBox = new BoundingBox(data.getPlayer())
                .expandSpecific(0, 0, 0.55, 0.6, 0, 0);

        final double minX = boundingBox.getMinX();
        final double minY = boundingBox.getMinY();
        final double minZ = boundingBox.getMinZ();
        final double maxX = boundingBox.getMaxX();
        final double maxY = boundingBox.getMaxY();
        final double maxZ = boundingBox.getMaxZ();

        for (double x = minX; x <= maxX; x += (maxX - minX)) {
            for (double y = minY; y <= maxY + 0.01; y += (maxY - minY) / 5) { //Expand max by 0.01 to compensate shortly for precision issues due to FP.
                for (double z = minZ; z <= maxZ; z += (maxZ - minZ)) {
                    final Location location = new Location(data.getPlayer().getWorld(), x, y, z);
                    final Block block = this.getBlock(location);
                    blocks.add(block);
                }
            }
        }
        List<Block> b = blocks;
        data.isInLiquid = b.stream().anyMatch(Block::isLiquid);
        data.inLiquid = b.stream().allMatch(Block::isLiquid);
        data.inweb = b.stream().anyMatch(block -> block.getType().toString().contains("WEB"));
        data.inAir = b.stream().allMatch(block -> block.getType() == Material.AIR);
        data.onIce = b.stream().anyMatch(block -> block.getType().toString().contains("ICE"));
        if(data.onIce) {
            data.lastice = System.currentTimeMillis();
        }
        data.onSolidGround = b.stream().anyMatch(block -> block.getType().isSolid());
        data.isonSlab = b.stream().anyMatch(block -> block.getType().toString().contains("STEP") || block.getType().toString().contains("SLAB"));        data.isonStair = b.stream().anyMatch(block -> block.getType().toString().contains("STAIR"));
        data.isonStair = b.stream().anyMatch(block -> block.getType().toString().contains("STAIR"));
        data.nearTrapdoor = this.isCollidingAtLocation(data,1.801, material -> material.toString().contains("TRAP_DOOR"));
        data.blockabove = b.stream().filter(block -> block.getLocation().getY() - data.to.getY() > 1.5)
                .anyMatch(block -> block.getType() != Material.AIR) || data.nearTrapdoor;
        data.onSlime = b.stream().anyMatch(block -> block.getType().toString().contains("SLIME"));
        for(int i = 256; i > 0; i--) {
            Location loc = data.to.clone();
            loc.setY(loc.getY() - i);
            if(getBlock(loc).getType() == Material.SLIME_BLOCK) {
                data.onSlime2 = true;
            }
        }
        data.nearPiston = b.stream().anyMatch(block -> block.getType().toString().contains("PISTON"));
        data.onLowBlock = b.stream().anyMatch(block -> block.getType().toString().contains("TRAP_DOOR") || block.getType().toString().contains("BED") || block.getType().toString().contains("CARPET") || block.getType().toString().contains("REPEATER") || block.getType().toString().contains("COMPARATOR") || block.getType().toString().contains("SLAB") || block.getType().toString().contains("SNOW") || block.getType().toString().contains("CAULDRON") || block.getType().toString().contains("BREWING") || block.getType().toString().contains("HOPPER") || block.getType().toString().contains("DETECTOR") || block.getType().toString().contains("ENCHANTING") || block.getType().toString().contains("END_PORTAL") || block.getType().toString().contains("POT") || block.getType().toString().contains("SOUL_SAND") || block.getType().toString().contains("STAIRS") || block.getType().toString().contains("SLAB") || block.getType().toString().contains("STAIR") || block.getType().toString().contains("STEP")  || block.getType().toString().contains("BED")  || block.getType().toString().contains("HEAD")  || block.getType().toString().contains("FENCE")  || block.getType().toString().contains("WALL")  || block.getType().toString().contains("PISTON")  || block.getType().toString().contains("SLIME"));
        if(data.onSlime) {
            data.sinceSlimeTicks = 0;
        } else {
            data.sinceSlimeTicks++;
        }
        final Location location = data.getPlayer().getLocation();
        final int var1 = NumberConversions.floor(location.getX());
        final int var2 = NumberConversions.floor(location.getY());
        final int var3 = NumberConversions.floor(location.getZ());
        final Block var4 = this.getBlock(new Location(location.getWorld(), var1, var2, var3));
        data.onClimbable = var4.getType() == Material.LADDER || var4.getType() == Material.VINE;
    }

    public boolean isCollidingAtLocation(PlayerData data,double drop, Predicate<Material> predicate) {
        final ArrayList<Material> materials = new ArrayList<>();

        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double z = -0.3; z <= 0.3; z+= 0.3) {
                final Material material = getBlock(data.getPlayer().getLocation().clone().add(x, drop, z)).getType();
                if (material != null) {
                    materials.add(material);
                }
            }
        }

        return materials.stream().allMatch(predicate);
    }

    //Taken from Fiona
    public Block getBlock(final Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getBlock();
        } else {
            FutureTask<Block> futureTask = new FutureTask<>(() -> {
                location.getWorld().loadChunk(location.getBlockX() >> 4, location.getBlockZ() >> 4);
                return location.getBlock();
            });
            Bukkit.getScheduler().runTask(LAC.instance, futureTask);
            try {
                return futureTask.get();
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
            return null;
        }
    }

}

