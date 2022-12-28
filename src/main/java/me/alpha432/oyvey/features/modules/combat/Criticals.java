/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import java.util.Objects;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals
extends Module {
    public Setting<Boolean> noDesync = this.register(new Setting<Boolean>("NotDesynced[Forced]", true));
    public Setting<Boolean> cancelFirst = this.register(new Setting<Boolean>("Cancel32k", true));
    public Setting<Integer> delay32k = this.register(new Setting<Object>("[DelayFor32k]", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> this.cancelFirst.getValue()));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Modes", Mode.PACKET));
    private final Setting<Integer> packets = this.register(new Setting<Object>("Packets", 2, 1, 5, v -> this.mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    private final Setting<Integer> desyncDelay = this.register(new Setting<Object>("DesyncDelay", 10, 0, 500, v -> this.mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    private final Timer timer = new Timer();
    private final Timer timer32k = new Timer();
    private boolean firstCanceled = false;
    private boolean resetTimer = false;

    public Criticals() {
        super("Crits", "Tries To Land Critical Hits", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK) {
            if (this.firstCanceled) {
                this.timer32k.reset();
                this.resetTimer = true;
                this.timer.setMs(this.desyncDelay.getValue() + 1);
                this.firstCanceled = false;
                return;
            }
            if (this.resetTimer && !this.timer32k.passedMs(this.delay32k.getValue().intValue())) {
                return;
            }
            if (this.resetTimer && this.timer32k.passedMs(this.delay32k.getValue().intValue())) {
                this.resetTimer = false;
            }
            if (!this.timer.passedMs(this.desyncDelay.getValue().intValue())) {
                return;
            }
            if (!(!Criticals.mc.field_71439_g.field_70122_E || Criticals.mc.field_71474_y.field_74314_A.func_151470_d() || !(packet.func_149564_a((World)Criticals.mc.field_71441_e) instanceof EntityLivingBase) && this.noDesync.getValue().booleanValue() || Criticals.mc.field_71439_g.func_70090_H() || Criticals.mc.field_71439_g.func_180799_ab())) {
                if (this.mode.getValue() == Mode.PACKET) {
                    switch (this.packets.getValue()) {
                        case 1: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + (double)0.1f, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 2: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0625101, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 1.1E-5, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 3: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0625101, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0125, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 4: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.05, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.03, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 5: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.1625, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 4.0E-6, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 1.0E-6, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer());
                            Criticals.mc.field_71439_g.func_71009_b(Objects.requireNonNull(packet.func_149564_a((World)Criticals.mc.field_71441_e)));
                        }
                    }
                } else if (this.mode.getValue() == Mode.BYPASS) {
                    Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.11, Criticals.mc.field_71439_g.field_70161_v, false));
                    Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.1100013579, Criticals.mc.field_71439_g.field_70161_v, false));
                    Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.1100013579, Criticals.mc.field_71439_g.field_70161_v, false));
                } else {
                    Criticals.mc.field_71439_g.func_70664_aZ();
                    if (this.mode.getValue() == Mode.MINIJUMP) {
                        Criticals.mc.field_71439_g.field_70181_x /= 2.0;
                    }
                }
                this.timer.reset();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        JUMP,
        MINIJUMP,
        PACKET,
        BYPASS;

    }
}

