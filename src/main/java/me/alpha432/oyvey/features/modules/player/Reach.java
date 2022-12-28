/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class Reach
extends Module {
    private static Reach INSTANCE = new Reach();
    public Setting<Boolean> override = this.register(new Setting<Boolean>("Forced", false));
    public Setting<Float> add = this.register(new Setting<Object>("ThreeBReach", Float.valueOf(3.0f), v -> this.override.getValue() == false));
    public Setting<Float> reach = this.register(new Setting<Object>("Force6B", Float.valueOf(6.0f), v -> this.override.getValue()));

    public Reach() {
        super("Reach", "Reaches Beyond The Vanilla Limit", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static Reach getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Reach();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        return this.override.getValue() != false ? this.reach.getValue().toString() : this.add.getValue().toString();
    }
}

