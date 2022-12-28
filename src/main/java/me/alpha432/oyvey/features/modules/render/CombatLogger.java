/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import me.alpha432.oyvey.event.events.ConnectionEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.command.Command3;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.Colors;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CombatLogger
extends Module {
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Boolean> scaleing = this.register(new Setting<Boolean>("Scale", false));
    private final Setting<Float> scaling = this.register(new Setting<Float>("Size", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.scaleing.getValue()));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.FALSE, v -> this.scaleing.getValue()));
    private final Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", true));
    private final Setting<Boolean> coords = this.register(new Setting<Boolean>("Coords", true));
    private final Setting<Boolean> notification = this.register(new Setting<Boolean>("Notification", true));
    private final List<LogoutPos> spots = new CopyOnWriteArrayList<LogoutPos>();
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(300.0f), Float.valueOf(50.0f), Float.valueOf(500.0f)));
    public Setting<Boolean> message = this.register(new Setting<Boolean>("Message", false));

    public CombatLogger() {
        super("CombatLogger", "Will Expose pussies", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onLogout() {
        this.spots.clear();
    }

    @Override
    public void onDisable() {
        this.spots.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onRender3D(Render3DEvent event) {
        if (!this.spots.isEmpty()) {
            List<LogoutPos> list = this.spots;
            synchronized (list) {
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        AxisAlignedBB bb = RenderUtil.interpolateAxis(spot.getEntity().func_174813_aQ());
                        RenderUtil.drawBlockOutline(bb, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                        double x = this.interpolate(spot.getEntity().field_70142_S, spot.getEntity().field_70165_t, event.getPartialTicks()) - CombatLogger.mc.func_175598_ae().field_78725_b;
                        double y = this.interpolate(spot.getEntity().field_70137_T, spot.getEntity().field_70163_u, event.getPartialTicks()) - CombatLogger.mc.func_175598_ae().field_78726_c;
                        double z = this.interpolate(spot.getEntity().field_70136_U, spot.getEntity().field_70161_v, event.getPartialTicks()) - CombatLogger.mc.func_175598_ae().field_78723_d;
                        this.renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ());
                    }
                });
            }
        }
    }

    @Override
    public void onUpdate() {
        if (!CombatLogger.fullNullCheck()) {
            this.spots.removeIf(spot -> CombatLogger.mc.field_71439_g.func_70068_e((Entity)spot.getEntity()) >= MathUtil.square(this.range.getValue().floatValue()));
        }
    }

    @SubscribeEvent
    public void onConnection(ConnectionEvent event) {
        if (event.getStage() == 0) {
            UUID uuid = event.getUuid();
            EntityPlayer entity = CombatLogger.mc.field_71441_e.func_152378_a(uuid);
            if (entity != null && this.message.getValue().booleanValue()) {
                Command3.sendMessage("\\u00A75" + entity.func_70005_c_() + " just logged in" + (this.coords.getValue() != false ? " at (" + (int)entity.field_70165_t + ", " + (int)entity.field_70163_u + ", " + (int)entity.field_70161_v + ")!" : "!"), this.notification.getValue());
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
        } else if (event.getStage() == 1) {
            EntityPlayer entity = event.getEntity();
            UUID uuid = event.getUuid();
            String name = event.getName();
            if (this.message.getValue().booleanValue()) {
                Command3.sendMessage("\\u00A7d" + event.getName() + " just logged out" + (this.coords.getValue() != false ? " at (" + (int)entity.field_70165_t + ", " + (int)entity.field_70163_u + ", " + (int)entity.field_70161_v + ")!" : "!"), this.notification.getValue());
            }
            if (name != null && entity != null && uuid != null) {
                this.spots.add(new LogoutPos(name, uuid, entity));
            }
        }
    }

    private void renderNameTag(String name, double x, double yi, double z, float delta, double xPos, double yPos, double zPos) {
        double y = yi + 0.7;
        Entity camera = mc.func_175606_aa();
        assert (camera != null);
        double originalPositionX = camera.field_70165_t;
        double originalPositionY = camera.field_70163_u;
        double originalPositionZ = camera.field_70161_v;
        camera.field_70165_t = this.interpolate(camera.field_70169_q, camera.field_70165_t, delta);
        camera.field_70163_u = this.interpolate(camera.field_70167_r, camera.field_70163_u, delta);
        camera.field_70161_v = this.interpolate(camera.field_70166_s, camera.field_70161_v, delta);
        String displayTag = name + " XYZ: " + (int)xPos + ", " + (int)yPos + ", " + (int)zPos;
        double distance = camera.func_70011_f(x + CombatLogger.mc.func_175598_ae().field_78730_l, y + CombatLogger.mc.func_175598_ae().field_78731_m, z + CombatLogger.mc.func_175598_ae().field_78728_n);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (double)this.scaling.getValue().floatValue() * (distance * (double)this.factor.getValue().floatValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue().booleanValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue().booleanValue()) {
            scale = (double)this.scaling.getValue().floatValue() / 100.0;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a((float)1.0f, (float)-1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)((float)x), (float)((float)y + 1.4f), (float)((float)z));
        GlStateManager.func_179114_b((float)(-CombatLogger.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)CombatLogger.mc.func_175598_ae().field_78732_j, (float)(CombatLogger.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179147_l();
        if (this.rect.getValue().booleanValue()) {
            RenderUtil.drawRect(-width - 2, -(this.renderer.getFontHeight() + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.func_179084_k();
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColorHex() : ColorUtil.toRGBA(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue())));
        camera.field_70165_t = originalPositionX;
        camera.field_70163_u = originalPositionY;
        camera.field_70161_v = originalPositionZ;
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a((float)1.0f, (float)1500000.0f);
        GlStateManager.func_179121_F();
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    private static class LogoutPos {
        private final String name;
        private final UUID uuid;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;

        public LogoutPos(String name, UUID uuid, EntityPlayer entity) {
            this.name = name;
            this.uuid = uuid;
            this.entity = entity;
            this.x = entity.field_70165_t;
            this.y = entity.field_70163_u;
            this.z = entity.field_70161_v;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}

