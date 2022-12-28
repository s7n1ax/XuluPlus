/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package me.alpha432.oyvey.manager;

import java.awt.Font;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.font.CustomFont;
import me.alpha432.oyvey.features.modules.client.FontMod;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.util.math.MathHelper;

public class TextManager
extends Feature {
    private final Timer idleTimer = new Timer();
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
    private boolean idling;

    public TextManager() {
        this.updateResolution();
    }

    public void init(boolean startup) {
        FontMod cFont = OyVey.moduleManager.getModuleByClass(FontMod.class);
        try {
            this.setFontRenderer(new Font(cFont.fontName.getValue(), (int)cFont.fontStyle.getValue(), cFont.fontSize.getValue()), cFont.antiAlias.getValue(), cFont.fractionalMetrics.getValue());
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (OyVey.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        TextManager.mc.field_71466_p.func_175065_a(text, x, y, color, shadow);
    }

    public int getStringWidth(String text) {
        if (OyVey.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            return this.customFont.getStringWidth(text);
        }
        return TextManager.mc.field_71466_p.func_78256_a(text);
    }

    public int getFontHeight() {
        if (OyVey.moduleManager.isModuleEnabled(FontMod.getInstance().getName())) {
            String text = "A";
            return this.customFont.getStringHeight(text);
        }
        return TextManager.mc.field_71466_p.field_78288_b;
    }

    public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }

    public Font getCurrentFont() {
        return this.customFont.getFont();
    }

    public void updateResolution() {
        this.scaledWidth = TextManager.mc.field_71443_c;
        this.scaledHeight = TextManager.mc.field_71440_d;
        this.scaleFactor = 1;
        boolean flag = mc.func_152349_b();
        int i = TextManager.mc.field_71474_y.field_74335_Z;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        double scaledWidthD = this.scaledWidth / this.scaleFactor;
        double scaledHeightD = this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.func_76143_f((double)scaledWidthD);
        this.scaledHeight = MathHelper.func_76143_f((double)scaledHeightD);
    }

    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}

