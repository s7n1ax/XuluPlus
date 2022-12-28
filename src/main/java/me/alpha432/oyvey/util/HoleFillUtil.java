/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.alpha432.oyvey.util;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class HoleFillUtil {
    public static final List<Block> blackList = Arrays.asList(new Block[]{Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z, Blocks.field_150415_aT, Blocks.field_150381_bn});
    public static final List<Block> shulkerList = Arrays.asList(new Block[]{Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA});
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static Entity player = HoleFillUtil.mc.field_71439_g;
    public static FMLCommonHandler fmlHandler = FMLCommonHandler.instance();

    public static void placeBlockScaffold(BlockPos pos) {
        Vec3d eyesPos = new Vec3d(HoleFillUtil.player.field_70165_t, HoleFillUtil.player.field_70163_u + (double)player.func_70047_e(), HoleFillUtil.player.field_70161_v);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3d hitVec;
            BlockPos neighbor = pos.func_177972_a(side);
            EnumFacing side2 = side.func_176734_d();
            if (!HoleFillUtil.canBeClicked(neighbor) || !(eyesPos.func_72436_e(hitVec = new Vec3d((Vec3i)neighbor).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(side2.func_176730_m()).func_186678_a(0.5))) <= 18.0625)) continue;
            HoleFillUtil.faceVectorPacketInstant(hitVec);
            HoleFillUtil.processRightClickBlock(neighbor, side2, hitVec);
            HoleFillUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            HoleFillUtil.mc.field_71467_ac = 4;
            return;
        }
    }

    private static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = HoleFillUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{HoleFillUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - HoleFillUtil.mc.field_71439_g.field_70177_z)), HoleFillUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - HoleFillUtil.mc.field_71439_g.field_70125_A))};
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(HoleFillUtil.mc.field_71439_g.field_70165_t, HoleFillUtil.mc.field_71439_g.field_70163_u + (double)HoleFillUtil.mc.field_71439_g.func_70047_e(), HoleFillUtil.mc.field_71439_g.field_70161_v);
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = HoleFillUtil.getLegitRotations(vec);
        HoleFillUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], HoleFillUtil.mc.field_71439_g.field_70122_E));
    }

    static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
        HoleFillUtil.getPlayerController().func_187099_a(HoleFillUtil.mc.field_71439_g, HoleFillUtil.mc.field_71441_e, pos, side, hitVec, EnumHand.MAIN_HAND);
    }

    public static boolean canBeClicked(BlockPos pos) {
        return HoleFillUtil.getBlock(pos).func_176209_a(HoleFillUtil.getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return HoleFillUtil.getState(pos).func_177230_c();
    }

    private static PlayerControllerMP getPlayerController() {
        return Minecraft.func_71410_x().field_71442_b;
    }

    private static IBlockState getState(BlockPos pos) {
        return HoleFillUtil.mc.field_71441_e.func_180495_p(pos);
    }

    public static boolean checkForNeighbours(BlockPos blockPos) {
        if (!HoleFillUtil.hasNeighbour(blockPos)) {
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbour = blockPos.func_177972_a(side);
                if (!HoleFillUtil.hasNeighbour(neighbour)) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!HoleFillUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(HoleFillUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = HoleFillUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            return side;
        }
        return null;
    }

    public static boolean hasNeighbour(BlockPos blockPos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = blockPos.func_177972_a(side);
            if (HoleFillUtil.mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) continue;
            return true;
        }
        return false;
    }
}

