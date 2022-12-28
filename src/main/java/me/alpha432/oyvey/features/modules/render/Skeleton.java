/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.render;

import java.util.HashMap;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Skeleton
extends Module {
    private static final HashMap<EntityPlayer, float[][]> entities = new HashMap();
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("invisibles", false));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("alpha", 255, 0, 255));

    public Skeleton() {
        super("Skeleton", "Draws a skeleton inside the player", Module.Category.RENDER, false, false, false);
    }

    public static void addEntity(EntityPlayer e, ModelPlayer model) {
        entities.put(e, new float[][]{{model.field_78116_c.field_78795_f, model.field_78116_c.field_78796_g, model.field_78116_c.field_78808_h}, {model.field_178723_h.field_78795_f, model.field_178723_h.field_78796_g, model.field_178723_h.field_78808_h}, {model.field_178724_i.field_78795_f, model.field_178724_i.field_78796_g, model.field_178724_i.field_78808_h}, {model.field_178721_j.field_78795_f, model.field_178721_j.field_78796_g, model.field_178721_j.field_78808_h}, {model.field_178722_k.field_78795_f, model.field_178722_k.field_78796_g, model.field_178722_k.field_78808_h}});
    }

    private Vec3d getVec3(Render3DEvent event, EntityPlayer e) {
        float pt = event.getPartialTicks();
        double x = e.field_70142_S + (e.field_70165_t - e.field_70142_S) * (double)pt;
        double y = e.field_70137_T + (e.field_70163_u - e.field_70137_T) * (double)pt;
        double z = e.field_70136_U + (e.field_70161_v - e.field_70136_U) * (double)pt;
        return new Vec3d(x, y, z);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Skeleton.fullNullCheck()) {
            return;
        }
        this.startEnd(true);
        GL11.glEnable((int)2903);
        GL11.glDisable((int)2848);
        entities.keySet().removeIf(this::doesntContain);
        Skeleton.mc.field_71441_e.field_73010_i.forEach(e -> this.drawSkeleton(event, (EntityPlayer)e));
        Gui.func_73734_a((int)0, (int)0, (int)0, (int)0, (int)0);
        this.startEnd(false);
    }

    private void drawSkeleton(Render3DEvent event, EntityPlayer e) {
        if (!BlockUtil.isPosInFov(new BlockPos(e.field_70165_t, e.field_70163_u, e.field_70161_v)).booleanValue()) {
            return;
        }
        if (e.func_82150_aj() && !this.invisibles.getValue().booleanValue()) {
            return;
        }
        float[][] entPos = entities.get((Object)e);
        if (entPos != null && e.func_70089_S() && !e.field_70128_L && e != Skeleton.mc.field_71439_g && !e.func_70608_bn()) {
            GL11.glPushMatrix();
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
            if (OyVey.friendManager.isFriend(e.func_70005_c_())) {
                GlStateManager.func_179131_c((float)0.0f, (float)191.0f, (float)230.0f, (float)this.alpha.getValue().intValue());
            } else {
                GlStateManager.func_179131_c((float)((float)ClickGui.getInstance().red.getValue().intValue() / 255.0f), (float)((float)ClickGui.getInstance().green.getValue().intValue() / 255.0f), (float)((float)ClickGui.getInstance().blue.getValue().intValue() / 255.0f), (float)this.alpha.getValue().intValue());
            }
            Vec3d vec = this.getVec3(event, e);
            double x = vec.field_72450_a - Skeleton.mc.func_175598_ae().field_78725_b;
            double y = vec.field_72448_b - Skeleton.mc.func_175598_ae().field_78726_c;
            double z = vec.field_72449_c - Skeleton.mc.func_175598_ae().field_78723_d;
            GL11.glTranslated((double)x, (double)y, (double)z);
            float xOff = e.field_70760_ar + (e.field_70761_aq - e.field_70760_ar) * event.getPartialTicks();
            GL11.glRotatef((float)(-xOff), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)0.0, (double)(e.func_70093_af() ? -0.235 : 0.0));
            float yOff = e.func_70093_af() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.125, (double)yOff, (double)0.0);
            if (entPos[3][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[3][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[3][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[3][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[3][2] != 0.0f) {
                GL11.glRotatef((float)(entPos[3][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-yOff), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.125, (double)yOff, (double)0.0);
            if (entPos[4][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[4][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[4][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[4][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[4][2] != 0.0f) {
                GL11.glRotatef((float)(entPos[4][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)(-yOff), (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated((double)0.0, (double)0.0, (double)(e.func_70093_af() ? 0.25 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)(e.func_70093_af() ? -0.05 : 0.0), (double)(e.func_70093_af() ? -0.01725 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)-0.375, (double)((double)yOff + 0.55), (double)0.0);
            if (entPos[1][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[1][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[1][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[1][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[1][2] != 0.0f) {
                GL11.glRotatef((float)(-entPos[1][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.375, (double)((double)yOff + 0.55), (double)0.0);
            if (entPos[2][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[2][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (entPos[2][1] != 0.0f) {
                GL11.glRotatef((float)(entPos[2][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (entPos[2][2] != 0.0f) {
                GL11.glRotatef((float)(-entPos[2][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(xOff - e.field_70759_as), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)yOff + 0.55), (double)0.0);
            if (entPos[0][0] != 0.0f) {
                GL11.glRotatef((float)(entPos[0][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef((float)(e.func_70093_af() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glTranslated((double)0.0, (double)(e.func_70093_af() ? -0.16175 : 0.0), (double)(e.func_70093_af() ? -0.48025 : 0.0));
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)yOff, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)yOff, (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated((double)0.0, (double)((double)yOff + 0.55), (double)0.0);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    private void startEnd(boolean revert) {
        if (revert) {
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GL11.glEnable((int)2848);
            GlStateManager.func_179097_i();
            GlStateManager.func_179090_x();
            GL11.glHint((int)3154, (int)4354);
        } else {
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
            GL11.glDisable((int)2848);
            GlStateManager.func_179126_j();
            GlStateManager.func_179121_F();
        }
        GlStateManager.func_179132_a((!revert ? 1 : 0) != 0);
    }

    private boolean doesntContain(EntityPlayer entityPlayer) {
        return !Skeleton.mc.field_71441_e.field_73010_i.contains((Object)entityPlayer);
    }
}

