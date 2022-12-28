/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.SafeRunnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class FramebufferWrapper {
    private final Minecraft mc = Minecraft.func_71410_x();
    private Framebuffer framebuffer;
    protected static int lastScale;
    protected static int lastScaleWidth;
    protected static int lastScaleHeight;
    private boolean hasUpdated;

    public FramebufferWrapper() {
        this.updateFramebuffer();
    }

    public void updateFramebuffer() {
        this.hasUpdated = false;
        if (Display.isActive() || Display.isVisible()) {
            if (this.framebuffer != null) {
                this.framebuffer.func_147614_f();
                ScaledResolution scale = new ScaledResolution(Minecraft.func_71410_x());
                int factor = scale.func_78325_e();
                int factor2 = scale.func_78326_a();
                int factor3 = scale.func_78328_b();
                if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) {
                    this.framebuffer.func_147608_a();
                    this.framebuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
                    this.hasUpdated = true;
                }
                lastScale = factor;
                lastScaleWidth = factor2;
                lastScaleHeight = factor3;
            } else {
                this.framebuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
                this.hasUpdated = true;
            }
        } else if (this.framebuffer == null) {
            this.framebuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
            this.hasUpdated = true;
        }
    }

    public void renderToFramebuffer(SafeRunnable renderOp) {
        GlStateManager.func_179123_a();
        this.framebuffer.func_147610_a(true);
        renderOp.run();
        this.mc.func_147110_a().func_147610_a(true);
        GlStateManager.func_179099_b();
    }

    public void renderFramebuffer(SafeRunnable ... renderOp) {
        GlStateManager.func_179123_a();
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        GL11.glBlendFunc((int)770, (int)771);
        this.mc.func_147110_a().func_147610_a(true);
        for (SafeRunnable runnable : renderOp) {
            runnable.run();
        }
        this.drawFramebuffer(this.framebuffer);
        GlStateManager.func_179084_k();
        GlStateManager.func_179118_c();
        GlStateManager.func_179099_b();
    }

    public void drawFramebuffer(Framebuffer framebuffer) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GL11.glBindTexture((int)3553, (int)framebuffer.field_147617_g);
        GL11.glBegin((int)7);
        GL11.glTexCoord2d((double)0.0, (double)1.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)scaledResolution.func_78328_b());
        GL11.glTexCoord2d((double)1.0, (double)0.0);
        GL11.glVertex2d((double)scaledResolution.func_78326_a(), (double)scaledResolution.func_78328_b());
        GL11.glTexCoord2d((double)1.0, (double)1.0);
        GL11.glVertex2d((double)scaledResolution.func_78326_a(), (double)0.0);
        GL11.glEnd();
    }

    public Framebuffer getFramebuffer() {
        return this.framebuffer;
    }

    public boolean hasUpdated() {
        return this.hasUpdated;
    }
}

