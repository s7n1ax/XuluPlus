/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package me.alpha432.oyvey.util;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;

public class OyColor
extends Color {
    public OyColor(int r, int g, int b) {
        super(r, g, b);
    }

    public OyColor(int rgb) {
        super(rgb);
    }

    public OyColor(int rgba, boolean hasAlpha) {
        super(rgba, hasAlpha);
    }

    public OyColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public OyColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public OyColor(Color color, int alpha) {
        super(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static OyColor fromHSB(float hue, float saturation, float brightness) {
        return new OyColor(Color.getHSBColor(hue, saturation, brightness));
    }

    public float getHue() {
        return OyColor.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null)[0];
    }

    public float getSaturation() {
        return OyColor.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null)[1];
    }

    public float getBrightness() {
        return OyColor.RGBtoHSB(this.getRed(), this.getGreen(), this.getBlue(), null)[2];
    }

    public float getRedNorm() {
        return (float)this.getRed() / 255.0f;
    }

    public float getGreenNorm() {
        return (float)this.getGreen() / 255.0f;
    }

    public float getBlueNorm() {
        return (float)this.getBlue() / 255.0f;
    }

    public float getAlphaNorm() {
        return (float)this.getAlpha() / 255.0f;
    }

    public void glColor() {
        GlStateManager.func_179131_c((float)this.getRedNorm(), (float)this.getGreenNorm(), (float)this.getBlueNorm(), (float)this.getAlphaNorm());
    }
}

