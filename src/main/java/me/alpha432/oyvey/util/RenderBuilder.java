/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.util;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderBuilder {
    public static void glSetup() {
        RenderBuilder.glSetup(1.5f);
    }

    public static void glSetup(float lineWidth) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)lineWidth);
    }

    public static void glRelease() {
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void glPrepare() {
        GlStateManager.func_179129_p();
        GlStateManager.func_179118_c();
        GlStateManager.func_179103_j((int)7425);
    }

    public static void glRestore() {
        GlStateManager.func_179089_o();
        GlStateManager.func_179141_d();
        GlStateManager.func_179103_j((int)7424);
    }

    public static enum RenderMode {
        Fill,
        Outline,
        Both,
        Glow;

    }
}

