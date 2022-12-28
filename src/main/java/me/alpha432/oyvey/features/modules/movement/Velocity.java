/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.IceSpeed;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
extends Module {
    private static Velocity INSTANCE = new Velocity();
    public Setting<Boolean> knockBack = this.register(new Setting<Boolean>("KB", true));
    public Setting<Boolean> noPush = this.register(new Setting<Boolean>("NoPush", true));
    public Setting<Float> horizontal = this.register(new Setting<Float>("H-", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    public Setting<Float> vertical = this.register(new Setting<Float>("V-", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(100.0f)));
    public Setting<Boolean> explosions = this.register(new Setting<Boolean>("Explosions", true));
    public Setting<Boolean> bobbers = this.register(new Setting<Boolean>("Bobbers", true));
    public Setting<Boolean> water = this.register(new Setting<Boolean>("Water", false));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", false));
    public Setting<Boolean> ice = this.register(new Setting<Boolean>("Ice", false));

    public Velocity() {
        super("Velocity", "Allows you to control your velocity", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static Velocity getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Velocity();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (IceSpeed.getINSTANCE().isOff() && this.ice.getValue().booleanValue()) {
            Blocks.field_150432_aD.field_149765_K = 0.6f;
            Blocks.field_150403_cj.field_149765_K = 0.6f;
            Blocks.field_185778_de.field_149765_K = 0.6f;
        }
    }

    @Override
    public void onDisable() {
        if (IceSpeed.getINSTANCE().isOff()) {
            Blocks.field_150432_aD.field_149765_K = 0.98f;
            Blocks.field_150403_cj.field_149765_K = 0.98f;
            Blocks.field_185778_de.field_149765_K = 0.98f;
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getStage() == 0 && Velocity.mc.field_71439_g != null) {
            Entity entity;
            SPacketEntityStatus packet;
            SPacketEntityVelocity velocity;
            if (this.knockBack.getValue().booleanValue() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = (SPacketEntityVelocity)event.getPacket()).func_149412_c() == Velocity.mc.field_71439_g.field_145783_c) {
                if (this.horizontal.getValue().floatValue() == 0.0f && this.vertical.getValue().floatValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }
                velocity.field_149415_b = (int)((float)velocity.field_149415_b * this.horizontal.getValue().floatValue());
                velocity.field_149416_c = (int)((float)velocity.field_149416_c * this.vertical.getValue().floatValue());
                velocity.field_149414_d = (int)((float)velocity.field_149414_d * this.horizontal.getValue().floatValue());
            }
            if (event.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue().booleanValue() && (packet = (SPacketEntityStatus)event.getPacket()).func_149160_c() == 31 && (entity = packet.func_149161_a((World)Velocity.mc.field_71441_e)) instanceof EntityFishHook) {
                EntityFishHook fishHook = (EntityFishHook)entity;
                if (fishHook.field_146043_c == Velocity.mc.field_71439_g) {
                    event.setCanceled(true);
                }
            }
            if (this.explosions.getValue().booleanValue() && event.getPacket() instanceof SPacketExplosion) {
                if (this.horizontal.getValue().floatValue() == 0.0f && this.vertical.getValue().floatValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }
                SPacketExplosion velocity_ = (SPacketExplosion)event.getPacket();
                velocity_.field_149152_f *= this.horizontal.getValue().floatValue();
                velocity_.field_149153_g *= this.vertical.getValue().floatValue();
                velocity_.field_149159_h *= this.horizontal.getValue().floatValue();
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 0 && this.noPush.getValue().booleanValue() && event.entity.equals((Object)Velocity.mc.field_71439_g)) {
            if (this.horizontal.getValue().floatValue() == 0.0f && this.vertical.getValue().floatValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }
            event.x = -event.x * (double)this.horizontal.getValue().floatValue();
            event.y = -event.y * (double)this.vertical.getValue().floatValue();
            event.z = -event.z * (double)this.horizontal.getValue().floatValue();
        } else if (event.getStage() == 1 && this.blocks.getValue().booleanValue()) {
            event.setCanceled(true);
        } else if (event.getStage() == 2 && this.water.getValue().booleanValue() && Velocity.mc.field_71439_g != null && Velocity.mc.field_71439_g.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
}

