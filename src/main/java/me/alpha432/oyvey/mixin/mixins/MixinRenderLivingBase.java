/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.client.event.RenderLivingEvent$Post
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.render.Wireframe;
import me.alpha432.oyvey.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    @Shadow
    private static final Logger field_147923_a = LogManager.getLogger();
    @Shadow
    protected ModelBase field_77045_g;
    @Shadow
    protected boolean field_188323_j;
    float red = 0.0f;
    float green = 0.0f;
    float blue = 0.0f;

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Overwrite
    public void func_76986_a(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!MinecraftForge.EVENT_BUS.post((Event)new RenderLivingEvent.Pre(entity, (RenderLivingBase)RenderLivingBase.class.cast((Object)this), partialTicks, x, y, z))) {
            boolean shouldSit;
            GlStateManager.func_179094_E();
            GlStateManager.func_179129_p();
            this.field_77045_g.field_78095_p = this.func_77040_d(entity, partialTicks);
            this.field_77045_g.field_78093_q = shouldSit = entity.func_184218_aH() && entity.func_184187_bx() != null && entity.func_184187_bx().shouldRiderSit();
            this.field_77045_g.field_78091_s = entity.func_70631_g_();
            try {
                float f = this.func_77034_a(((EntityLivingBase)entity).field_70760_ar, ((EntityLivingBase)entity).field_70761_aq, partialTicks);
                float f1 = this.func_77034_a(((EntityLivingBase)entity).field_70758_at, ((EntityLivingBase)entity).field_70759_as, partialTicks);
                float f2 = f1 - f;
                if (shouldSit && entity.func_184187_bx() instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity.func_184187_bx();
                    f = this.func_77034_a(entitylivingbase.field_70760_ar, entitylivingbase.field_70761_aq, partialTicks);
                    f2 = f1 - f;
                    float f3 = MathHelper.func_76142_g((float)f2);
                    if (f3 < -85.0f) {
                        f3 = -85.0f;
                    }
                    if (f3 >= 85.0f) {
                        f3 = 85.0f;
                    }
                    f = f1 - f3;
                    if (f3 * f3 > 2500.0f) {
                        f += f3 * 0.2f;
                    }
                    f2 = f1 - f;
                }
                float f7 = ((EntityLivingBase)entity).field_70127_C + (((EntityLivingBase)entity).field_70125_A - ((EntityLivingBase)entity).field_70127_C) * partialTicks;
                this.func_77039_a(entity, x, y, z);
                float f8 = this.func_77044_a(entity, partialTicks);
                this.func_77043_a(entity, f8, f, partialTicks);
                float f4 = this.func_188322_c(entity, partialTicks);
                float f5 = 0.0f;
                float f6 = 0.0f;
                if (!entity.func_184218_aH()) {
                    f5 = ((EntityLivingBase)entity).field_184618_aE + (((EntityLivingBase)entity).field_70721_aZ - ((EntityLivingBase)entity).field_184618_aE) * partialTicks;
                    f6 = ((EntityLivingBase)entity).field_184619_aG - ((EntityLivingBase)entity).field_70721_aZ * (1.0f - partialTicks);
                    if (entity.func_70631_g_()) {
                        f6 *= 3.0f;
                    }
                    if (f5 > 1.0f) {
                        f5 = 1.0f;
                    }
                    f2 = f1 - f;
                }
                GlStateManager.func_179141_d();
                this.field_77045_g.func_78086_a(entity, f6, f5, partialTicks);
                this.field_77045_g.func_78087_a(f6, f5, f8, f2, f7, f4, entity);
                if (this.field_188301_f) {
                    boolean flag1 = this.func_177088_c(entity);
                    GlStateManager.func_179142_g();
                    GlStateManager.func_187431_e((int)this.func_188298_c((Entity)entity));
                    if (!this.field_188323_j) {
                        this.func_77036_a(entity, f6, f5, f8, f2, f7, f4);
                    }
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).func_175149_v()) {
                        this.func_177093_a(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    }
                    GlStateManager.func_187417_n();
                    GlStateManager.func_179119_h();
                    if (flag1) {
                        this.func_180565_e();
                    }
                } else {
                    if (Wireframe.getINSTANCE().isOn() && Wireframe.getINSTANCE().players.getValue().booleanValue() && entity instanceof EntityPlayer && Wireframe.getINSTANCE().mode.getValue().equals((Object)Wireframe.RenderMode.SOLID)) {
                        this.red = (float)ClickGui.getInstance().red.getValue().intValue() / 255.0f;
                        this.green = (float)ClickGui.getInstance().green.getValue().intValue() / 255.0f;
                        this.blue = (float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f;
                        GlStateManager.func_179094_E();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        GL11.glDisable((int)2929);
                        GL11.glDepthMask((boolean)false);
                        if (OyVey.friendManager.isFriend(entity.func_70005_c_()) || entity == Minecraft.func_71410_x().field_71439_g) {
                            GL11.glColor4f((float)0.0f, (float)191.0f, (float)255.0f, (float)(Wireframe.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
                        } else {
                            GL11.glColor4f((float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue), (float)(Wireframe.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
                        }
                        this.func_77036_a(entity, f6, f5, f8, f2, f7, f4);
                        GL11.glDisable((int)2896);
                        GL11.glEnable((int)2929);
                        GL11.glDepthMask((boolean)true);
                        if (OyVey.friendManager.isFriend(entity.func_70005_c_()) || entity == Minecraft.func_71410_x().field_71439_g) {
                            GL11.glColor4f((float)0.0f, (float)191.0f, (float)255.0f, (float)(Wireframe.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
                        } else {
                            GL11.glColor4f((float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue), (float)(Wireframe.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
                        }
                        this.func_77036_a(entity, f6, f5, f8, f2, f7, f4);
                        GL11.glEnable((int)2896);
                        GlStateManager.func_179099_b();
                        GlStateManager.func_179121_F();
                    }
                    boolean flag1 = this.func_177090_c(entity, partialTicks);
                    if (!(entity instanceof EntityPlayer) || Wireframe.getINSTANCE().isOn() && Wireframe.getINSTANCE().mode.getValue().equals((Object)Wireframe.RenderMode.WIREFRAME) && Wireframe.getINSTANCE().playerModel.getValue().booleanValue() || Wireframe.getINSTANCE().isOff()) {
                        this.func_77036_a(entity, f6, f5, f8, f2, f7, f4);
                    }
                    if (flag1) {
                        this.func_177091_f();
                    }
                    GlStateManager.func_179132_a((boolean)true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).func_175149_v()) {
                        this.func_177093_a(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    }
                    if (Wireframe.getINSTANCE().isOn() && Wireframe.getINSTANCE().players.getValue().booleanValue() && entity instanceof EntityPlayer && Wireframe.getINSTANCE().mode.getValue().equals((Object)Wireframe.RenderMode.WIREFRAME)) {
                        this.red = (float)ClickGui.getInstance().red.getValue().intValue() / 255.0f;
                        this.green = (float)ClickGui.getInstance().green.getValue().intValue() / 255.0f;
                        this.blue = (float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f;
                        GlStateManager.func_179094_E();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glPolygonMode((int)1032, (int)6913);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)2929);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        if (OyVey.friendManager.isFriend(entity.func_70005_c_()) || entity == Minecraft.func_71410_x().field_71439_g) {
                            GL11.glColor4f((float)0.0f, (float)191.0f, (float)255.0f, (float)(Wireframe.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
                        } else {
                            GL11.glColor4f((float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRed() / 255.0f : this.red), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getGreen() / 255.0f : this.green), (float)(ClickGui.getInstance().rainbow.getValue() != false ? (float)ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getBlue() / 255.0f : this.blue), (float)(Wireframe.getINSTANCE().alpha.getValue().floatValue() / 255.0f));
                        }
                        GL11.glLineWidth((float)Wireframe.getINSTANCE().lineWidth.getValue().floatValue());
                        this.func_77036_a(entity, f6, f5, f8, f2, f7, f4);
                        GL11.glEnable((int)2896);
                        GlStateManager.func_179099_b();
                        GlStateManager.func_179121_F();
                    }
                }
                GlStateManager.func_179101_C();
            }
            catch (Exception var20) {
                field_147923_a.error("Couldn't render entity", (Throwable)var20);
            }
            GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
            GlStateManager.func_179098_w();
            GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
            GlStateManager.func_179089_o();
            GlStateManager.func_179121_F();
            super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
            MinecraftForge.EVENT_BUS.post((Event)new RenderLivingEvent.Post(entity, (RenderLivingBase)RenderLivingBase.class.cast((Object)this), partialTicks, x, y, z));
        }
    }

    @Shadow
    protected abstract boolean func_193115_c(EntityLivingBase var1);

    @Shadow
    protected abstract float func_77040_d(T var1, float var2);

    @Shadow
    protected abstract float func_77034_a(float var1, float var2, float var3);

    @Shadow
    protected abstract float func_77044_a(T var1, float var2);

    @Shadow
    protected abstract void func_77043_a(T var1, float var2, float var3, float var4);

    @Shadow
    public abstract float func_188322_c(T var1, float var2);

    @Shadow
    protected abstract void func_180565_e();

    @Shadow
    protected abstract boolean func_177088_c(T var1);

    @Shadow
    protected abstract void func_77039_a(T var1, double var2, double var4, double var6);

    @Shadow
    protected abstract void func_177091_f();

    @Shadow
    protected abstract void func_77036_a(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    protected abstract void func_177093_a(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    @Shadow
    protected abstract boolean func_177090_c(T var1, float var2);
}

