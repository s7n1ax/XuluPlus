/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityAgeable
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.entity.passive.EntityAmbientCreature
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityFireball
 *  net.minecraft.entity.projectile.EntityShulkerBullet
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.util;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.mixin.mixins.XuluProtect.XuluLivingBase;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class XuluRenderUtilBoss
implements Util {
    public static final Vec3d[] antiDropOffsetList = new Vec3d[]{new Vec3d(0.0, -2.0, 0.0)};
    public static final Vec3d[] platformOffsetList = new Vec3d[]{new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(1.0, -1.0, 0.0)};
    public static final Vec3d[] legOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0)};
    public static final Vec3d[] doubleLegOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, 0.0, 2.0)};
    public static final Vec3d[] OffsetList = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, 0.0)};
    public static final Vec3d[] headpiece = new Vec3d[]{new Vec3d(0.0, 2.0, 0.0)};
    public static final Vec3d[] offsetsNoHead = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0)};
    public static final Vec3d[] antiStepOffsetList = new Vec3d[]{new Vec3d(-1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0)};
    public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[]{new Vec3d(0.0, 3.0, 0.0)};

    public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
        if (packet) {
            XuluRenderUtilBoss.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
        } else {
            XuluRenderUtilBoss.mc.field_71442_b.func_78764_a((EntityPlayer)XuluRenderUtilBoss.mc.field_71439_g, entity);
        }
        if (swingArm) {
            XuluRenderUtilBoss.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)time);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
        return new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U).func_178787_e(XuluRenderUtilBoss.getInterpolatedAmount(entity, partialTicks));
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float partialTicks) {
        return XuluRenderUtilBoss.getInterpolatedPos(entity, partialTicks).func_178786_a(XuluRenderUtilBoss.mc.func_175598_ae().field_78725_b, XuluRenderUtilBoss.mc.func_175598_ae().field_78726_c, XuluRenderUtilBoss.mc.func_175598_ae().field_78723_d);
    }

    public static Vec3d getInterpolatedRenderPos(Vec3d vec) {
        return new Vec3d(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c).func_178786_a(XuluRenderUtilBoss.mc.func_175598_ae().field_78725_b, XuluRenderUtilBoss.mc.func_175598_ae().field_78726_c, XuluRenderUtilBoss.mc.func_175598_ae().field_78723_d);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return XuluRenderUtilBoss.getInterpolatedAmount(entity, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return XuluRenderUtilBoss.getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }

    public static boolean isPassive(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf)entity).func_70919_bu()) {
            return false;
        }
        if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid) {
            return true;
        }
        return entity instanceof EntityIronGolem && ((EntityIronGolem)entity).func_70643_av() == null;
    }

    public static boolean isSafe(Entity entity, int height, boolean floor, boolean face) {
        return XuluRenderUtilBoss.getUnsafeBlocks(entity, height, floor, face).size() == 0;
    }

    public static boolean stopSneaking(boolean isSneaking) {
        if (isSneaking && XuluRenderUtilBoss.mc.field_71439_g != null) {
            XuluRenderUtilBoss.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)XuluRenderUtilBoss.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return false;
    }

    public static boolean isSafe(Entity entity) {
        return XuluRenderUtilBoss.isSafe(entity, 0, false, true);
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u), Math.floor(player.field_70161_v));
    }

    public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor, boolean face) {
        return XuluRenderUtilBoss.getUnsafeBlocksFromVec3d(entity.func_174791_d(), height, floor, face);
    }

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).func_184734_db() || ((EntityPigZombie)entity).func_175457_ck()) {
                return true;
            }
        } else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).func_70919_bu() && !XuluRenderUtilBoss.mc.field_71439_g.equals((Object)((EntityWolf)entity).func_70902_q());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).func_70823_r();
            }
        }
        return XuluRenderUtilBoss.isHostileMob(entity);
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static boolean isProjectile(Entity entity) {
        return entity instanceof EntityShulkerBullet || entity instanceof EntityFireball;
    }

    public static boolean isVehicle(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isFriendlyMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.CREATURE, false) && !XuluRenderUtilBoss.isNeutralMob(entity) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || XuluRenderUtilBoss.isNeutralMob(entity) && !XuluRenderUtilBoss.isMobAggressive(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !XuluRenderUtilBoss.isNeutralMob(entity);
    }

    public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor, boolean face) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        for (Vec3d vector : XuluRenderUtilBoss.getOffsets(height, floor, face)) {
            BlockPos targetPos = new BlockPos(pos).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
            Block block = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInHole(Entity entity) {
        return XuluRenderUtilBoss.isBlockValid(new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return XuluRenderUtilBoss.isBedrockHole(blockPos) || XuluRenderUtilBoss.isObbyHole(blockPos) || XuluRenderUtilBoss.isBothHole(blockPos);
    }

    public static boolean isCrystalAtFeet(EntityEnderCrystal crystal, double range) {
        for (EntityPlayer player : XuluRenderUtilBoss.mc.field_71441_e.field_73010_i) {
            if (XuluRenderUtilBoss.mc.field_71439_g.func_70068_e((Entity)player) > range * range || OyVey.friendManager.isFriend(player)) continue;
            for (Vec3d vec : doubleLegOffsetList) {
                if (new BlockPos(player.func_174791_d()).func_177963_a(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c) != crystal.func_180425_c()) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150343_Z) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150357_h) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && (touchingState.func_177230_c() == Blocks.field_150357_h || touchingState.func_177230_c() == Blocks.field_150343_Z)) continue;
            return false;
        }
        return true;
    }

    public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor, boolean face) {
        List<Vec3d> list = XuluRenderUtilBoss.getUnsafeBlocks(entity, height, floor, face);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray((T[])array);
    }

    public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d pos, int height, boolean floor, boolean face) {
        List<Vec3d> list = XuluRenderUtilBoss.getUnsafeBlocksFromVec3d(pos, height, floor, face);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray((T[])array);
    }

    public static double getDst(Vec3d vec) {
        return XuluRenderUtilBoss.mc.field_71439_g.func_174791_d().func_72438_d(vec);
    }

    public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        return XuluRenderUtilBoss.getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop, face).size() == 0;
    }

    public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean noScaffoldExtend, boolean face) {
        return XuluRenderUtilBoss.getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace, noScaffoldExtend, face).size() == 0;
    }

    public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && XuluRenderUtilBoss.getUnsafeBlocks((Entity)player, 2, false, face).size() == 4) {
            vec3ds.addAll(XuluRenderUtilBoss.getUnsafeBlocks((Entity)player, 2, false, face));
        }
        for (int i = 0; i < XuluRenderUtilBoss.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face).length; ++i) {
            Vec3d vector = XuluRenderUtilBoss.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop, face)[i];
            BlockPos targetPos = new BlockPos(player.func_174791_d()).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
            Block block = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInWater(Entity entity) {
        if (entity == null) {
            return false;
        }
        double y = entity.field_70163_u + 0.01;
        for (int x = MathHelper.func_76128_c((double)entity.field_70165_t); x < MathHelper.func_76143_f((double)entity.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)entity.field_70161_v); z < MathHelper.func_76143_f((double)entity.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isDrivenByPlayer(Entity entityIn) {
        return XuluRenderUtilBoss.mc.field_71439_g != null && entityIn != null && entityIn.equals((Object)XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx());
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isAboveWater(Entity entity) {
        return XuluRenderUtilBoss.isAboveWater(entity, false);
    }

    public static boolean isAboveWater(Entity entity, boolean packet) {
        if (entity == null) {
            return false;
        }
        double y = entity.field_70163_u - (packet ? 0.03 : (XuluRenderUtilBoss.isPlayer(entity) ? 0.2 : 0.5));
        for (int x = MathHelper.func_76128_c((double)entity.field_70165_t); x < MathHelper.func_76143_f((double)entity.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)entity.field_70161_v); z < MathHelper.func_76143_f((double)entity.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.func_76128_c((double)y), z);
                if (!(XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean noScaffoldExtend, boolean face) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (extension == 1) {
            placeTargets.addAll(XuluRenderUtilBoss.targets(player.func_174791_d(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace, face));
        } else {
            int extend = 1;
            for (Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
                if (extend > extension) break;
                placeTargets.addAll(XuluRenderUtilBoss.targets(vec3d, !noScaffoldExtend, antiStep, legs, platform, antiDrop, raytrace, face));
                ++extend;
            }
        }
        ArrayList<Vec3d> removeList = new ArrayList<Vec3d>();
        for (Vec3d vec3d : placeTargets) {
            BlockPos pos = new BlockPos(vec3d);
            if (BlockUtil.isPositionPlaceable(pos, raytrace) != -1) continue;
            removeList.add(vec3d);
        }
        for (Vec3d vec3d : removeList) {
            placeTargets.remove((Object)vec3d);
        }
        return placeTargets;
    }

    public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace, boolean face) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, legOffsetList));
        }
        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiStepOffsetList));
        } else {
            List<Vec3d> vec3ds = XuluRenderUtilBoss.getUnsafeBlocksFromVec3d(vec3d, 2, false, face);
            if (vec3ds.size() == 4) {
                block5: for (Vec3d vector : vec3ds) {
                    BlockPos position = new BlockPos(vec3d).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
                        case 0: {
                            break block5;
                        }
                        case -1: 
                        case 1: 
                        case 2: {
                            continue block5;
                        }
                        case 3: {
                            placeTargets.add(vec3d.func_178787_e(vector));
                        }
                        default: {
                            continue block5;
                        }
                    }
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
        }
        if (!face) {
            ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
            offsets.add(new Vec3d(1.0, 1.0, 0.0));
            offsets.add(new Vec3d(0.0, 1.0, -1.0));
            offsets.add(new Vec3d(0.0, 1.0, 1.0));
            Vec3d[] array = new Vec3d[offsets.size()];
            placeTargets.removeAll(Arrays.asList(BlockUtil.convertVec3ds(vec3d, offsets.toArray((T[])array))));
        }
        return placeTargets;
    }

    public static List<Vec3d> getOffsetList(int y, boolean floor, boolean face) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        if (face) {
            offsets.add(new Vec3d(-1.0, (double)y, 0.0));
            offsets.add(new Vec3d(1.0, (double)y, 0.0));
            offsets.add(new Vec3d(0.0, (double)y, -1.0));
            offsets.add(new Vec3d(0.0, (double)y, 1.0));
        } else {
            offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        }
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getOffsets(int y, boolean floor, boolean face) {
        List<Vec3d> offsets = XuluRenderUtilBoss.getOffsetList(y, floor, face);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        List<Vec3d> offsets = XuluRenderUtilBoss.getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop, face);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean face) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(XuluRenderUtilBoss.getOffsetList(1, false, face));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(XuluRenderUtilBoss.getOffsetList(2, false, face));
        }
        if (legs) {
            offsets.addAll(XuluRenderUtilBoss.getOffsetList(0, false, face));
        }
        if (platform) {
            offsets.addAll(XuluRenderUtilBoss.getOffsetList(-1, false, face));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getHeightOffsets(int min, int max) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        for (int i = min; i <= max; ++i) {
            offsets.add(new Vec3d(0.0, (double)i, 0.0));
        }
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.func_174791_d(), 0));
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAlive(Entity entity) {
        return XuluRenderUtilBoss.isLiving(entity) && !entity.field_70128_L && ((EntityLivingBase)entity).func_110143_aJ() > 0.0f;
    }

    public static boolean isDead(Entity entity) {
        return !XuluRenderUtilBoss.isAlive(entity);
    }

    public static float getHealth(Entity entity) {
        if (XuluRenderUtilBoss.isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.func_110143_aJ() + livingBase.func_110139_bj();
        }
        return 0.0f;
    }

    public static float getHealth(Entity entity, boolean absorption) {
        if (XuluRenderUtilBoss.isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.func_110143_aJ() + (absorption ? livingBase.func_110139_bj() : 0.0f);
        }
        return 0.0f;
    }

    public static boolean canEntityFeetBeSeen(Entity entityIn) {
        return XuluRenderUtilBoss.mc.field_71441_e.func_147447_a(new Vec3d(XuluRenderUtilBoss.mc.field_71439_g.field_70165_t, XuluRenderUtilBoss.mc.field_71439_g.field_70165_t + (double)XuluRenderUtilBoss.mc.field_71439_g.func_70047_e(), XuluRenderUtilBoss.mc.field_71439_g.field_70161_v), new Vec3d(entityIn.field_70165_t, entityIn.field_70163_u, entityIn.field_70161_v), false, true, false) == null;
    }

    public static boolean isntValid(Entity entity, double range) {
        return entity == null || XuluRenderUtilBoss.isDead(entity) || entity.equals((Object)XuluRenderUtilBoss.mc.field_71439_g) || entity instanceof EntityPlayer && OyVey.friendManager.isFriend(entity.func_70005_c_()) || XuluRenderUtilBoss.mc.field_71439_g.func_70068_e(entity) > MathUtil.square(range);
    }

    public static boolean isValid(Entity entity, double range) {
        return !XuluRenderUtilBoss.isntValid(entity, range);
    }

    public static boolean holdingWeapon(EntityPlayer player) {
        return player.func_184614_ca().func_77973_b() instanceof ItemSword || player.func_184614_ca().func_77973_b() instanceof ItemAxe;
    }

    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (XuluRenderUtilBoss.mc.field_71439_g.func_70644_a(Objects.requireNonNull(Potion.func_188412_a((int)1)))) {
            maxModifier *= 1.0 + 0.2 * (double)(Objects.requireNonNull(XuluRenderUtilBoss.mc.field_71439_g.func_70660_b(Objects.requireNonNull(Potion.func_188412_a((int)1)))).func_76458_c() + 1);
        }
        return maxModifier;
    }

    public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
        if (entity != null) {
            entity.field_70159_w *= multiplier;
            entity.field_70179_y *= multiplier;
        }
    }

    public static boolean isEntityMoving(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return XuluRenderUtilBoss.mc.field_71474_y.field_74351_w.func_151470_d() || XuluRenderUtilBoss.mc.field_71474_y.field_74368_y.func_151470_d() || XuluRenderUtilBoss.mc.field_71474_y.field_74370_x.func_151470_d() || XuluRenderUtilBoss.mc.field_71474_y.field_74366_z.func_151470_d();
        }
        return entity.field_70159_w != 0.0 || entity.field_70181_x != 0.0 || entity.field_70179_y != 0.0;
    }

    public static boolean movementKey() {
        return XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_187255_c || XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_187258_f || XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_187257_e || XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_187256_d || XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_78901_c || XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_78899_d;
    }

    public static double getEntitySpeed(Entity entity) {
        if (entity != null) {
            double distTraveledLastTickX = entity.field_70165_t - entity.field_70169_q;
            double distTraveledLastTickZ = entity.field_70161_v - entity.field_70166_s;
            double speed = MathHelper.func_76133_a((double)(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ));
            return speed * 20.0;
        }
        return 0.0;
    }

    public static boolean holding32k(EntityPlayer player) {
        return XuluRenderUtilBoss.is32k(player.func_184614_ca());
    }

    public static boolean is32k(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.func_77978_p() == null) {
            return false;
        }
        NBTTagList enchants = (NBTTagList)stack.func_77978_p().func_74781_a("ench");
        if (enchants == null) {
            return false;
        }
        for (int i = 0; i < enchants.func_74745_c(); ++i) {
            NBTTagCompound enchant = enchants.func_150305_b(i);
            if (enchant.func_74762_e("id") != 16) continue;
            int lvl = enchant.func_74762_e("lvl");
            if (lvl < 42) break;
            return true;
        }
        return false;
    }

    public static boolean simpleIs32k(ItemStack stack) {
        return EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185302_k, (ItemStack)stack) >= 1000;
    }

    public static void moveEntityStrafe(double speed, Entity entity) {
        if (entity != null) {
            MovementInput movementInput = XuluRenderUtilBoss.mc.field_71439_g.field_71158_b;
            double forward = movementInput.field_192832_b;
            double strafe = movementInput.field_78902_a;
            float yaw = XuluRenderUtilBoss.mc.field_71439_g.field_70177_z;
            if (forward == 0.0 && strafe == 0.0) {
                entity.field_70159_w = 0.0;
                entity.field_70179_y = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                entity.field_70159_w = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                entity.field_70179_y = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }

    public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
        return !shouldCheck || XuluRenderUtilBoss.mc.field_71439_g.func_70685_l(entity);
    }

    public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
        Color color = new Color((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f, (float)alpha / 255.0f);
        if (entity instanceof EntityPlayer) {
            if (colorFriends && OyVey.friendManager.isFriend((EntityPlayer)entity)) {
                color = new Color(0.33333334f, 1.0f, 1.0f, (float)alpha / 255.0f);
            }
            Killaura killaura = OyVey.moduleManager.getModuleByClass(Killaura.class);
            if (killaura.info.getValue().booleanValue() && Killaura.target != null && Killaura.target.equals((Object)entity)) {
                color = new Color(1.0f, 0.0f, 0.0f, (float)alpha / 255.0f);
            }
        }
        return color;
    }

    public static boolean isMoving() {
        return (double)XuluRenderUtilBoss.mc.field_71439_g.field_191988_bg != 0.0 || (double)XuluRenderUtilBoss.mc.field_71439_g.field_70702_br != 0.0;
    }

    public static EntityPlayer getClosestEnemy(double distance) {
        EntityPlayer closest = null;
        for (EntityPlayer player : XuluRenderUtilBoss.mc.field_71441_e.field_73010_i) {
            if (XuluRenderUtilBoss.isntValid((Entity)player, distance)) continue;
            if (closest == null) {
                closest = player;
                continue;
            }
            if (!(XuluRenderUtilBoss.mc.field_71439_g.func_70068_e((Entity)player) < XuluRenderUtilBoss.mc.field_71439_g.func_70068_e((Entity)closest))) continue;
            closest = player;
        }
        return closest;
    }

    public static boolean checkCollide() {
        if (XuluRenderUtilBoss.mc.field_71439_g.func_70093_af()) {
            return false;
        }
        if (XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx() != null && XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx().field_70143_R >= 3.0f) {
            return false;
        }
        return !(XuluRenderUtilBoss.mc.field_71439_g.field_70143_R >= 3.0f);
    }

    public static boolean isInLiquid() {
        if (XuluRenderUtilBoss.mc.field_71439_g.field_70143_R >= 3.0f) {
            return false;
        }
        boolean inLiquid = false;
        AxisAlignedBB bb = XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx() != null ? XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx().func_174813_aQ() : XuluRenderUtilBoss.mc.field_71439_g.func_174813_aQ();
        int y = (int)bb.field_72338_b;
        for (int x = MathHelper.func_76128_c((double)bb.field_72340_a); x < MathHelper.func_76128_c((double)bb.field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c((double)bb.field_72339_c); z < MathHelper.func_76128_c((double)bb.field_72334_f) + 1; ++z) {
                Block block = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                if (block instanceof BlockAir) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static boolean isOnLiquid(double offset) {
        if (XuluRenderUtilBoss.mc.field_71439_g.field_70143_R >= 3.0f) {
            return false;
        }
        AxisAlignedBB bb = XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx() != null ? XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0, 0.0, 0.0).func_72317_d(0.0, -offset, 0.0) : XuluRenderUtilBoss.mc.field_71439_g.func_174813_aQ().func_191195_a(0.0, 0.0, 0.0).func_72317_d(0.0, -offset, 0.0);
        boolean onLiquid = false;
        int y = (int)bb.field_72338_b;
        for (int x = MathHelper.func_76128_c((double)bb.field_72340_a); x < MathHelper.func_76128_c((double)(bb.field_72336_d + 1.0)); ++x) {
            for (int z = MathHelper.func_76128_c((double)bb.field_72339_c); z < MathHelper.func_76128_c((double)(bb.field_72334_f + 1.0)); ++z) {
                Block block = XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                if (block == Blocks.field_150350_a) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static boolean isAboveLiquid(Entity entity) {
        if (entity == null) {
            return false;
        }
        double n = entity.field_70163_u + 0.01;
        for (int i = MathHelper.func_76128_c((double)entity.field_70165_t); i < MathHelper.func_76143_f((double)entity.field_70165_t); ++i) {
            for (int j = MathHelper.func_76128_c((double)entity.field_70161_v); j < MathHelper.func_76143_f((double)entity.field_70161_v); ++j) {
                if (!(XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(new BlockPos(i, (int)n, j)).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos(XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx() != null ? XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx().field_70165_t : XuluRenderUtilBoss.mc.field_71439_g.field_70165_t, XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx() != null ? XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx().field_70163_u : XuluRenderUtilBoss.mc.field_71439_g.field_70163_u, XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx() != null ? XuluRenderUtilBoss.mc.field_71439_g.func_184187_bx().field_70161_v : XuluRenderUtilBoss.mc.field_71439_g.field_70161_v);
    }

    public static boolean checkForLiquid(Entity entity, boolean b) {
        if (entity == null) {
            return false;
        }
        double posY = entity.field_70163_u;
        double n = b ? 0.03 : (entity instanceof EntityPlayer ? 0.2 : 0.5);
        double n2 = posY - n;
        for (int i = MathHelper.func_76128_c((double)entity.field_70165_t); i < MathHelper.func_76143_f((double)entity.field_70165_t); ++i) {
            for (int j = MathHelper.func_76128_c((double)entity.field_70161_v); j < MathHelper.func_76143_f((double)entity.field_70161_v); ++j) {
                if (!(XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(new BlockPos(i, MathHelper.func_76128_c((double)n2), j)).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isOnLiquid() {
        double y = XuluRenderUtilBoss.mc.field_71439_g.field_70163_u - 0.03;
        for (int x = MathHelper.func_76128_c((double)XuluRenderUtilBoss.mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f((double)XuluRenderUtilBoss.mc.field_71439_g.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)XuluRenderUtilBoss.mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f((double)XuluRenderUtilBoss.mc.field_71439_g.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.func_76128_c((double)y), z);
                if (!(XuluRenderUtilBoss.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static double[] forward(double speed) {
        float forward = XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_192832_b;
        float side = XuluRenderUtilBoss.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = XuluRenderUtilBoss.mc.field_71439_g.field_70126_B + (XuluRenderUtilBoss.mc.field_71439_g.field_70177_z - XuluRenderUtilBoss.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static Map<String, Integer> getTextRadarPlayers() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        StringBuilder healthSB = new StringBuilder();
        StringBuilder distanceSB = new StringBuilder();
        for (EntityPlayer player : XuluRenderUtilBoss.mc.field_71441_e.field_73010_i) {
            int hpRaw = (int)XuluRenderUtilBoss.getHealth((Entity)player);
            String hp = dfHealth.format(hpRaw);
            healthSB.append("\u00a7");
            if (hpRaw >= 20) {
                healthSB.append("a");
            } else if (hpRaw >= 10) {
                healthSB.append("e");
            } else if (hpRaw >= 5) {
                healthSB.append("6");
            } else {
                healthSB.append("c");
            }
            healthSB.append(hp);
            int distanceInt = (int)XuluRenderUtilBoss.mc.field_71439_g.func_70032_d((Entity)player);
            String distance = dfDistance.format(distanceInt);
            distanceSB.append("\u00a7");
            if (distanceInt >= 25) {
                distanceSB.append("a");
            } else if (distanceInt > 10) {
                distanceSB.append("6");
            } else if (distanceInt >= 50) {
                distanceSB.append("7");
            } else {
                distanceSB.append("c");
            }
            distanceSB.append(distance);
            output.put(healthSB.toString() + " " + (OyVey.friendManager.isFriend(player) ? "\u00a7b" : "\u00a7r") + player.func_70005_c_() + " " + distanceSB.toString() + " \u00a7f" + OyVey.totemPopManager.getTotemPopString(player) + OyVey.potionManager.getTextRadarPotion(player), (int)XuluRenderUtilBoss.mc.field_71439_g.func_70032_d((Entity)player));
            healthSB.setLength(0);
            distanceSB.setLength(0);
        }
        if (!output.isEmpty()) {
            output = MathUtil.sortByValue(output, false);
        }
        return output;
    }

    public static void swingArmNoPacket(EnumHand hand, EntityLivingBase entity) {
        ItemStack stack = entity.func_184586_b(hand);
        if (!stack.func_190926_b() && stack.func_77973_b().onEntitySwing(entity, stack)) {
            return;
        }
        if (!entity.field_82175_bq || entity.field_110158_av >= ((XuluLivingBase)entity).getArmSwingAnimationEnd() / 2 || entity.field_110158_av < 0) {
            entity.field_110158_av = -1;
            entity.field_82175_bq = true;
            entity.field_184622_au = hand;
        }
    }

    public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
        return entity.field_70163_u >= (double)blockPos.func_177956_o();
    }
}

