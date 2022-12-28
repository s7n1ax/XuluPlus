/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package me.alpha432.oyvey.mixin.mixins.XuluProtect;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={EntityLivingBase.class})
public interface XuluLivingBase {
    @Invoker(value="getArmSwingAnimationEnd")
    public int getArmSwingAnimationEnd();
}

