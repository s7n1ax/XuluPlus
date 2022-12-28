/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.NordTessellator;
import me.alpha432.oyvey.util.TotemPopCham;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopChams
extends Module {
    public static final Setting<Boolean> self = new Setting<Boolean>("Self", false);
    public static final Setting<Integer> rL = new Setting<Integer>("RedLine", 255, 0, 255);
    public static final Setting<Integer> gL = new Setting<Integer>("BlueLine", 26, 0, 255);
    public static final Setting<Integer> bL = new Setting<Integer>("Greeline", 42, 0, 255);
    public static final Setting<Integer> aL = new Setting<Integer>("AlphaLine", 42, 0, 255);
    public static final Setting<Integer> rF = new Setting<Integer>("RedFill", 255, 0, 255);
    public static final Setting<Integer> gF = new Setting<Integer>("BlueFill", 26, 0, 255);
    public static final Setting<Integer> bF = new Setting<Integer>("GreenFill", 42, 0, 255);
    public static final Setting<Integer> aF = new Setting<Integer>("AlphaFill", 42, 0, 255);
    public static final Setting<Integer> fadestart = new Setting<Integer>("FadeStart", 200, 0, 3000);
    public static final Setting<Double> fadetime = new Setting<Double>("FadeStart", 0.5, 0.0, 2.0);
    public static final Setting<Boolean> onlyOneEsp = new Setting<Boolean>("OnlyOneEsp", true);
    public static final Setting<Boolean> rainbow = new Setting<Boolean>("Rainbow", false);
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Renders when some1 pops", Module.Category.RENDER, true, false, false);
        this.register(self);
        this.register(rL);
        this.register(gL);
        this.register(bL);
        this.register(aL);
        this.register(rF);
        this.register(gF);
        this.register(bF);
        this.register(aF);
        this.register(fadestart);
        this.register(fadetime);
        this.register(onlyOneEsp);
        this.register(rainbow);
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).func_149160_c() == 35 && packet.func_149161_a((World)PopChams.mc.field_71441_e) != null && (self.getValue().booleanValue() || packet.func_149161_a((World)PopChams.mc.field_71441_e).func_145782_y() != PopChams.mc.field_71439_g.func_145782_y())) {
            GameProfile profile = new GameProfile(PopChams.mc.field_71439_g.func_110124_au(), "");
            this.player = new EntityOtherPlayerMP((World)PopChams.mc.field_71441_e, profile);
            this.player.func_82149_j(packet.func_149161_a((World)PopChams.mc.field_71441_e));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
            this.playerModel.field_78116_c.field_78806_j = false;
            this.playerModel.field_78115_e.field_78806_j = false;
            this.playerModel.field_178734_a.field_78806_j = false;
            this.playerModel.field_178733_c.field_78806_j = false;
            this.playerModel.field_178732_b.field_78806_j = false;
            this.playerModel.field_178731_d.field_78806_j = false;
            this.alphaFill = aF.getValue().intValue();
            this.alphaLine = aL.getValue().intValue();
            if (!onlyOneEsp.getValue().booleanValue()) {
                TotemPopCham totemPopCham = new TotemPopCham(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (onlyOneEsp.getValue().booleanValue()) {
            if (this.player == null || PopChams.mc.field_71441_e == null || PopChams.mc.field_71439_g == null) {
                return;
            }
            GL11.glLineWidth((float)1.0f);
            Color lineColorS = new Color(rL.getValue(), bL.getValue(), gL.getValue(), aL.getValue());
            Color fillColorS = new Color(rF.getValue(), bF.getValue(), gF.getValue(), aF.getValue());
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            long time = System.currentTimeMillis() - this.startTime - ((Number)fadestart.getValue()).longValue();
            if (System.currentTimeMillis() - this.startTime > ((Number)fadestart.getValue()).longValue()) {
                double normal = this.normalize(time, 0.0, ((Number)fadetime.getValue()).doubleValue());
                normal = MathHelper.func_151237_a((double)normal, (double)0.0, (double)1.0);
                normal = -normal + 1.0;
                lineA *= (int)normal;
                fillA *= (int)normal;
            }
            Color lineColor = PopChams.newAlpha(lineColorS, lineA);
            Color fillColor = PopChams.newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                NordTessellator.prepareGL();
                GL11.glPushAttrib((int)1048575);
                GL11.glEnable((int)2881);
                GL11.glEnable((int)2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= fadetime.getValue().doubleValue();
                }
                Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= fadetime.getValue().doubleValue();
                }
                Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
                PopChams.glColor(fillFinal);
                GL11.glPolygonMode((int)1032, (int)6914);
                PopChams.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.field_184619_aG, this.player.field_70721_aZ, this.player.field_70173_aa, this.player.field_70759_as, this.player.field_70125_A, 1.0f);
                PopChams.glColor(outlineFinal);
                GL11.glPolygonMode((int)1032, (int)6913);
                PopChams.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.field_184619_aG, this.player.field_70721_aZ, this.player.field_70173_aa, this.player.field_70759_as, this.player.field_70125_A, 1.0f);
                GL11.glPolygonMode((int)1032, (int)6914);
                GL11.glPopAttrib();
                NordTessellator.releaseGL();
            }
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
        double x = entity.field_70165_t - PopChams.mc.func_175598_ae().field_78730_l;
        double y = entity.field_70163_u - PopChams.mc.func_175598_ae().field_78731_m;
        double z = entity.field_70161_v - PopChams.mc.func_175598_ae().field_78728_n;
        GlStateManager.func_179094_E();
        if (entity.func_70093_af()) {
            y -= 0.125;
        }
        float interpolateRotation = PopChams.interpolateRotation(entity.field_70760_ar, entity.field_70761_aq, partialTicks);
        float interpolateRotation2 = PopChams.interpolateRotation(entity.field_70758_at, entity.field_70759_as, partialTicks);
        float rotationInterp = interpolateRotation2 - interpolateRotation;
        float renderPitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
        PopChams.renderLivingAt(x, y, z);
        float f8 = PopChams.handleRotationFloat(entity, partialTicks);
        PopChams.prepareRotations(entity);
        float f9 = PopChams.prepareScale(entity, scale);
        GlStateManager.func_179141_d();
        modelBase.func_78086_a(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.func_78087_a(limbSwing, limbSwingAmount, f8, entity.field_70177_z, entity.field_70125_A, f9, (Entity)entity);
        modelBase.func_78088_a((Entity)entity, limbSwing, limbSwingAmount, f8, entity.field_70177_z, entity.field_70125_A, f9);
        GlStateManager.func_179121_F();
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        PopChams.renderLivingAt(x - PopChams.mc.func_175598_ae().field_78730_l, y - PopChams.mc.func_175598_ae().field_78731_m, z - PopChams.mc.func_175598_ae().field_78728_n);
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

