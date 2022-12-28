/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.util.shader;

import me.alpha432.oyvey.util.shader.FramebufferShader;

@FunctionalInterface
public interface ShaderProducer {
    public FramebufferShader getInstance();
}

