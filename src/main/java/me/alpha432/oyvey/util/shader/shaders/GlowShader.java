/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL20
 */
package me.alpha432.oyvey.util.shader.shaders;

import me.alpha432.oyvey.util.shader.FramebufferShader;
import org.lwjgl.opengl.GL20;

public class GlowShader
extends FramebufferShader {
    private static GlowShader INSTANCE;

    private GlowShader() {
        super("glow.frag");
    }

    public static GlowShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GlowShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1i((int)this.getUniform("texture"), (int)0);
        GL20.glUniform2f((int)this.getUniform("texelSize"), (float)(Float.intBitsToFloat(Float.floatToIntBits(1531.2186f) ^ 0x7B3F66FF) / (float)this.mc.field_71443_c * (this.radius * this.quality)), (float)(Float.intBitsToFloat(Float.floatToIntBits(103.132805f) ^ 0x7D4E43FF) / (float)this.mc.field_71440_d * (this.radius * this.quality)));
        GL20.glUniform3f((int)this.getUniform("color"), (float)this.red, (float)this.green, (float)this.blue);
        GL20.glUniform1f((int)this.getUniform("divider"), (float)Float.intBitsToFloat(Float.floatToIntBits(0.060076397f) ^ 0x7E7A12AB));
        GL20.glUniform1f((int)this.getUniform("radius"), (float)this.radius);
        GL20.glUniform1f((int)this.getUniform("maxSample"), (float)Float.intBitsToFloat(Float.floatToIntBits(0.08735179f) ^ 0x7C92E57F));
    }
}

