/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class BlockEvent
extends EventStage {
    public BlockPos pos;
    public EnumFacing facing;

    public BlockEvent(int stage, BlockPos pos, EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }
}

