/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class NameChanger
extends Module {
    public final Setting<String> NameString = this.register(new Setting<String>("Name", "New Name Here"));
    private static NameChanger instance;

    public NameChanger() {
        super("NameChanger", "Changes name", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        Command.sendMessage((Object)ChatFormatting.DARK_PURPLE + "Success! Name succesfully changed to " + (Object)ChatFormatting.LIGHT_PURPLE + this.NameString.getValue());
    }

    public static NameChanger getInstance() {
        if (instance == null) {
            instance = new NameChanger();
        }
        return instance;
    }
}

