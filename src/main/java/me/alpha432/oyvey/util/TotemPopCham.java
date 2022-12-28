/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.util;

import java.awt.Color;
import me.alpha432.oyvey.features.modules.render.PopChams;
import me.alpha432.oyvey.util.NordTessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class TotemPopCham {
    private static final Minecraft mc = Minecraft.func_71410_x();
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public TotemPopCham(EntityOtherPlayerMP player, ModelPlayer playerModel, Long startTime, double alphaFill, double alphaLine) {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.player = player;
        this.playerModel = playerModel;
        this.startTime = startTime;
        this.alphaFill = alphaFill;
        this.alphaLine = alphaFill;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (this.player == null || TotemPopCham.mc.field_71441_e == null || TotemPopCham.mc.field_71439_g == null) {
            return;
        }
        GL11.glLineWidth((float)1.0f);
        Color lineColorS = new Color(PopChams.rL.getValue(), PopChams.bL.getValue(), PopChams.gL.getValue(), PopChams.aL.getValue());
        Color fillColorS = new Color(PopChams.rF.getValue(), PopChams.bF.getValue(), PopChams.gF.getValue(), PopChams.aF.getValue());
        int lineA = lineColorS.getAlpha();
        int fillA = fillColorS.getAlpha();
        long time = System.currentTimeMillis() - this.startTime - ((Number)PopChams.fadestart.getValue()).longValue();
        if (System.currentTimeMillis() - this.startTime > ((Number)PopChams.fadestart.getValue()).longValue()) {
            double normal = this.normalize(time, 0.0, ((Number)PopChams.fadetime.getValue()).doubleValue());
            normal = MathHelper.func_151237_a((double)normal, (double)0.0, (double)1.0);
            normal = -normal + 1.0;
            lineA *= (int)normal;
            fillA *= (int)normal;
        }
        Color lineColor = TotemPopCham.newAlpha(lineColorS, lineA);
        Color fillColor = TotemPopCham.newAlpha(fillColorS, fillA);
        if (this.player != null && this.playerModel != null) {
            NordTessellator.prepareGL();
            GL11.glPushAttrib((int)1048575);
            GL11.glEnable((int)2881);
            GL11.glEnable((int)2848);
            if (this.alphaFill > 1.0) {
                this.alphaFill -= PopChams.fadetime.getValue().doubleValue();
            }
            Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
            if (this.alphaLine > 1.0) {
                this.alphaLine -= PopChams.fadetime.getValue().doubleValue();
            }
            Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
            TotemPopCham.glColor(fillFinal);
            GL11.glPolygonMode((int)1032, (int)6914);
            TotemPopCham.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.field_184619_aG, this.player.field_70721_aZ, this.player.field_70173_aa, this.player.field_70759_as, this.player.field_70125_A, 1.0f);
            TotemPopCham.glColor(outlineFinal);
            GL11.glPolygonMode((int)1032, (int)6913);
            TotemPopCham.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.field_184619_aG, this.player.field_70721_aZ, this.player.field_70173_aa, this.player.field_70759_as, this.player.field_70125_A, 1.0f);
            GL11.glPolygonMode((int)1032, (int)6914);
            GL11.glPopAttrib();
            NordTessellator.releaseGL();
        }
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (mc.func_175598_ae() == null) {
            return;
        }
        float partialTicks = mc.func_184121_ak();
        double x = entity.field_70165_t - TotemPopCham.mc.func_175598_ae().field_78730_l;
        double y = entity.field_70163_u - TotemPopCham.mc.func_175598_ae().field_78731_m;
        double z = entity.field_70161_v - TotemPopCham.mc.func_175598_ae().field_78728_n;
        GlStateManager.func_179094_E();
        if (entity.func_70093_af()) {
            y -= 0.125;
        }
        float interpolateRotation = TotemPopCham.interpolateRotation(entity.field_70760_ar, entity.field_70761_aq, partialTicks);
        float interpolateRotation2 = TotemPopCham.interpolateRotation(entity.field_70758_at, entity.field_70759_as, partialTicks);
        float rotationInterp = interpolateRotation2 - interpolateRotation;
        float renderPitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
        TotemPopCham.renderLivingAt(x, y, z);
        float f8 = TotemPopCham.handleRotationFloat(entity, partialTicks);
        TotemPopCham.prepareRotations(entity);
        float f9 = TotemPopCham.prepareScale(entity, scale);
        GlStateManager.func_179141_d();
        modelBase.func_78086_a(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.func_78087_a(limbSwing, limbSwingAmount, f8, entity.field_70759_as, entity.field_70125_A, f9, (Entity)entity);
        modelBase.func_78088_a((Entity)entity, limbSwing, limbSwingAmount, f8, entity.field_70759_as, entity.field_70125_A, f9);
        GlStateManager.func_179121_F();
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        TotemPopCham.renderLivingAt(x - TotemPopCham.mc.func_175598_ae().field_78730_l, y - TotemPopCham.mc.func_175598_ae().field_78731_m, z - TotemPopCham.mc.func_175598_ae().field_78728_n);
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.func_179109_b((float)((float)x), (float)((float)y), (float)((float)z));
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.func_179091_B();
        GlStateManager.func_179152_a((float)-1.0f, (float)-1.0f, (float)1.0f);
        double widthX = entity.func_184177_bl().field_72336_d - entity.func_184177_bl().field_72340_a;
        double widthZ = entity.func_184177_bl().field_72334_f - entity.func_184177_bl().field_72339_c;
        GlStateManager.func_179139_a((double)((double)scale + widthX), (double)(scale * entity.field_70131_O), (double)((double)scale + widthZ));
        float f = 0.0625f;
        GlStateManager.func_179109_b((float)0.0f, (float)-1.501f, (float)0.0f);
        return 0.0625f;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.func_179114_b((float)(180.0f - entityLivingBase.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    public static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
        return 0.0f;
    }
}

