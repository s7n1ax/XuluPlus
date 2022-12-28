/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketTabComplete
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.util.Random;
import java.util.UUID;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AntiAFK
extends Module {
    private final Random random;
    private final Setting<Boolean> swing = this.register(new Setting<Boolean>("Swing", true));
    private final Setting<Boolean> turn = this.register(new Setting<Boolean>("Turn", true));
    private final Setting<Boolean> jump = this.register(new Setting<Boolean>("Jump", true));
    private final Setting<Boolean> sneak = this.register(new Setting<Boolean>("Sneak", true));
    private final Setting<Boolean> interact = this.register(new Setting<Boolean>("InteractBlock", false));
    private final Setting<Boolean> tabcomplete = this.register(new Setting<Boolean>("TabComplete", true));
    private final Setting<Boolean> msgs = this.register(new Setting<Boolean>("ChatMsgs", true));
    private final Setting<Boolean> stats = this.register(new Setting<Boolean>("Stats", true));
    private final Setting<Boolean> window = this.register(new Setting<Boolean>("WindowClick", true));
    private final Setting<Boolean> swap = this.register(new Setting<Boolean>("ItemSwap", true));
    private final Setting<Boolean> dig = this.register(new Setting<Boolean>("HitBlock", true));
    private final Setting<Boolean> move = this.register(new Setting<Boolean>("Move", true));

    public AntiAFK() {
        super("-NoDisconnect", "Attempts to stop the server from kicking u when ur afk.", Module.Category.Flex, true, false, false);
        this.random = new Random();
    }

    @Override
    public void onUpdate() {
        BlockPos blockPos;
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 45 == 0 && this.swing.getValue().booleanValue()) {
            AntiAFK.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 20 == 0 && this.turn.getValue().booleanValue()) {
            AntiAFK.mc.field_71439_g.field_70177_z = this.random.nextInt(360) - 180;
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 60 == 0 && this.jump.getValue().booleanValue() && AntiAFK.mc.field_71439_g.field_70122_E) {
            AntiAFK.mc.field_71439_g.func_70664_aZ();
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 50 == 0 && this.sneak.getValue().booleanValue() && !AntiAFK.mc.field_71439_g.func_70093_af()) {
            AntiAFK.mc.field_71439_g.field_71158_b.field_78899_d = true;
        }
        if ((double)AntiAFK.mc.field_71439_g.field_70173_aa % 52.5 == 0.0 && this.sneak.getValue().booleanValue() && AntiAFK.mc.field_71439_g.func_70093_af()) {
            AntiAFK.mc.field_71439_g.field_71158_b.field_78899_d = false;
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 30 == 0 && this.interact.getValue().booleanValue() && !AntiAFK.mc.field_71441_e.func_175623_d(blockPos = AntiAFK.mc.field_71476_x.func_178782_a())) {
            AntiAFK.mc.field_71442_b.func_180511_b(blockPos, AntiAFK.mc.field_71476_x.field_178784_b);
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 80 == 0 && this.tabcomplete.getValue().booleanValue() && !AntiAFK.mc.field_71439_g.field_70128_L) {
            AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketTabComplete("/" + UUID.randomUUID().toString().replace('-', 'v'), AntiAFK.mc.field_71439_g.func_180425_c(), false));
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 200 == 0 && this.msgs.getValue().booleanValue() && !AntiAFK.mc.field_71439_g.field_70128_L) {
            AntiAFK.mc.field_71439_g.func_71165_d("Xulu Anti Disconnect " + this.random.nextInt());
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 300 == 0 && this.stats.getValue().booleanValue() && !AntiAFK.mc.field_71439_g.field_70128_L) {
            AntiAFK.mc.field_71439_g.func_71165_d("/stats");
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 125 == 0 && this.window.getValue().booleanValue() && !AntiAFK.mc.field_71439_g.field_70128_L) {
            AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(1, 1, 1, ClickType.CLONE, new ItemStack(Blocks.field_150343_Z), 1));
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 70 == 0 && this.swap.getValue().booleanValue() && !AntiAFK.mc.field_71439_g.field_70128_L) {
            AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, AntiAFK.mc.field_71439_g.func_180425_c(), EnumFacing.DOWN));
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 50 == 0 && this.dig.getValue().booleanValue()) {
            AntiAFK.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, AntiAFK.mc.field_71439_g.func_180425_c(), EnumFacing.DOWN));
        }
        if (AntiAFK.mc.field_71439_g.field_70173_aa % 150 == 0 && this.move.getValue().booleanValue()) {
            AntiAFK.mc.field_71474_y.field_74351_w.field_74513_e = true;
            AntiAFK.mc.field_71474_y.field_74368_y.field_74513_e = true;
            AntiAFK.mc.field_71474_y.field_74366_z.field_74513_e = true;
            AntiAFK.mc.field_71474_y.field_74370_x.field_74513_e = true;
        }
    }
}

