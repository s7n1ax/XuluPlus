/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.GraphicsEnvironment;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FontMod
extends Module {
    private static FontMod INSTANCE = new FontMod();
    public Setting<String> fontName = this.register(new Setting<String>("fontname", "Verdana", "Name of the font."));
    public Setting<Boolean> antiAlias = this.register(new Setting<Boolean>("antialias", Boolean.valueOf(true), "Fps Safer"));
    public Setting<Boolean> fractionalMetrics = this.register(new Setting<Boolean>("metrics", Boolean.valueOf(true), "Thinner font."));
    public Setting<Integer> fontSize = this.register(new Setting<Integer>("size", Integer.valueOf(15), Integer.valueOf(12), Integer.valueOf(30), "Size of the font."));
    public Setting<Integer> fontStyle = this.register(new Setting<Integer>("style", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(3), "Style of the font."));
    private boolean reloadFont = false;

    public FontMod() {
        super("Font", "Font change of Xulu+", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static FontMod getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FontMod();
        }
        return INSTANCE;
    }

    public static boolean checkFont(String font, boolean message) {
        String[] fonts;
        for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!message && s.equals(font)) {
                return true;
            }
            if (!message) continue;
            Command.sendMessage(s);
        }
        return false;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !FontMod.checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage((Object)ChatFormatting.RED + "That font doesnt exist.");
                event.setCanceled(true);
                return;
            }
            this.reloadFont = true;
        }
    }

    @Override
    public void onTick() {
        if (this.reloadFont) {
            OyVey.textManager.init(false);
            this.reloadFont = false;
        }
    }
}

