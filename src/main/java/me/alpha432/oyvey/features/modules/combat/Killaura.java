/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.DamageUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Killaura
extends Module {
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> delay = this.register(new Setting<Boolean>("Hitdelay", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> onlySharp = this.register(new Setting<Boolean>("Swordonly", true));
    public Setting<Float> raytrace = this.register(new Setting<Float>("Raytrace", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(7.0f), "Wall Range."));
    public Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    public Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    public Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    public Setting<Boolean> vehicles = this.register(new Setting<Boolean>("Entities", false));
    public Setting<Boolean> projectiles = this.register(new Setting<Boolean>("Projectiles", false));
    public Setting<Boolean> tps = this.register(new Setting<Boolean>("TPSsync", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));
    public Setting<Boolean> info = this.register(new Setting<Boolean>("Info", true));

    public Killaura() {
        super("Aura", "Punches for you", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (!this.rotate.getValue().booleanValue()) {
            this.doKillaura();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue()) {
            this.doKillaura();
        }
    }

    private void doKillaura() {
        int wait;
        if (this.onlySharp.getValue().booleanValue() && !EntityUtil.holdingWeapon((EntityPlayer)Util.mc.field_71439_g)) {
            target = null;
            return;
        }
        int n = this.delay.getValue() == false ? 0 : (wait = (int)((float)DamageUtil.getCooldownByWeapon((EntityPlayer)Util.mc.field_71439_g) * (this.tps.getValue() != false ? OyVey.serverManager.getTpsFactor() : 1.0f)));
        if (!this.timer.passedMs(wait)) {
            return;
        }
        target = this.getTarget();
        if (target == null) {
            return;
        }
        if (this.rotate.getValue().booleanValue()) {
            OyVey.rotationManager.lookAtEntity(target);
        }
        EntityUtil.attackEntity(target, this.packet.getValue(), true);
        this.timer.reset();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = this.range.getValue().floatValue();
        double maxHealth = 36.0;
        for (Entity entity : Util.mc.field_71441_e.field_73010_i) {
            if (!(this.players.getValue() != false && entity instanceof EntityPlayer || this.animals.getValue() != false && EntityUtil.isPassive(entity) || this.mobs.getValue() != false && EntityUtil.isMobAggressive(entity) || this.vehicles.getValue() != false && EntityUtil.isVehicle(entity)) && (!this.projectiles.getValue().booleanValue() || !EntityUtil.isProjectile(entity)) || entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance) || !Util.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && Util.mc.field_71439_g.func_70068_e(entity) > MathUtil.square(this.raytrace.getValue().floatValue())) continue;
            if (target == null) {
                target = entity;
                distance = Util.mc.field_71439_g.func_70068_e(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                target = entity;
                break;
            }
            if (Util.mc.field_71439_g.func_70068_e(entity) < distance) {
                target = entity;
                distance = Util.mc.field_71439_g.func_70068_e(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (!((double)EntityUtil.getHealth(entity) < maxHealth)) continue;
            target = entity;
            distance = Util.mc.field_71439_g.func_70068_e(entity);
            maxHealth = EntityUtil.getHealth(entity);
        }
        return target;
    }

    @Override
    public String getDisplayInfo() {
        if (this.info.getValue().booleanValue() && target instanceof EntityPlayer) {
            return target.func_70005_c_();
        }
        return null;
    }
}

