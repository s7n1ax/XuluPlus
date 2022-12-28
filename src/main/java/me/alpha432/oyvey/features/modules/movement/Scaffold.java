/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.movement;

import java.util.Arrays;
import java.util.List;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.SprintFuture;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Scaffold
extends Module {
    private final Setting<?> mode = this.register(new Setting<Mode>("Modes", Mode.Normal));
    private final Setting swing;
    private final Setting bSwitch;
    private final Setting center;
    private final Setting keepY;
    private final Setting sprint;
    private final Setting replenishBlocks;
    private final Setting down;
    private final Setting expand;
    private final List<? extends Block> invalid;
    private final Timer timerMotion;
    private final Timer itemTimer;
    private final Timer timer;
    public Setting rotation = this.register(new Setting<Object>("Rotate", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.Normal));
    private int lastY;
    private BlockPos pos;
    private boolean teleported;

    public Scaffold() {
        super("Scaffold", "TowersUpwards ", Module.Category.MOVEMENT, true, false, false);
        this.swing = this.register(new Setting<Object>("Swing(for2b)", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.Vanilla));
        this.bSwitch = this.register(new Setting<Object>("Switches", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.Vanilla));
        this.center = this.register(new Setting<Object>("CenterMyself", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.Vanilla));
        this.keepY = this.register(new Setting<Object>("KeepYLevel", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.Vanilla));
        this.sprint = this.register(new Setting<Object>("UseSprintFuture", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.Vanilla));
        this.replenishBlocks = this.register(new Setting<Object>("ReplenishBlocks", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.Vanilla));
        this.down = this.register(new Setting<Object>("Down", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.Vanilla));
        this.expand = this.register(new Setting<Object>("Expand", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), v -> this.mode.getValue() == Mode.Vanilla));
        this.invalid = Arrays.asList(new Block[]{Blocks.field_150381_bn, Blocks.field_150460_al, Blocks.field_150404_cg, Blocks.field_150462_ai, Blocks.field_150447_bR, Blocks.field_150486_ae, Blocks.field_150367_z, Blocks.field_150350_a, Blocks.field_150355_j, Blocks.field_150353_l, Blocks.field_150358_i, Blocks.field_150356_k, Blocks.field_150431_aC, Blocks.field_150478_aa, Blocks.field_150467_bQ, Blocks.field_150421_aI, Blocks.field_150430_aB, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150323_B, Blocks.field_150456_au, Blocks.field_150445_bS, Blocks.field_150452_aw, Blocks.field_150443_bT, Blocks.field_150337_Q, Blocks.field_150338_P, Blocks.field_150327_N, Blocks.field_150328_O, Blocks.field_150467_bQ, Blocks.field_150434_aF, Blocks.field_150468_ap, Blocks.field_150477_bB});
        this.timerMotion = new Timer();
        this.itemTimer = new Timer();
        this.timer = new Timer();
    }

    public static void swap(int slot, int hotbarNum) {
        Scaffold.mc.field_71442_b.func_187098_a(Scaffold.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.field_71439_g);
        Scaffold.mc.field_71442_b.func_187098_a(Scaffold.mc.field_71439_g.field_71069_bz.field_75152_c, hotbarNum, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.field_71439_g);
        Scaffold.mc.field_71442_b.func_187098_a(Scaffold.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, ClickType.PICKUP, (EntityPlayer)Scaffold.mc.field_71439_g);
        Scaffold.mc.field_71442_b.func_78765_e();
    }

    public static int getItemSlot(Container container, Item item) {
        int slot = 0;
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!container.func_75139_a(i).func_75216_d() || (is = container.func_75139_a(i).func_75211_c()).func_77973_b() != item) continue;
            slot = i;
        }
        return slot;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.field_191988_bg != 0.0f || entity.field_70702_br != 0.0f;
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Mode.Normal) {
            BlockPos playerBlock;
            if (this.isOff() || Scaffold.fullNullCheck() || event.getStage() == 0) {
                return;
            }
            if (!Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                this.timer.reset();
            }
            if (BlockUtil.isScaffoldPos((playerBlock = EntityUtil.getPlayerPosWithEntity()).func_177982_a(0, -1, 0))) {
                if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -2, 0))) {
                    this.place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.UP);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 0))) {
                    this.place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.EAST);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 0))) {
                    this.place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.WEST);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, -1))) {
                    this.place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.SOUTH);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
                    this.place(playerBlock.func_177982_a(0, -1, 0), EnumFacing.NORTH);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
                        this.place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.NORTH);
                    }
                    this.place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.EAST);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.func_177982_a(-1, -1, 0))) {
                        this.place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.WEST);
                    }
                    this.place(playerBlock.func_177982_a(-1, -1, 1), EnumFacing.SOUTH);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
                        this.place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.SOUTH);
                    }
                    this.place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.WEST);
                } else if (BlockUtil.isValidBlock(playerBlock.func_177982_a(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.func_177982_a(0, -1, 1))) {
                        this.place(playerBlock.func_177982_a(0, -1, 1), EnumFacing.EAST);
                    }
                    this.place(playerBlock.func_177982_a(1, -1, 1), EnumFacing.NORTH);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.Vanilla) {
            if (OyVey.moduleManager.isModuleEnabled("SprintFuture") && (((Boolean)this.down.getValue()).booleanValue() && Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() || !((Boolean)this.sprint.getValue()).booleanValue())) {
                Scaffold.mc.field_71439_g.func_70031_b(false);
                SprintFuture.getInstance().disable();
            }
            if (((Boolean)this.replenishBlocks.getValue()).booleanValue() && !(Scaffold.mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemBlock) && this.getBlockCountHotbar() <= 0 && this.itemTimer.passedMs(100L)) {
                for (int i = 9; i < 45; ++i) {
                    ItemStack is;
                    if (!Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() || !((is = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c()).func_77973_b() instanceof ItemBlock) || this.invalid.contains((Object)Block.func_149634_a((Item)is.func_77973_b())) || i >= 36) continue;
                    Scaffold.swap(Scaffold.getItemSlot(Scaffold.mc.field_71439_g.field_71069_bz, is.func_77973_b()), 44);
                }
            }
            if (((Boolean)this.keepY.getValue()).booleanValue()) {
                if (!Scaffold.isMoving((EntityLivingBase)Scaffold.mc.field_71439_g) && Scaffold.mc.field_71474_y.field_74314_A.func_151470_d() || Scaffold.mc.field_71439_g.field_70124_G || Scaffold.mc.field_71439_g.field_70122_E) {
                    this.lastY = MathHelper.func_76128_c((double)Scaffold.mc.field_71439_g.field_70163_u);
                }
            } else {
                this.lastY = MathHelper.func_76128_c((double)Scaffold.mc.field_71439_g.field_70163_u);
            }
            BlockData blockData = null;
            double x = Scaffold.mc.field_71439_g.field_70165_t;
            double z = Scaffold.mc.field_71439_g.field_70161_v;
            double y = (Boolean)this.keepY.getValue() != false ? (double)this.lastY : Scaffold.mc.field_71439_g.field_70163_u;
            double forward = Scaffold.mc.field_71439_g.field_71158_b.field_192832_b;
            double strafe = Scaffold.mc.field_71439_g.field_71158_b.field_78902_a;
            float yaw = Scaffold.mc.field_71439_g.field_70177_z;
            if (!Scaffold.mc.field_71439_g.field_70123_F) {
                double[] coords = this.getExpandCoords(x, z, forward, strafe, yaw);
                x = coords[0];
                z = coords[1];
            }
            if (this.canPlace(Scaffold.mc.field_71441_e.func_180495_p(new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u - (double)(Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && (Boolean)this.down.getValue() != false ? 2 : 1), Scaffold.mc.field_71439_g.field_70161_v)).func_177230_c())) {
                x = Scaffold.mc.field_71439_g.field_70165_t;
                z = Scaffold.mc.field_71439_g.field_70161_v;
            }
            BlockPos blockBelow = new BlockPos(x, y - 1.0, z);
            if (Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && ((Boolean)this.down.getValue()).booleanValue()) {
                blockBelow = new BlockPos(x, y - 2.0, z);
            }
            this.pos = blockBelow;
            if (Scaffold.mc.field_71441_e.func_180495_p(blockBelow).func_177230_c() == Blocks.field_150350_a) {
                blockData = this.getBlockData2(blockBelow);
            }
            if (blockData != null) {
                if (this.getBlockCountHotbar() <= 0 || !((Boolean)this.bSwitch.getValue()).booleanValue() && !(Scaffold.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock)) {
                    return;
                }
                int heldItem = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
                if (((Boolean)this.bSwitch.getValue()).booleanValue()) {
                    for (int j = 0; j < 9; ++j) {
                        Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(j);
                        if (Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(j).func_190916_E() == 0 || !(Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(j).func_77973_b() instanceof ItemBlock) || this.invalid.contains((Object)((ItemBlock)Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(j).func_77973_b()).func_179223_d())) continue;
                        Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = j;
                        break;
                    }
                }
                if (this.mode.getValue() == Mode.Vanilla) {
                    if (Scaffold.mc.field_71474_y.field_74314_A.func_151470_d() && Scaffold.mc.field_71439_g.field_191988_bg == 0.0f && Scaffold.mc.field_71439_g.field_70702_br == 0.0f && !Scaffold.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                        if (!this.teleported && ((Boolean)this.center.getValue()).booleanValue()) {
                            this.teleported = true;
                            BlockPos pos = new BlockPos(Scaffold.mc.field_71439_g.field_70165_t, Scaffold.mc.field_71439_g.field_70163_u, Scaffold.mc.field_71439_g.field_70161_v);
                            Scaffold.mc.field_71439_g.func_70107_b((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o(), (double)pos.func_177952_p() + 0.5);
                        }
                        if (((Boolean)this.center.getValue()).booleanValue() && !this.teleported) {
                            return;
                        }
                        Scaffold.mc.field_71439_g.field_70181_x = 0.42f;
                        Scaffold.mc.field_71439_g.field_70179_y = 0.0;
                        Scaffold.mc.field_71439_g.field_70159_w = 0.0;
                        if (this.timerMotion.sleep(1500L)) {
                            Scaffold.mc.field_71439_g.field_70181_x = -0.28;
                        }
                    } else {
                        this.timerMotion.reset();
                        if (this.teleported && ((Boolean)this.center.getValue()).booleanValue()) {
                            this.teleported = false;
                        }
                    }
                }
                if (Scaffold.mc.field_71442_b.func_187099_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, blockData.position, blockData.face, new Vec3d((double)blockData.position.func_177958_n() + Math.random(), (double)blockData.position.func_177956_o() + Math.random(), (double)blockData.position.func_177952_p() + Math.random()), EnumHand.MAIN_HAND) != EnumActionResult.FAIL) {
                    if (((Boolean)this.swing.getValue()).booleanValue()) {
                        Scaffold.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                    } else {
                        Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    }
                }
                Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = heldItem;
            }
        }
    }

    public double[] getExpandCoords(double x, double z, double forward, double strafe, float YAW) {
        BlockPos underPos = new BlockPos(x, Scaffold.mc.field_71439_g.field_70163_u - (double)(Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && (Boolean)this.down.getValue() != false ? 2 : 1), z);
        Block underBlock = Scaffold.mc.field_71441_e.func_180495_p(underPos).func_177230_c();
        double xCalc = -999.0;
        double zCalc = -999.0;
        double dist = 0.0;
        double expandDist = ((Float)this.expand.getValue()).floatValue() * 2.0f;
        while (!this.canPlace(underBlock)) {
            if ((dist += 1.0) > expandDist) {
                dist = expandDist;
            }
            xCalc = x + (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc = z + (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if (dist == expandDist) break;
            underPos = new BlockPos(xCalc, Scaffold.mc.field_71439_g.field_70163_u - (double)(Scaffold.mc.field_71474_y.field_74311_E.func_151470_d() && (Boolean)this.down.getValue() != false ? 2 : 1), zCalc);
            underBlock = Scaffold.mc.field_71441_e.func_180495_p(underPos).func_177230_c();
        }
        return new double[]{xCalc, zCalc};
    }

    public boolean canPlace(Block block) {
        return (block instanceof BlockAir || block instanceof BlockLiquid) && Scaffold.mc.field_71441_e != null && Scaffold.mc.field_71439_g != null && this.pos != null && Scaffold.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(this.pos)).isEmpty();
    }

    private int getBlockCountHotbar() {
        int blockCount = 0;
        for (int i = 36; i < 45; ++i) {
            if (!Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d()) continue;
            ItemStack is = Scaffold.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            Item item = is.func_77973_b();
            if (!(is.func_77973_b() instanceof ItemBlock) || this.invalid.contains((Object)((ItemBlock)item).func_179223_d())) continue;
            blockCount += is.func_190916_E();
        }
        return blockCount;
    }

    private BlockData getBlockData2(BlockPos pos) {
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        BlockPos pos2 = pos.func_177982_a(-1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.func_177982_a(1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.func_177982_a(0, 0, 1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.func_177982_a(0, 0, -1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        pos.func_177982_a(-2, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos2.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos2.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        pos.func_177982_a(2, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos3.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos3.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        pos.func_177982_a(0, 0, 2);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos4.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos4.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        pos.func_177982_a(0, 0, -2);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos5.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos5.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos10 = pos.func_177982_a(0, -1, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos10.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos10.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos10.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos10.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos10.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos10.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos10.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos10.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos10.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos10.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos10.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos10.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos11 = pos10.func_177982_a(1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos11.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos11.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos11.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos11.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos11.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos11.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos11.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos11.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos11.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos11.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos11.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos11.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos12 = pos10.func_177982_a(-1, 0, 0);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos12.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos12.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos12.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos12.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos12.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos12.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos12.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos12.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos12.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos12.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos12.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos12.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos13 = pos10.func_177982_a(0, 0, 1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos13.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos13.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos13.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos13.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos13.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos13.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos13.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos13.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos13.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos13.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos13.func_177982_a(0, 0, -1)).func_177230_c())) {
            return new BlockData(pos13.func_177982_a(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos14 = pos10.func_177982_a(0, 0, -1);
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos14.func_177982_a(0, -1, 0)).func_177230_c())) {
            return new BlockData(pos14.func_177982_a(0, -1, 0), EnumFacing.UP);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos14.func_177982_a(0, 1, 0)).func_177230_c())) {
            return new BlockData(pos14.func_177982_a(0, 1, 0), EnumFacing.DOWN);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos14.func_177982_a(-1, 0, 0)).func_177230_c())) {
            return new BlockData(pos14.func_177982_a(-1, 0, 0), EnumFacing.EAST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos14.func_177982_a(1, 0, 0)).func_177230_c())) {
            return new BlockData(pos14.func_177982_a(1, 0, 0), EnumFacing.WEST);
        }
        if (!this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos14.func_177982_a(0, 0, 1)).func_177230_c())) {
            return new BlockData(pos14.func_177982_a(0, 0, 1), EnumFacing.NORTH);
        }
        return !this.invalid.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos14.func_177982_a(0, 0, -1)).func_177230_c()) ? new BlockData(pos14.func_177982_a(0, 0, -1), EnumFacing.SOUTH) : null;
    }

    public void place(BlockPos posI, EnumFacing face) {
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = posI.func_177982_a(0, -1, 0);
        } else if (face == EnumFacing.NORTH) {
            pos = posI.func_177982_a(0, 0, 1);
        } else if (face == EnumFacing.SOUTH) {
            pos = posI.func_177982_a(0, 0, -1);
        } else if (face == EnumFacing.EAST) {
            pos = posI.func_177982_a(-1, 0, 0);
        } else if (face == EnumFacing.WEST) {
            pos = posI.func_177982_a(1, 0, 0);
        }
        int oldSlot = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Scaffold.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (InventoryUtil.isNull(stack) || !(stack.func_77973_b() instanceof ItemBlock) || !Block.func_149634_a((Item)stack.func_77973_b()).func_176223_P().func_185913_b()) continue;
            newSlot = i;
            break;
        }
        if (newSlot != -1) {
            boolean crouched = false;
            if (!Scaffold.mc.field_71439_g.func_70093_af() && BlockUtil.blackList.contains((Object)Scaffold.mc.field_71441_e.func_180495_p(pos).func_177230_c())) {
                Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Scaffold.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                crouched = true;
            }
            if (!(Scaffold.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock)) {
                Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(newSlot));
                Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
                Scaffold.mc.field_71442_b.func_78765_e();
            }
            if (Scaffold.mc.field_71474_y.field_74314_A.func_151470_d()) {
                EntityPlayerSP var10000 = Scaffold.mc.field_71439_g;
                var10000.field_70159_w *= 0.3;
                var10000 = Scaffold.mc.field_71439_g;
                var10000.field_70179_y *= 0.3;
                Scaffold.mc.field_71439_g.func_70664_aZ();
                if (this.timer.passedMs(1500L)) {
                    Scaffold.mc.field_71439_g.field_70181_x = -0.28;
                    this.timer.reset();
                }
            }
            if (((Boolean)this.rotation.getValue()).booleanValue()) {
                float[] angle = MathUtil.calcAngle(Scaffold.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
                Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.func_180184_b((int)((int)angle[1]), (int)360), Scaffold.mc.field_71439_g.field_70122_E));
            }
            Scaffold.mc.field_71442_b.func_187099_a(Scaffold.mc.field_71439_g, Scaffold.mc.field_71441_e, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
            Scaffold.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
            Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
            Scaffold.mc.field_71442_b.func_78765_e();
            if (crouched) {
                Scaffold.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Scaffold.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
    }

    private static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }

    public static enum Mode {
        Vanilla,
        Normal;

    }
}

