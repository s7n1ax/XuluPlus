/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL20
 */
package me.alpha432.oyvey.util.shader.shaders;

import me.alpha432.oyvey.util.shader.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class SmokeShader
extends FramebufferShader {
    private static SmokeShader INSTANCE;
    protected float time = 0.0f;

    private SmokeShader() {
        super("smoke.frag");
    }

    public static SmokeShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SmokeShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
        this.setupUniform("radius");
        this.setupUniform("divider");
        this.setupUniform("maxSample");
        this.setupUniform("texelSize");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f((int)this.getUniform("time"), (float)this.time);
        GL20.glUniform2f((int)this.getUniform("resolution"), (float)((float)new ScaledResolution(this.mc).func_78326_a() / 2.0f), (float)((float)new ScaledResolution(this.mc).func_78328_b() / 2.0f));
        GL20.glUniform1f((int)this.getUniform("radius"), (float)this.radius);
        GL20.glUniform1f((int)this.getUniform("divider"), (float)this.divider);
        GL20.glUniform1f((int)this.getUniform("maxSample"), (float)this.maxSample);
        GL20.glUniform2f((int)this.getUniform("texelSize"), (float)(1.0f / (float)this.mc.field_71443_c * (this.radius * this.quality)), (float)(1.0f / (float)this.mc.field_71440_d * (this.radius * this.quality)));
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.05 * (double)this.animationSpeed);
    }
}

