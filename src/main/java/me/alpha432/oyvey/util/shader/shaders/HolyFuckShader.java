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

public class HolyFuckShader
extends FramebufferShader {
    private static HolyFuckShader INSTANCE;
    protected float time = 0.0f;

    private HolyFuckShader() {
        super("holyfuck.frag");
    }

    public static HolyFuckShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HolyFuckShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
        this.setupUniform("speed");
        this.setupUniform("shift");
        this.setupUniform("color");
        this.setupUniform("radius");
        this.setupUniform("quality");
        this.setupUniform("divider");
        this.setupUniform("texelSize");
        this.setupUniform("maxSample");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f((int)this.getUniform("time"), (float)this.time);
        GL20.glUniform2f((int)this.getUniform("resolution"), (float)new ScaledResolution(this.mc).func_78326_a(), (float)new ScaledResolution(this.mc).func_78328_b());
        GL20.glUniform3f((int)this.getUniform("color"), (float)this.red, (float)this.green, (float)this.blue);
        GL20.glUniform1f((int)this.getUniform("radius"), (float)this.radius);
        GL20.glUniform1f((int)this.getUniform("quality"), (float)this.quality);
        GL20.glUniform1f((int)this.getUniform("divider"), (float)this.divider);
        GL20.glUniform2f((int)this.getUniform("speed"), (float)this.animationSpeed, (float)this.animationSpeed);
        GL20.glUniform1f((int)this.getUniform("shift"), (float)1.0f);
        GL20.glUniform1f((int)this.getUniform("maxSample"), (float)this.maxSample);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (this.time += 0.01f * (float)this.animationSpeed);
    }
}

