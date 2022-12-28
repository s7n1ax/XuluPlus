/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketCloseWindow
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.server.SPacketCloseWindow
 *  net.minecraft.network.play.server.SPacketEntityMetadata
 *  net.minecraft.network.play.server.SPacketSetSlot
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.Flex;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.TimerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Bypass
extends Module {
    private static Bypass instance;
    private final TimerUtil timer = new TimerUtil();
    public Setting<Boolean> illegals = this.register(new Setting<Boolean>("", false));
    public Setting<Boolean> secretClose = this.register(new Setting<Object>("", Boolean.valueOf(false), v -> this.illegals.getValue()));
    public Setting<Boolean> rotation = this.register(new Setting<Object>("", Boolean.valueOf(false), v -> this.secretClose.getValue() != false && this.illegals.getValue() != false));
    public Setting<Boolean> elytra = this.register(new Setting<Boolean>("", false));
    public Setting<Boolean> reopen = this.register(new Setting<Object>("", Boolean.valueOf(false), v -> this.elytra.getValue()));
    public Setting<Integer> reopen_interval = this.register(new Setting<Object>("", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(5000), v -> this.elytra.getValue()));
    public Setting<Integer> delay = this.register(new Setting<Object>("", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> this.elytra.getValue()));
    public Setting<Boolean> allow_ghost = this.register(new Setting<Object>("GhostBlocks", Boolean.valueOf(true), v -> this.elytra.getValue()));
    public Setting<Boolean> cancel_close = this.register(new Setting<Object>("AttackCancel", Boolean.valueOf(true), v -> this.elytra.getValue()));
    public Setting<Boolean> discreet = this.register(new Setting<Object>("HiddenPacket", Boolean.valueOf(true), v -> this.elytra.getValue()));
    public Setting<Boolean> packets = this.register(new Setting<Boolean>("5PacketsForSilentCa", false));
    public Setting<Boolean> limitSwing = this.register(new Setting<Object>("SwingLimiter", Boolean.valueOf(false), v -> this.packets.getValue()));
    public Setting<Integer> swingPackets = this.register(new Setting<Object>("SwingPackets", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(100), v -> this.packets.getValue()));
    public Setting<Boolean> noLimit = this.register(new Setting<Object>("NoPressure", Boolean.valueOf(false), v -> this.packets.getValue()));
    int cooldown;
    private float yaw;
    private float pitch;
    private boolean rotate;
    private BlockPos pos;
    private int swingPacket;

    public Bypass() {
        super("-PacketBypass2b", "G", Module.Category.Flex, true, false, false);
        instance = this;
    }

    public static Bypass getInstance() {
        if (instance == null) {
            instance = new Bypass();
        }
        return instance;
    }

    @Override
    public void onToggle() {
        this.swingPacket = 0;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() == null && this.secretClose.getValue().booleanValue() && this.rotation.getValue().booleanValue()) {
            this.pos = new BlockPos(Bypass.mc.field_71439_g.func_174791_d());
            this.yaw = Bypass.mc.field_71439_g.field_70177_z;
            this.pitch = Bypass.mc.field_71439_g.field_70125_A;
            this.rotate = true;
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPacketSend(PacketEvent.Send event) {
        if (this.illegals.getValue().booleanValue() && this.secretClose.getValue().booleanValue()) {
            if (event.getPacket() instanceof CPacketCloseWindow) {
                event.setCanceled(true);
            } else if (event.getPacket() instanceof CPacketPlayer && this.rotation.getValue().booleanValue() && this.rotate) {
                CPacketPlayer packet = (CPacketPlayer)event.getPacket();
                packet.field_149476_e = this.yaw;
                packet.field_149473_f = this.pitch;
            }
        }
        if (this.packets.getValue().booleanValue() && this.limitSwing.getValue().booleanValue() && event.getPacket() instanceof CPacketAnimation) {
            if (this.swingPacket > this.swingPackets.getValue()) {
                event.setCanceled(true);
            }
            ++this.swingPacket;
        }
    }

    @SubscribeEvent
    public void onIncomingPacket(PacketEvent.Receive event) {
        if (!Bypass.fullNullCheck() && this.elytra.getValue().booleanValue()) {
            if (event.getPacket() instanceof SPacketSetSlot) {
                SPacketSetSlot packet = (SPacketSetSlot)event.getPacket();
                if (packet.func_149173_d() == 6) {
                    event.setCanceled(true);
                }
                if (!this.allow_ghost.getValue().booleanValue() && packet.func_149174_e().func_77973_b().equals((Object)Items.field_185160_cR)) {
                    event.setCanceled(true);
                }
            }
            if (this.cancel_close.getValue().booleanValue() && Bypass.mc.field_71439_g.func_184613_cA() && event.getPacket() instanceof SPacketEntityMetadata && ((SPacketEntityMetadata)event.getPacket()).func_149375_d() == Bypass.mc.field_71439_g.func_145782_y()) {
                event.setCanceled(true);
            }
        }
        if (event.getPacket() instanceof SPacketCloseWindow) {
            this.rotate = false;
        }
    }

    @Override
    public void onTick() {
        if (this.secretClose.getValue().booleanValue() && this.rotation.getValue().booleanValue() && this.rotate && this.pos != null && Bypass.mc.field_71439_g != null && Bypass.mc.field_71439_g.func_174818_b(this.pos) > 400.0) {
            this.rotate = false;
        }
        if (this.elytra.getValue().booleanValue()) {
            if (this.cooldown > 0) {
                --this.cooldown;
            } else if (!(Bypass.mc.field_71439_g == null || Bypass.mc.field_71462_r instanceof GuiInventory || Bypass.mc.field_71439_g.field_70122_E && this.discreet.getValue().booleanValue())) {
                for (int i = 0; i < 36; ++i) {
                    ItemStack item = Bypass.mc.field_71439_g.field_71071_by.func_70301_a(i);
                    if (!item.func_77973_b().equals((Object)Items.field_185160_cR)) continue;
                    Bypass.mc.field_71442_b.func_187098_a(0, i < 9 ? i + 36 : i, 0, ClickType.QUICK_MOVE, (EntityPlayer)Bypass.mc.field_71439_g);
                    this.cooldown = this.delay.getValue();
                    return;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        this.swingPacket = 0;
        if (this.elytra.getValue().booleanValue() && this.timer.passedMs(this.reopen_interval.getValue().intValue()) && this.reopen.getValue().booleanValue() && !Bypass.mc.field_71439_g.func_184613_cA() && Bypass.mc.field_71439_g.field_70143_R > 0.0f) {
            Bypass.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Bypass.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }
}

