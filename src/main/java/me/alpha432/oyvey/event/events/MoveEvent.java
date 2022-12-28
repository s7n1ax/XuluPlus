/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.MoverType
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;
import net.minecraft.entity.MoverType;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class MoveEvent
extends EventStage {
    private MoverType type;
    private double x;
    private double y;
    private double z;

    public MoveEvent(int stage, MoverType type2, double x, double y, double z) {
        super(stage);
        this.type = type2;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MoverType getType() {
        return this.type;
    }

    public void setType(MoverType type2) {
        this.type = type2;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}

