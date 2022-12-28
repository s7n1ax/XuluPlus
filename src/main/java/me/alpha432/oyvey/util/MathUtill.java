/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.alpha432.oyvey.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtill
implements Util {
    private static final Random random = new Random();

    public static int getRandom(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static double getRandom(double min, double max) {
        return MathHelper.func_151237_a((double)(min + random.nextDouble() * max), (double)min, (double)max);
    }

    public static float getRandom(float min, float max) {
        return MathHelper.func_76131_a((float)(min + random.nextFloat() * max), (float)min, (float)max);
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float sin(float value) {
        return MathHelper.func_76126_a((float)value);
    }

    public static float cos(float value) {
        return MathHelper.func_76134_b((float)value);
    }

    public static float wrapDegrees(float value) {
        return MathHelper.func_76142_g((float)value);
    }

    public static double wrapDegrees(double value) {
        return MathHelper.func_76138_g((double)value);
    }

    public static Vec3d roundVec(Vec3d vec3d, int places) {
        return new Vec3d(MathUtill.round(vec3d.field_72450_a, places), MathUtill.round(vec3d.field_72448_b, places), MathUtill.round(vec3d.field_72449_c, places));
    }

    public static double square(double input) {
        return input * input;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }

    public static float wrap(float valI) {
        float val = valI % 360.0f;
        if (val >= 180.0f) {
            val -= 360.0f;
        }
        if (val < -180.0f) {
            val += 360.0f;
        }
        return val;
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(MathUtill.degToRad(yaw + 90.0f)), 0.0, Math.sin(MathUtill.degToRad(yaw + 90.0f)));
    }

    public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
        double length = Math.sqrt(MathUtill.multiply(x2.field_72450_a - x1.field_72450_a) + MathUtill.multiply(x2.field_72448_b - x1.field_72448_b) + MathUtill.multiply(x2.field_72449_c - x1.field_72449_c));
        double unitSlopeX = (x2.field_72450_a - x1.field_72450_a) / length;
        double unitSlopeY = (x2.field_72448_b - x1.field_72448_b) / length;
        double unitSlopeZ = (x2.field_72449_c - x1.field_72449_c) / length;
        double x = x1.field_72450_a + unitSlopeX * distance;
        double y = x1.field_72448_b + unitSlopeY * distance;
        double z = x1.field_72449_c + unitSlopeZ * distance;
        return new Vec3d(x, y, z);
    }

    public static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.floatValue();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        if (descending) {
            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        } else {
            list.sort(Map.Entry.comparingByValue());
        }
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getTimeOfDay() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(11);
        if (timeOfDay < 12) {
            return "Good Morning ";
        }
        if (timeOfDay < 16) {
            return "Good Afternoon ";
        }
        if (timeOfDay < 21) {
            return "Good Evening ";
        }
        return "Good Night ";
    }

    public static double radToDeg(double rad) {
        return rad * (double)57.29578f;
    }

    public static double degToRad(double deg) {
        return deg * 0.01745329238474369;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static double[] directionSpeed(double speed) {
        float forward = MathUtill.mc.field_71439_g.field_71158_b.field_192832_b;
        float side = MathUtill.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MathUtill.mc.field_71439_g.field_70126_B + (MathUtill.mc.field_71439_g.field_70177_z - MathUtill.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difZ = to.field_72449_c - from.field_72449_c;
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0))};
    }

    public static double multiply(double one) {
        return one * one;
    }

    public static Vec3d extrapolatePlayerPosition(EntityPlayer player, int ticks) {
        Vec3d lastPos = new Vec3d(player.field_70142_S, player.field_70137_T, player.field_70136_U);
        Vec3d currentPos = new Vec3d(player.field_70165_t, player.field_70163_u, player.field_70161_v);
        double distance = MathUtill.multiply(player.field_70159_w) + MathUtill.multiply(player.field_70181_x) + MathUtill.multiply(player.field_70179_y);
        Vec3d tempVec = MathUtill.calculateLine(lastPos, currentPos, distance * (double)ticks);
        return new Vec3d(tempVec.field_72450_a, player.field_70163_u, tempVec.field_72449_c);
    }

    public static List<Vec3d> getBlockBlocks(Entity entity) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        AxisAlignedBB bb = entity.func_174813_aQ();
        double y = entity.field_70163_u;
        double minX = MathUtill.round(bb.field_72340_a, 0);
        double minZ = MathUtill.round(bb.field_72339_c, 0);
        double maxX = MathUtill.round(bb.field_72336_d, 0);
        double maxZ = MathUtill.round(bb.field_72334_f, 0);
        if (minX != maxX) {
            vec3ds.add(new Vec3d(minX, y, minZ));
            vec3ds.add(new Vec3d(maxX, y, minZ));
            if (minZ != maxZ) {
                vec3ds.add(new Vec3d(minX, y, maxZ));
                vec3ds.add(new Vec3d(maxX, y, maxZ));
                return vec3ds;
            }
        } else if (minZ != maxZ) {
            vec3ds.add(new Vec3d(minX, y, minZ));
            vec3ds.add(new Vec3d(minX, y, maxZ));
            return vec3ds;
        }
        vec3ds.add(entity.func_174791_d());
        return vec3ds;
    }

    public static boolean areVec3dsAligned(Vec3d vec3d1, Vec3d vec3d2) {
        return MathUtill.areVec3dsAlignedRetarded(vec3d1, vec3d2);
    }

    public static boolean areVec3dsAlignedRetarded(Vec3d vec3d1, Vec3d vec3d2) {
        BlockPos pos1 = new BlockPos(vec3d1);
        BlockPos pos2 = new BlockPos(vec3d2.field_72450_a, vec3d1.field_72448_b, vec3d2.field_72449_c);
        return pos1.equals((Object)pos2);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difY = (to.field_72448_b - from.field_72448_b) * -1.0;
        double difZ = to.field_72449_c - from.field_72449_c;
        double dist = MathHelper.func_76133_a((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.func_76138_g((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }
}

