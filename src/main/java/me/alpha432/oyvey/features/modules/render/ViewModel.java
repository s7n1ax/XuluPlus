/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.RenderItemEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel
extends Module {
    private static ViewModel INSTANCE = new ViewModel();
    public Setting<Settings> settings = this.register(new Setting<Settings>("settings", Settings.TRANSLATE));
    public Setting<Boolean> noEatAnimation = this.register(new Setting<Boolean>("noeatanimation", Boolean.valueOf(false), v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> eatX = this.register(new Setting<Double>("eatx", Double.valueOf(1.0), Double.valueOf(-2.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TWEAKS && this.noEatAnimation.getValue() == false));
    public Setting<Double> eatY = this.register(new Setting<Double>("eaty", Double.valueOf(1.0), Double.valueOf(-2.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TWEAKS && this.noEatAnimation.getValue() == false));
    public Setting<Boolean> doBob = this.register(new Setting<Boolean>("itembob", Boolean.valueOf(true), v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> mainX = this.register(new Setting<Double>("mainx", Double.valueOf(1.2), Double.valueOf(-2.0), Double.valueOf(4.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainY = this.register(new Setting<Double>("mainy", Double.valueOf(-0.95), Double.valueOf(-3.0), Double.valueOf(3.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainZ = this.register(new Setting<Double>("mainz", Double.valueOf(-1.45), Double.valueOf(-5.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offX = this.register(new Setting<Double>("offx", Double.valueOf(1.2), Double.valueOf(-2.0), Double.valueOf(4.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offY = this.register(new Setting<Double>("offy", Double.valueOf(-0.95), Double.valueOf(-3.0), Double.valueOf(3.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offZ = this.register(new Setting<Double>("offz", Double.valueOf(-1.45), Double.valueOf(-5.0), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Integer> mainRotX = this.register(new Setting<Integer>("mainrotationx", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> mainRotY = this.register(new Setting<Integer>("mainrotationy", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> mainRotZ = this.register(new Setting<Integer>("mainrotationz", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotX = this.register(new Setting<Integer>("offrotationx", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotY = this.register(new Setting<Integer>("offrotationy", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotZ = this.register(new Setting<Integer>("offrotationz", Integer.valueOf(0), Integer.valueOf(-36), Integer.valueOf(36), v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Double> mainScaleX = this.register(new Setting<Double>("mainscalex", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleY = this.register(new Setting<Double>("mainscaley", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleZ = this.register(new Setting<Double>("mainscalez", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleX = this.register(new Setting<Double>("offscalex", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleY = this.register(new Setting<Double>("offscaley", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleZ = this.register(new Setting<Double>("offscalez", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(5.0), v -> this.settings.getValue() == Settings.SCALE));

    public ViewModel() {
        super("Viewmodel", "X+", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static ViewModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModel();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onItemRender(RenderItemEvent event) {
        event.setMainX(this.mainX.getValue());
        event.setMainY(this.mainY.getValue());
        event.setMainZ(this.mainZ.getValue());
        event.setOffX(-this.offX.getValue().doubleValue());
        event.setOffY(this.offY.getValue());
        event.setOffZ(this.offZ.getValue());
        event.setMainRotX(this.mainRotX.getValue() * 5);
        event.setMainRotY(this.mainRotY.getValue() * 5);
        event.setMainRotZ(this.mainRotZ.getValue() * 5);
        event.setOffRotX(this.offRotX.getValue() * 5);
        event.setOffRotY(this.offRotY.getValue() * 5);
        event.setOffRotZ(this.offRotZ.getValue() * 5);
        event.setOffHandScaleX(this.offScaleX.getValue());
        event.setOffHandScaleY(this.offScaleY.getValue());
        event.setOffHandScaleZ(this.offScaleZ.getValue());
        event.setMainHandScaleX(this.mainScaleX.getValue());
        event.setMainHandScaleY(this.mainScaleY.getValue());
        event.setMainHandScaleZ(this.mainScaleZ.getValue());
    }

    private static enum Settings {
        TRANSLATE,
        ROTATE,
        SCALE,
        TWEAKS;

    }
}

