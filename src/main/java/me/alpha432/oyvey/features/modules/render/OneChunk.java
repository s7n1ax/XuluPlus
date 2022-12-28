/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;

public class OneChunk
extends Module {
    private static OneChunk INSTANCE;
    private int renderDistance;

    private OneChunk() {
        super("OneChunk", "Sets your render distance to 1", Module.Category.RENDER, false, false, true);
        this.renderDistance = OneChunk.mc.field_71474_y.field_151451_c;
    }

    public static OneChunk getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OneChunk();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.renderDistance = OneChunk.mc.field_71474_y.field_151451_c;
        OneChunk.mc.field_71474_y.field_151451_c = 1;
    }

    @Override
    public void onDisable() {
        OneChunk.mc.field_71474_y.field_151451_c = this.renderDistance;
    }
}

