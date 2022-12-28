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
import java.awt.Font;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.gui.font.CustomFont;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.EntityUtil;
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

public class RenderUtill
implements Util {
    public static RenderItem itemRender = mc.func_175599_af();
    public static int deltaTime;
    public static ICamera camera;
    private static final Frustum frustrum;
    private static boolean depth;
    private static boolean texture;
    private static boolean clean;
    private static boolean bind;
    private static boolean override;
    private static final FloatBuffer screenCoords;
    private static final IntBuffer viewport;
    private static final FloatBuffer modelView;
    private static final FloatBuffer projection;
    private CustomFont customFont = new CustomFont(new Font("Arial", 3, 23), true, false);

    public static void updateModelViewProjectionMatrix() {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        ScaledResolution res = new ScaledResolution(Minecraft.func_71410_x());
    }

    public static void drawRectangleCorrectly(int x, int y, int w, int h, int color) {
        GL11.glLineWidth((float)1.0f);
        Gui.func_73734_a((int)x, (int)y, (int)(x + w), (int)(y + h), (int)color);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.field_72340_a - RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72338_b - RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72339_c - RenderUtill.mc.func_175598_ae().field_78728_n, bb.field_72336_d - RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72337_e - RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72334_f - RenderUtill.mc.func_175598_ae().field_78728_n);
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
            RenderUtill.drawGradientPlane(pos, face, startColor, endColor, height);
        }
    }

    public static void drawClosedGradientBox(BlockPos pos, Color startColor, Color endColor, double height) {
        for (EnumFacing face : EnumFacing.values()) {
            RenderUtill.drawGradientPlane(pos, face, startColor, endColor, height);
        }
    }

    public static void drawTricolorGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
        for (EnumFacing face : EnumFacing.values()) {
            if (face == EnumFacing.UP) continue;
            RenderUtill.drawGradientPlane(pos, face, startColor, midColor, true, false);
        }
        for (EnumFacing face : EnumFacing.values()) {
            if (face == EnumFacing.DOWN) continue;
            RenderUtill.drawGradientPlane(pos, face, midColor, endColor, true, true);
        }
    }

    public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, boolean half, boolean top) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
        AxisAlignedBB bb = iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c);
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
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
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        } else if (face == EnumFacing.UP) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
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

    public static void drawGradientPlane(BlockPos pos, EnumFacing face, Color startColor, Color endColor, double height) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
        AxisAlignedBB bb = iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c).func_72321_a(0.0, height, 0.0);
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
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
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        } else if (face == EnumFacing.UP) {
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y1, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x1, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z1).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b(x2, y2, z2).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
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
        float f2 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(startColor & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y + (double)h, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y + (double)h, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth, double height) {
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
        RenderUtill.drawGradientBlockOutline(iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c).func_72321_a(0.0, height, 0.0), startColor, endColor, linewidth);
    }

    public static void drawProperGradientBlockOutline(BlockPos pos, Color startColor, Color midColor, Color endColor, float linewidth) {
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
        RenderUtill.drawProperGradientBlockOutline(iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), startColor, midColor, endColor, linewidth);
    }

    public static void drawProperGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color midColor, Color endColor, float linewidth) {
        float red = (float)endColor.getRed() / 255.0f;
        float green = (float)endColor.getGreen() / 255.0f;
        float blue = (float)endColor.getBlue() / 255.0f;
        float alpha = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)midColor.getRed() / 255.0f;
        float green2 = (float)midColor.getGreen() / 255.0f;
        float blue2 = (float)midColor.getBlue() / 255.0f;
        float alpha2 = (float)midColor.getAlpha() / 255.0f;
        float red3 = (float)startColor.getRed() / 255.0f;
        float green3 = (float)startColor.getGreen() / 255.0f;
        float blue3 = (float)startColor.getBlue() / 255.0f;
        float alpha3 = (float)startColor.getAlpha() / 255.0f;
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
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glColor4f((float)red3, (float)green3, (float)blue3, (float)alpha3);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72340_a, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
        GL11.glVertex3d((double)bb.field_72340_a, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72334_f);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72334_f);
        GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72334_f);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72338_b, (double)bb.field_72339_c);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glVertex3d((double)bb.field_72336_d, (double)(bb.field_72338_b + dif), (double)bb.field_72339_c);
        GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
        GL11.glVertex3d((double)bb.field_72336_d, (double)bb.field_72337_e, (double)bb.field_72339_c);
        GL11.glColor4d((double)red3, (double)green3, (double)blue3, (double)alpha3);
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
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
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
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
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
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
        RenderUtill.drawGradientFilledBox(iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), startColor, endColor);
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
        float alpha2 = (float)startColor.getAlpha() / 255.0f;
        float red2 = (float)startColor.getRed() / 255.0f;
        float green2 = (float)startColor.getGreen() / 255.0f;
        float blue2 = (float)startColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        bufferbuilder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
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
        float f2 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(startColor & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j((int)7425);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        vertexbuffer.func_181662_b((double)x, (double)y + (double)h, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        vertexbuffer.func_181662_b((double)x + (double)w, (double)y + (double)h, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
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
        RenderUtill.blockEsp(b, c, length, length2);
    }

    public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air) {
        if (box) {
            RenderUtill.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
        }
        if (outline) {
            RenderUtill.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height, boolean gradientBox, boolean gradientOutline, boolean invertGradientBox, boolean invertGradientOutline, int gradientAlpha) {
        if (box) {
            RenderUtill.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha), height, gradientBox, invertGradientBox, gradientAlpha);
        }
        if (outline) {
            RenderUtill.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, height, gradientOutline, invertGradientOutline, gradientAlpha);
        }
    }

    public static void glScissor(float x, float y, float x1, float y1, ScaledResolution sr) {
        GL11.glScissor((int)((int)(x * (float)sr.func_78325_e())), (int)((int)((float)RenderUtill.mc.field_71440_d - y1 * (float)sr.func_78325_e())), (int)((int)((x1 - x) * (float)sr.func_78325_e())), (int)((int)((y1 - y) * (float)sr.func_78325_e())));
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
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtill.mc.func_175606_aa()).field_70165_t, RenderUtill.mc.func_175606_aa().field_70163_u, RenderUtill.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtill.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtill.mc.func_175598_ae().field_78728_n))) {
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

    public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
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
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
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
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
        float red3 = (float)midColor.getRed() / 255.0f;
        float green3 = (float)midColor.getGreen() / 255.0f;
        float blue3 = (float)midColor.getBlue() / 255.0f;
        float alpha3 = (float)midColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
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
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72339_c).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b + offset, bb.field_72334_f).func_181666_a(red3, green3, blue3, alpha3).func_181675_d();
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
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
        float red3 = (float)midColor.getRed() / 255.0f;
        float green3 = (float)midColor.getGreen() / 255.0f;
        float blue3 = (float)midColor.getBlue() / 255.0f;
        float alpha3 = (float)midColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
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
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        builder.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
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
            RenderUtill.drawOpenGradientBox(pos, invert ? endColor : color, invert ? color : endColor, height);
            return;
        }
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m + height, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtill.mc.func_175606_aa()).field_70165_t, RenderUtill.mc.func_175606_aa().field_70163_u, RenderUtill.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtill.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtill.mc.func_175598_ae().field_78728_n))) {
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
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && RenderUtill.mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
            RenderUtill.drawBlockOutline(iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), color, linewidth);
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height, boolean gradient, boolean invert, int alpha) {
        if (gradient) {
            Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            RenderUtill.drawGradientBlockOutline(pos, invert ? endColor : color, invert ? color : endColor, linewidth, height);
            return;
        }
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        if ((air || iblockstate.func_185904_a() != Material.field_151579_a) && RenderUtill.mc.field_71441_e.func_175723_af().func_177746_a(pos)) {
            AxisAlignedBB blockAxis = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m + height, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
            RenderUtill.drawBlockOutline(blockAxis.func_186662_g((double)0.002f), color, linewidth);
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

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
        camera.func_78547_a(Objects.requireNonNull(RenderUtill.mc.func_175606_aa()).field_70165_t, RenderUtill.mc.func_175606_aa().field_70163_u, RenderUtill.mc.func_175606_aa().field_70161_v);
        if (camera.func_78546_a(new AxisAlignedBB(bb.field_72340_a + RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72338_b + RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72339_c + RenderUtill.mc.func_175598_ae().field_78728_n, bb.field_72336_d + RenderUtill.mc.func_175598_ae().field_78730_l, bb.field_72337_e + RenderUtill.mc.func_175598_ae().field_78731_m, bb.field_72334_f + RenderUtill.mc.func_175598_ae().field_78728_n))) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179097_i();
            GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
            GlStateManager.func_179090_x();
            GlStateManager.func_179132_a((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)lineWidth);
            double dist = RenderUtill.mc.field_71439_g.func_70011_f((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() + 0.5f), (double)((float)pos.func_177952_p() + 0.5f)) * 0.75;
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

    public void drawText(BlockPos pos, String text, Color color) {
        if (pos == null || text == null) {
            return;
        }
        GlStateManager.func_179094_E();
        RenderUtill.glBillboardDistanceScaled((float)pos.func_177958_n() + 0.5f, (float)pos.func_177956_o() + 0.5f, (float)pos.func_177952_p() + 0.5f, (EntityPlayer)RenderUtill.mc.field_71439_g, 1.0f);
        GlStateManager.func_179097_i();
        GlStateManager.func_179137_b((double)(-((double)OyVey.textManager.getStringWidth(text) / 2.0)), (double)0.0, (double)0.0);
        this.customFont.drawString(text, 0.0, 0.0, ColorUtil.toRGBA(color), true);
        GlStateManager.func_179121_F();
    }

    public static void drawOutlinedBlockESP(BlockPos pos, Color color, float linewidth) {
        IBlockState iblockstate = RenderUtill.mc.field_71441_e.func_180495_p(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtill.mc.field_71439_g, mc.func_184121_ak());
        RenderUtill.drawBoundingBox(iblockstate.func_185918_c((World)RenderUtill.mc.field_71441_e, pos).func_186662_g((double)0.002f).func_72317_d(-interp.field_72450_a, -interp.field_72448_b, -interp.field_72449_c), linewidth, ColorUtil.toRGBA(color));
    }

    public static void blockEsp(BlockPos blockPos, Color c, double length, double length2) {
        double x = (double)blockPos.func_177958_n() - RenderUtill.mc.field_175616_W.field_78725_b;
        double y = (double)blockPos.func_177956_o() - RenderUtill.mc.field_175616_W.field_78726_c;
        double z = (double)blockPos.func_177952_p() - RenderUtill.mc.field_175616_W.field_78723_d;
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f), (double)0.25);
        RenderUtill.drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glColor4d((double)0.0, (double)0.0, (double)0.0, (double)0.5);
        RenderUtill.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
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
        double viewerPosX = RenderUtill.mc.func_175598_ae().field_78730_l;
        double viewerPosY = RenderUtill.mc.func_175598_ae().field_78731_m;
        double viewerPosZ = RenderUtill.mc.func_175598_ae().field_78728_n;
        GL11.glPushMatrix();
        GL11.glTranslated((double)(-viewerPosX), (double)(-viewerPosY), (double)(-viewerPosZ));
    }

    public static void glStart(float n, float n2, float n3, float n4) {
        RenderUtill.glrendermethod();
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
        return RenderUtill.mc.field_71441_e.func_180495_p(blockPos).func_185900_c((IBlockAccess)RenderUtill.mc.field_71441_e, blockPos).func_186670_a(blockPos);
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
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.func_177958_n() - RenderUtill.mc.func_175598_ae().field_78730_l, (double)pos.func_177956_o() - RenderUtill.mc.func_175598_ae().field_78731_m, (double)pos.func_177952_p() - RenderUtill.mc.func_175598_ae().field_78728_n, (double)(pos.func_177958_n() + 1) - RenderUtill.mc.func_175598_ae().field_78730_l, (double)(pos.func_177956_o() + 1) - RenderUtill.mc.func_175598_ae().field_78731_m, (double)(pos.func_177952_p() + 1) - RenderUtill.mc.func_175598_ae().field_78728_n);
        int rgba = ColorUtil.toRGBA(color);
        RenderUtill.drawFilledBox(bb, rgba);
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
        GlStateManager.func_179137_b((double)((double)x - RenderUtill.mc.func_175598_ae().field_78725_b), (double)((double)y - RenderUtill.mc.func_175598_ae().field_78726_c), (double)((double)z - RenderUtill.mc.func_175598_ae().field_78723_d));
        GlStateManager.func_187432_a((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-RenderUtill.mc.field_71439_g.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)RenderUtill.mc.field_71439_g.field_70125_A, (float)(RenderUtill.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)-0.02666667f, (float)-0.02666667f, (float)0.02666667f);
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtill.glBillboard(x, y, z);
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
        GL11.glTranslated((double)(x - RenderUtill.mc.field_175616_W.field_78725_b), (double)(y - RenderUtill.mc.field_175616_W.field_78726_c), (double)(z - RenderUtill.mc.field_175616_W.field_78723_d));
        s.draw(size, slices, stacks);
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
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
        float alpha2 = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)endColor.getRed() / 255.0f;
        float green2 = (float)endColor.getGreen() / 255.0f;
        float blue2 = (float)endColor.getBlue() / 255.0f;
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
            Color color2 = new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
            for (int j = 0; j < hVectors.size() - 1; ++j) {
                GL11.glColor4f((float)((float)color2.getRed() / 255.0f), (float)((float)color2.getGreen() / 255.0f), (float)((float)color2.getBlue() / 255.0f), (float)((float)color2.getAlpha() / 255.0f));
                GL11.glVertex3d((double)((Vec2f)hVectors.get((int)j)).field_189982_i, (double)((Vec2f)hVectors.get((int)j)).field_189983_j, (double)0.0);
                GL11.glVertex3d((double)((Vec2f)hVectors.get((int)(j + 1))).field_189982_i, (double)((Vec2f)hVectors.get((int)(j + 1))).field_189983_j, (double)0.0);
                color2 = new Color(Color.HSBtoRGB(hue += 0.0027777778f, 1.0f, 1.0f));
            }
            GL11.glEnd();
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
        RenderUtill.drawLine(x, y, x + (float)Math.sin((double)hourAngle * Math.PI / 180.0) * (radius / 2.0f), y + (float)Math.cos((double)hourAngle * Math.PI / 180.0) * (radius / 2.0f), 1.0f, Color.WHITE.getRGB());
        RenderUtill.drawLine(x, y, x + (float)Math.sin((double)minuteAngle * Math.PI / 180.0) * (radius - radius / 10.0f), y + (float)Math.cos((double)minuteAngle * Math.PI / 180.0) * (radius - radius / 10.0f), 1.0f, Color.WHITE.getRGB());
        RenderUtill.drawLine(x, y, x + (float)Math.sin((double)secondAngle * Math.PI / 180.0) * (radius - radius / 10.0f), y + (float)Math.cos((double)secondAngle * Math.PI / 180.0) * (radius - radius / 10.0f), 1.0f, Color.RED.getRGB());
    }

    public static void GLPre(float lineWidth) {
        depth = GL11.glIsEnabled((int)2896);
        texture = GL11.glIsEnabled((int)3042);
        clean = GL11.glIsEnabled((int)3553);
        bind = GL11.glIsEnabled((int)2929);
        override = GL11.glIsEnabled((int)2848);
        RenderUtill.GLPre(depth, texture, clean, bind, override, lineWidth);
    }

    public static void GlPost() {
        RenderUtill.GLPost(depth, texture, clean, bind, override);
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
        RenderUtill.glEnd();
    }

    public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
        GL11.glBegin((int)2);
        int i = (int)((float)num_segments / (360.0f / start_angle)) + 1;
        while ((float)i <= (float)num_segments / (360.0f / end_angle)) {
            double angle = Math.PI * 2 * (double)i / (double)num_segments;
            GL11.glVertex2d((double)((double)cx + Math.cos(angle) * (double)r), (double)((double)cy + Math.sin(angle) * (double)r));
            ++i;
        }
        RenderUtill.glEnd();
    }

    public static void drawCircleOutline(float x, float y, float radius) {
        RenderUtill.drawCircleOutline(x, y, radius, 0, 360, 40);
    }

    public static void drawCircleOutline(float x, float y, float radius, int start, int end, int segments) {
        RenderUtill.drawArcOutline(x, y, radius, start, end, segments);
    }

    public static void drawCircle(float x, float y, float radius) {
        RenderUtill.drawCircle(x, y, radius, 0, 360, 64);
    }

    public static void drawCircle(float x, float y, float radius, int start, int end, int segments) {
        RenderUtill.drawArc(x, y, radius, start, end, segments);
    }

    public static void drawOutlinedRoundedRectangle(int x, int y, int width, int height, float radius, float dR, float dG, float dB, float dA, float outlineWidth) {
        RenderUtill.drawRoundedRectangle(x, y, width, height, radius);
        GL11.glColor4f((float)dR, (float)dG, (float)dB, (float)dA);
        RenderUtill.drawRoundedRectangle((float)x + outlineWidth, (float)y + outlineWidth, (float)width - outlineWidth * 2.0f, (float)height - outlineWidth * 2.0f, radius);
    }

    public static void drawRectangle(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)width, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)height);
        GL11.glVertex2d((double)width, (double)height);
        RenderUtill.glEnd();
    }

    public static void drawRectangleXY(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        RenderUtill.glEnd();
    }

    public static void drawFilledRectangle(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        RenderUtill.glEnd();
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
        RenderUtill.hexColor(color);
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
        return RenderUtill.isInViewFrustrum(entity.func_174813_aQ()) || entity.field_70158_ak;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.func_71410_x().func_175606_aa();
        frustrum.func_78547_a(current.field_70165_t, current.field_70163_u, current.field_70161_v);
        return frustrum.func_78546_a(bb);
    }

    public static void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
        GL11.glEnable((int)3042);
        RenderUtill.drawArc(x + width - radius, y + height - radius, radius, 0.0f, 90.0f, 16);
        RenderUtill.drawArc(x + radius, y + height - radius, radius, 90.0f, 180.0f, 16);
        RenderUtill.drawArc(x + radius, y + radius, radius, 180.0f, 270.0f, 16);
        RenderUtill.drawArc(x + width - radius, y + radius, radius, 270.0f, 360.0f, 16);
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
        RenderUtill.glEnd();
    }

    public static void renderOne(float lineWidth) {
        RenderUtill.checkSetupFBO();
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
        RenderUtill.setColor(color);
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
        Framebuffer fbo = RenderUtill.mc.field_147124_at;
        if (fbo != null && fbo.field_147624_h > -1) {
            RenderUtill.setupFBO(fbo);
            fbo.field_147624_h = -1;
        }
    }

    private static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.field_147624_h);
        int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencilDepthBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)RenderUtill.mc.field_71443_c, (int)RenderUtill.mc.field_71440_d);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencilDepthBufferID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencilDepthBufferID);
    }

    static {
        camera = new Frustum();
        frustrum = new Frustum();
        depth = GL11.glIsEnabled((int)2896);
        texture = GL11.glIsEnabled((int)3042);
        clean = GL11.glIsEnabled((int)3553);
        bind = GL11.glIsEnabled((int)2929);
        override = GL11.glIsEnabled((int)2848);
        screenCoords = BufferUtils.createFloatBuffer((int)3);
        viewport = BufferUtils.createIntBuffer((int)16);
        modelView = BufferUtils.createFloatBuffer((int)16);
        projection = BufferUtils.createFloatBuffer((int)16);
    }
}

