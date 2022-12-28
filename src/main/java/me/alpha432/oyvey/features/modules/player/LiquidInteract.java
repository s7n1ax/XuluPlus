/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;

public class LiquidInteract
extends Module {
    private static LiquidInteract INSTANCE = new LiquidInteract();

    public LiquidInteract() {
        super("liquidinteract", "lets you place on liquid", Module.Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static LiquidInteract getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LiquidInteract();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

