/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.alpha432.oyvey.features.Notifications;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notifications {
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer = new Timer();

    public Notifications(String text, long disableTime) {
        this.text = text;
        this.disableTime = disableTime;
        this.width = OyVey.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }

    public void onDraw(int y) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        if (this.timer.passedMs(this.disableTime)) {
            OyVey.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect((float)(scaledResolution.func_78326_a() - 4) - this.width, y, scaledResolution.func_78326_a() - 2, y + OyVey.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3, 0x75000000);
        OyVey.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, (float)scaledResolution.func_78326_a() - this.width - 3.0f, y + 2, -1, true);
    }
}

