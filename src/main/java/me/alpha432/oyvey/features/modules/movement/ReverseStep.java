/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class ReverseStep
extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 0, 0, 20));

    public ReverseStep() {
        super("ReverseStep", "Speeds up downwards motion", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.mc.field_71439_g.func_180799_ab() || ReverseStep.mc.field_71439_g.func_70090_H()) {
            return;
        }
        if (ReverseStep.mc.field_71439_g.field_70122_E) {
            ReverseStep.mc.field_71439_g.field_70181_x -= (double)this.speed.getValue().intValue();
        }
    }
}

