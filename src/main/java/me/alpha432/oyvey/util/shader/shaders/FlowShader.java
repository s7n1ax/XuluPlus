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

public class FlowShader
extends FramebufferShader {
    public static FlowShader INSTANCE;
    protected float time = 0.0f;

    private FlowShader() {
        super("flow.frag");
    }

    public static FlowShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FlowShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f((int)this.getUniform("resolution"), (float)new ScaledResolution(this.mc).func_78326_a(), (float)new ScaledResolution(this.mc).func_78328_b());
        GL20.glUniform1f((int)this.getUniform("time"), (float)this.time);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.001 * (double)this.animationSpeed);
    }
}

