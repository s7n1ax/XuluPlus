/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  net.minecraft.block.BlockPlanks
 *  net.minecraft.block.BlockWorkbench
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiCrafting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityBed
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.BlockUtilPhob;
import me.alpha432.oyvey.util.DamageUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtilPhob;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RotationUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedBombPhob
extends Module {
    private final Setting<Boolean> server = this.register(new Setting<Boolean>("ServerPingPacket", false));
    private final Setting<Boolean> place = this.register(new Setting<Boolean>("Place", false));
    private final Setting<Integer> placeDelay = this.register(new Setting<Object>("Delays", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.place.getValue()));
    private final Setting<Float> placeRange = this.register(new Setting<Object>("Ranges", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.place.getValue()));
    private final Setting<Boolean> extraPacket = this.register(new Setting<Object>("PacketForce", Boolean.FALSE, v -> this.place.getValue()));
    private final Setting<Boolean> packet = this.register(new Setting<Object>("Packet", Boolean.FALSE, v -> this.place.getValue()));
    private final Setting<Boolean> explode = this.register(new Setting<Boolean>("Break", true));
    private final Setting<BreakLogic> breakMode = this.register(new Setting<Object>("BreakMode", (Object)BreakLogic.ALL, v -> this.explode.getValue()));
    private final Setting<Integer> breakDelay = this.register(new Setting<Object>("Delays", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.explode.getValue()));
    private final Setting<Float> breakRange = this.register(new Setting<Object>("Ranges", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.explode.getValue()));
    private final Setting<Float> minDamage = this.register(new Setting<Object>("MinDMG", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(36.0f), v -> this.explode.getValue()));
    private final Setting<Float> range = this.register(new Setting<Object>("ERange", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(12.0f), v -> this.explode.getValue()));
    private final Setting<Boolean> suicide = this.register(new Setting<Object>("Suicide", Boolean.FALSE, v -> this.explode.getValue()));
    private final Setting<Boolean> removeTiles = this.register(new Setting<Boolean>("RemoveFire", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("RotationsPassed", false));
    private final Setting<Boolean> oneDot15 = this.register(new Setting<Boolean>("1.15", false));
    private final Setting<Logic> logic = this.register(new Setting<Object>("Logic", (Object)Logic.BREAKPLACE, v -> this.place.getValue() != false && this.explode.getValue() != false));
    private final Setting<Boolean> craft = this.register(new Setting<Boolean>("CraftBed", false));
    private final Setting<Boolean> placeCraftingTable = this.register(new Setting<Object>("PlaceTable", Boolean.FALSE, v -> this.craft.getValue()));
    private final Setting<Boolean> openCraftingTable = this.register(new Setting<Object>("OpenTable", Boolean.FALSE, v -> this.craft.getValue()));
    private final Setting<Boolean> craftTable = this.register(new Setting<Object>("CraftTable", Boolean.FALSE, v -> this.craft.getValue()));
    private final Setting<Float> tableRange = this.register(new Setting<Object>("TableRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.craft.getValue()));
    private final Setting<Integer> craftDelay = this.register(new Setting<Object>("CraftDelay", Integer.valueOf(4), Integer.valueOf(1), Integer.valueOf(10), v -> this.craft.getValue()));
    private final Setting<Integer> tableSlot = this.register(new Setting<Object>("TableSlot", Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(8), v -> this.craft.getValue()));
    private final Setting<Boolean> sslot = this.register(new Setting<Boolean>("S-Slot", false));
    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer craftTimer = new Timer();
    private final AtomicDouble yaw = new AtomicDouble(-1.0);
    private final AtomicDouble pitch = new AtomicDouble(-1.0);
    private final AtomicBoolean shouldRotate = new AtomicBoolean(false);
    private final int lastCraftStage = -1;
    private EntityPlayer target;
    private boolean sendRotationPacket;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;
    private boolean five;
    private boolean six;
    private BlockPos maxPos;
    private boolean shouldCraft;
    private int craftStage;
    private int bedSlot = -1;
    private BlockPos finalPos;
    private EnumFacing finalFacing;

    public BedBombPhob() {
        super("YippieMeta", "The YippieMod", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (!BedBombPhob.fullNullCheck() && this.shouldServer()) {
            BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled true"));
        }
    }

    @Override
    public void onDisable() {
        if (!BedBombPhob.fullNullCheck() && this.shouldServer()) {
            BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module BedBomb set Enabled false"));
            if (this.sslot.getValue().booleanValue()) {
                BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(BedBombPhob.mc.field_71439_g.field_71071_by.field_70461_c));
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (this.shouldRotate.get() && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.field_149476_e = (float)this.yaw.get();
            packet.field_149473_f = (float)this.pitch.get();
            this.shouldRotate.set(false);
        }
    }

    private boolean shouldServer() {
        return this.server.getValue();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (BedBombPhob.fullNullCheck() || BedBombPhob.mc.field_71439_g.field_71093_bK != -1 && BedBombPhob.mc.field_71439_g.field_71093_bK != 1 || this.shouldServer()) {
            return;
        }
        if (event.getStage() == 0) {
            this.doBedBomb();
            if (this.shouldCraft && BedBombPhob.mc.field_71462_r instanceof GuiCrafting) {
                int woolSlot = InventoryUtilPhob.findInventoryWool(false);
                int woodSlot = InventoryUtilPhob.findInventoryBlock(BlockPlanks.class, true);
                if (woolSlot == -1 || woodSlot == -1) {
                    mc.func_147108_a(null);
                    BedBombPhob.mc.field_71462_r = null;
                    this.shouldCraft = false;
                    return;
                }
                if (this.craftStage > 1 && !this.one) {
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 1, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.one = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() && !this.two) {
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 2, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.two = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 2 && !this.three) {
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 3, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.three = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 3 && !this.four) {
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 4, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.four = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 4 && !this.five) {
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 5, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.five = true;
                } else if (this.craftStage > 1 + this.craftDelay.getValue() * 5 && !this.six) {
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 6, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.recheckBedSlots(woolSlot, woodSlot);
                    BedBombPhob.mc.field_71442_b.func_187098_a(((GuiContainer)BedBombPhob.mc.field_71462_r).field_147002_h.field_75152_c, 0, 0, ClickType.QUICK_MOVE, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                    this.six = true;
                    this.one = false;
                    this.two = false;
                    this.three = false;
                    this.four = false;
                    this.five = false;
                    this.six = false;
                    this.craftStage = -2;
                    this.shouldCraft = false;
                }
                ++this.craftStage;
            }
        } else if (event.getStage() == 1 && this.finalPos != null) {
            Vec3d hitVec = new Vec3d((Vec3i)this.finalPos.func_177977_b()).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(this.finalFacing.func_176734_d().func_176730_m()).func_186678_a(0.5));
            BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedBombPhob.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            InventoryUtilPhob.switchToHotbarSlot(this.bedSlot, false);
            BlockUtilPhob.rightClickBlock(this.finalPos.func_177977_b(), hitVec, this.bedSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, EnumFacing.UP, this.packet.getValue());
            BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedBombPhob.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            this.placeTimer.reset();
            this.finalPos = null;
        }
    }

    public void recheckBedSlots(int woolSlot, int woodSlot) {
        int i;
        for (i = 1; i <= 3; ++i) {
            if (BedBombPhob.mc.field_71439_g.field_71070_bA.func_75138_a().get(i) != ItemStack.field_190927_a) continue;
            BedBombPhob.mc.field_71442_b.func_187098_a(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(1, i, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(1, woolSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
        }
        for (i = 4; i <= 6; ++i) {
            if (BedBombPhob.mc.field_71439_g.field_71070_bA.func_75138_a().get(i) != ItemStack.field_190927_a) continue;
            BedBombPhob.mc.field_71442_b.func_187098_a(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(1, i, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(1, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
        }
    }

    public void incrementCraftStage() {
        if (this.craftTimer.passedMs(this.craftDelay.getValue().intValue())) {
            ++this.craftStage;
            if (this.craftStage > 9) {
                this.craftStage = 0;
            }
            this.craftTimer.reset();
        }
    }

    private void doBedBomb() {
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.mapBeds();
                this.breakBeds();
                this.placeBeds();
                break;
            }
            case PLACEBREAK: {
                this.mapBeds();
                this.placeBeds();
                this.breakBeds();
            }
        }
    }

    private void breakBeds() {
        if (this.explode.getValue().booleanValue() && this.breakTimer.passedMs(this.breakDelay.getValue().intValue())) {
            if (this.breakMode.getValue() == BreakLogic.CALC) {
                if (this.maxPos != null) {
                    RayTraceResult result;
                    Vec3d hitVec = new Vec3d((Vec3i)this.maxPos).func_72441_c(0.5, 0.5, 0.5);
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set((double)rotations[0]);
                    if (this.rotate.getValue().booleanValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set((double)rotations[1]);
                    }
                    EnumFacing facing = (result = BedBombPhob.mc.field_71441_e.func_72933_a(new Vec3d(BedBombPhob.mc.field_71439_g.field_70165_t, BedBombPhob.mc.field_71439_g.field_70163_u + (double)BedBombPhob.mc.field_71439_g.func_70047_e(), BedBombPhob.mc.field_71439_g.field_70161_v), new Vec3d((double)this.maxPos.func_177958_n() + 0.5, (double)this.maxPos.func_177956_o() - 0.5, (double)this.maxPos.func_177952_p() + 0.5))) == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
                    BlockUtilPhob.rightClickBlock(this.maxPos, hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            } else {
                for (TileEntity entityBed : BedBombPhob.mc.field_71441_e.field_147482_g) {
                    RayTraceResult result;
                    if (!(entityBed instanceof TileEntityBed) || BedBombPhob.mc.field_71439_g.func_174818_b(entityBed.func_174877_v()) > MathUtil.square(this.breakRange.getValue().floatValue())) continue;
                    Vec3d hitVec = new Vec3d((Vec3i)entityBed.func_174877_v()).func_72441_c(0.5, 0.5, 0.5);
                    float[] rotations = RotationUtil.getLegitRotations(hitVec);
                    this.yaw.set((double)rotations[0]);
                    if (this.rotate.getValue().booleanValue()) {
                        this.shouldRotate.set(true);
                        this.pitch.set((double)rotations[1]);
                    }
                    EnumFacing facing = (result = BedBombPhob.mc.field_71441_e.func_72933_a(new Vec3d(BedBombPhob.mc.field_71439_g.field_70165_t, BedBombPhob.mc.field_71439_g.field_70163_u + (double)BedBombPhob.mc.field_71439_g.func_70047_e(), BedBombPhob.mc.field_71439_g.field_70161_v), new Vec3d((double)entityBed.func_174877_v().func_177958_n() + 0.5, (double)entityBed.func_174877_v().func_177956_o() - 0.5, (double)entityBed.func_174877_v().func_177952_p() + 0.5))) == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
                    BlockUtilPhob.rightClickBlock(entityBed.func_174877_v(), hitVec, EnumHand.MAIN_HAND, facing, true);
                    this.breakTimer.reset();
                }
            }
        }
    }

    private void mapBeds() {
        this.maxPos = null;
        float maxDamage = 0.5f;
        if (this.removeTiles.getValue().booleanValue()) {
            ArrayList<BedData> removedBlocks = new ArrayList<BedData>();
            for (TileEntity tile : BedBombPhob.mc.field_71441_e.field_147482_g) {
                if (!(tile instanceof TileEntityBed)) continue;
                TileEntityBed bed = (TileEntityBed)tile;
                BedData data = new BedData(tile.func_174877_v(), BedBombPhob.mc.field_71441_e.func_180495_p(tile.func_174877_v()), bed, bed.func_193050_e());
                removedBlocks.add(data);
            }
            for (BedData data : removedBlocks) {
                BedBombPhob.mc.field_71441_e.func_175698_g(data.getPos());
            }
            for (BedData data : removedBlocks) {
                float selfDamage;
                BlockPos blockPos;
                if (!data.isHeadPiece()) continue;
                BlockPos pos = data.getPos();
                if (!(BedBombPhob.mc.field_71439_g.func_174818_b(blockPos) <= MathUtil.square(this.breakRange.getValue().floatValue())) || !((double)(selfDamage = DamageUtil.calculateDamage(pos, (Entity)BedBombPhob.mc.field_71439_g)) + 1.0 < (double)EntityUtil.getHealth((Entity)BedBombPhob.mc.field_71439_g)) && DamageUtil.canTakeDamage(this.suicide.getValue())) continue;
                for (EntityPlayer player : BedBombPhob.mc.field_71441_e.field_73010_i) {
                    float damage;
                    if (!(player.func_174818_b(pos) < MathUtil.square(this.range.getValue().floatValue()) && EntityUtil.isValid((Entity)player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue()) && ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) > selfDamage || damage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue()) || damage > EntityUtil.getHealth((Entity)player)) && damage > maxDamage)) continue;
                    maxDamage = damage;
                    this.maxPos = pos;
                }
            }
            for (BedData data : removedBlocks) {
                BedBombPhob.mc.field_71441_e.func_175656_a(data.getPos(), data.getState());
            }
        } else {
            for (TileEntity tile : BedBombPhob.mc.field_71441_e.field_147482_g) {
                float selfDamage;
                BlockPos blockPos;
                TileEntityBed bed;
                if (!(tile instanceof TileEntityBed) || !(bed = (TileEntityBed)tile).func_193050_e()) continue;
                BlockPos pos = bed.func_174877_v();
                if (!(BedBombPhob.mc.field_71439_g.func_174818_b(blockPos) <= MathUtil.square(this.breakRange.getValue().floatValue())) || !((double)(selfDamage = DamageUtil.calculateDamage(pos, (Entity)BedBombPhob.mc.field_71439_g)) + 1.0 < (double)EntityUtil.getHealth((Entity)BedBombPhob.mc.field_71439_g)) && DamageUtil.canTakeDamage(this.suicide.getValue())) continue;
                for (EntityPlayer player : BedBombPhob.mc.field_71441_e.field_73010_i) {
                    float damage;
                    if (!(player.func_174818_b(pos) < MathUtil.square(this.range.getValue().floatValue()) && EntityUtil.isValid((Entity)player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue()) && ((damage = DamageUtil.calculateDamage(pos, (Entity)player)) > selfDamage || damage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue()) || damage > EntityUtil.getHealth((Entity)player)) && damage > maxDamage)) continue;
                    maxDamage = damage;
                    this.maxPos = pos;
                }
            }
        }
    }

    private void placeBeds() {
        if (this.place.getValue().booleanValue() && this.placeTimer.passedMs(this.placeDelay.getValue().intValue()) && this.maxPos == null) {
            this.bedSlot = this.findBedSlot();
            if (this.bedSlot == -1) {
                if (BedBombPhob.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151104_aV) {
                    this.bedSlot = -2;
                } else {
                    if (this.craft.getValue().booleanValue() && !this.shouldCraft && EntityUtil.getClosestEnemy(this.placeRange.getValue().floatValue()) != null) {
                        this.doBedCraft();
                    }
                    return;
                }
            }
            this.target = EntityUtil.getClosestEnemy(this.placeRange.getValue().floatValue());
            if (this.target != null) {
                BlockPos targetPos = new BlockPos(this.target.func_174791_d());
                this.placeBed(targetPos, true);
                if (this.craft.getValue().booleanValue()) {
                    this.doBedCraft();
                }
            }
        }
    }

    private void placeBed(BlockPos pos, boolean firstCheck) {
        if (BedBombPhob.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C) {
            return;
        }
        float damage = DamageUtil.calculateDamage(pos, (Entity)BedBombPhob.mc.field_71439_g);
        if ((double)damage > (double)EntityUtil.getHealth((Entity)BedBombPhob.mc.field_71439_g) + 0.5) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.func_177984_a(), false);
            }
            return;
        }
        if (!BedBombPhob.mc.field_71441_e.func_180495_p(pos).func_185904_a().func_76222_j()) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.func_177984_a(), false);
            }
            return;
        }
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        HashMap<BlockPos, EnumFacing> facings = new HashMap<BlockPos, EnumFacing>();
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos blockPos;
            if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) continue;
            BlockPos position = pos.func_177972_a(facing);
            if (!(BedBombPhob.mc.field_71439_g.func_174818_b(blockPos) <= MathUtil.square(this.placeRange.getValue().floatValue())) || !BedBombPhob.mc.field_71441_e.func_180495_p(position).func_185904_a().func_76222_j() || BedBombPhob.mc.field_71441_e.func_180495_p(position.func_177977_b()).func_185904_a().func_76222_j()) continue;
            positions.add(position);
            facings.put(position, facing.func_176734_d());
        }
        if (positions.isEmpty()) {
            if (firstCheck && this.oneDot15.getValue().booleanValue()) {
                this.placeBed(pos.func_177984_a(), false);
            }
            return;
        }
        positions.sort(Comparator.comparingDouble(pos2 -> BedBombPhob.mc.field_71439_g.func_174818_b(pos2)));
        this.finalPos = (BlockPos)positions.get(0);
        this.finalFacing = (EnumFacing)facings.get((Object)this.finalPos);
        float[] rotation = RotationUtil.simpleFacing(this.finalFacing);
        if (!this.sendRotationPacket && this.extraPacket.getValue().booleanValue()) {
            RotationUtil.faceYawAndPitch(rotation[0], rotation[1]);
            this.sendRotationPacket = true;
        }
        this.yaw.set((double)rotation[0]);
        this.pitch.set((double)rotation[1]);
        this.shouldRotate.set(true);
        OyVey.rotationManager.setPlayerRotations(rotation[0], rotation[1]);
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.func_70005_c_();
        }
        return null;
    }

    public void doBedCraft() {
        BlockPos target;
        List targets;
        int woolSlot = InventoryUtilPhob.findInventoryWool(false);
        int woodSlot = InventoryUtilPhob.findInventoryBlock(BlockPlanks.class, true);
        if (woolSlot == -1 || woodSlot == -1) {
            if (BedBombPhob.mc.field_71462_r instanceof GuiCrafting) {
                mc.func_147108_a(null);
                BedBombPhob.mc.field_71462_r = null;
            }
            return;
        }
        if (this.placeCraftingTable.getValue().booleanValue() && BlockUtil.getBlockSphere(this.tableRange.getValue().floatValue() - 1.0f, BlockWorkbench.class).size() == 0 && !(targets = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)BedBombPhob.mc.field_71439_g), this.tableRange.getValue().floatValue(), this.tableRange.getValue().intValue(), false, true, 0).stream().filter(pos -> BlockUtil.isPositionPlaceable(pos, false) == 3).sorted(Comparator.comparingInt(pos -> -this.safety((BlockPos)pos))).collect(Collectors.toList())).isEmpty()) {
            target = (BlockPos)targets.get(0);
            int tableSlot = InventoryUtilPhob.findHotbarBlock(BlockWorkbench.class);
            if (tableSlot != -1) {
                BedBombPhob.mc.field_71439_g.field_71071_by.field_70461_c = tableSlot;
                BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
            } else {
                if (this.craftTable.getValue().booleanValue()) {
                    this.craftTable();
                }
                if ((tableSlot = InventoryUtilPhob.findHotbarBlock(BlockWorkbench.class)) != -1) {
                    BedBombPhob.mc.field_71439_g.field_71071_by.field_70461_c = tableSlot;
                    BlockUtil.placeBlock(target, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                }
            }
        }
        if (this.openCraftingTable.getValue().booleanValue()) {
            List<BlockPos> tables = BlockUtil.getBlockSphere(this.tableRange.getValue().floatValue(), BlockWorkbench.class);
            tables.sort(Comparator.comparingDouble(pos -> BedBombPhob.mc.field_71439_g.func_174818_b(pos)));
            if (!tables.isEmpty() && !(BedBombPhob.mc.field_71462_r instanceof GuiCrafting)) {
                RayTraceResult result;
                target = tables.get(0);
                BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedBombPhob.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                if (BedBombPhob.mc.field_71439_g.func_174818_b(target) > MathUtil.square(this.breakRange.getValue().floatValue())) {
                    return;
                }
                Vec3d hitVec = new Vec3d((Vec3i)target);
                float[] rotations = RotationUtil.getLegitRotations(hitVec);
                this.yaw.set((double)rotations[0]);
                if (this.rotate.getValue().booleanValue()) {
                    this.shouldRotate.set(true);
                    this.pitch.set((double)rotations[1]);
                }
                EnumFacing facing = (result = BedBombPhob.mc.field_71441_e.func_72933_a(new Vec3d(BedBombPhob.mc.field_71439_g.field_70165_t, BedBombPhob.mc.field_71439_g.field_70163_u + (double)BedBombPhob.mc.field_71439_g.func_70047_e(), BedBombPhob.mc.field_71439_g.field_70161_v), new Vec3d((double)target.func_177958_n() + 0.5, (double)target.func_177956_o() - 0.5, (double)target.func_177952_p() + 0.5))) == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
                BlockUtil.rightClickBlock(target, hitVec, EnumHand.MAIN_HAND, facing, true);
                this.breakTimer.reset();
                if (BedBombPhob.mc.field_71439_g.func_70093_af()) {
                    BedBombPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedBombPhob.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
            this.shouldCraft = BedBombPhob.mc.field_71462_r instanceof GuiCrafting;
            this.craftStage = 0;
            this.craftTimer.reset();
        }
    }

    public void craftTable() {
        int woodSlot = InventoryUtilPhob.findInventoryBlock(BlockPlanks.class, true);
        if (woodSlot != -1) {
            BedBombPhob.mc.field_71442_b.func_187098_a(0, woodSlot, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(0, 1, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(0, 2, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(0, 3, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(0, 4, 1, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            BedBombPhob.mc.field_71442_b.func_187098_a(0, 0, 0, ClickType.QUICK_MOVE, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            int table = InventoryUtilPhob.findInventoryBlock(BlockWorkbench.class, true);
            if (table != -1) {
                BedBombPhob.mc.field_71442_b.func_187098_a(0, table, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                BedBombPhob.mc.field_71442_b.func_187098_a(0, this.tableSlot.getValue().intValue(), 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
                BedBombPhob.mc.field_71442_b.func_187098_a(0, table, 0, ClickType.PICKUP, (EntityPlayer)BedBombPhob.mc.field_71439_g);
            }
        }
    }

    @Override
    public void onToggle() {
        this.bedSlot = -1;
        this.sendRotationPacket = false;
        this.target = null;
        this.yaw.set(-1.0);
        this.pitch.set(-1.0);
        this.shouldRotate.set(false);
        this.shouldCraft = false;
    }

    private int findBedSlot() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = BedBombPhob.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || stack.func_77973_b() != Items.field_151104_aV) continue;
            return i;
        }
        return -1;
    }

    private int safety(BlockPos pos) {
        int safety = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if (BedBombPhob.mc.field_71441_e.func_180495_p(pos.func_177972_a(facing)).func_185904_a().func_76222_j()) continue;
            ++safety;
        }
        return safety;
    }

    public static class BedData {
        private final BlockPos pos;
        private final IBlockState state;
        private final boolean isHeadPiece;
        private final TileEntityBed entity;

        public BedData(BlockPos pos, IBlockState state, TileEntityBed bed, boolean isHeadPiece) {
            this.pos = pos;
            this.state = state;
            this.entity = bed;
            this.isHeadPiece = isHeadPiece;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public IBlockState getState() {
            return this.state;
        }

        public boolean isHeadPiece() {
            return this.isHeadPiece;
        }

        public TileEntityBed getEntity() {
            return this.entity;
        }
    }

    public static enum Logic {
        BREAKPLACE,
        PLACEBREAK;

    }

    public static enum BreakLogic {
        ALL,
        CALC;

    }
}

