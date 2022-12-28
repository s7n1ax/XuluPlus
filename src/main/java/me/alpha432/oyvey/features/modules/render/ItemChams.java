/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraftforge.client.event.RenderHandEvent
 *  org.lwjgl.opengl.Display
 */
package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.ColorSetting;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.mixin.mixins.IEntityRenderer;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.OyColor;
import me.alpha432.oyvey.util.PlayerUtil;
import me.alpha432.oyvey.util.shader.FramebufferShader;
import me.alpha432.oyvey.util.shader.ShaderMode;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderHandEvent;
import org.lwjgl.opengl.Display;

public class ItemChams
extends Module {
    private static ItemChams INSTANCE;
    public Setting<Boolean> chams = new Setting<Boolean>("Chams", true);
    public Setting<ShaderMode> shader = this.register(new Setting<ShaderMode>("Shader Mode", ShaderMode.AQUA));
    public Setting<Boolean> animation = this.register(new Setting<Boolean>("Animation", true));
    public Setting<Integer> animationSpeed = this.register(new Setting<Integer>("Animation Speed", 1, 1, 10));
    public ColorSetting color = new ColorSetting("Color", new OyColor(230, 101, 255, 1));
    public Setting<Float> radius = this.register(new Setting<Float>("Glow Radius", Float.valueOf(3.3f), Float.valueOf(1.0f), Float.valueOf(10.0f)));
    public Setting<Float> divider = this.register(new Setting<Float>("Glow Divider", Float.valueOf(158.6f), Float.valueOf(1.0f), Float.valueOf(1000.0f)));
    public Setting<Float> maxSample = this.register(new Setting<Float>("Glow MaxSample", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(20.0f)));
    private Boolean criticalSection = false;

    private ItemChams() {
        super("ItemChams", "Whateverthefuck", Module.Category.RENDER, true, false, false);
    }

    public static ItemChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemChams();
        }
        return INSTANCE;
    }

    @Override
    public void onRenderHand(RenderHandEvent event) {
        if (!this.criticalSection.booleanValue()) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if ((Display.isActive() || Display.isVisible()) && this.chams.getValue().booleanValue()) {
            if (EntityUtil.posEqualsBlock(PlayerUtil.getPlayerPos().func_177984_a(), (Block)Blocks.field_150355_j).booleanValue()) {
                return;
            }
            FramebufferShader shader = this.shader.getValue().getShader();
            if (shader == null) {
                return;
            }
            shader.setShaderParams(this.animation.getValue(), this.animationSpeed.getValue(), this.color.getValue(), this.radius.getValue().floatValue(), this.divider.getValue().floatValue(), this.maxSample.getValue().floatValue());
            this.criticalSection = true;
            shader.startDraw(mc.func_184121_ak());
            ((IEntityRenderer)ItemChams.mc.field_71460_t).invokeRenderHand(mc.func_184121_ak(), 2);
            shader.stopDraw();
            this.criticalSection = false;
        }
    }
}

