/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEndPortalFrame
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.player;

import java.awt.Color;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.BlockEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.TimerUtil;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speedmine
extends Module {
    private static Speedmine INSTANCE = new Speedmine();
    public final TimerUtil timer = new TimerUtil();
    private final Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(10.0f), Float.valueOf(0.0f), Float.valueOf(50.0f)));
    public Setting<Boolean> tweaks = this.register(new Setting<Boolean>("Tweaks", true));
    public Setting<Mode> mode = this.register(new Setting<Object>("Mode", (Object)Mode.PACKET, v -> this.tweaks.getValue()));
    public Setting<Float> damage = this.register(new Setting<Object>("Damage", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> this.mode.getValue() == Mode.DAMAGE && this.tweaks.getValue() != false));
    public Setting<Boolean> reset = this.register(new Setting<Boolean>("Reset", true));
    public Setting<Boolean> noBreakAnim = this.register(new Setting<Boolean>("NoBreakAnim", false));
    public Setting<Boolean> noDelay = this.register(new Setting<Boolean>("NoDelay", false));
    public Setting<Boolean> noSwing = this.register(new Setting<Boolean>("NoSwing", false));
    public Setting<Boolean> allow = this.register(new Setting<Boolean>("AllowMultiTask", false));
    public Setting<Boolean> doubleBreak = this.register(new Setting<Boolean>("DoubleBreak", false));
    public Setting<Boolean> webSwitch = this.register(new Setting<Boolean>("WebSwitch", false));
    public Setting<Boolean> silentSwitch = this.register(new Setting<Boolean>("SilentSwitch", false));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", false));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue()));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(false), v -> this.render.getValue()));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.render.getValue() != false));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.render.getValue()));
    public final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() != false && this.render.getValue() != false));
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    public float breakTime = -1.0f;
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;

    public Speedmine() {
        super("Speedmine (X)", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static Speedmine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speedmine();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (Speedmine.mc.field_71439_g != null && Speedmine.mc.field_71439_g.func_174818_b(this.currentPos) > MathUtil.square(this.range.getValue().floatValue())) {
                this.currentPos = null;
                this.currentBlockState = null;
                return;
            }
            if (Speedmine.mc.field_71439_g != null && this.silentSwitch.getValue().booleanValue() && this.timer.passedMs((int)(2000.0f * OyVey.serverManager.getTpsFactor())) && this.getPickSlot() != -1) {
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.getPickSlot()));
            }
            if (Speedmine.mc.field_71439_g != null && this.silentSwitch.getValue().booleanValue() && this.timer.passedMs((int)(2200.0f * OyVey.serverManager.getTpsFactor()))) {
                int oldSlot = Speedmine.mc.field_71439_g.field_71071_by.field_70461_c;
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
            }
            if (Speedmine.fullNullCheck()) {
                return;
            }
            if (!Speedmine.mc.field_71441_e.func_180495_p(this.currentPos).equals((Object)this.currentBlockState) || Speedmine.mc.field_71441_e.func_180495_p(this.currentPos).func_177230_c() == Blocks.field_150350_a) {
                this.currentPos = null;
                this.currentBlockState = null;
            } else if (this.webSwitch.getValue().booleanValue() && this.currentBlockState.func_177230_c() == Blocks.field_150321_G && Speedmine.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue().booleanValue()) {
            Speedmine.mc.field_71442_b.field_78781_i = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue().booleanValue()) {
            Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue().booleanValue() && Speedmine.mc.field_71474_y.field_74313_G.func_151470_d() && !this.allow.getValue().booleanValue()) {
            Speedmine.mc.field_71442_b.field_78778_j = false;
        }
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (this.render.getValue().booleanValue() && this.currentPos != null) {
            Color color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.boxAlpha.getValue());
            RenderUtil.boxESP(this.currentPos, color, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            CPacketPlayerDigging packet;
            if (this.noSwing.getValue().booleanValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            if (this.noBreakAnim.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()) != null) {
                packet.func_179715_a();
                try {
                    for (Entity entity : Speedmine.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(packet.func_179715_a()))) {
                        if (!(entity instanceof EntityEnderCrystal)) continue;
                        this.showAnimation();
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (packet.func_180762_c().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.func_179715_a(), packet.func_179714_b());
                }
                if (packet.func_180762_c().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.field_71441_e.func_180495_p(event.pos).func_177230_c() instanceof BlockEndPortalFrame) {
            Speedmine.mc.field_71441_e.func_180495_p(event.pos).func_177230_c().func_149711_c(50.0f);
        }
        if (event.getStage() == 3 && this.reset.getValue().booleanValue() && Speedmine.mc.field_71442_b.field_78770_f > 0.1f) {
            Speedmine.mc.field_71442_b.field_78778_j = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue().booleanValue()) {
            BlockPos above;
            if (BlockUtil.canBreak(event.pos)) {
                if (this.reset.getValue().booleanValue()) {
                    Speedmine.mc.field_71442_b.field_78778_j = false;
                }
                switch (this.mode.getValue()) {
                    case PACKET: {
                        if (this.currentPos == null) {
                            this.currentPos = event.pos;
                            this.currentBlockState = Speedmine.mc.field_71441_e.func_180495_p(this.currentPos);
                            ItemStack object = new ItemStack(Items.field_151046_w);
                            this.breakTime = object.func_150997_a(this.currentBlockState) / 3.71f;
                            this.timer.reset();
                        }
                        Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        event.setCanceled(true);
                        break;
                    }
                    case DAMAGE: {
                        if (Speedmine.mc.field_71442_b.field_78770_f < this.damage.getValue().floatValue()) break;
                        Speedmine.mc.field_71442_b.field_78770_f = 1.0f;
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                        Speedmine.mc.field_71442_b.func_187103_a(event.pos);
                        Speedmine.mc.field_71441_e.func_175698_g(event.pos);
                    }
                }
            }
            if (this.doubleBreak.getValue().booleanValue() && BlockUtil.canBreak(above = event.pos.func_177982_a(0, 1, 0)) && Speedmine.mc.field_71439_g.func_70011_f((double)above.func_177958_n(), (double)above.func_177956_o(), (double)above.func_177952_p()) <= 5.0) {
                Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.field_71442_b.func_187103_a(above);
                Speedmine.mc.field_71441_e.func_175698_g(above);
            }
        }
    }

    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Speedmine.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151046_w) continue;
            return i;
        }
        return -1;
    }

    private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void showAnimation() {
        this.showAnimation(false, null, null);
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        PACKET,
        DAMAGE,
        INSTANT;

    }
}

