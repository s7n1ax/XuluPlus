/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.render.HandChams;
import me.alpha432.oyvey.features.modules.render.SmallShield;
import me.alpha432.oyvey.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    @Final
    public Minecraft field_78455_a;
    private boolean injection = true;

    @Shadow
    public abstract void func_187457_a(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Shadow
    protected abstract void func_187456_a(float var1, float var2, EnumHandSide var3);

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            if (hand == EnumHand.MAIN_HAND) {
                if (offset.isOn()) {
                    xOffset = offset.mainX.getValue().floatValue();
                    yOffset = offset.mainY.getValue().floatValue();
                }
            } else if (offset.isOn()) {
                xOffset = offset.offX.getValue().floatValue();
                yOffset = offset.offY.getValue().floatValue();
            }
            if (HandChams.getINSTANCE().isOn() && hand == EnumHand.MAIN_HAND && stack.func_190926_b()) {
                if (HandChams.getINSTANCE().mode.getValue().equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    this.func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                }
                GlStateManager.func_179094_E();
                if (HandChams.getINSTANCE().mode.getValue().equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPushAttrib((int)1048575);
                } else {
                    GlStateManager.func_179123_a();
                }
                if (HandChams.getINSTANCE().mode.getValue().equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPolygonMode((int)1032, (int)6913);
                }
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                if (HandChams.getINSTANCE().mode.getValue().equals((Object)HandChams.RenderMode.WIREFRAME)) {
                    GL11.glEnable((int)2848);
                    GL11.glEnable((int)3042);
                }
                GL11.glColor4f((float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRed() / 255.0f : (float)HandChams.getINSTANCE().red.getValue().intValue() / 255.0f), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : (float)HandChams.getINSTANCE().green.getValue().intValue() / 255.0f), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : (float)HandChams.getINSTANCE().blue.getValue().intValue() / 255.0f), (float)((float)HandChams.getINSTANCE().alpha.getValue().intValue() / 255.0f));
                this.func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                GlStateManager.func_179099_b();
                GlStateManager.func_179121_F();
            }
            if (SmallShield.getINSTANCE().isOn() && (!stack.field_190928_g || HandChams.getINSTANCE().isOff())) {
                this.func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            } else if (!stack.field_190928_g || HandChams.getINSTANCE().isOff()) {
                this.func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
            }
            this.injection = true;
        }
    }
}

