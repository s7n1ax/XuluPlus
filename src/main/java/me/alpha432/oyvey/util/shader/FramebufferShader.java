/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.shader.Framebuffer
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package me.alpha432.oyvey.util.shader;

import me.alpha432.oyvey.util.OyColor;
import me.alpha432.oyvey.util.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class FramebufferShader
extends Shader {
    public Minecraft mc = Minecraft.func_71410_x();
    private static Framebuffer framebuffer;
    protected static int lastScale;
    protected static int lastScaleWidth;
    protected static int lastScaleHeight;
    protected float red;
    protected float green;
    protected float blue;
    protected float alpha = 1.0f;
    protected float radius = 2.0f;
    protected float quality = 1.0f;
    protected boolean animation = true;
    protected int animationSpeed = 1;
    protected float divider = 1.0f;
    protected float maxSample = 1.0f;
    private boolean entityShadows;

    public FramebufferShader(String fragmentShader) {
        super(fragmentShader);
    }

    public void setShaderParams(Boolean animation, int animationSpeed, OyColor color) {
        this.animation = animation;
        this.animationSpeed = animationSpeed;
        this.red = color.getRedNorm();
        this.green = color.getGreenNorm();
        this.blue = color.getBlueNorm();
        this.alpha = color.getBlueNorm();
    }

    public void setShaderParams(Boolean animation, int animationSpeed, OyColor color, float radius) {
        this.setShaderParams(animation, animationSpeed, color);
        this.radius = radius;
    }

    public void setShaderParams(Boolean animation, int animationSpeed, OyColor color, float radius, float divider, float maxSample) {
        this.setShaderParams(animation, animationSpeed, color, radius);
        this.divider = divider;
        this.maxSample = maxSample;
    }

    public void startDraw(float partialTicks) {
        GlStateManager.func_179141_d();
        GlStateManager.func_179094_E();
        framebuffer = this.setupFrameBuffer(framebuffer);
        framebuffer.func_147610_a(true);
        this.entityShadows = this.mc.field_71474_y.field_181151_V;
        this.mc.field_71474_y.field_181151_V = false;
    }

    public void stopDraw() {
        this.mc.field_71474_y.field_181151_V = this.entityShadows;
        GlStateManager.func_179147_l();
        GL11.glBlendFunc((int)770, (int)771);
        this.mc.func_147110_a().func_147610_a(true);
        this.mc.field_71460_t.func_175072_h();
        RenderHelper.func_74518_a();
        this.startShader();
        this.mc.field_71460_t.func_78478_c();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        this.mc.field_71460_t.func_175072_h();
        GlStateManager.func_179121_F();
    }

    public void stopDraw(OyColor color, float radius, float quality, Runnable ... shaderOps) {
        this.mc.field_71474_y.field_181151_V = this.entityShadows;
        GlStateManager.func_179147_l();
        GL11.glBlendFunc((int)770, (int)771);
        this.mc.func_147110_a().func_147610_a(true);
        this.red = (float)color.getRed() / 255.0f;
        this.green = (float)color.getGreen() / 255.0f;
        this.blue = (float)color.getBlue() / 255.0f;
        this.alpha = (float)color.getAlpha() / 255.0f;
        this.radius = radius;
        this.quality = quality;
        this.mc.field_71460_t.func_175072_h();
        RenderHelper.func_74518_a();
        this.startShader();
        this.mc.field_71460_t.func_78478_c();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        this.mc.field_71460_t.func_175072_h();
        GlStateManager.func_179121_F();
        GlStateManager.func_179099_b();
    }

    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (Display.isActive() || Display.isVisible()) {
            if (frameBuffer != null) {
                frameBuffer.func_147614_f();
                ScaledResolution scale = new ScaledResolution(this.mc);
                int factor = scale.func_78325_e();
                int factor2 = scale.func_78326_a();
                int factor3 = scale.func_78328_b();
                if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) {
                    frameBuffer.func_147608_a();
                    frameBuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
                    frameBuffer.func_147614_f();
                }
                lastScale = factor;
                lastScaleWidth = factor2;
                lastScaleHeight = factor3;
            } else {
                frameBuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
            }
        } else if (frameBuffer == null) {
            frameBuffer = new Framebuffer(this.mc.field_71443_c, this.mc.field_71440_d, true);
        }
        return frameBuffer;
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
        GL20.glUseProgram((int)0);
    }
}

