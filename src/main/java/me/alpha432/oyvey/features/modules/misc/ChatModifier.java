/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.XuluTextSex;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatModifier
extends Module {
    private static ChatModifier INSTANCE = new ChatModifier();
    private final Timer timer = new Timer();
    public Setting<Suffix> suffix = this.register(new Setting<Suffix>("Suffix", Suffix.Ax3Mode, "Is The Current Suffix."));
    public Setting<String> customSuffix = this.register(new Setting<String>("", " | Xulu+ Owns Yo Ass ", v -> this.suffix.getValue() == Suffix.Ax3Mode));
    public Setting<Boolean> clean = this.register(new Setting<Boolean>("Clean", Boolean.TRUE, "Cleans your chat"));
    public Setting<Boolean> infinite = this.register(new Setting<Boolean>("Infinite", Boolean.FALSE, "Makes your chat infinite."));
    public Setting<Boolean> disability = this.register(new Setting<Boolean>("", false));

    public ChatModifier() {
        super("Chat Modifier (X)", "Modifies your chat", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static ChatModifier getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatModifier();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
        if (this.disability.getValue().booleanValue()) {
            ChatModifier.mc.field_71439_g.func_71165_d(XuluTextSex.disability);
            this.disability.setValue(false);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage)event.getPacket();
            String s = packet.func_149439_c();
            if (s.startsWith("/")) {
                return;
            }
            switch (this.suffix.getValue()) {
                case Ax3Mode: {
                    s = s + this.customSuffix.getValue();
                }
            }
            if (s.length() >= 256) {
                s = s.substring(0, 256);
            }
            packet.field_149440_a = s;
        }
    }

    @SubscribeEvent
    public void onChatPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0) {
            event.getPacket();
        }
    }

    public static enum Suffix {
        NONE,
        Ax3Mode;

    }
}

