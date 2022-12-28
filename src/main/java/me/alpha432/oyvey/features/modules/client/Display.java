/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class Display
extends Module {
    private static Display INSTANCE = new Display();
    public Setting<String> title = this.register(new Setting<String>("Title", "Xulu+ v1.0.6-DEV"));
    public Setting<Boolean> version = this.register(new Setting<Boolean>("version", true));

    public Display() {
        super("Title", "Sets the title of your game", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static Display getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Display();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        org.lwjgl.opengl.Display.setTitle((String)this.title.getValue());
    }
}

