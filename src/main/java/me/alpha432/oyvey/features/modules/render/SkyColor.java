/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogColors
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkyColor
extends Module {
    private final Setting<Integer> red = this.register(new Setting<Integer>("red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("blue", 255, 0, 255));

    public SkyColor() {
        super("Skycolor", "Change the sky color.", Module.Category.RENDER, false, false, false);
    }

    @SubscribeEvent
    public void onUpdate(EntityViewRenderEvent.FogColors event) {
        event.setRed((float)this.red.getValue().intValue() / 255.0f);
        event.setGreen((float)this.green.getValue().intValue() / 255.0f);
        event.setBlue((float)this.blue.getValue().intValue() / 255.0f);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
}

