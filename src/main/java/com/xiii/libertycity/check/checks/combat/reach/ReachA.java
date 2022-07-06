package com.xiii.libertycity.check.checks.combat.reach;

import com.xiii.libertycity.check.Category;
import com.xiii.libertycity.check.Check;
import com.xiii.libertycity.check.CheckInfo;
import com.xiii.libertycity.utils.CustomLocation;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

@CheckInfo(name = "Reach A", category = Category.COMBAT)
public class ReachA extends Check {

    public void onPacket(PacketPlayReceiveEvent packet) {
        if(packet.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            if(data.target != null) {
                data.lasttargetreach = data.target;
                Vector vecplayer = data.player.getPlayer().getLocation().clone().toVector();
                List<Vector> pastLocation = data.targetpastlocations.getEstimatedLocation(data.ping, 150).stream().map(CustomLocation::toVector).collect(Collectors.toList());

                float reach = (float) pastLocation.stream().mapToDouble(vec -> vec.clone().setY(0).distance(vecplayer.setY(0)) - 0.3f).min().orElse(0);
                if(data.player.getLocation().clone().distance(data.target.getLocation().clone()) > 3.9) {
                    reach -= 0.2;
                }
                reach -= 0.11;
                double maxreach = 2.96;
                double diff = reach - maxreach;
                if (reach > maxreach) {
                    fail("Attaque de trop loin", "reach=" + reach);
                } else {
                    if(diff > 0.1) {
                        removeBuffer();
                    }else {
                        if (buffer >= 0.5) {
                            buffer -= 0.5;
                        }
                    }
                }

            }
        }

    }

}
