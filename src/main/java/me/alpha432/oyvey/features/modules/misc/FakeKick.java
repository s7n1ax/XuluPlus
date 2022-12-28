/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.play.server.SPacketDisconnect
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class FakeKick
extends Module {
    private final Setting<Boolean> healthDisplay = this.register(new Setting<Boolean>("displayhealth", false));

    public FakeKick() {
        super("FakeKick", "Log with the press of a button", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (this.healthDisplay.getValue().booleanValue()) {
            float health = Util.mc.field_71439_g.func_110139_bj() + Util.mc.field_71439_g.func_110143_aJ();
            Minecraft.func_71410_x().func_147114_u().func_147253_a(new SPacketDisconnect((ITextComponent)new TextComponentString("logged out with " + health + " health remaining.")));
            this.disable();
        } else {
            Minecraft.func_71410_x().func_147114_u().func_147253_a(new SPacketDisconnect((ITextComponent)new TextComponentString("Internal Exception: java.lang.NullPointerException")));
            this.disable();
        }
    }
}

