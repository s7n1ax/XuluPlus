/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.alpha432.oyvey.util;

import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class RotationUtil
implements Util {
    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.field_71439_g.field_70165_t, RotationUtil.mc.field_71439_g.field_70163_u + (double)RotationUtil.mc.field_71439_g.func_70047_e(), RotationUtil.mc.field_71439_g.field_70161_v);
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.field_70165_t - px;
        double diry = me.field_70163_u - py;
        double dirz = me.field_70161_v - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - RotationUtil.mc.field_71439_g.field_70177_z)), RotationUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - RotationUtil.mc.field_71439_g.field_70125_A))};
    }

    public static float[] simpleFacing(EnumFacing facing) {
        switch (facing) {
            case DOWN: {
                return new float[]{RotationUtil.mc.field_71439_g.field_70177_z, 90.0f};
            }
            case UP: {
                return new float[]{RotationUtil.mc.field_71439_g.field_70177_z, -90.0f};
            }
            case NORTH: {
                return new float[]{180.0f, 0.0f};
            }
            case SOUTH: {
                return new float[]{0.0f, 0.0f};
            }
            case WEST: {
                return new float[]{90.0f, 0.0f};
            }
        }
        return new float[]{270.0f, 0.0f};
    }

    public static void faceYawAndPitch(float yaw, float pitch) {
        RotationUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(yaw, pitch, RotationUtil.mc.field_71439_g.field_70122_E));
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = RotationUtil.getLegitRotations(vec);
        RotationUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float)MathHelper.func_180184_b((int)((int)rotations[1]), (int)360) : rotations[1], RotationUtil.mc.field_71439_g.field_70122_E));
    }

    public static void faceEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(RotationUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
        RotationUtil.faceYawAndPitch(angle[0], angle[1]);
    }

    public static float[] getAngle(Entity entity) {
        return MathUtil.calcAngle(RotationUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
    }

    public static float transformYaw() {
        float yaw = RotationUtil.mc.field_71439_g.field_70177_z % 360.0f;
        if (RotationUtil.mc.field_71439_g.field_70177_z > 0.0f) {
            if (yaw > 180.0f) {
                yaw = -180.0f + (yaw - 180.0f);
            }
        } else if (yaw < -180.0f) {
            yaw = 180.0f + (yaw + 180.0f);
        }
        if (yaw < 0.0f) {
            return 180.0f + yaw;
        }
        return -180.0f + yaw;
    }

    public static boolean isInFov(BlockPos pos) {
        return pos != null && (RotationUtil.mc.field_71439_g.func_174818_b(pos) < 4.0 || RotationUtil.yawDist(pos) < (double)(RotationUtil.getHalvedfov() + 2.0f));
    }

    public static boolean isInFov(Entity entity) {
        return entity != null && (RotationUtil.mc.field_71439_g.func_70068_e(entity) < 4.0 || RotationUtil.yawDist(entity) < (double)(RotationUtil.getHalvedfov() + 2.0f));
    }

    public static double yawDist(BlockPos pos) {
        if (pos != null) {
            Vec3d difference = new Vec3d((Vec3i)pos).func_178788_d(RotationUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()));
            double d = Math.abs((double)RotationUtil.mc.field_71439_g.field_70177_z - (Math.toDegrees(Math.atan2(difference.field_72449_c, difference.field_72450_a)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static double yawDist(Entity e) {
        if (e != null) {
            Vec3d difference = e.func_174791_d().func_72441_c(0.0, (double)(e.func_70047_e() / 2.0f), 0.0).func_178788_d(RotationUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()));
            double d = Math.abs((double)RotationUtil.mc.field_71439_g.field_70177_z - (Math.toDegrees(Math.atan2(difference.field_72449_c, difference.field_72450_a)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static boolean isInFov(Vec3d vec3d, Vec3d other) {
        if (RotationUtil.mc.field_71439_g.field_70125_A > 30.0f ? other.field_72448_b > RotationUtil.mc.field_71439_g.field_70163_u : RotationUtil.mc.field_71439_g.field_70125_A < -30.0f && other.field_72448_b < RotationUtil.mc.field_71439_g.field_70163_u) {
            return true;
        }
        float angle = MathUtil.calcAngleNoY(vec3d, other)[0] - RotationUtil.transformYaw();
        if (angle < -270.0f) {
            return true;
        }
        float fov = (ClickGui.getInstance().customFov.getValue() != false ? ClickGui.getInstance().fov.getValue().floatValue() : RotationUtil.mc.field_71474_y.field_74334_X) / 2.0f;
        return angle < fov + 10.0f && angle > -fov - 10.0f;
    }

    public static float getFov() {
        return ClickGui.getInstance().customFov.getValue() != false ? ClickGui.getInstance().fov.getValue().floatValue() : RotationUtil.mc.field_71474_y.field_74334_X;
    }

    public static float getHalvedfov() {
        return RotationUtil.getFov() / 2.0f;
    }

    public static int getDirection4D() {
        return MathHelper.func_76128_c((double)((double)(RotationUtil.mc.field_71439_g.field_70177_z * 4.0f / 360.0f) + 0.5)) & 3;
    }

    public static String getDirection4D(boolean northRed) {
        int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0) {
            return "South (+Z)";
        }
        if (dirnumber == 1) {
            return "West (-X)";
        }
        if (dirnumber == 2) {
            return (northRed ? "\u00a7c" : "") + "North (-Z)";
        }
        if (dirnumber == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }
}

