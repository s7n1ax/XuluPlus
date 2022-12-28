/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.util.EnumHand
 */
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public class Swing
extends Module {
    public Setting<mode> swingmode = this.register(new Setting<mode>("Mode", mode.CancelAnimation));
    public Setting<Boolean> cancelmotion = this.register(new Setting<Boolean>("Motion", true));

    public Swing() {
        super("Swing", "Swinged", Module.Category.PLAYER, true, false, false);
    }

    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketAnimation && this.swingmode.getValue() == mode.CancelAnimation) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (this.cancelmotion.getValue().booleanValue()) {
            if ((double)Swing.mc.field_71460_t.field_78516_c.field_187472_i >= 0.9) {
                Swing.mc.field_71460_t.field_78516_c.field_187471_h = 1.0f;
            }
            if ((double)Swing.mc.field_71460_t.field_78516_c.field_187470_g >= 0.9) {
                Swing.mc.field_71460_t.field_78516_c.field_187469_f = 1.0f;
            }
        }
        switch (this.swingmode.getValue()) {
            case Mainhand: {
                Swing.setSwingingHand(EnumHand.MAIN_HAND);
            }
            case Offhand: {
                Swing.setSwingingHand(EnumHand.OFF_HAND);
            }
        }
    }

    public static void setSwingingHand(EnumHand hand) {
        if (!Swing.mc.field_71439_g.field_82175_bq || Swing.mc.field_71439_g.field_110158_av < 0) {
            Swing.mc.field_71439_g.field_110158_av = -1;
            Swing.mc.field_71439_g.field_184622_au = hand;
        }
    }

    public static enum mode {
        CancelAnimation,
        Mainhand,
        Offhand;

    }
}

