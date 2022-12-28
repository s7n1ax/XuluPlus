/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$LeftClickBlock
 *  net.minecraftforge.event.world.BlockEvent$BreakEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.Flex;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.player.Speedmine;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockTweaks
extends Module {
    private static BlockTweaks INSTANCE = new BlockTweaks();
    public Setting<Boolean> autoTool = this.register(new Setting<Boolean>("AutoTool", false));
    public Setting<Boolean> autoWeapon = this.register(new Setting<Boolean>("AutoWeapon", false));
    public Setting<Boolean> noFriendAttack = this.register(new Setting<Boolean>("BetterFriendProtect", false));
    public Setting<Boolean> noBlock = this.register(new Setting<Boolean>("NoHitboxBlock", true));
    public Setting<Boolean> noGhost = this.register(new Setting<Boolean>("GhostBlockFix", false));
    public Setting<Boolean> destroy = this.register(new Setting<Object>("destruction", Boolean.valueOf(false), v -> this.noGhost.getValue()));
    private int lastHotbarSlot = -1;
    private int currentTargetSlot = -1;
    private boolean switched;

    public BlockTweaks() {
        super("-BlockTweak", "Some tweaks for blocks.", Module.Category.Flex, true, false, false);
        this.setInstance();
    }

    public static BlockTweaks getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new BlockTweaks();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        if (this.switched) {
            this.equip(this.lastHotbarSlot, false);
        }
        this.lastHotbarSlot = -1;
        this.currentTargetSlot = -1;
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (BlockTweaks.fullNullCheck() || !this.noGhost.getValue().booleanValue() || !this.destroy.getValue().booleanValue()) {
            return;
        }
        if (!(BlockTweaks.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemBlock)) {
            BlockPos pos = BlockTweaks.mc.field_71439_g.func_180425_c();
            this.removeGlitchBlocks(pos);
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (this.autoTool.getValue().booleanValue() && (Speedmine.getInstance().mode.getValue() != Speedmine.Mode.PACKET || Speedmine.getInstance().isOff() || !Speedmine.getInstance().tweaks.getValue().booleanValue()) && !BlockTweaks.fullNullCheck()) {
            event.getPos();
            this.equipBestTool(BlockTweaks.mc.field_71441_e.func_180495_p(event.getPos()));
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (this.autoWeapon.getValue().booleanValue() && !BlockTweaks.fullNullCheck() && event.getTarget() != null) {
            this.equipBestWeapon(event.getTarget());
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        Entity entity;
        if (BlockTweaks.fullNullCheck()) {
            return;
        }
        if (this.noFriendAttack.getValue().booleanValue() && event.getPacket() instanceof CPacketUseEntity && (entity = ((CPacketUseEntity)event.getPacket()).func_149564_a((World)BlockTweaks.mc.field_71441_e)) != null && OyVey.friendManager.isFriend(entity.func_70005_c_())) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (!BlockTweaks.fullNullCheck()) {
            if (BlockTweaks.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && BlockTweaks.mc.field_71439_g.field_71071_by.field_70461_c != this.currentTargetSlot) {
                this.lastHotbarSlot = BlockTweaks.mc.field_71439_g.field_71071_by.field_70461_c;
            }
            if (!BlockTweaks.mc.field_71474_y.field_74312_F.func_151470_d() && this.switched) {
                this.equip(this.lastHotbarSlot, false);
            }
        }
    }

    private void removeGlitchBlocks(BlockPos pos) {
        for (int dx = -4; dx <= 4; ++dx) {
            for (int dy = -4; dy <= 4; ++dy) {
                for (int dz = -4; dz <= 4; ++dz) {
                    BlockPos blockPos = new BlockPos(pos.func_177958_n() + dx, pos.func_177956_o() + dy, pos.func_177952_p() + dz);
                    if (!BlockTweaks.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals((Object)Blocks.field_150350_a)) continue;
                    BlockTweaks.mc.field_71442_b.func_187099_a(BlockTweaks.mc.field_71439_g, BlockTweaks.mc.field_71441_e, blockPos, EnumFacing.DOWN, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
                }
            }
        }
    }

    private void equipBestTool(IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            float f;
            float f2;
            ItemStack stack = BlockTweaks.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack.field_190928_g) continue;
            float speed = stack.func_150997_a(blockState);
            if (!(f2 > 1.0f)) continue;
            int eff = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)stack);
            speed += (float)(eff > 0 ? Math.pow(eff, 2.0) + 1.0 : 0.0);
            if (!((double)f > max)) continue;
            max = speed;
            bestSlot = i;
        }
        this.equip(bestSlot, true);
    }

    public void equipBestWeapon(Entity entity) {
        int bestSlot = -1;
        double maxDamage = 0.0;
        EnumCreatureAttribute creatureAttribute = EnumCreatureAttribute.UNDEFINED;
        if (EntityUtil.isLiving(entity)) {
            EntityLivingBase base = (EntityLivingBase)entity;
            creatureAttribute = base.func_70668_bt();
        }
        for (int i = 0; i < 9; ++i) {
            double d;
            double damage;
            ItemStack stack = BlockTweaks.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack.field_190928_g) continue;
            if (stack.func_77973_b() instanceof ItemTool) {
                damage = (double)((ItemTool)stack.func_77973_b()).field_77865_bY + (double)EnchantmentHelper.func_152377_a((ItemStack)stack, (EnumCreatureAttribute)creatureAttribute);
                if (!(damage > maxDamage)) continue;
                maxDamage = damage;
                bestSlot = i;
                continue;
            }
            if (!(stack.func_77973_b() instanceof ItemSword)) continue;
            damage = (double)((ItemSword)stack.func_77973_b()).func_150931_i() + (double)EnchantmentHelper.func_152377_a((ItemStack)stack, (EnumCreatureAttribute)creatureAttribute);
            if (!(d > maxDamage)) continue;
            maxDamage = damage;
            bestSlot = i;
        }
        this.equip(bestSlot, true);
    }

    private void equip(int slot, boolean equipTool) {
        if (slot != -1) {
            if (slot != BlockTweaks.mc.field_71439_g.field_71071_by.field_70461_c) {
                this.lastHotbarSlot = BlockTweaks.mc.field_71439_g.field_71071_by.field_70461_c;
            }
            this.currentTargetSlot = slot;
            BlockTweaks.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            this.switched = equipTool;
        }
    }
}

