/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed
extends Module {
    private static IceSpeed INSTANCE = new IceSpeed();
    private final Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(0.4f), Float.valueOf(0.1f), Float.valueOf(1.5f)));

    public IceSpeed() {
        super("IceSpeed", "Speeds you up on ice.", Module.Category.MOVEMENT, false, false, false);
        INSTANCE = this;
    }

    public static IceSpeed getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new IceSpeed();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        Blocks.field_150432_aD.field_149765_K = this.speed.getValue().floatValue();
        Blocks.field_150403_cj.field_149765_K = this.speed.getValue().floatValue();
        Blocks.field_185778_de.field_149765_K = this.speed.getValue().floatValue();
    }

    @Override
    public void onDisable() {
        Blocks.field_150432_aD.field_149765_K = 0.98f;
        Blocks.field_150403_cj.field_149765_K = 0.98f;
        Blocks.field_185778_de.field_149765_K = 0.98f;
    }
}

