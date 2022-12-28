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

public class AquaGlShader
extends FramebufferShader {
    private static AquaGlShader INSTANCE;
    public float time = 0.0f;

    public AquaGlShader() {
        super("aquaglow.frag");
    }

    public static FramebufferShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AquaGlShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f((int)this.getUniform("time"), (float)this.time);
        GL20.glUniform2f((int)this.getUniform("resolution"), (float)new ScaledResolution(this.mc).func_78326_a(), (float)new ScaledResolution(this.mc).func_78328_b());
        GL20.glUniform1i((int)this.getUniform("texture"), (int)0);
        GL20.glUniform2f((int)this.getUniform("texelSize"), (float)(Float.intBitsToFloat(Float.floatToIntBits(1531.2186f) ^ 0x7B3F66FF) / (float)this.mc.field_71443_c * (this.radius * this.quality)), (float)(Float.intBitsToFloat(Float.floatToIntBits(103.132805f) ^ 0x7D4E43FF) / (float)this.mc.field_71440_d * (this.radius * this.quality)));
        GL20.glUniform3f((int)this.getUniform("color"), (float)this.red, (float)this.green, (float)this.blue);
        GL20.glUniform1f((int)this.getUniform("divider"), (float)this.divider);
        GL20.glUniform1f((int)this.getUniform("radius"), (float)this.radius);
        GL20.glUniform1f((int)this.getUniform("maxSample"), (float)this.maxSample);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.01 * (double)this.animationSpeed);
    }
}

