/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.Flex;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Animations
extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("OldAnimations", Mode.OneDotEight));
    private final Setting<Swing> swing = this.register(new Setting<Swing>("Swing", Swing.Mainhand));
    private final Setting<Boolean> slow = this.register(new Setting<Boolean>("Slow", false));

    public Animations() {
        super("-Animations(old)", "Change animations.", Module.Category.Flex, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (Animations.nullCheck()) {
            return;
        }
        if (this.swing.getValue() == Swing.Offhand) {
            Animations.mc.field_71439_g.field_184622_au = EnumHand.OFF_HAND;
        }
        if (this.mode.getValue() == Mode.OneDotEight && (double)Animations.mc.field_71460_t.field_78516_c.field_187470_g >= 0.9) {
            Animations.mc.field_71460_t.field_78516_c.field_187469_f = 1.0f;
            Animations.mc.field_71460_t.field_78516_c.field_187467_d = Animations.mc.field_71439_g.func_184614_ca();
        }
    }

    @Override
    public void onEnable() {
        if (this.slow.getValue().booleanValue()) {
            Animations.mc.field_71439_g.func_70690_d(new PotionEffect(MobEffects.field_76419_f, 255000));
        }
    }

    @Override
    public void onDisable() {
        if (this.slow.getValue().booleanValue()) {
            Animations.mc.field_71439_g.func_184589_d(MobEffects.field_76419_f);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        Object t = send.getPacket();
        if (t instanceof CPacketAnimation && this.swing.getValue() == Swing.Disable) {
            send.setCanceled(true);
        }
    }

    private static enum Swing {
        Mainhand,
        Offhand,
        Disable;

    }

    private static enum Mode {
        Normal,
        OneDotEight;

    }
}

