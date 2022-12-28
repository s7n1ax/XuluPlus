/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.Wrapper;
import net.minecraft.client.Minecraft;

public interface Util {
    public static final Minecraft mc = Minecraft.func_71410_x();

    public static double[] directionSpeed(double speed) {
        float forward = Wrapper.INSTANCE.mc().field_71439_g.field_71158_b.field_192832_b;
        float side = Wrapper.INSTANCE.mc().field_71439_g.field_71158_b.field_78902_a;
        float yaw = Wrapper.INSTANCE.mc().field_71439_g.field_70126_B + (Wrapper.INSTANCE.mc().field_71439_g.field_70177_z - Wrapper.INSTANCE.mc().field_71439_g.field_70126_B) * Wrapper.INSTANCE.mc().func_184121_ak();
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
}

