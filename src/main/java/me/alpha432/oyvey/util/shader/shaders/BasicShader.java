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

public class BasicShader
extends FramebufferShader {
    private static BasicShader INSTANCE;
    private float time = 0.0f;
    private float timeMult = 0.1f;
    private int timeLimit = 10000;

    private BasicShader(String fragmentShader) {
        super(fragmentShader);
    }

    private BasicShader(String fragmentShader, float timeMult) {
        super(fragmentShader);
        this.timeMult = timeMult;
    }

    public static FramebufferShader getInstance(String fragmentShader) {
        if (INSTANCE == null || !BasicShader.INSTANCE.fragmentShader.equals(fragmentShader)) {
            INSTANCE = new BasicShader(fragmentShader);
        }
        return INSTANCE;
    }

    public static FramebufferShader getInstance(String fragmentShader, float timeMult) {
        if (INSTANCE == null || !BasicShader.INSTANCE.fragmentShader.equals(fragmentShader)) {
            INSTANCE = new BasicShader(fragmentShader, timeMult);
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f((int)this.getUniform("time"), (float)this.time);
        GL20.glUniform2f((int)this.getUniform("resolution"), (float)new ScaledResolution(this.mc).func_78326_a(), (float)new ScaledResolution(this.mc).func_78328_b());
        if (!this.animation) {
            return;
        }
        this.time = this.time > (float)this.timeLimit ? 0.0f : (this.time += this.timeMult * (float)this.animationSpeed);
    }
}

