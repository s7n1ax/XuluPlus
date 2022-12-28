/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityWaterMob
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityThrowable
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.render;

import java.util.ArrayList;
import java.util.Objects;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.shader.FramebufferShader;
import me.alpha432.oyvey.util.shader.ShaderMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShaderChams
extends Module {
    private static ShaderChams INSTANCE;
    public Setting<ShaderMode> shader = this.register(new Setting<ShaderMode>("Shader Mode", ShaderMode.AQUA));
    public Setting<Boolean> targetParent = this.register(new Setting<Boolean>("Targets", true));
    public Setting<Boolean> players = this.register(new Setting<Boolean>("Players", Boolean.valueOf(true), s -> this.targetParent.getValue()));
    public Setting<Boolean> crystals = this.register(new Setting<Boolean>("Crystals", Boolean.valueOf(true), s -> this.targetParent.getValue()));
    public Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", Boolean.valueOf(false), s -> this.targetParent.getValue()));
    public Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", Boolean.valueOf(false), s -> this.targetParent.getValue()));

    private ShaderChams() {
        super("ShaderChams [broken]", "Europa except it does still suck", Module.Category.RENDER, true, false, false);
    }

    public static ShaderChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShaderChams();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        if (ShaderChams.nullCheck()) {
            return;
        }
        FramebufferShader framebufferShader = this.shader.getValue().getShader();
        if (framebufferShader == null) {
            return;
        }
        ArrayList<Entity> entityList = new ArrayList<Entity>();
        for (Entity entity : ShaderChams.mc.field_71441_e.field_72996_f) {
            if (entity.equals((Object)ShaderChams.mc.field_71439_g) || entity.equals((Object)mc.func_175606_aa()) || entity instanceof EntityItem || entity instanceof EntityItemFrame || entity instanceof EntityThrowable || entity instanceof EntityPlayer && !this.players.getValue().booleanValue() || entity instanceof EntityEnderCrystal && !this.crystals.getValue().booleanValue() || entity instanceof EntityMob && !this.mobs.getValue().booleanValue() || (entity instanceof EntityAnimal || entity instanceof EntitySlime || entity instanceof EntityWaterMob) && !this.animals.getValue().booleanValue()) continue;
            entityList.add(entity);
        }
        framebufferShader.startDraw(event.getPartialTicks());
        for (Entity entity : entityList) {
            Vec3d vector = MathUtil.getInterpolatedRenderPos(entity, event.getPartialTicks());
            Objects.requireNonNull(mc.func_175598_ae().func_78713_a(entity)).func_76986_a(entity, vector.field_72450_a, vector.field_72448_b, vector.field_72449_c, entity.field_70177_z, event.getPartialTicks());
        }
        framebufferShader.stopDraw();
    }
}

