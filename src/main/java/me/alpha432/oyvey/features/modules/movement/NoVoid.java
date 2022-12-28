/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class NoVoid
extends Module {
    public NoVoid() {
        super("NoVoid", "Tries to keep you off the void", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (NoVoid.fullNullCheck()) {
            return;
        }
        if (!NoVoid.mc.field_71439_g.field_70145_X && NoVoid.mc.field_71439_g.field_70163_u <= 0.0) {
            RayTraceResult trace = NoVoid.mc.field_71441_e.func_147447_a(NoVoid.mc.field_71439_g.func_174791_d(), new Vec3d(NoVoid.mc.field_71439_g.field_70165_t, 0.0, NoVoid.mc.field_71439_g.field_70161_v), false, false, false);
            if (trace != null && trace.field_72313_a == RayTraceResult.Type.BLOCK) {
                return;
            }
            NoVoid.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
            if (NoVoid.mc.field_71439_g.func_184187_bx() != null) {
                NoVoid.mc.field_71439_g.func_184187_bx().func_70016_h(0.0, 0.0, 0.0);
            }
        }
    }
}

