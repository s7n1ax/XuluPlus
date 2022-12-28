/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.potion.Potion
 */
package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class MotionUtil
implements Util {
    public static boolean isMoving(EntityLivingBase entity) {
        return entity.field_191988_bg != 0.0f || entity.field_70702_br != 0.0f;
    }

    public static boolean isMovings() {
        return (double)Minecraft.func_71410_x().field_71439_g.field_191988_bg != 0.0 || (double)Minecraft.func_71410_x().field_71439_g.field_70702_br != 0.0;
    }

    public static void setSpeed(EntityLivingBase entity, double speed) {
        double[] dir = MotionUtil.forward(speed);
        entity.field_70159_w = dir[0];
        entity.field_70179_y = dir[1];
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.func_71410_x().field_71439_g != null && Minecraft.func_71410_x().field_71439_g.func_70644_a(Potion.func_188412_a((int)1))) {
            int amplifier = Minecraft.func_71410_x().field_71439_g.func_70660_b(Potion.func_188412_a((int)1)).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double[] forward(double speed) {
        float forward = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_192832_b;
        float side = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78902_a;
        float yaw = Minecraft.func_71410_x().field_71439_g.field_70126_B + (Minecraft.func_71410_x().field_71439_g.field_70177_z - Minecraft.func_71410_x().field_71439_g.field_70126_B) * Minecraft.func_71410_x().func_184121_ak();
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

    public static double[] getMoveSpeed(double speed) {
        float forward = MotionUtil.mc.field_71439_g.field_71158_b.field_192832_b;
        float strafe = MotionUtil.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MotionUtil.mc.field_71439_g.field_70177_z;
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double motionX = (double)forward * speed * cos + (double)strafe * speed * sin;
        double motionZ = (double)forward * speed * sin - (double)strafe * speed * cos;
        if (!MotionUtil.isMovingMomentum()) {
            motionX = 0.0;
            motionZ = 0.0;
        }
        return new double[]{motionX, motionZ};
    }

    public static boolean isMovingMomentum() {
        return (double)MotionUtil.mc.field_71439_g.field_191988_bg != 0.0 || (double)MotionUtil.mc.field_71439_g.field_70702_br != 0.0;
    }
}

