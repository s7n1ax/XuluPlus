/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.setting;

import java.awt.Color;
import java.util.function.Predicate;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.OyColor;

public class ColorSetting
extends Setting<OyColor> {
    private boolean rainbow;
    private String name;
    private OyColor value;
    private Predicate<OyColor> shown;
    private boolean isRainbowPredicate;
    private Predicate<Boolean> rainbowShown;

    public ColorSetting(String name, OyColor value) {
        super(name, value);
        this.name = name;
        this.value = value;
    }

    public ColorSetting(String name, OyColor value, Predicate<OyColor> shown) {
        super(name, value, shown);
        this.name = name;
        this.value = value;
        this.shown = shown;
    }

    public ColorSetting(String name, OyColor value, Boolean isRainbowPredicate, Predicate<Boolean> rainbowShown) {
        super(name, value);
        this.name = name;
        this.value = value;
        this.isRainbowPredicate = isRainbowPredicate;
        this.rainbowShown = rainbowShown;
    }

    @Override
    public OyColor getValue() {
        this.doRainBow();
        return this.value;
    }

    private void doRainBow() {
        if (this.rainbow) {
            OyColor c = OyColor.fromHSB((float)(System.currentTimeMillis() % 11520L) / 11520.0f, this.value.getSaturation(), this.value.getBrightness());
            this.setValue(new OyColor(c.getRed(), c.getGreen(), c.getBlue(), this.value.getAlpha()));
        }
    }

    @Override
    public void setValue(OyColor value) {
        this.value = value;
    }

    @Override
    public void setValue(Color value) {
        this.value = new OyColor(value);
    }

    public void setValue(int red, int green, int blue, int alpha) {
        this.value = new OyColor(red, green, blue, alpha);
    }

    public Color getColor() {
        return this.value;
    }

    public boolean getRainbow() {
        if (this.isRainbowPredicate && this.rainbowShown != null) {
            this.rainbow = this.rainbowShown.test(this.rainbow);
        }
        return this.rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    public boolean isShown() {
        if (this.shown == null) {
            return true;
        }
        return this.shown.test(this.getValue());
    }

    @Override
    public String getType() {
        return "OyColor";
    }
}

