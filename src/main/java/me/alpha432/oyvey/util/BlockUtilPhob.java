/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.BlockWorkbench
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package me.alpha432.oyvey.util;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RotationUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUtilPhob
implements Util {
    public static final List<Block> blackList = Arrays.asList(new Block[]{Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn});
    public static final List<Block> shulkerList = Arrays.asList(new Block[]{Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA});
    public static List<Block> unSolidBlocks = Arrays.asList(new Block[]{Blocks.field_150356_k, Blocks.field_150457_bL, Blocks.field_150433_aE, Blocks.field_150404_cg, Blocks.field_185764_cQ, Blocks.field_150465_bP, Blocks.field_150457_bL, Blocks.field_150473_bD, Blocks.field_150479_bC, Blocks.field_150471_bO, Blocks.field_150442_at, Blocks.field_150430_aB, Blocks.field_150468_ap, Blocks.field_150441_bU, Blocks.field_150455_bV, Blocks.field_150413_aR, Blocks.field_150416_aS, Blocks.field_150437_az, Blocks.field_150429_aA, Blocks.field_150488_af, Blocks.field_150350_a, Blocks.field_150427_aO, Blocks.field_150384_bq, Blocks.field_150355_j, Blocks.field_150358_i, Blocks.field_150353_l, Blocks.field_150356_k, Blocks.field_150345_g, Blocks.field_150328_O, Blocks.field_150327_N, Blocks.field_150338_P, Blocks.field_150337_Q, Blocks.field_150464_aj, Blocks.field_150459_bM, Blocks.field_150469_bN, Blocks.field_185773_cZ, Blocks.field_150436_aH, Blocks.field_150393_bb, Blocks.field_150394_bc, Blocks.field_150392_bi, Blocks.field_150388_bm, Blocks.field_150375_by, Blocks.field_185766_cS, Blocks.field_185765_cR, Blocks.field_150329_H, Blocks.field_150330_I, Blocks.field_150395_bd, Blocks.field_150480_ab, Blocks.field_150448_aq, Blocks.field_150408_cc, Blocks.field_150319_E, Blocks.field_150318_D, Blocks.field_150478_aa});
    private static BlockPos _currBlock;
    private static boolean _started;

    public static List<BlockPos> getBlockSphere(float breakRange, Class<BlockWorkbench> clazz) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)BlockUtil.mc.field_71439_g), breakRange, (int)breakRange, false, true, 0).stream().filter(pos -> clazz.isInstance((Object)BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c())).collect(Collectors.toList()));
        return positions;
    }

    public static EnumFacing getFacing(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            RayTraceResult rayTraceResult = BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5 + (double)facing.func_176730_m().func_177958_n() / 2.0, (double)pos.func_177956_o() + 0.5 + (double)facing.func_176730_m().func_177956_o() / 2.0, (double)pos.func_177952_p() + 0.5 + (double)facing.func_176730_m().func_177952_p() / 2.0), false, true, false);
            if (rayTraceResult != null && (rayTraceResult.field_72313_a != RayTraceResult.Type.BLOCK || !rayTraceResult.func_178782_a().equals((Object)pos))) continue;
            return facing;
        }
        if ((double)pos.func_177956_o() > BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (BlockUtil.mc.field_71441_e == null || pos == null) {
            return facings;
        }
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.func_177972_a(side);
            IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour);
            if (!blockState.func_177230_c().func_176209_a(blockState, false) || blockState.func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static EnumFacing getRayTraceFacing(BlockPos pos) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177958_n() - 0.5, (double)pos.func_177958_n() + 0.5));
        if (result == null || result.field_178784_b == null) {
            return EnumFacing.UP;
        }
        return result.field_178784_b;
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
        return BlockUtil.isPositionPlaceable(pos, rayTrace, true);
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace, boolean entityCheck) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return 0;
        }
        if (!BlockUtil.rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity instanceof EntityExpBottle) continue;
                return 1;
            }
        }
        for (EnumFacing side : BlockUtil.getPossibleSides(pos)) {
            if (!BlockUtil.canBeClicked(pos.func_177972_a(side))) continue;
            return 3;
        }
        return 2;
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
    }

    public static void rightClickBed(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble yaw, AtomicDouble pitch, AtomicBoolean rotating, boolean packet) {
        Vec3d posVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5);
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), posVec);
        EnumFacing face = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        RotationUtil.getEyesPos();
        if (rotate) {
            float[] rotations = RotationUtil.getLegitRotations(posVec);
            yaw.set((double)rotations[0]);
            pitch.set((double)rotations[1]);
            rotating.set(true);
        }
        BlockUtil.rightClickBlock(pos, posVec, hand, face, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(hand);
        BlockUtil.mc.field_71467_ac = 4;
    }

    public static void rightClickBlockLegit(BlockPos pos, float range, boolean rotate, EnumHand hand, AtomicDouble Yaw2, AtomicDouble Pitch, AtomicBoolean rotating, boolean packet) {
        Vec3d eyesPos = RotationUtil.getEyesPos();
        Vec3d posVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5);
        double distanceSqPosVec = eyesPos.func_72436_e(posVec);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec = posVec.func_178787_e(new Vec3d(side.func_176730_m()).func_186678_a(0.5));
            double distanceSqHitVec = eyesPos.func_72436_e(hitVec);
            if (distanceSqHitVec > MathUtil.square(range) || distanceSqHitVec >= distanceSqPosVec || BlockUtil.mc.field_71441_e.func_147447_a(eyesPos, hitVec, false, true, false) != null) continue;
            if (rotate) {
                float[] rotations = RotationUtil.getLegitRotations(hitVec);
                Yaw2.set((double)rotations[0]);
                Pitch.set((double)rotations[1]);
                rotating.set(true);
            }
            BlockUtil.rightClickBlock(pos, hitVec, hand, side, packet);
            BlockUtil.mc.field_71439_g.func_184609_a(hand);
            BlockUtil.mc.field_71467_ac = 4;
            break;
        }
    }

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (blackList.contains((Object)neighbourBlock) || shulkerList.contains((Object)neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }

    public static boolean placeBlockNotRetarded(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean altRotate) {
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (blackList.contains((Object)neighbourBlock) || shulkerList.contains((Object)neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
        }
        if (rotate) {
            RotationUtil.faceVector(altRotate ? new Vec3d((Vec3i)pos) : hitVec, true);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        return true;
    }

    public static boolean placeBlockSmartRotate(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BlockUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (blackList.contains((Object)neighbourBlock) || shulkerList.contains((Object)neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
        if (rotate) {
            OyVey.rotationManager.lookAtVec3d(hitVec);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }

    public static void placeBlockStopSneaking(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = BlockUtil.placeBlockSmartRotate(pos, hand, rotate, packet, isSneaking);
        if (!isSneaking && sneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
        return new Vec3d[]{new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0, vec3d.field_72449_c), new Vec3d(vec3d.field_72450_a != 0.0 ? vec3d.field_72450_a * 2.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a != 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c * 2.0), new Vec3d(vec3d.field_72450_a == 0.0 ? vec3d.field_72450_a + 1.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a == 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c + 1.0), new Vec3d(vec3d.field_72450_a == 0.0 ? vec3d.field_72450_a - 1.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a == 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c - 1.0), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0, vec3d.field_72449_c)};
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(BlockUtil::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.func_177958_n();
        int cy = pos.func_177956_o();
        int cz = pos.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static List<BlockPos> getDisc(BlockPos pos, float r) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.func_177958_n();
        int cy = pos.func_177956_o();
        int cz = pos.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < (double)(r * r)) {
                    BlockPos position = new BlockPos(x, cy, z);
                    circleblocks.add(position);
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            return (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }

    public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)BlockUtil.mc.field_71439_g), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> BlockUtil.canPlaceCrystal(pos, specialEntityCheck, oneDot15)).collect(Collectors.toList()));
        return positions;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean oneDot15) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                return false;
            }
            if (!oneDot15 && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a || BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
                return false;
            }
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                if (entity.field_70128_L || specialEntityCheck && entity instanceof EntityEnderCrystal) continue;
                return false;
            }
            if (!oneDot15) {
                for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity.field_70128_L || specialEntityCheck && entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static boolean canBeClicked(BlockPos pos) {
        return BlockUtilPhob.getBlock(pos).func_176209_a(BlockUtilPhob.getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return BlockUtilPhob.getState(pos).func_177230_c();
    }

    private static IBlockState getState(BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos);
    }

    public static boolean isBlockAboveEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u + 2.0, entity.field_70161_v);
            return BlockUtil.isBlockSolid(pos);
        }
        return false;
    }

    public static void debugPos(String message, BlockPos pos) {
        Command.sendMessage(message + pos.func_177958_n() + "x, " + pos.func_177956_o() + "y, " + pos.func_177952_p() + "z");
    }

    public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing, boolean exactHand, boolean silent) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        EnumFacing facing = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        int old = BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        int crystal = InventoryUtil.getItemHotbar(Items.field_185158_cP);
        if (hand == EnumHand.MAIN_HAND && silent && crystal != -1 && crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(crystal));
        }
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (hand == EnumHand.MAIN_HAND && silent && crystal != -1 && crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(old));
        }
        if (swing) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }

    public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
        BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }

    public static Vec3d posToVec3d(BlockPos pos) {
        return new Vec3d((Vec3i)pos);
    }

    public static BlockPos vec3dToPos(Vec3d vec3d) {
        return new BlockPos(vec3d);
    }

    public static Boolean isPosInFov(BlockPos pos) {
        int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0 && (double)pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c < 0.0) {
            return false;
        }
        if (dirnumber == 1 && (double)pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a > 0.0) {
            return false;
        }
        if (dirnumber == 2 && (double)pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c > 0.0) {
            return false;
        }
        return dirnumber != 3 || !((double)pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a < 0.0);
    }

    public static boolean isBlockBelowEntitySolid(Entity entity) {
        if (entity != null) {
            BlockPos pos = new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v);
            return BlockUtil.isBlockSolid(pos);
        }
        return false;
    }

    public static boolean isBlockSolid(BlockPos pos) {
        return !BlockUtil.isBlockUnSolid(pos);
    }

    public static boolean isBlockUnSolid(BlockPos pos) {
        return BlockUtil.isBlockUnSolid(BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c());
    }

    public static boolean isBlockUnSolid(Block block) {
        return unSolidBlocks.contains((Object)block);
    }

    public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
        Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.func_178787_e(input[i]);
        }
        return output;
    }

    public static Vec3d[] convertVec3ds(EntityPlayer entity, Vec3d[] input) {
        return BlockUtil.convertVec3ds(entity.func_174791_d(), input);
    }

    public static boolean canBreak(BlockPos pos) {
        IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
        Block block = blockState.func_177230_c();
        return block.func_176195_g(blockState, (World)BlockUtil.mc.field_71441_e, pos) != -1.0f;
    }

    public static boolean isValidBlock(BlockPos pos) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return !(block instanceof BlockLiquid) && block.func_149688_o(null) != Material.field_151579_a;
    }

    public static boolean isScaffoldPos(BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_175623_d(pos) || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150431_aC || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150329_H || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
        return !shouldCheck || BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + height), (double)pos.func_177952_p()), false, true, false) == null;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
        return BlockUtil.rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }

    public static boolean rayTracePlaceCheck(BlockPos pos) {
        return BlockUtil.rayTracePlaceCheck(pos, true);
    }

    public static boolean isInHole() {
        BlockPos blockPos = new BlockPos(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u, BlockUtil.mc.field_71439_g.field_70161_v);
        IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(blockPos);
        return BlockUtil.isBlockValid(blockState, blockPos);
    }

    public static double getNearestBlockBelow() {
        for (double y = BlockUtil.mc.field_71439_g.field_70163_u; y > 0.0; y -= 0.001) {
            if (BlockUtil.mc.field_71441_e.func_180495_p(new BlockPos(BlockUtil.mc.field_71439_g.field_70165_t, y, BlockUtil.mc.field_71439_g.field_70161_v)).func_177230_c() instanceof BlockSlab || BlockUtil.mc.field_71441_e.func_180495_p(new BlockPos(BlockUtil.mc.field_71439_g.field_70165_t, y, BlockUtil.mc.field_71439_g.field_70161_v)).func_177230_c().func_176223_P().func_185890_d((IBlockAccess)BlockUtil.mc.field_71441_e, new BlockPos(0, 0, 0)) == null) continue;
            return y;
        }
        return -1.0;
    }

    public static boolean isBlockValid(IBlockState blockState, BlockPos blockPos) {
        if (blockState.func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        if (BlockUtil.mc.field_71439_g.func_174818_b(blockPos) < 1.0) {
            return false;
        }
        if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        return BlockUtil.isBedrockHole(blockPos) || BlockUtil.isObbyHole(blockPos) || BlockUtil.isBothHole(blockPos) || BlockUtil.isElseHole(blockPos);
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        for (BlockPos pos : BlockUtil.getTouchingBlocks(blockPos)) {
            IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150343_Z) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        for (BlockPos pos : BlockUtil.getTouchingBlocks(blockPos)) {
            IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150357_h) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        for (BlockPos pos : BlockUtil.getTouchingBlocks(blockPos)) {
            IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && (touchingState.func_177230_c() == Blocks.field_150357_h || touchingState.func_177230_c() == Blocks.field_150343_Z)) continue;
            return false;
        }
        return true;
    }

    public static boolean isElseHole(BlockPos blockPos) {
        for (BlockPos pos : BlockUtil.getTouchingBlocks(blockPos)) {
            IBlockState touchingState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_185913_b()) continue;
            return false;
        }
        return true;
    }

    public static BlockPos[] getTouchingBlocks(BlockPos blockPos) {
        return new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
    }

    public static void SetCurrentBlock(BlockPos block) {
        _currBlock = block;
        _started = false;
    }

    public static BlockPos GetCurrBlock() {
        return _currBlock;
    }

    public static boolean GetState() {
        return _currBlock != null && BlockUtilPhob.IsDoneBreaking(BlockUtilPhob.mc.field_71441_e.func_180495_p(_currBlock));
    }

    private static boolean IsDoneBreaking(IBlockState blockState) {
        return blockState.func_177230_c() == Blocks.field_150357_h || blockState.func_177230_c() == Blocks.field_150350_a || blockState.func_177230_c() instanceof BlockLiquid;
    }

    public static boolean Update(float range, boolean rayTrace) {
        if (_currBlock == null) {
            return false;
        }
        IBlockState state = BlockUtilPhob.mc.field_71441_e.func_180495_p(_currBlock);
        if (!BlockUtilPhob.IsDoneBreaking(state) && !(BlockUtilPhob.mc.field_71439_g.func_174818_b(_currBlock) > Math.pow(range, range))) {
            RayTraceResult result;
            BlockUtilPhob.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            EnumFacing facing = EnumFacing.UP;
            if (rayTrace && (result = BlockUtilPhob.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtilPhob.mc.field_71439_g.field_70165_t, BlockUtilPhob.mc.field_71439_g.field_70163_u + (double)BlockUtilPhob.mc.field_71439_g.func_70047_e(), BlockUtilPhob.mc.field_71439_g.field_70161_v), new Vec3d((double)_currBlock.func_177958_n() + 0.5, (double)_currBlock.func_177956_o() - 0.5, (double)_currBlock.func_177952_p() + 0.5))) != null && result.field_178784_b != null) {
                facing = result.field_178784_b;
            }
            if (!_started) {
                _started = true;
                BlockUtilPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, _currBlock, facing));
                BlockUtilPhob.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, _currBlock, facing));
            } else {
                BlockUtilPhob.mc.field_71442_b.func_180512_c(_currBlock, facing);
            }
            return true;
        }
        _currBlock = null;
        return false;
    }

    public static void placeBlock(BlockPos pos, EnumFacing side, boolean packet) {
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        if (!Util.mc.field_71439_g.func_70093_af()) {
            Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Util.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        if (packet) {
            Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, side, EnumHand.MAIN_HAND, (float)hitVec.field_72450_a - (float)pos.func_177958_n(), (float)hitVec.field_72448_b - (float)pos.func_177956_o(), (float)hitVec.field_72449_c - (float)pos.func_177952_p()));
        } else {
            Util.mc.field_71442_b.func_187099_a(Util.mc.field_71439_g, Util.mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        }
        Util.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
    }
}

