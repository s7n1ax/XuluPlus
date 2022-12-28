/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.alpha432.oyvey.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BurrowUtil
implements Util {
    public static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = BurrowUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = BurrowUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BurrowUtil.mc.field_71439_g.func_70093_af()) {
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BurrowUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BurrowUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            BurrowUtil.faceVector(hitVec, true);
        }
        BurrowUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BurrowUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BurrowUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!BurrowUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BurrowUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BurrowUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = BurrowUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BurrowUtil.mc.field_71439_g.field_70165_t, BurrowUtil.mc.field_71439_g.field_70163_u + (double)BurrowUtil.mc.field_71439_g.func_70047_e(), BurrowUtil.mc.field_71439_g.field_70161_v);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = BurrowUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BurrowUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - BurrowUtil.mc.field_71439_g.field_70177_z)), BurrowUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - BurrowUtil.mc.field_71439_g.field_70125_A))};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = BurrowUtil.getLegitRotations(vec);
        BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float)MathHelper.func_180184_b((int)((int)rotations[1]), (int)360) : rotations[1], BurrowUtil.mc.field_71439_g.field_70122_E));
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f2 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f3 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        } else {
            BurrowUtil.mc.field_71442_b.func_187099_a(BurrowUtil.mc.field_71439_g, BurrowUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        BurrowUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BurrowUtil.mc.field_71467_ac = 4;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (clazz.isInstance((Object)stack.func_77973_b())) {
                return i;
            }
            if (!(stack.func_77973_b() instanceof ItemBlock) || !clazz.isInstance((Object)(block = ((ItemBlock)stack.func_77973_b()).func_179223_d()))) continue;
            return i;
        }
        return -1;
    }

    public static void switchToSlot(int slot) {
        BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
        BurrowUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        BurrowUtil.mc.field_71442_b.func_78765_e();
    }
}

