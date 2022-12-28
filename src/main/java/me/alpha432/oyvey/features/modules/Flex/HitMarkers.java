/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.awt.Color;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class HitMarkers
extends Module {
    public final ResourceLocation image;
    private int renderTicks = 100;
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Integer> thickness = this.register(new Setting<Integer>("BoldLVL", 2, 1, 6));
    public Setting<Double> time = this.register(new Setting<Double>("Duration", 20.0, 1.0, 50.0));

    public HitMarkers() {
        super("HitMarkers", "Displays Hitmarkers", Module.Category.Flex, false, false, false);
        this.image = new ResourceLocation("hitmarker.png");
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if ((double)this.renderTicks < this.time.getValue()) {
            ScaledResolution resolution = new ScaledResolution(mc);
            this.drawHitMarkers();
        }
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!event.getEntity().equals((Object)HitMarkers.mc.field_71439_g)) {
            return;
        }
        this.renderTicks = 0;
    }

    @SubscribeEvent
    public void onTickClientTick(TickEvent event) {
        ++this.renderTicks;
    }

    public void drawHitMarkers() {
        ScaledResolution resolution = new ScaledResolution(mc);
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f - 4.0f, (float)resolution.func_78328_b() / 2.0f - 4.0f, (float)resolution.func_78326_a() / 2.0f - 8.0f, (float)resolution.func_78328_b() / 2.0f - 8.0f, this.thickness.getValue().intValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f + 4.0f, (float)resolution.func_78328_b() / 2.0f - 4.0f, (float)resolution.func_78326_a() / 2.0f + 8.0f, (float)resolution.func_78328_b() / 2.0f - 8.0f, this.thickness.getValue().intValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f - 4.0f, (float)resolution.func_78328_b() / 2.0f + 4.0f, (float)resolution.func_78326_a() / 2.0f - 8.0f, (float)resolution.func_78328_b() / 2.0f + 8.0f, this.thickness.getValue().intValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        RenderUtil.drawLine((float)resolution.func_78326_a() / 2.0f + 4.0f, (float)resolution.func_78328_b() / 2.0f + 4.0f, (float)resolution.func_78326_a() / 2.0f + 8.0f, (float)resolution.func_78328_b() / 2.0f + 8.0f, this.thickness.getValue().intValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
    }
}

