/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESP
extends Module {
    private static ESP INSTANCE = new ESP();
    private final Setting<Boolean> items = this.register(new Setting<Boolean>("items", false));
    private final Setting<Boolean> xporbs = this.register(new Setting<Boolean>("xporbs", false));
    private final Setting<Boolean> xpbottles = this.register(new Setting<Boolean>("xpbottles", false));
    private final Setting<Boolean> pearl = this.register(new Setting<Boolean>("pearls", false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("blue", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("boxalpha", 120, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("alpha", 255, 0, 255));

    public ESP() {
        super("ESP", "Render things for you", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    public static ESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        AxisAlignedBB bb;
        Vec3d interp;
        int i;
        if (this.items.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityItem) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.xporbs.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityXPOrb) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.pearl.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityEnderPearl) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
        if (this.xpbottles.getValue().booleanValue()) {
            i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityExpBottle) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
            }
        }
    }
}

