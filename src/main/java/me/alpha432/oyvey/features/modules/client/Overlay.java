/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 */
package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Overlay
extends Module {
    public static final ResourceLocation mark = new ResourceLocation("textures/aa12.png");
    public Setting<Integer> imageX = this.register(new Setting<Integer>("x", 0, 0, 300));
    public Setting<Integer> imageY = this.register(new Setting<Integer>("y", 0, 0, 300));
    public Setting<Integer> imageWidth = this.register(new Setting<Integer>("width", 154, 0, 1600));
    public Setting<Integer> imageHeight = this.register(new Setting<Integer>("height", 105, 0, 900));
    private int color;

    public Overlay() {
        super("Heaven", "Xulu+ Logo", Module.Category.CLIENT, false, false, false);
    }

    public void renderLogo() {
        int width = this.imageWidth.getValue();
        int height = this.imageHeight.getValue();
        int x = this.imageX.getValue();
        int y = this.imageY.getValue();
        Util.mc.field_71446_o.func_110577_a(mark);
        GlStateManager.func_179124_c((float)255.0f, (float)255.0f, (float)255.0f);
        Gui.func_152125_a((int)(x - 2), (int)(y - 36), (float)7.0f, (float)7.0f, (int)(width - 7), (int)(height - 7), (int)width, (int)height, (float)width, (float)height);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (!Feature.fullNullCheck()) {
            int width = this.renderer.scaledWidth;
            int height = this.renderer.scaledHeight;
            this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
            if (((Boolean)this.enabled.getValue()).booleanValue()) {
                this.renderLogo();
            }
        }
    }
}

