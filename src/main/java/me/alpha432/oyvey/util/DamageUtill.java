/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemSpade
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package me.alpha432.oyvey.util;

import me.alpha432.oyvey.util.Util;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class DamageUtill
implements Util {
    public static boolean isArmorLow(EntityPlayer player, int durability) {
        for (ItemStack piece : player.field_71071_by.field_70460_b) {
            if (piece == null) {
                return true;
            }
            if (DamageUtill.getItemDamage(piece) >= durability) continue;
            return true;
        }
        return false;
    }

    public static boolean isNaked(EntityPlayer player) {
        for (ItemStack piece : player.field_71071_by.field_70460_b) {
            if (piece == null || piece.func_190926_b()) continue;
            return false;
        }
        return true;
    }

    public static int getItemDamage(ItemStack stack) {
        return stack.func_77958_k() - stack.func_77952_i();
    }

    public static float getDamageInPercent(ItemStack stack) {
        return (float)DamageUtill.getItemDamage(stack) / (float)stack.func_77958_k() * 100.0f;
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int)DamageUtill.getDamageInPercent(stack);
    }

    public static boolean hasDurability(ItemStack stack) {
        Item item = stack.func_77973_b();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }

    public static boolean canBreakWeakness(EntityPlayer player) {
        int strengthAmp = 0;
        PotionEffect effect = DamageUtill.mc.field_71439_g.func_70660_b(MobEffects.field_76420_g);
        if (effect != null) {
            strengthAmp = effect.func_76458_c();
        }
        return !DamageUtill.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t) || strengthAmp >= 1 || DamageUtill.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword || DamageUtill.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemPickaxe || DamageUtill.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemAxe || DamageUtill.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSpade;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.func_70011_f(posX, posY, posZ) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = DamageUtill.getBlastReduction((EntityLivingBase)entity, DamageUtill.getDamageMultiplied(damage), new Explosion((World)DamageUtill.mc.field_71441_e, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.func_94539_a((Explosion)explosion);
            damage = CombatRules.func_189427_a((float)damage, (float)ep.func_70658_aO(), (float)((float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
            int k = 0;
            try {
                k = EnchantmentHelper.func_77508_a((Iterable)ep.func_184193_aE(), (DamageSource)ds);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.func_76131_a((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(MobEffects.field_76429_m)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = DamageUtill.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(Entity crystal, Entity entity) {
        return DamageUtill.calculateDamage(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, entity);
    }

    public static float calculateDamage(BlockPos pos, Entity entity) {
        return DamageUtill.calculateDamage((double)pos.func_177958_n() + 0.5, pos.func_177956_o() + 1, (double)pos.func_177952_p() + 0.5, entity);
    }

    public static boolean canTakeDamage(boolean suicide) {
        return !DamageUtill.mc.field_71439_g.field_71075_bZ.field_75098_d && !suicide;
    }

    public static int getCooldownByWeapon(EntityPlayer player) {
        Item item = player.func_184614_ca().func_77973_b();
        if (item instanceof ItemSword) {
            return 600;
        }
        if (item instanceof ItemPickaxe) {
            return 850;
        }
        if (item == Items.field_151036_c) {
            return 1100;
        }
        if (item == Items.field_151018_J) {
            return 500;
        }
        if (item == Items.field_151019_K) {
            return 350;
        }
        if (item == Items.field_151053_p || item == Items.field_151049_t) {
            return 1250;
        }
        if (item instanceof ItemSpade || item == Items.field_151006_E || item == Items.field_151056_x || item == Items.field_151017_I || item == Items.field_151013_M) {
            return 1000;
        }
        return 250;
    }
}

