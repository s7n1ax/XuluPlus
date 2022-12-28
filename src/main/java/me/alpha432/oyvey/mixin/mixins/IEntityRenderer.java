/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.EntityRenderer
 */
package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={EntityRenderer.class})
public interface IEntityRenderer {
    @Invoker(value="setupCameraTransform")
    public void invokeSetupCameraTransform(float var1, int var2);

    @Invoker(value="renderHand")
    public void invokeRenderHand(float var1, int var2);

    @Accessor(value="lightmapUpdateNeeded")
    public void setLightmapUpdateNeeded(boolean var1);
}

