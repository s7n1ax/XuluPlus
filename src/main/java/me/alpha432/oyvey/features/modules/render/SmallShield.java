/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class SmallShield
extends Module {
    private static SmallShield INSTANCE = new SmallShield();
    public Setting<Float> offX = this.register(new Setting<Float>("offhandx", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> offY = this.register(new Setting<Float>("offhandy", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> mainX = this.register(new Setting<Float>("mainhandx", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> mainY = this.register(new Setting<Float>("mainhandy", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));

    public SmallShield() {
        super("Smallshield", "Makes you offhand lower.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static SmallShield getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SmallShield();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

