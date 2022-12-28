/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.settings.GameSettings$Options
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = this.register(new Setting<String>("prefix", "."));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("customfov", false));
    public Setting<Float> fov = this.register(new Setting<Float>("fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Integer> red = this.register(new Setting<Integer>("red", 0, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("blue", 255, 0, 255));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("outline", true));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("alpha", 180, 0, 255));
    public Setting<Integer> topRed = this.register(new Setting<Integer>("secondred", 0, 0, 255));
    public Setting<Integer> topGreen = this.register(new Setting<Integer>("secondgreen", 0, 0, 255));
    public Setting<Integer> topBlue = this.register(new Setting<Integer>("secondblue", 150, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("hoveralpha", 240, 0, 255));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("hrainbowmode", (Object)rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("arainbowmode", (Object)rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    private OyVeyGui click;

    public ClickGui() {
        super("ClickGUI", "Shows You The Click options", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.field_71474_y.func_74304_a(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("prefix set to " + (Object)ChatFormatting.LIGHT_PURPLE + OyVey.commandManager.getPrefix());
            }
            OyVey.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.func_147108_a((GuiScreen)OyVeyGui.getClickGui());
    }

    @Override
    public void onLoad() {
        OyVey.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        OyVey.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.field_71462_r instanceof OyVeyGui)) {
            this.disable();
        }
    }

    public static enum rainbowMode {
        Static,
        Sideway;

    }

    public static enum rainbowModeArray {
        Static,
        Up;

    }
}

