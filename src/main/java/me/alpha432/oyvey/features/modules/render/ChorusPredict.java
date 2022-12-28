/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChorusPredict
extends Module {
    private final Timer renderTimer = new Timer();
    private BlockPos pos;
    private final Setting<Boolean> debug = this.register(new Setting<Boolean>("debug", true));
    private final Setting<Integer> renderDelay = this.register(new Setting<Integer>("renderdelay", 4000, 0, 4000));
    private Setting<Boolean> rainbow = this.register(new Setting<Boolean>("rainbow", false));
    private Setting<Integer> red = this.register(new Setting<Object>("red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> green = this.register(new Setting<Object>("green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> blue = this.register(new Setting<Object>("blue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> alpha = this.register(new Setting<Object>("alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));
    private Setting<Integer> outlineAlpha = this.register(new Setting<Object>("ol-alpha", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue() == false));

    public ChorusPredict() {
        super("ChorusPredict(OLD)", "Predicts Chorus", Module.Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSoundEffect packet;
        if (event.getPacket() instanceof SPacketSoundEffect && ((packet = (SPacketSoundEffect)event.getPacket()).func_186978_a() == SoundEvents.field_187544_ad || packet.func_186978_a() == SoundEvents.field_187534_aX)) {
            this.renderTimer.reset2();
            this.pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("A player chorused to: " + (Object)ChatFormatting.RED + "X: " + this.pos.func_177958_n() + ", Y: " + this.pos.func_177956_o() + ", Z: " + this.pos.func_177952_p());
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.pos != null) {
            if (this.renderTimer.passed(this.renderDelay.getValue().intValue())) {
                this.pos = null;
                return;
            }
            RenderUtil.drawBoxESP(this.pos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.outlineAlpha.getValue()), 1.5f, true, true, this.alpha.getValue());
        }
    }
}

