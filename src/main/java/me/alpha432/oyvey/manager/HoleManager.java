/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.alpha432.oyvey.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleManager
extends Feature {
    private static final BlockPos[] surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    private final List<BlockPos> midSafety = new ArrayList<BlockPos>();
    private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
    private List<BlockPos> holes = new ArrayList<BlockPos>();
    private ScheduledExecutorService executorService;
    private Thread thread;

    public void update() {
        if (!HoleManager.fullNullCheck()) {
            this.holes = this.calcHoles();
        }
    }

    public void settingChanged() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
    }

    public List<BlockPos> getHoles() {
        return this.holes;
    }

    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }

    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.field_71439_g.func_174818_b(hole)));
        return this.getHoles();
    }

    public List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        List<BlockPos> positions = BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.field_71439_g), 6.0f, 6, false, true, 0);
        for (BlockPos pos : positions) {
            if (!HoleManager.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals((Object)Blocks.field_150350_a) || !HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals((Object)Blocks.field_150350_a) || !HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals((Object)Blocks.field_150350_a)) continue;
            boolean isSafe = true;
            boolean midSafe = true;
            for (BlockPos offset : surroundOffset) {
                Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
                if (BlockUtil.isBlockUnSolid(block)) {
                    midSafe = false;
                }
                if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ) continue;
                isSafe = false;
            }
            if (isSafe) {
                safeSpots.add(pos);
            }
            if (!midSafe) continue;
            this.midSafety.add(pos);
        }
        return safeSpots;
    }

    public boolean isSafe(BlockPos pos) {
        boolean isSafe = true;
        for (BlockPos offset : surroundOffset) {
            Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
            if (block == Blocks.field_150357_h) continue;
            isSafe = false;
            break;
        }
        return isSafe;
    }
}

