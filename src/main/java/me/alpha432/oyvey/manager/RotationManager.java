/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RotationManager
extends Feature {
    private float yaw;
    private float pitch;

    public void updateRotations() {
        this.yaw = RotationManager.mc.field_71439_g.field_70177_z;
        this.pitch = RotationManager.mc.field_71439_g.field_70125_A;
    }

    public void restoreRotations() {
        RotationManager.mc.field_71439_g.field_70177_z = this.yaw;
        RotationManager.mc.field_71439_g.field_70759_as = this.yaw;
        RotationManager.mc.field_71439_g.field_70125_A = this.pitch;
    }

    public void setPlayerRotations(float yaw, float pitch) {
        RotationManager.mc.field_71439_g.field_70177_z = yaw;
        RotationManager.mc.field_71439_g.field_70759_as = yaw;
        RotationManager.mc.field_71439_g.field_70125_A = pitch;
    }

    public void setPlayerYaw(float yaw) {
        RotationManager.mc.field_71439_g.field_70177_z = yaw;
        RotationManager.mc.field_71439_g.field_70759_as = yaw;
    }

    public void lookAtPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(RotationManager.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() + 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(Vec3d vec3d) {
        float[] angle = MathUtil.calcAngle(RotationManager.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(double x, double y, double z) {
        Vec3d vec3d = new Vec3d(x, y, z);
        this.lookAtVec3d(vec3d);
    }

    public void lookAtEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(RotationManager.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174824_e(mc.func_184121_ak()));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void setPlayerPitch(float pitch) {
        RotationManager.mc.field_71439_g.field_70125_A = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public int getDirection4D() {
        return RotationUtil.getDirection4D();
    }

    public String getDirection4D(boolean northRed) {
        return RotationUtil.getDirection4D(northRed);
    }
}

