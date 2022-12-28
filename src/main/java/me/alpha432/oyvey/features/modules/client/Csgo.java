/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;

public class Csgo
extends Module {
    Timer delayTimer = new Timer();
    public Setting<Integer> X = this.register(new Setting<Integer>("watermarkx", 0, 0, 300));
    public Setting<Integer> Y = this.register(new Setting<Integer>("watermarky", 0, 0, 300));
    public Setting<Integer> delay = this.register(new Setting<Integer>("delay", 240, 0, 600));
    public Setting<Integer> saturation = this.register(new Setting<Integer>("saturation", 127, 1, 255));
    public Setting<Integer> brightness = this.register(new Setting<Integer>("brightness", 100, 0, 255));
    public float hue;
    public int red = 1;
    public int green = 1;
    public int blue = 1;
    private String message = "";

    public Csgo() {
        super("Xulu+Watermark", "Nice Screen Extras", Module.Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.drawCsgoWatermark();
    }

    public void drawCsgoWatermark() {
        int padding = 5;
        this.message = "Xulu+ v1.0.6-DEV | " + Csgo.mc.field_71439_g.func_70005_c_() + " | " + OyVey.serverManager.getPing() + "Ms";
        Integer textWidth = Csgo.mc.field_71466_p.func_78256_a(this.message);
        Integer textHeight = Csgo.mc.field_71466_p.field_78288_b;
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 150));
        RenderUtil.drawRectangleCorrectly(this.X.getValue(), this.Y.getValue(), textWidth + 8, 2, ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue()));
        Csgo.mc.field_71466_p.func_175065_a(this.message, (float)(this.X.getValue() + 3), (float)(this.Y.getValue() + 3), ColorUtil.toRGBA(255, 255, 255, 255), false);
    }
}

