/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.oyvey.util;

import java.util.HashMap;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleUtil
implements Util {
    public static BlockSafety isBlockSafe(Block block) {
        if (block == Blocks.field_150357_h) {
            return BlockSafety.UNBREAKABLE;
        }
        if (block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ) {
            return BlockSafety.RESISTANT;
        }
        return BlockSafety.BREAKABLE;
    }

    public static HoleInfo isHole(BlockPos centreBlock, boolean onlyOneWide, boolean ignoreDown) {
        HoleInfo output = new HoleInfo();
        HashMap<BlockOffset, BlockSafety> unsafeSides = HoleUtil.getUnsafeSides(centreBlock);
        if (unsafeSides.containsKey((Object)BlockOffset.DOWN) && unsafeSides.remove((Object)BlockOffset.DOWN, (Object)BlockSafety.BREAKABLE) && !ignoreDown) {
            output.setSafety(BlockSafety.BREAKABLE);
            return output;
        }
        int size = unsafeSides.size();
        unsafeSides.entrySet().removeIf(entry -> entry.getValue() == BlockSafety.RESISTANT);
        if (unsafeSides.size() != size) {
            output.setSafety(BlockSafety.RESISTANT);
        }
        if ((size = unsafeSides.size()) == 0) {
            output.setType(HoleType.SINGLE);
            output.setCentre(new AxisAlignedBB(centreBlock));
            return output;
        }
        if (size == 1 && !onlyOneWide) {
            return HoleUtil.isDoubleHole(output, centreBlock, (BlockOffset)((Object)unsafeSides.keySet().stream().findFirst().get()));
        }
        output.setSafety(BlockSafety.BREAKABLE);
        return output;
    }

    private static HoleInfo isDoubleHole(HoleInfo info, BlockPos centreBlock, BlockOffset weakSide) {
        BlockPos unsafePos = weakSide.offset(centreBlock);
        HashMap<BlockOffset, BlockSafety> unsafeSides = HoleUtil.getUnsafeSides(unsafePos);
        int size = unsafeSides.size();
        unsafeSides.entrySet().removeIf(entry -> entry.getValue() == BlockSafety.RESISTANT);
        if (unsafeSides.size() != size) {
            info.setSafety(BlockSafety.RESISTANT);
        }
        if (unsafeSides.containsKey((Object)BlockOffset.DOWN)) {
            info.setType(HoleType.CUSTOM);
            unsafeSides.remove((Object)BlockOffset.DOWN);
        }
        if (unsafeSides.size() > 1) {
            info.setType(HoleType.NONE);
            return info;
        }
        double minX = Math.min(centreBlock.func_177958_n(), unsafePos.func_177958_n());
        double maxX = Math.max(centreBlock.func_177958_n(), unsafePos.func_177958_n()) + 1;
        double minZ = Math.min(centreBlock.func_177952_p(), unsafePos.func_177952_p());
        double maxZ = Math.max(centreBlock.func_177952_p(), unsafePos.func_177952_p()) + 1;
        info.setCentre(new AxisAlignedBB(minX, (double)centreBlock.func_177956_o(), minZ, maxX, (double)(centreBlock.func_177956_o() + 1), maxZ));
        if (info.getType() != HoleType.CUSTOM) {
            info.setType(HoleType.DOUBLE);
        }
        return info;
    }

    public static HashMap<BlockOffset, BlockSafety> getUnsafeSides(BlockPos pos) {
        HashMap<BlockOffset, BlockSafety> output = new HashMap<BlockOffset, BlockSafety>();
        BlockSafety temp = HoleUtil.isBlockSafe(HoleUtil.mc.field_71441_e.func_180495_p(BlockOffset.DOWN.offset(pos)).func_177230_c());
        if (temp != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.DOWN, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.field_71441_e.func_180495_p(BlockOffset.NORTH.offset(pos)).func_177230_c())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.NORTH, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.field_71441_e.func_180495_p(BlockOffset.SOUTH.offset(pos)).func_177230_c())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.SOUTH, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.field_71441_e.func_180495_p(BlockOffset.EAST.offset(pos)).func_177230_c())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.EAST, temp);
        }
        if ((temp = HoleUtil.isBlockSafe(HoleUtil.mc.field_71441_e.func_180495_p(BlockOffset.WEST.offset(pos)).func_177230_c())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.WEST, temp);
        }
        return output;
    }

    public static enum BlockOffset {
        DOWN(0, -1, 0),
        UP(0, 1, 0),
        NORTH(0, 0, -1),
        EAST(1, 0, 0),
        SOUTH(0, 0, 1),
        WEST(-1, 0, 0);

        private final int x;
        private final int y;
        private final int z;

        private BlockOffset(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockPos offset(BlockPos pos) {
            return pos.func_177982_a(this.x, this.y, this.z);
        }

        public BlockPos forward(BlockPos pos, int scale) {
            return pos.func_177982_a(this.x * scale, 0, this.z * scale);
        }

        public BlockPos backward(BlockPos pos, int scale) {
            return pos.func_177982_a(-this.x * scale, 0, -this.z * scale);
        }

        public BlockPos left(BlockPos pos, int scale) {
            return pos.func_177982_a(this.z * scale, 0, -this.x * scale);
        }

        public BlockPos right(BlockPos pos, int scale) {
            return pos.func_177982_a(-this.z * scale, 0, this.x * scale);
        }
    }

    public static class HoleInfo {
        private HoleType type;
        private BlockSafety safety;
        private AxisAlignedBB centre;

        public HoleInfo() {
            this(BlockSafety.UNBREAKABLE, HoleType.NONE);
        }

        public HoleInfo(BlockSafety safety, HoleType type2) {
            this.type = type2;
            this.safety = safety;
        }

        public void setType(HoleType type2) {
            this.type = type2;
        }

        public void setSafety(BlockSafety safety) {
            this.safety = safety;
        }

        public void setCentre(AxisAlignedBB centre) {
            this.centre = centre;
        }

        public HoleType getType() {
            return this.type;
        }

        public BlockSafety getSafety() {
            return this.safety;
        }

        public AxisAlignedBB getCentre() {
            return this.centre;
        }
    }

    public static enum HoleType {
        SINGLE,
        DOUBLE,
        CUSTOM,
        NONE;

    }

    public static enum BlockSafety {
        UNBREAKABLE,
        RESISTANT,
        BREAKABLE;

    }
}

