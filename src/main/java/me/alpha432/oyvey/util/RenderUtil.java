/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Disk
 *  org.lwjgl.util.glu.GLU
 *  org.lwjgl.util.glu.Sphere
 */
package me.alpha432.oyvey.util;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.Colors;
import me.alpha432.oyvey.features.modules.player.Speedmine;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.GLUProjection;
import me.alpha432.oyvey.util.OyColor;
import me.alpha432.oyvey.util.RenderBuilder;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

public class RenderUtil
implements Util {
    private static final Frustum frustrum = new Frustum();
    private static final FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
    private static final IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
    private static final FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
    private static final FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
    public static RenderItem itemRender = mc.func_175599_af();
    public static ICamera camera = new Frustum();
    private static boolean depth = GL11.glIsEnabled((int)2896);
    private static boolean texture = GL11.glIsEnabled((int)3042);
    private static boolean clean = GL11.glIsEnabled((int)3553);
    private static boolean bind = GL11.glIsEnabled((int)2929);
    private static boolean override = GL11.glIsEnabled((int)2848);

    public static void drawBoundingBoxBottomBlockPosXInMiddle(BlockPos bp, float width, int r, int g, int b, int alpha) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)width);
        Minecraft mc = Minecraft.func_71410_x();
        double x = (double)bp.func_177958_n() - mc.func_175598_ae().field_78730_l;
        double y = (double)bp.func_177956_o() - mc.func_175598_ae().field_78731_m;
        double z = (double)bp.func_177952_p() - mc.func_175598_ae().field_78728_n;
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181669_b(r, g, b, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181669_b(r, g, b, alpha).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawBoundingBoxBottomBlockPosXInMiddle2(BlockPos bp, float width, int r, int g, int b, int alpha) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)width);
        Minecraft mc = Minecraft.func_71410_x();
        double x = (double)bp.func_177958_n() - mc.func_175598_ae().field_78730_l;
        double y = (double)bp.func_177956_o() - mc.func_175598_ae().field_78731_m;
        double z = (double)bp.func_177952_p() - mc.func_175598_ae().field_78728_n;
        AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181669_b(r, g, b, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181669_b(r, g, b, alpha).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void updateModelViewProjectionMatrix() {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        ScaledResolution res = new ScaledResolution(Minecraft.func_71410_x());
        GLUProjection.getInstance().updateMatrices(viewport, modelView, projection, (float)res.func_78326_a() / (float)Minecraft.func_71410_x().field_71443_c, (float)res.func_78328_b() / (float)Minecraft.func_71410_x().field_71440_d);
    }

    public static void drawRectangleCorrectly(int x, int y, int w, int h, int color) {
        GL11.glLineWidth((float)1.0f);
        Gui.func_73734_a((int)x, (int)y, (int)(x + w), (int)(y + h), (int)color);
    }

    public static void drawWaypointImage(BlockPos pos, GLUProjection.Projection projection, Color color, String name, boolean rectangle, Color rectangleColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)projection.getX(), (double)projection.getY(), (double)0.0);
        String text = name + ": " + Math.round(RenderUtil.mc.field_71439_g.func_70011_f((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p())) + "M";
        float textWidth = OyVey.textManager.getStringWidth(text);
        OyVey.textManager.drawString(text, -(textWidth / 2.0f), -((float)OyVey.textManager.getFontHeight() / 2.0f) + (float)OyVey.textManager.getFontHeight() / 2.0f, color.getRGB(), false);
        GlStateManager.func_179137_b((double)(-projection.getX()), (double)(-projection.getY()), (double)0.0);
        GlStateManager.func_179121_F();
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.field_72340_a - RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b - RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c - RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d - RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e - RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f - RenderUtil.mc.func_175598_ae().field_78728_n);
    }

    public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder BufferBuilder2 = tessellator.func_178180_c();
        BufferBuilder2.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        BufferBuilder2.func_181662_b((double)(x + 0), (double)(y + height), (double)zLevel).func_187315_a((double)((float)(textureX + 0) * 0.00390625f), (double)((float)(textureY + height) * 0.00390625f)).func_181675_d();
        BufferBuilder2.func_181662_b((double)(x + width), (double)(y + height), (double)zLevel).func_187315_a((double)((float)(textureX + width) * 0.00390625f), (double)((float)(textureY + height) * 0.00390625f)).func_181675_d();
        BufferBuilder2.func_181662_b((double)(x + width), (double)(y + 0), (double)zLevel).func_187315_a((double)((float)(textureX + width) * 0.00390625f), (double)((float)(textureY + 0) * 0.00390625f)).func_181675_d();
        BufferBuilder2.func_181662_b((double)(x + 0), (double)(y + 0), (double)zLevel).func_187315_a((double)((float)(textureX + 0) * 0.00390625f), (double)((float)(textureY + 0) * 0.00390625f)).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void drawOpenGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
        for (EnumFacing face : EnumFacing.values()) {
            if (face == EnumFacing.UP) continue;
            RenderUtil.drawGradientPlane(pos, face, startColor, endColor, height);
        }
    }

    public static void drawClosedGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
        for (EnumFacing face : EnumFacing.values()) {
            RenderUtil.drawGradientPlane(pos, face, startColor, endColor, height);
        }
    }

    public static void drawTricolorGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
        for (EnumFacing face : EnumFacing.values()) {
            if (face == EnumFacing.UP) continue;
            RenderUtil.drawGradientPlane(pos, face, startColor, midColor, true, false);
        }
        for (EnumFacing face : EnumFacing.values()) {
            if (face == EnumFacing.DOWN) continue;
            RenderUtil.drawGradientPlane(pos, face, midColor, endColor, true, true);
        }
    }

    public static void drawSexyBoxPhobosIsRetardedFuckYouESP(AxisAlignedBB a, Color boxColor, Color outlineColor, float lineWidth, boolean outline, boolean box, boolean colorSync, float alpha, float scale, float slab) {
        double f = 0.5 * (double)(1.0f - scale);
        AxisAlignedBB bb = RenderUtil.interpolateAxis(new AxisAlignedBB(a.field_72340_a + f, a.field_72338_b + f + (double)(1.0f - slab), a.field_72339_c + f, a.field_72336_d - f, a.field_72337_e - f, a.field_72334_f - f));
        float rB = (float)boxColor.getRed() / 255.0f;
        float gB = (float)boxColor.getGreen() / 255.0f;
        float bB = (float)boxColor.getBlue() / 255.0f;
        float aB = (float)boxColor.getAlpha() / 255.0f;
        float rO = (float)outlineColor.getRed() / 255.0f;
        float gO = (float)outlineColor.getGreen() / 255.0f;
        float bO = (float)outlineColor.getBlue() / 255.0f;
        float aO = (float)outlineColor.getAlpha() / 255.0f;
        if (colorSync) {
            rB = (float)Colors.INSTANCE.getCurrentColor().getRed() / 255.0f;
            gB = (float)Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f;
            bB = (float)Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f;
            rO = (float)Colors.INSTANCE.getCurrentColor().getRed() / 255.0f;
            gO = (float)Colors.INSTANCE.getCurrentColor().getGreen() / 255.0f;
            bO = (float)Colors.INSTANCE.getCurrentColor().getBlue() / 255.0f;
        }
        if (alpha > 1.0f) {
            alpha = 1.0f;
        }
        aB *= alpha;
        aO *= alpha;
        if (box) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)rB, (float)gB, (float)bB, (float)aB);
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
        if (outline) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)lineWidth);
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder bufferbuilder = tessellator.func_178180_c();
            bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(rO, gO, bO, aO).func_181675_d();
            tessellator.func_78381_a();
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawText(AxisAlignedBB pos, String text) {
        if (pos == null || text == null) {
            return;
        }
        GlStateManager.func_179094_E();
        RenderUtil.glBillboardDistanceScaled((float)pos.field_72340_a + 0.5f, (float)pos.field_72338_b + 0.5f, (float)pos.field_72339_c + 0.5f, (EntityPlayer)RenderUtil.mc.field_71439_g, 1.0f);
        GlStateManager.func_179097_i();
        GlStateManager.func_179137_b((double)(-((double)OyVey.textManager.getStringWidth(text) / 2.0)), (double)0.0, (double)0.0);
        OyVey.textManager.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        GlStateManager.func_179121_F();
    }

    public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, boolean half, boolean top) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
        AxisAlignedBB bb = iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c);
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        if (face == EnumFacing.DOWN) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b + (top ? 0.5 : 0.0);
            y2 = bb.field_72338_b + (top ? 0.5 : 0.0);
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.UP) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72337_e / (double)(half ? 2 : 1);
            y2 = bb.field_72337_e / (double)(half ? 2 : 1);
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.EAST) {
            x1 = bb.field_72336_d;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b + (top ? 0.5 : 0.0);
            y2 = bb.field_72337_e / (double)(half ? 2 : 1);
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.WEST) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72340_a;
            y1 = bb.field_72338_b + (top ? 0.5 : 0.0);
            y2 = bb.field_72337_e / (double)(half ? 2 : 1);
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.SOUTH) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b + (top ? 0.5 : 0.0);
            y2 = bb.field_72337_e / (double)(half ? 2 : 1);
            z1 = bb.field_72334_f;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.NORTH) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b + (top ? 0.5 : 0.0);
            y2 = bb.field_72337_e / (double)(half ? 2 : 1);
            z1 = bb.field_72339_c;
            z2 = bb.field_72339_c;
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179132_a((boolean)false);
        builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        } else if (face == EnumFacing.UP) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        } else if (face == EnumFacing.DOWN) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }

    public static void boxESP(BlockPos blockPos, Color color, float f, boolean bl, boolean bl2, int n, boolean bl3) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)blockPos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)blockPos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(blockPos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(blockPos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(blockPos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(axisAlignedBB.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, axisAlignedBB.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, axisAlignedBB.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, axisAlignedBB.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, axisAlignedBB.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, axisAlignedBB.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
            double d14;
            double d13;
            double d12;
            double d11;
            double d10;
            double d9;
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)f);
            double d8 = RenderUtil.mc.field_71442_b.field_78770_f;
            float f2 = (float)Speedmine.getInstance().timer.getPassedTimeMs() / 1000.0f / (Speedmine.getInstance().breakTime * OyVey.serverManager.getTpsFactor());
            f2 = Math.min(f2, 1.0f);
            if (bl3) {
                d9 = axisAlignedBB.field_72340_a + 0.5 - (double)(f2 / 2.0f);
                d10 = axisAlignedBB.field_72338_b + 0.5 - (double)(f2 / 2.0f);
                d11 = axisAlignedBB.field_72339_c + 0.5 - (double)(f2 / 2.0f);
                d12 = axisAlignedBB.field_72336_d - 0.5 + (double)(f2 / 2.0f);
                d13 = axisAlignedBB.field_72337_e - 0.5 + (double)(f2 / 2.0f);
                d14 = axisAlignedBB.field_72334_f - 0.5 + (double)(f2 / 2.0f);
            } else {
                d9 = axisAlignedBB.field_72340_a + 0.5 - d8 / 2.0;
                d10 = axisAlignedBB.field_72338_b + 0.5 - d8 / 2.0;
                d11 = axisAlignedBB.field_72339_c + 0.5 - d8 / 2.0;
                d12 = axisAlignedBB.field_72336_d - 0.5 + d8 / 2.0;
                d13 = axisAlignedBB.field_72337_e - 0.5 + d8 / 2.0;
                d14 = axisAlignedBB.field_72334_f - 0.5 + d8 / 2.0;
            }
            AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(d9, d10, d11, d12, d13, d14);
            if (bl2) {
                RenderUtil.drawFilledBox(axisAlignedBB2, new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)n / 255.0f).getRGB());
            }
            if (bl) {
                RenderUtil.drawBlockOutline(axisAlignedBB2, new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f), Speedmine.getInstance().lineWidth.getValue().floatValue());
            }
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, double height) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
        AxisAlignedBB bb = iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c).func_72321_a(0.0, height, 0.0);
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        if (face == EnumFacing.DOWN) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b;
            y2 = bb.field_72338_b;
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.UP) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72337_e;
            y2 = bb.field_72337_e;
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.EAST) {
            x1 = bb.field_72336_d;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b;
            y2 = bb.field_72337_e;
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.WEST) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72340_a;
            y1 = bb.field_72338_b;
            y2 = bb.field_72337_e;
            z1 = bb.field_72339_c;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.SOUTH) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b;
            y2 = bb.field_72337_e;
            z1 = bb.field_72334_f;
            z2 = bb.field_72334_f;
        } else if (face == EnumFacing.NORTH) {
            x1 = bb.field_72340_a;
            x2 = bb.field_72336_d;
            y1 = bb.field_72338_b;
            y2 = bb.field_72337_e;
            z1 = bb.field_72339_c;
            z2 = bb.field_72339_c;
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179132_a((boolean)false);
        builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        } else if (face == EnumFacing.UP) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        } else if (face == EnumFacing.DOWN) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }

    public static void drawGradientRect(int x, int y, int w, int h, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y + (double)h, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y + (double)h, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth, double height) {
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
        RenderUtil.drawGradientBlockOutline(iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c).func_72321_a(0.0, height, 0.0), startColor, endColor, linewidth);
    }

    public static void drawProperGradientBlockOutline(BlockPos pos, Color startColor, Color midColor, Color endColor, float linewidth) {
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
        RenderUtil.drawProperGradientBlockOutline(iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), startColor, midColor, endColor, linewidth);
    }

    public static void drawProperGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color midColor, Color endColor, float linewidth) {
        float red = (float)endColor.getRed() / 255.0f;
        float green = (float)endColor.getGreen() / 255.0f;
        float blue = (float)endColor.getBlue() / 255.0f;
        float alpha = (float)endColor.getAlpha() / 255.0f;
        float red1 = (float)midColor.getRed() / 255.0f;
        float green1 = (float)midColor.getGreen() / 255.0f;
        float blue1 = (float)midColor.getBlue() / 255.0f;
        float alpha1 = (float)midColor.getAlpha() / 255.0f;
        float red2 = (float)startColor.getRed() / 255.0f;
        float green2 = (float)startColor.getGreen() / 255.0f;
        float blue2 = (float)startColor.getBlue() / 255.0f;
        float alpha2 = (float)startColor.getAlpha() / 255.0f;
        double dif = (bb.field_72337_e - bb.field_72338_b) / 2.0;
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        GL11.glBegin((int)1);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glColor4f((float)red2, (float)green2, (float)blue2, (float)alpha2);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color endColor, float linewidth) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawGradientFilledBox(BlockPos pos, Color startColor, Color endColor) {
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
        RenderUtil.drawGradientFilledBox(iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), startColor, endColor);
    }

    public static void drawGradientFilledBox(AxisAlignedBB bb, Color startColor, Color endColor) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        float alpha = (float)endColor.getAlpha() / 255.0f;
        float red = (float)endColor.getRed() / 255.0f;
        float green = (float)endColor.getGreen() / 255.0f;
        float blue = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)startColor.getRed() / 255.0f;
        float green1 = (float)startColor.getGreen() / 255.0f;
        float blue1 = (float)startColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawGradientRect(float x, float y, float w, float h, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(f1, f2, f3, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y + (double)h, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y + (double)h, 0.0).func_181666_a(f5, f6, f7, f4).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawFilledCircle(double x, double y, double z, Color color, double radius) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
    }

    public static void drawGradientBoxTest(BlockPos pos, Color startColor, Color endColor) {
    }

    public static void blockESP(BlockPos b, Color c, double length, double length2) {
        RenderUtil.blockEsp(b, c, length, length2);
    }

    public static void drawBox(BlockPos blockPos, double height, OyColor color, int sides) {
        RenderUtil.drawBox(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0, height, 1.0, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, OyColor color, int sides) {
        RenderUtil.drawBox(bb, check, height, color, color.getAlpha(), sides);
    }

    public static void drawBox(AxisAlignedBB bb, boolean check, double height, OyColor color, int alpha, int sides) {
        if (check) {
            RenderUtil.drawBox(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, bb.field_72337_e - bb.field_72338_b, bb.field_72334_f - bb.field_72339_c, color, alpha, sides);
        } else {
            RenderUtil.drawBox(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c, bb.field_72336_d - bb.field_72340_a, height, bb.field_72334_f - bb.field_72339_c, color, alpha, sides);
        }
    }

    public static void drawBox(BlockPos pos, Color color, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && RenderUtil.mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
            AxisAlignedBB bb = iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c);
            camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
            if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
            }
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color secondColor, float lineWidth, boolean outline, boolean box, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
        if (box) {
            RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), height, gradientBox, invertGradientBox, gradientAlpha);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondColor, lineWidth, air, height, gradientOutline, invertGradientOutline);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha, Double height) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m + height, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)lineWidth);
            if (box) {
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)boxAlpha / 255.0f));
            }
            if (outline) {
                RenderGlobal.func_189694_a((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72339_c, (double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)lineWidth);
            double dist = RenderUtil.mc.field_71439_g.func_70011_f((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() + 0.5f), (double)((float)pos.func_177952_p() + 0.5f)) * 0.75;
            if (box) {
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)boxAlpha / 255.0f));
            }
            if (outline) {
                RenderGlobal.func_189694_a((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72339_c, (double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color secondColor, float lineWidth, boolean outline, boolean box, boolean air) {
        if (box) {
            RenderUtil.drawBox(pos, color, air);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondColor, lineWidth, air);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air) {
        if (box) {
            RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, Color outLineColor, double lineWidth, boolean outline, boolean box, Float height) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m + (double)height.floatValue(), (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)((float)lineWidth));
            if (box) {
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            if (outline) {
                RenderGlobal.func_189694_a((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72339_c, (double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f, (float)((float)outLineColor.getRed() / 255.0f), (float)((float)outLineColor.getGreen() / 255.0f), (float)((float)outLineColor.getBlue() / 255.0f), (float)((float)outLineColor.getAlpha() / 255.0f));
            }
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
        if (box) {
            RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha), height, gradientBox, invertGradientBox, gradientAlpha);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, height, gradientOutline, invertGradientOutline, gradientAlpha);
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height, boolean gradient, boolean invert) {
        if (gradient) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            RenderUtil.drawGradientBlockOutline(pos, invert ? endColor : color, invert ? color : endColor, linewidth, height);
            return;
        }
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && RenderUtil.mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
            AxisAlignedBB blockAxis = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m + height, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
            RenderUtil.drawBlockOutline(blockAxis.func_186662_g((double)0.002f), color, linewidth);
        }
    }

    public static void glScissor(float x, float y, float x1, float y1, ScaledResolution sr) {
        GL11.glScissor((int)((int)(x * (float)sr.func_78325_e())), (int)((int)((float)RenderUtil.mc.field_71440_d - y1 * (float)sr.func_78325_e())), (int)((int)((x1 - x) * (float)sr.func_78325_e())), (int)((int)((y1 - y) * (float)sr.func_78325_e())));
    }

    public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        GlStateManager.func_179094_E();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        GL11.glLineWidth((float)thickness);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)x1, (double)y1, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GL11.glDisable((int)2848);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179121_F();
    }

    public static void drawBox(BlockPos pos, Color color) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawBox(double x, double y, double z, double w, double h, double d, OyColor color, int alpha, int sides) {
        GlStateManager.func_179118_c();
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        color.glColor();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        RenderUtil.doVertices(new AxisAlignedBB(x, y, z, x + w, y + h, z + d), color, alpha, bufferbuilder, sides, false);
        tessellator.func_78381_a();
        GlStateManager.func_179141_d();
    }

    private static void doVertices(AxisAlignedBB axisAlignedBB, OyColor color, int alpha, BufferBuilder bufferbuilder, int sides, boolean five) {
        if ((sides & 0x20) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 0x10) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 4) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 8) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 2) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            }
        }
        if ((sides & 1) != 0 || sides == -1) {
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72334_f, color, alpha, bufferbuilder);
            RenderUtil.colorVertex(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            if (five) {
                RenderUtil.colorVertex(axisAlignedBB.field_72336_d, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, color, alpha, bufferbuilder);
            }
        }
    }

    public static void drawGlowBox(BlockPos blockPos, double height, Float lineWidth, Color color, Color outlineColor) {
        RenderUtil.drawBoxESP(blockPos, outlineColor, lineWidth.floatValue(), true, false, outlineColor.getAlpha(), -1.0);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)blockPos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)blockPos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(blockPos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(blockPos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(blockPos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        RenderBuilder.glSetup();
        RenderBuilder.glPrepare();
        RenderUtil.drawSelectionGlowFilledBox(axisAlignedBB, height, new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()), new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
        RenderBuilder.glRestore();
        RenderBuilder.glRelease();
    }

    public static void drawSelectionGlowFilledBox(AxisAlignedBB axisAlignedBB, double height, Color startColor, Color endColor) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder BufferBuilder2 = Tessellator.func_178181_a().func_178180_c();
        BufferBuilder2.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        RenderUtil.addChainedGlowBoxVertices(axisAlignedBB.field_72340_a, axisAlignedBB.field_72338_b, axisAlignedBB.field_72339_c, axisAlignedBB.field_72336_d, axisAlignedBB.field_72337_e + height, axisAlignedBB.field_72334_f, startColor, endColor);
        tessellator.func_78381_a();
    }

    public static void addChainedGlowBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color startColor, Color endColor) {
        BufferBuilder BufferBuilder2 = Tessellator.func_178181_a().func_178180_c();
        BufferBuilder2.func_181662_b(minX, minY, minZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, minY, minZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, minY, maxZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, minY, maxZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, maxY, minZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, maxY, maxZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, maxY, maxZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, maxY, minZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, minY, minZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, maxY, minZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, maxY, minZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, minY, minZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, minY, minZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, maxY, minZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, maxY, maxZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, minY, maxZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, minY, maxZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, minY, maxZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(maxX, maxY, maxZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, maxY, maxZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, minY, minZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, minY, maxZ).func_181666_a((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, maxY, maxZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
        BufferBuilder2.func_181662_b(minX, maxY, minZ).func_181666_a((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).func_181675_d();
    }

    private static void colorVertex(double x, double y, double z, OyColor color, int alpha, BufferBuilder bufferbuilder) {
        bufferbuilder.func_181662_b(x - RenderUtil.mc.func_175598_ae().field_78730_l, y - RenderUtil.mc.func_175598_ae().field_78731_m, z - RenderUtil.mc.func_175598_ae().field_78728_n).func_181669_b(color.getRed(), color.getGreen(), color.getBlue(), alpha).func_181675_d();
    }

    public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        double offset = (bb.field_72337_e - bb.field_72338_b) / 2.0;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
    }

    public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)midColor.getRed() / 255.0f;
        float green2 = (float)midColor.getGreen() / 255.0f;
        float blue2 = (float)midColor.getBlue() / 255.0f;
        float alpha2 = (float)midColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        double offset = (bb.field_72337_e - bb.field_72338_b) / 2.0;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawEvenBetterGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)midColor.getRed() / 255.0f;
        float green2 = (float)midColor.getGreen() / 255.0f;
        float blue2 = (float)midColor.getBlue() / 255.0f;
        float alpha2 = (float)midColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        double offset = (bb.field_72337_e - bb.field_72338_b) / 2.0;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        builder.func_181668_a(5, DefaultVertexFormats.field_181706_f);
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red1, green1, blue1, alpha1).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawBox(BlockPos pos, Color color, double height, boolean gradient, boolean invert, int alpha) {
        if (gradient) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            RenderUtil.drawOpenGradientBox(pos, invert ? endColor : color, invert ? color : endColor, height);
            return;
        }
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m + height, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtil.mc.func_175606_aa()).field_70165_t, RenderUtil.mc.func_175606_aa().field_70163_u, RenderUtil.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtil.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtil.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtil.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtil.mc.func_175598_ae().field_78728_n))) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && RenderUtil.mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
            RenderUtil.drawBlockOutline(iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), color, linewidth);
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height, boolean gradient, boolean invert, int alpha) {
        if (gradient) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            RenderUtil.drawGradientBlockOutline(pos, invert ? endColor : color, invert ? color : endColor, linewidth, height);
            return;
        }
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && RenderUtil.mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
            AxisAlignedBB blockAxis = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m + height, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
            RenderUtil.drawBlockOutline(blockAxis.func_186662_g((double)0.002f), color, linewidth);
        }
    }

    public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawText(BlockPos pos, String text) {
        if (pos == null || text == null) {
            return;
        }
        GlStateManager.func_179094_E();
        RenderUtil.glBillboardDistanceScaled((float)pos.func_177958_n() + 0.5f, (float)pos.func_177956_o() + 0.5f, (float)pos.func_177952_p() + 0.5f, (EntityPlayer)RenderUtil.mc.field_71439_g, 1.0f);
        GlStateManager.func_179097_i();
        GlStateManager.func_179137_b((double)(-((double)OyVey.textManager.getStringWidth(text) / 2.0)), (double)0.0, (double)0.0);
        OyVey.textManager.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        GlStateManager.func_179121_F();
    }

    public static void drawOutlinedBlockESP(BlockPos pos, Color color, float linewidth) {
        IBlockState iblockstate = RenderUtil.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.field_71439_g, mc.func_184121_ak());
        RenderUtil.drawBoundingBox(iblockstate.func_185918_c((World)RenderUtil.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), linewidth, ColorUtil.toRGBA(color));
    }

    public static void blockEsp(BlockPos blockPos, Color c, double length, double length2) {
        double x = (double)blockPos.func_177958_n() - RenderUtil.mc.field_175616_W.field_78725_b;
        double y = (double)blockPos.func_177956_o() - RenderUtil.mc.field_175616_W.field_78726_c;
        double z = (double)blockPos.func_177952_p() - RenderUtil.mc.field_175616_W.field_78723_d;
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f), (double)0.25);
        RenderUtil.drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glColor4d((double)0.0, (double)0.0, (double)0.0, (double)0.5);
        RenderUtil.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.func_178181_a();
        BufferBuilder vb = ts.func_178180_c();
        vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        ts.func_78381_a();
        vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        ts.func_78381_a();
        vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        ts.func_78381_a();
        vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        ts.func_78381_a();
        vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        ts.func_78381_a();
        vb.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        vb.func_181662_b(axisalignedbb.field_72336_d, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        ts.func_78381_a();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        vertexbuffer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        tessellator.func_78381_a();
        vertexbuffer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72339_c).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72336_d, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72338_b, boundingBox.field_72334_f).func_181675_d();
        vertexbuffer.func_181662_b(boundingBox.field_72340_a, boundingBox.field_72337_e, boundingBox.field_72334_f).func_181675_d();
        tessellator.func_78381_a();
    }

    public static void glrendermethod() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)2929);
        double viewerPosX = RenderUtil.mc.func_175598_ae().field_78730_l;
        double viewerPosY = RenderUtil.mc.func_175598_ae().field_78731_m;
        double viewerPosZ = RenderUtil.mc.func_175598_ae().field_78728_n;
        GL11.glPushMatrix();
        GL11.glTranslated((double)(-viewerPosX), (double)(-viewerPosY), (double)(-viewerPosZ));
    }

    public static void glStart(float n, float n2, float n3, float n4) {
        RenderUtil.glrendermethod();
        GL11.glColor4f((float)n, (float)n2, (float)n3, (float)n4);
    }

    public static void glEnd() {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static AxisAlignedBB getBoundingBox(BlockPos blockPos) {
        return RenderUtil.mc.field_71441_e.func_180495_p(blockPos).func_185900_c((IBlockAccess)RenderUtil.mc.field_71441_e, blockPos).func_186670_a(blockPos);
    }

    public static void drawOutlinedBox(AxisAlignedBB axisAlignedBB) {
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72338_b, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72339_c);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72336_d, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72334_f);
        GL11.glVertex3d((double)axisAlignedBB.field_72340_a, (double)axisAlignedBB.field_72337_e, (double)axisAlignedBB.field_72339_c);
        GL11.glEnd();
    }

    public static void drawFilledBoxESPN(BlockPos pos, Color color) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtil.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtil.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtil.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtil.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtil.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtil.mc.func_175598_ae().field_78728_n);
        int rgba = ColorUtil.toRGBA(color);
        RenderUtil.drawFilledBox(bb, rgba);
    }

    public static void drawFilledBox(AxisAlignedBB bb, int color) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void drawBoundingBox(AxisAlignedBB bb, float width, int color) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)width);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        GlStateManager.func_179137_b((double)((double)x - RenderUtil.mc.func_175598_ae().field_78725_b), (double)((double)y - RenderUtil.mc.func_175598_ae().field_78726_c), (double)((double)z - RenderUtil.mc.func_175598_ae().field_78723_d));
        GlStateManager.func_187432_a((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-RenderUtil.mc.field_71439_g.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)RenderUtil.mc.field_71439_g.field_70125_A, (float)(RenderUtil.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)(-scale), (float)(-scale), (float)scale);
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtil.glBillboard(x, y, z);
        int distance = (int)player.func_70011_f((double)x, (double)y, (double)z);
        float scaleDistance = (float)distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.func_179152_a((float)scaleDistance, (float)scaleDistance, (float)scaleDistance);
    }

    public static void drawColoredBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)width);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, 0.0f).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, 0.0f).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, 0.0f).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, 0.0f).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, 0.0f).func_181675_d();
        tessellator.func_78381_a();
        GL11.glDisable((int)2848);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }

    public static Color getRainbowAlpha(int speed, int offset, float s, float b, int alpha) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        Color c = new Color(Color.getHSBColor(hue / (float)speed, s, b).getRGB());
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
        Sphere s = new Sphere();
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)1.2f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        s.setDrawStyle(100013);
        GL11.glTranslated((double)(x - RenderUtil.mc.field_175616_W.field_78725_b), (double)(y - RenderUtil.mc.field_175616_W.field_78726_c), (double)(z - RenderUtil.mc.field_175616_W.field_78723_d));
        s.draw(size, slices, stacks);
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBar(GLUProjection.Projection projection, float width, float height, float totalWidth, Color startColor, Color outlineColor) {
        if (projection.getType() == GLUProjection.Projection.Type.INSIDE) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b((double)projection.getX(), (double)projection.getY(), (double)0.0);
            RenderUtil.drawOutlineRect(-(totalWidth / 2.0f), -(height / 2.0f), totalWidth, height, outlineColor.getRGB());
            RenderUtil.drawRect(-(totalWidth / 2.0f), -(height / 2.0f), width, height, startColor.getRGB());
            GlStateManager.func_179137_b((double)(-projection.getX()), (double)(-projection.getY()), (double)0.0);
            GlStateManager.func_179121_F();
        }
    }

    public static void drawProjectedText(GLUProjection.Projection projection, float addX, float addY, String text, Color color, boolean shadow) {
        if (projection.getType() == GLUProjection.Projection.Type.INSIDE) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b((double)projection.getX(), (double)projection.getY(), (double)0.0);
            OyVey.textManager.drawString(text, -((float)OyVey.textManager.getStringWidth(text) / 2.0f) + addX, addY, color.getRGB(), shadow);
            GlStateManager.func_179137_b((double)(-projection.getX()), (double)(-projection.getY()), (double)0.0);
            GlStateManager.func_179121_F();
        }
    }

    public static void drawChungusESP(GLUProjection.Projection projection, float width, float height, ResourceLocation location) {
        if (projection.getType() == GLUProjection.Projection.Type.INSIDE) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179137_b((double)projection.getX(), (double)projection.getY(), (double)0.0);
            mc.func_110434_K().func_110577_a(location);
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            mc.func_110434_K().func_110577_a(location);
            RenderUtil.drawCompleteImage(0.0f, 0.0f, width, height);
            mc.func_110434_K().func_147645_c(location);
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179137_b((double)(-projection.getX()), (double)(-projection.getY()), (double)0.0);
            GlStateManager.func_179121_F();
        }
    }

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)posY, (float)0.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)width, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)width, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static void drawOutlineRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187441_d((float)1.0f);
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.func_181668_a(2, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void draw3DRect(float x, float y, float w, float h, Color startColor, Color endColor, float lineWidth) {
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187441_d((float)lineWidth);
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)h, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)w, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    public static void drawClock(float x, float y, float radius, int slices, int loops, float lineWidth, boolean fill, Color color) {
        Disk disk = new Disk();
        Date date = new Date();
        int hourAngle = 180 + -(Calendar.getInstance().get(10) * 30 + Calendar.getInstance().get(12) / 2);
        int minuteAngle = 180 + -(Calendar.getInstance().get(12) * 6 + Calendar.getInstance().get(13) / 10);
        int secondAngle = 180 + -(Calendar.getInstance().get(13) * 6);
        int totalMinutesTime = Calendar.getInstance().get(12);
        int totalHoursTime = Calendar.getInstance().get(10);
        if (fill) {
            GL11.glPushMatrix();
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glLineWidth((float)lineWidth);
            GL11.glDisable((int)3553);
            disk.setOrientation(100020);
            disk.setDrawStyle(100012);
            GL11.glTranslated((double)x, (double)y, (double)0.0);
            disk.draw(0.0f, radius, slices, loops);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glEnable((int)3042);
            GL11.glLineWidth((float)lineWidth);
            GL11.glDisable((int)3553);
            GL11.glBegin((int)3);
            ArrayList<Vec2f> hVectors = new ArrayList<Vec2f>();
            float hue = (float)(System.currentTimeMillis() % 7200L) / 7200.0f;
            for (int i = 0; i <= 360; ++i) {
                Vec2f vec = new Vec2f(x + (float)Math.sin((double)i * Math.PI / 180.0) * radius, y + (float)Math.cos((double)i * Math.PI / 180.0) * radius);
                hVectors.add(vec);
            }
            Color color1 = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
            for (int j = 0; j < hVectors.size() - 1; ++j) {
                GL11.glColor4f((float)((float)color1.getRed() / 255.0f), (float)((float)color1.getGreen() / 255.0f), (float)((float)color1.getBlue() / 255.0f), (float)((float)color1.getAlpha() / 255.0f));
                GL11.glVertex3d((double)((Vec2f)hVectors.get((int)j)).field_189982_i, (double)((Vec2f)hVectors.get((int)j)).field_189983_j, (double)0.0);
                GL11.glVertex3d((double)((Vec2f)hVectors.get((int)(j + 1))).field_189982_i, (double)((Vec2f)hVectors.get((int)(j + 1))).field_189983_j, (double)0.0);
                color1 = new Color(Color.HSBtoRGB(hue += 0.0027777778f, 1.0f, 1.0f));
            }
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
        RenderUtil.drawLine(x, y, x + (float)Math.sin((double)hourAngle * Math.PI / 180.0) * (radius / 2.0f), y + (float)Math.cos((double)hourAngle * Math.PI / 180.0) * (radius / 2.0f), 1.0f, Color.WHITE.getRGB());
        RenderUtil.drawLine(x, y, x + (float)Math.sin((double)minuteAngle * Math.PI / 180.0) * (radius - radius / 10.0f), y + (float)Math.cos((double)minuteAngle * Math.PI / 180.0) * (radius - radius / 10.0f), 1.0f, Color.WHITE.getRGB());
        RenderUtil.drawLine(x, y, x + (float)Math.sin((double)secondAngle * Math.PI / 180.0) * (radius - radius / 10.0f), y + (float)Math.cos((double)secondAngle * Math.PI / 180.0) * (radius - radius / 10.0f), 1.0f, Color.RED.getRGB());
    }

    public static void GLPre(float lineWidth) {
        depth = GL11.glIsEnabled((int)2896);
        texture = GL11.glIsEnabled((int)3042);
        clean = GL11.glIsEnabled((int)3553);
        bind = GL11.glIsEnabled((int)2929);
        override = GL11.glIsEnabled((int)2848);
        RenderUtil.GLPre(depth, texture, clean, bind, override, lineWidth);
    }

    public static void GlPost() {
        RenderUtil.GLPost(depth, texture, clean, bind, override);
    }

    private static void GLPre(boolean depth, boolean texture, boolean clean, boolean bind, boolean override, float lineWidth) {
        if (depth) {
            GL11.glDisable((int)2896);
        }
        if (!texture) {
            GL11.glEnable((int)3042);
        }
        GL11.glLineWidth((float)lineWidth);
        if (clean) {
            GL11.glDisable((int)3553);
        }
        if (bind) {
            GL11.glDisable((int)2929);
        }
        if (!override) {
            GL11.glEnable((int)2848);
        }
        GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glHint((int)3154, (int)4354);
        GlStateManager.func_179132_a((boolean)false);
    }

    public static float[][] getBipedRotations(ModelBiped biped) {
        float[][] rotations = new float[5][];
        float[] headRotation = new float[]{biped.field_78116_c.field_78795_f, biped.field_78116_c.field_78796_g, biped.field_78116_c.field_78808_h};
        rotations[0] = headRotation;
        float[] rightArmRotation = new float[]{biped.field_178723_h.field_78795_f, biped.field_178723_h.field_78796_g, biped.field_178723_h.field_78808_h};
        rotations[1] = rightArmRotation;
        float[] leftArmRotation = new float[]{biped.field_178724_i.field_78795_f, biped.field_178724_i.field_78796_g, biped.field_178724_i.field_78808_h};
        rotations[2] = leftArmRotation;
        float[] rightLegRotation = new float[]{biped.field_178721_j.field_78795_f, biped.field_178721_j.field_78796_g, biped.field_178721_j.field_78808_h};
        rotations[3] = rightLegRotation;
        float[] leftLegRotation = new float[]{biped.field_178722_k.field_78795_f, biped.field_178722_k.field_78796_g, biped.field_178722_k.field_78808_h};
        rotations[4] = leftLegRotation;
        return rotations;
    }

    private static void GLPost(boolean depth, boolean texture, boolean clean, boolean bind, boolean override) {
        GlStateManager.func_179132_a((boolean)true);
        if (!override) {
            GL11.glDisable((int)2848);
        }
        if (bind) {
            GL11.glEnable((int)2929);
        }
        if (clean) {
            GL11.glEnable((int)3553);
        }
        if (!texture) {
            GL11.glDisable((int)3042);
        }
        if (depth) {
            GL11.glEnable((int)2896);
        }
    }

    public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
        GL11.glBegin((int)4);
        int i = (int)((float)num_segments / (360.0f / start_angle)) + 1;
        while ((float)i <= (float)num_segments / (360.0f / end_angle)) {
            double previousangle = Math.PI * 2 * (double)(i - 1) / (double)num_segments;
            double angle = Math.PI * 2 * (double)i / (double)num_segments;
            GL11.glVertex2d((double)cx, (double)cy);
            GL11.glVertex2d((double)((double)cx + Math.cos(angle) * (double)r), (double)((double)cy + Math.sin(angle) * (double)r));
            GL11.glVertex2d((double)((double)cx + Math.cos(previousangle) * (double)r), (double)((double)cy + Math.sin(previousangle) * (double)r));
            ++i;
        }
        RenderUtil.glEnd();
    }

    public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
        GL11.glBegin((int)2);
        int i = (int)((float)num_segments / (360.0f / start_angle)) + 1;
        while ((float)i <= (float)num_segments / (360.0f / end_angle)) {
            double angle = Math.PI * 2 * (double)i / (double)num_segments;
            GL11.glVertex2d((double)((double)cx + Math.cos(angle) * (double)r), (double)((double)cy + Math.sin(angle) * (double)r));
            ++i;
        }
        RenderUtil.glEnd();
    }

    public static void drawCircleOutline(float x, float y, float radius) {
        RenderUtil.drawCircleOutline(x, y, radius, 0, 360, 40);
    }

    public static void drawCircleOutline(float x, float y, float radius, int start, int end, int segments) {
        RenderUtil.drawArcOutline(x, y, radius, start, end, segments);
    }

    public static void drawCircle(float x, float y, float radius) {
        RenderUtil.drawCircle(x, y, radius, 0, 360, 64);
    }

    public static void drawCircle(float x, float y, float radius, int start, int end, int segments) {
        RenderUtil.drawArc(x, y, radius, start, end, segments);
    }

    public static void drawOutlinedRoundedRectangle(int x, int y, int width, int height, float radius, float dR, float dG, float dB, float dA, float outlineWidth) {
        RenderUtil.drawRoundedRectangle(x, y, width, height, radius);
        GL11.glColor4f((float)dR, (float)dG, (float)dB, (float)dA);
        RenderUtil.drawRoundedRectangle((float)x + outlineWidth, (float)y + outlineWidth, (float)width - outlineWidth * 2.0f, (float)height - outlineWidth * 2.0f, radius);
    }

    public static void drawRectangle(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)width, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)height);
        GL11.glVertex2d((double)width, (double)height);
        RenderUtil.glEnd();
    }

    public static void drawRectangleXY(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        RenderUtil.glEnd();
    }

    public static void drawFilledRectangle(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        RenderUtil.glEnd();
    }

    public static Vec3d to2D(double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new Vec3d((double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2));
        }
        return null;
    }

    public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, boolean outline, float outlineWidth, int color) {
        boolean blend = GL11.glIsEnabled((int)3042);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.hexColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x - size / widthDiv), (double)(y + size));
        GL11.glVertex2d((double)x, (double)(y + size / heightDiv));
        GL11.glVertex2d((double)(x + size / widthDiv), (double)(y + size));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)alpha);
            GL11.glBegin((int)2);
            GL11.glVertex2d((double)x, (double)y);
            GL11.glVertex2d((double)(x - size / widthDiv), (double)(y + size));
            GL11.glVertex2d((double)x, (double)(y + size / heightDiv));
            GL11.glVertex2d((double)(x + size / widthDiv), (double)(y + size));
            GL11.glVertex2d((double)x, (double)y);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2848);
    }

    public static int getRainbow(int speed, int offset, float s, float b) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, s, b).getRGB();
    }

    public static void hexColor(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.func_71410_x().func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
        GL11.glEnable((int)3042);
        RenderUtil.drawArc(x + width - radius, y + height - radius, radius, 0.0f, 90.0f, 16);
        RenderUtil.drawArc(x + radius, y + height - radius, radius, 90.0f, 180.0f, 16);
        RenderUtil.drawArc(x + radius, y + radius, radius, 180.0f, 270.0f, 16);
        RenderUtil.drawArc(x + width - radius, y + radius, radius, 270.0f, 360.0f, 16);
        GL11.glBegin((int)4);
        GL11.glVertex2d((double)(x + width - radius), (double)y);
        GL11.glVertex2d((double)(x + radius), (double)y);
        GL11.glVertex2d((double)(x + width - radius), (double)(y + radius));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + radius));
        GL11.glVertex2d((double)(x + radius), (double)y);
        GL11.glVertex2d((double)(x + radius), (double)(y + radius));
        GL11.glVertex2d((double)(x + width), (double)(y + radius));
        GL11.glVertex2d((double)x, (double)(y + radius));
        GL11.glVertex2d((double)x, (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width), (double)(y + radius));
        GL11.glVertex2d((double)x, (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + radius), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + height));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + height));
        GL11.glVertex2d((double)(x + radius), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + radius), (double)(y + height));
        RenderUtil.glEnd();
    }

    public static void renderOne(float lineWidth) {
        RenderUtil.checkSetupFBO();
        GL11.glPushAttrib((int)1048575);
        GL11.glDisable((int)3008);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)lineWidth);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2960);
        GL11.glClear((int)1024);
        GL11.glClearStencil((int)15);
        GL11.glStencilFunc((int)512, (int)1, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public static void renderTwo() {
        GL11.glStencilFunc((int)512, (int)0, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    public static void renderThree() {
        GL11.glStencilFunc((int)514, (int)1, (int)15);
        GL11.glStencilOp((int)7680, (int)7680, (int)7680);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public static void renderFour(Color color) {
        RenderUtil.setColor(color);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)1.0f, (float)-2000000.0f);
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)240.0f, (float)240.0f);
    }

    public static void renderFive() {
        GL11.glPolygonOffset((float)1.0f, (float)2000000.0f);
        GL11.glDisable((int)10754);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2960);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3008);
        GL11.glPopAttrib();
    }

    public static void setColor(Color color) {
        GL11.glColor4d((double)((double)color.getRed() / 255.0), (double)((double)color.getGreen() / 255.0), (double)((double)color.getBlue() / 255.0), (double)((double)color.getAlpha() / 255.0));
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = RenderUtil.mc.field_147124_at;
        if (fbo != null && fbo.field_147624_h > -1) {
            RenderUtil.setupFBO(fbo);
            fbo.field_147624_h = -1;
        }
    }

    private static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.field_147624_h);
        int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencilDepthBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)RenderUtil.mc.field_71443_c, (int)RenderUtil.mc.field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencilDepthBufferID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencilDepthBufferID);
    }

    public static void gradientBox(BlockPos currentPos, Color color, Float value, Boolean value1, Boolean value2, Integer value3, boolean b) {
    }

    public static final class GeometryMasks {
        public static final HashMap<EnumFacing, Integer> FACEMAP = new HashMap();

        static {
            FACEMAP.put(EnumFacing.DOWN, 1);
            FACEMAP.put(EnumFacing.WEST, 16);
            FACEMAP.put(EnumFacing.NORTH, 4);
            FACEMAP.put(EnumFacing.SOUTH, 8);
            FACEMAP.put(EnumFacing.EAST, 32);
            FACEMAP.put(EnumFacing.UP, 2);
        }

        public static final class Quad {
            public static final int DOWN = 1;
            public static final int UP = 2;
            public static final int NORTH = 4;
            public static final int SOUTH = 8;
            public static final int WEST = 16;
            public static final int EAST = 32;
            public static final int ALL = 63;
        }

        public static final class Line {
            public static final int DOWN_WEST = 17;
            public static final int UP_WEST = 18;
            public static final int DOWN_EAST = 33;
            public static final int UP_EAST = 34;
            public static final int DOWN_NORTH = 5;
            public static final int UP_NORTH = 6;
            public static final int DOWN_SOUTH = 9;
            public static final int UP_SOUTH = 10;
            public static final int NORTH_WEST = 20;
            public static final int NORTH_EAST = 36;
            public static final int SOUTH_WEST = 24;
            public static final int SOUTH_EAST = 40;
            public static final int ALL = 63;
        }
    }

    public static class RenderTesselator
    extends Tessellator {
        public static RenderTesselator INSTANCE = new RenderTesselator();

        public RenderTesselator() {
            super(0x200000);
        }

        public static void prepare(int mode2) {
            RenderTesselator.prepareGL();
            RenderTesselator.begin(mode2);
        }

        public static void prepareGL() {
            GL11.glBlendFunc((int)770, (int)771);
            GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GlStateManager.func_187441_d((float)1.5f);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179140_f();
            GlStateManager.func_179129_p();
            GlStateManager.func_179141_d();
            GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        }

        public static void begin(int mode2) {
            INSTANCE.func_178180_c().func_181668_a(mode2, DefaultVertexFormats.field_181706_f);
        }

        public static void release() {
            RenderTesselator.render();
            RenderTesselator.releaseGL();
        }

        public static void render() {
            INSTANCE.func_78381_a();
        }

        public static void releaseGL() {
            GlStateManager.func_179089_o();
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179098_w();
            GlStateManager.func_179147_l();
            GlStateManager.func_179126_j();
        }

        public static void drawBox(BlockPos blockPos, int argb, int sides) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawBox(blockPos, r, g, b, a, sides);
        }

        public static void drawBox(float x, float y, float z, int argb, int sides) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawBox(INSTANCE.func_178180_c(), x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
        }

        public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
            RenderTesselator.drawBox(INSTANCE.func_178180_c(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
        }

        public static BufferBuilder getBufferBuilder() {
            return INSTANCE.func_178180_c();
        }

        public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
            if ((sides & 1) != 0) {
                buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 2) != 0) {
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 4) != 0) {
                buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 8) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x10) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x20) != 0) {
                buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
        }

        public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
            if ((sides & 0x11) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x12) != 0) {
                buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x21) != 0) {
                buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x22) != 0) {
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 5) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 6) != 0) {
                buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 9) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0xA) != 0) {
                buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x14) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x24) != 0) {
                buffer.func_181662_b((double)(x + w), (double)y, (double)z).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)z).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x18) != 0) {
                buffer.func_181662_b((double)x, (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)x, (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
            if ((sides & 0x28) != 0) {
                buffer.func_181662_b((double)(x + w), (double)y, (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
                buffer.func_181662_b((double)(x + w), (double)(y + h), (double)(z + d)).func_181669_b(r, g, b, a).func_181675_d();
            }
        }

        public static void drawBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)width);
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder bufferbuilder = tessellator.func_178180_c();
            bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181706_f);
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
            tessellator.func_78381_a();
            GL11.glDisable((int)2848);
            GlStateManager.func_179132_a((boolean)true);
            GlStateManager.func_179126_j();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }

        public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int argb, int alpha2) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawFullBox(bb, blockPos, width, r, g, b, a, alpha2);
        }

        public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int red, int green, int blue, int alpha, int alpha2) {
            RenderTesselator.prepare(7);
            RenderTesselator.drawBox(blockPos, red, green, blue, alpha, 63);
            RenderTesselator.release();
            RenderTesselator.drawBoundingBox(bb, width, red, green, blue, alpha2);
        }

        public static void drawHalfBox(BlockPos blockPos, int argb, int sides) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawHalfBox(blockPos, r, g, b, a, sides);
        }

        public static void drawHalfBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
            RenderTesselator.drawBox(INSTANCE.func_178180_c(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p(), 1.0f, 0.5f, 1.0f, r, g, b, a, sides);
        }
    }
}

