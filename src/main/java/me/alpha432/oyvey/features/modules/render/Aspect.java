/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aspect
extends Module {
    private Setting<Double> aspect;

    public Aspect() {
        super("Aspect", "Changes the res without the FOV", Module.Category.RENDER, true, false, false);
        this.aspect = this.register(new Setting<Double>("Ratio", (double)Aspect.mc.field_71443_c / (double)Aspect.mc.field_71440_d, 0.0, 3.0));
    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue().floatValue());
    }
}

