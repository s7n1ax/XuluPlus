/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.eventbus.Subscribe
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.features.modules.Flex;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Anchor
extends Module {
    public static boolean Anchoring;
    private final Setting<Integer> pitch = this.register(new Setting<Integer>("Pitch", 60, 0, 90));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Toggle", true));
    private final Setting<Boolean> pull = this.register(new Setting<Boolean>("Pull", true));
    int holeblocks;

    public Anchor() {
        super("-HolePull", "Automatically makes u go into holes.", Module.Category.Flex, false, false, false);
    }

    public boolean isBlockHole(BlockPos blockPos) {
        this.holeblocks = 0;
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 3, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 2, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 0)).func_177230_c() == Blocks.field_150350_a) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        if (Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150343_Z || Anchor.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150357_h) {
            ++this.holeblocks;
        }
        return this.holeblocks >= 9;
    }

    public Vec3d GetCenter(double d, double d2, double d3) {
        double d4 = Math.floor(d) + 0.5;
        double d5 = Math.floor(d2);
        double d6 = Math.floor(d3) + 0.5;
        return new Vec3d(d4, d5, d6);
    }

    @Override
    @Subscribe
    public void onUpdate() {
        if (Anchor.mc.field_71441_e == null) {
            return;
        }
        if (Anchor.mc.field_71439_g.field_70125_A >= (float)this.pitch.getValue().intValue()) {
            if (this.isBlockHole(this.getPlayerPos().func_177979_c(1)) || this.isBlockHole(this.getPlayerPos().func_177979_c(2)) || this.isBlockHole(this.getPlayerPos().func_177979_c(3)) || this.isBlockHole(this.getPlayerPos().func_177979_c(4))) {
                Anchoring = true;
                if (!this.pull.getValue().booleanValue()) {
                    Anchor.mc.field_71439_g.field_70159_w = 0.0;
                    Anchor.mc.field_71439_g.field_70179_y = 0.0;
                } else {
                    Vec3d center = this.GetCenter(Anchor.mc.field_71439_g.field_70165_t, Anchor.mc.field_71439_g.field_70163_u, Anchor.mc.field_71439_g.field_70161_v);
                    double d = Math.abs(center.field_72450_a - Anchor.mc.field_71439_g.field_70165_t);
                    double d2 = Math.abs(center.field_72449_c - Anchor.mc.field_71439_g.field_70161_v);
                    if (d > 0.1 || d2 > 0.1) {
                        double d3 = center.field_72450_a - Anchor.mc.field_71439_g.field_70165_t;
                        double d4 = center.field_72449_c - Anchor.mc.field_71439_g.field_70161_v;
                        Anchor.mc.field_71439_g.field_70159_w = d3 / 2.0;
                        Anchor.mc.field_71439_g.field_70179_y = d4 / 2.0;
                    }
                }
            } else {
                Anchoring = false;
            }
        }
        if (this.disable.getValue().booleanValue() && EntityUtil.isSafe((Entity)Anchor.mc.field_71439_g)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        Anchoring = false;
        this.holeblocks = 0;
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Anchor.mc.field_71439_g.field_70165_t), Math.floor(Anchor.mc.field_71439_g.field_70163_u), Math.floor(Anchor.mc.field_71439_g.field_70161_v));
    }
}

