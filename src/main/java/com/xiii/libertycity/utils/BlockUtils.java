package com.xiii.libertycity.utils;

import com.xiii.libertycity.data.Data;
import com.xiii.libertycity.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlockUtils {

    public static ArrayList<Block> getBlocksaround = new ArrayList<>();
    public static ArrayList<String> getMaterialsaround = new ArrayList<>();


    public static boolean inLiquid(Player player) {
        if(player != null) {
            try {
                PlayerData data = Data.data.getUserData(player);
                setGetBlocksaround(player);
                if (data != null) {
                    ArrayList<Block> blocks = new ArrayList<>();
                    for (Block b : getBlocksaround) {
                        blocks.add(b.getLocation().clone().add(0, -0.2, 0).getBlock());
                    }
                    if (getBlocksaround.stream().parallel().allMatch(material -> material.toString().contains("WATER") || material.toString().contains("LAVA") || material.toString().contains("BUBBLE")))
                        return true;
                    return blocks.stream().parallel().allMatch(material2 -> material2.toString().contains("WATER") || material2.toString().contains("LAVA") || material2.toString().contains("BUBBLE"));
                }
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }
        return false;
    }

    public static boolean isOnLadder(Player player) {
        if(player != null) {
            try {
                setGetBlocksaround(player);
                return getBlocksaround.stream().parallel().anyMatch(material -> material.toString().contains("VINES") || material.toString().contains("VINE") || material.toString().contains("LADDER"));
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }
        return false;
    }

    public static boolean isonSlab(Player player) {
        if(player != null) {
            try {
                setGetBlocksaround(player);
                if (getBlocksaround.stream().parallel().anyMatch(material3 -> material3.toString().contains("SLAB") || material3.toString().contains("STEP"))) {
                    return true;
                }
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }
        return false;
    }

    public static boolean inWeb(Player player) {
        if(player != null) {
            try {
                boolean foundweb = false;
                boolean foundair = false;
                for (double i = -0.1; i <= 1.99; i += 0.1) {
                    ArrayList<Block> blocks = setGetBlocksaroundY(player, i);
                    double finalI = i;
                    if (blocks.stream().parallel().anyMatch(block -> block.getLocation().clone().add(0, finalI, 0).toString().contains("web"))) {
                        foundweb = true;
                        return true;
                    } else {
                        foundair = true;
                    }
                }
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }
        return false;
    }

    public static boolean onSlime(Player player) {
        if(player != null) {
            try {


                setGetBlocksaround(player);
                PlayerData data = Data.data.getUserData(player);
                ArrayList<Block> blocks = new ArrayList<>();
                for(Block b : getBlocksaround) {
                    blocks.add(b.getLocation().clone().add(0, -1, 0).getBlock());
                }
                if (blocks.stream().parallel().anyMatch(material3 -> material3.toString().contains("SLIME"))) {
                    data.lastslime = System.currentTimeMillis();
                    return true;
                }
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }
        return false;
    }

    public static boolean pistonmove(Player player) {
        if(player != null) {
            try {
                setGetBlocksaround(player);
                PlayerData data = Data.data.getUserData(player);
                ArrayList<Block> blocks = new ArrayList<>();
                ArrayList<Block> blocks2 = new ArrayList<>();
                for(Block b : getBlocksaround) {
                    blocks.add(b.getLocation().clone().add(0, -1, 0).getBlock());
                    blocks2.add(b.getLocation().clone().add(0, -2, 0).getBlock());
                }
                if(blocks.stream().parallel().anyMatch(material -> material.toString().contains("PISTON"))) {
                    data.lastpiston = System.currentTimeMillis();
                    return true;
                }
                if(blocks2.stream().parallel().anyMatch(material2 -> material2.toString().contains("PISTON"))) {
                    data.lastpiston = System.currentTimeMillis();
                    return true;
                }


            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }

        }

        return false;
    }


    public static boolean onIce(Player player) {
        if(player != null) {
            try {


                PlayerData data = Data.data.getUserData(player);
                setGetBlocksaround(player);
                ArrayList<Block> blocks = new ArrayList<>();
                for (Block b : getBlocksaround) {
                    blocks.add(b.getLocation().clone().add(0, -1, 0).getBlock());
                }
                if (blocks.stream().parallel().anyMatch(block -> block.toString().contains("ICE"))) {
                    data.lastice = System.currentTimeMillis();
                    return true;
                }
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }

        return false;
    }

    public static boolean onGroundshit(Player player) {
        if(player != null) {
            try {
                setGetBlocksaround(player);
                if(getBlocksaround.stream().parallel().allMatch(material3 -> material3.toString().contains("AIR"))) return true;
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }

        return false;
    }

    public static boolean isonStair(Player player) {
        if(player != null) {
            try {


                setGetBlocksaround(player);
                ArrayList<Block> blocks = new ArrayList<>();
                for(Block b : getBlocksaround) {
                    blocks.add(b.getLocation().clone().add(0, -0.1, 0).getBlock());
                }
                if (blocks.stream().parallel().anyMatch( material4 -> material4.toString().contains("STAIR"))) return true;
                if(getBlocksaround.stream().parallel().anyMatch(material3 -> material3.toString().contains("STAIR"))) return true;
            }catch(java.util.ConcurrentModificationException | NullPointerException e) {

            }
        }
        return false;
    }


    public static boolean isBlock(Block m, String name) {

        return false;
    }

    public static void setGetBlocksaround(Player player) {
        getBlocksaround.clear();
        getMaterialsaround.clear();
        try {


            for (double x = -0.3; x <= 0.3; x += 0.3) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    String[] s = player.getLocation().clone().add(x, -0.1, z).getBlock().toString().split(",");
                    String[] s3 = player.getLocation().clone().add(x, 0, z).getBlock().toString().split(",");
                    String[] s2 = player.getLocation().clone().add(x, -0.5001, z).getBlock().toString().split(",");

                    if(!getBlocksaround.contains(player.getLocation().clone().add(x, 0, z).getBlock()) && (!getMaterialsaround.contains(s2[3]) || !getMaterialsaround.contains(s3[3]))) {
                        //player.sendMessage("" + s[1] + " " + s[2] + " " + s[3] + " " + s[4] + " " + player.getLocation().clone().add(x, 0, z).getBlock());
                        getBlocksaround.add(player.getLocation().clone().add(x, 0, z).getBlock());
                        getMaterialsaround.add(s[3]);
                    }
                }
            }
        }catch(java.util.ConcurrentModificationException | NullPointerException e) {

        }
    }

    public static ArrayList<Block> setGetBlocksaroundY(Player player, double Y) {
        ArrayList<Block> blocksaround = new ArrayList();
        ArrayList<String> materialsaround = new ArrayList();
        //getBlocksaround.clear();
        //getMaterialsaround.clear();
        try {


            for (double x = -0.3; x <= 0.3; x += 0.3) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    String[] s = new Location(player.getWorld(), x, -0.1 + Y, z).getBlock().toString().split(",");
                    String[] s3 = new Location(player.getWorld(), x, Y, z).getBlock().toString().split(",");
                    String[] s2 = new Location(player.getWorld(), x, -0.5001 + Y, z).getBlock().toString().split(",");

                    if(!blocksaround.contains(new Location(player.getWorld(), x, Y, z).getBlock()) && (!materialsaround.contains(s2[3]) || !materialsaround.contains(s3[3]))) {
                        //player.sendMessage("" + s[1] + " " + s[2] + " " + s[3] + " " + s[4] + " " + player.getLocation().clone().add(x, 0, z).getBlock());
                        blocksaround.add(player.getLocation().clone().add(x, Y, z).getBlock());
                        materialsaround.add(s[3]);

                    }
                }
            }
        }catch(java.util.ConcurrentModificationException | NullPointerException e) {

        }
        return blocksaround;
    }



}
