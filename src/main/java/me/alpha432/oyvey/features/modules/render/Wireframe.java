/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Wireframe
extends Module {
    private static Wireframe INSTANCE = new Wireframe();
    public final Setting<Float> alpha = this.register(new Setting<Float>("playeralpha", Float.valueOf(255.0f), Float.valueOf(0.1f), Float.valueOf(255.0f)));
    public final Setting<Float> cAlpha = this.register(new Setting<Float>("crystalap", Float.valueOf(255.0f), Float.valueOf(0.1f), Float.valueOf(255.0f)));
    public final Setting<Float> lineWidth = this.register(new Setting<Float>("plinewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    public final Setting<Float> crystalLineWidth = this.register(new Setting<Float>("clinewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    public Setting<RenderMode> mode = this.register(new Setting<RenderMode>("pmode", RenderMode.SOLID));
    public Setting<RenderMode> cMode = this.register(new Setting<RenderMode>("cmode", RenderMode.SOLID));
    public Setting<Boolean> players = this.register(new Setting<Boolean>("players", Boolean.FALSE));
    public Setting<Boolean> playerModel = this.register(new Setting<Boolean>("playersmodel", Boolean.FALSE));
    public Setting<Boolean> crystals = this.register(new Setting<Boolean>("crystals", Boolean.FALSE));
    public Setting<Boolean> crystalModel = this.register(new Setting<Boolean>("crystalmodel", Boolean.FALSE));

    public Wireframe() {
        super("WireFrame", "Draws a wireframe esp around other players.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static Wireframe getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Wireframe();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {
        event.getEntityPlayer().field_70737_aN = 0;
    }

    public static enum RenderMode {
        SOLID,
        WIREFRAME;

    }
}

