/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.render;

import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Map;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class ArrowESP
extends Module {
    private final Setting<Integer> red = this.register(new Setting<Integer>("red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("blue", 255, 0, 255));
    private final Setting<Integer> radius = this.register(new Setting<Integer>("placement", 45, 10, 200));
    private final Setting<Float> size = this.register(new Setting<Float>("size", Float.valueOf(10.0f), Float.valueOf(5.0f), Float.valueOf(25.0f)));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("outline", true));
    private final Setting<Float> outlineWidth = this.register(new Setting<Float>("outlinewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    private final Setting<Integer> fadeDistance = this.register(new Setting<Integer>("range", 100, 10, 200));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("invisibles", false));
    private final EntityListener entityListener = new EntityListener();

    public ArrowESP() {
        super("ArrowESP", "Arrow tracers ", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        this.entityListener.render();
        ArrowESP.mc.field_71441_e.field_72996_f.forEach(o -> {
            if (o instanceof EntityPlayer && this.isValid((EntityPlayer)o)) {
                EntityPlayer entity = (EntityPlayer)o;
                Vec3d pos = this.entityListener.getEntityLowerBounds().get((Object)entity);
                if (pos != null && !this.isOnScreen(pos) && !RenderUtil.isInViewFrustrum((Entity)entity)) {
                    Color color = EntityUtil.getColor((Entity)entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), (int)MathHelper.func_76131_a((float)(255.0f - 255.0f / (float)this.fadeDistance.getValue().intValue() * ArrowESP.mc.field_71439_g.func_70032_d((Entity)entity)), (float)100.0f, (float)255.0f), true);
                    int x = Display.getWidth() / 2 / (ArrowESP.mc.field_71474_y.field_74335_Z == 0 ? 1 : ArrowESP.mc.field_71474_y.field_74335_Z);
                    int y = Display.getHeight() / 2 / (ArrowESP.mc.field_71474_y.field_74335_Z == 0 ? 1 : ArrowESP.mc.field_71474_y.field_74335_Z);
                    float yaw = this.getRotations((EntityLivingBase)entity) - ArrowESP.mc.field_71439_g.field_70177_z;
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)yaw, (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                    RenderUtil.drawTracerPointer(x, y - this.radius.getValue(), this.size.getValue().floatValue(), 2.0f, 1.0f, this.outline.getValue(), this.outlineWidth.getValue().floatValue(), color.getRGB());
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)(-yaw), (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                }
            }
        });
    }

    private boolean isOnScreen(Vec3d pos) {
        int n3;
        int n2;
        int n;
        if (!(pos.field_72450_a > -1.0)) {
            return false;
        }
        if (!(pos.field_72448_b < 1.0)) {
            return false;
        }
        if (!(pos.field_72450_a > -1.0)) {
            return false;
        }
        if (!(pos.field_72449_c < 1.0)) {
            return false;
        }
        int n4 = n = ArrowESP.mc.field_71474_y.field_74335_Z == 0 ? 1 : ArrowESP.mc.field_71474_y.field_74335_Z;
        if (!(pos.field_72450_a / (double)n >= 0.0)) {
            return false;
        }
        int n5 = n2 = ArrowESP.mc.field_71474_y.field_74335_Z == 0 ? 1 : ArrowESP.mc.field_71474_y.field_74335_Z;
        if (!(pos.field_72450_a / (double)n2 <= (double)Display.getWidth())) {
            return false;
        }
        int n6 = n3 = ArrowESP.mc.field_71474_y.field_74335_Z == 0 ? 1 : ArrowESP.mc.field_71474_y.field_74335_Z;
        if (!(pos.field_72448_b / (double)n3 >= 0.0)) {
            return false;
        }
        int n42 = ArrowESP.mc.field_71474_y.field_74335_Z == 0 ? 1 : ArrowESP.mc.field_71474_y.field_74335_Z;
        return pos.field_72448_b / (double)n42 <= (double)Display.getHeight();
    }

    private boolean isValid(EntityPlayer entity) {
        return entity != ArrowESP.mc.field_71439_g && (!entity.func_82150_aj() || this.invisibles.getValue() != false) && entity.func_70089_S();
    }

    private float getRotations(EntityLivingBase ent) {
        double x = ent.field_70165_t - ArrowESP.mc.field_71439_g.field_70165_t;
        double z = ent.field_70161_v - ArrowESP.mc.field_71439_g.field_70161_v;
        return (float)(-(Math.atan2(x, z) * 57.29577951308232));
    }

    private static class EntityListener {
        private final Map<Entity, Vec3d> entityUpperBounds = Maps.newHashMap();
        private final Map<Entity, Vec3d> entityLowerBounds = Maps.newHashMap();

        private EntityListener() {
        }

        private void render() {
            if (!this.entityUpperBounds.isEmpty()) {
                this.entityUpperBounds.clear();
            }
            if (!this.entityLowerBounds.isEmpty()) {
                this.entityLowerBounds.clear();
            }
            for (Entity e : Util.mc.field_71441_e.field_72996_f) {
                Vec3d bound = this.getEntityRenderPosition(e);
                bound.func_178787_e(new Vec3d(0.0, (double)e.field_70131_O + 0.2, 0.0));
                Vec3d upperBounds = RenderUtil.to2D(bound.field_72450_a, bound.field_72448_b, bound.field_72449_c);
                Vec3d lowerBounds = RenderUtil.to2D(bound.field_72450_a, bound.field_72448_b - 2.0, bound.field_72449_c);
                if (upperBounds == null || lowerBounds == null) continue;
                this.entityUpperBounds.put(e, upperBounds);
                this.entityLowerBounds.put(e, lowerBounds);
            }
        }

        private Vec3d getEntityRenderPosition(Entity entity) {
            double partial = Util.mc.field_71428_T.field_194147_b;
            double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partial - Util.mc.func_175598_ae().field_78730_l;
            double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partial - Util.mc.func_175598_ae().field_78731_m;
            double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partial - Util.mc.func_175598_ae().field_78728_n;
            return new Vec3d(x, y, z);
        }

        public Map<Entity, Vec3d> getEntityLowerBounds() {
            return this.entityLowerBounds;
        }
    }
}

