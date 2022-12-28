/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class PositionManager
extends Feature {
    private double x;
    private double y;
    private double z;
    private boolean onground;

    public void updatePosition() {
        this.x = PositionManager.mc.field_71439_g.field_70165_t;
        this.y = PositionManager.mc.field_71439_g.field_70163_u;
        this.z = PositionManager.mc.field_71439_g.field_70161_v;
        this.onground = PositionManager.mc.field_71439_g.field_70122_E;
    }

    public void restorePosition() {
        PositionManager.mc.field_71439_g.field_70165_t = this.x;
        PositionManager.mc.field_71439_g.field_70163_u = this.y;
        PositionManager.mc.field_71439_g.field_70161_v = this.z;
        PositionManager.mc.field_71439_g.field_70122_E = this.onground;
    }

    public void setPlayerPosition(double x, double y, double z) {
        PositionManager.mc.field_71439_g.field_70165_t = x;
        PositionManager.mc.field_71439_g.field_70163_u = y;
        PositionManager.mc.field_71439_g.field_70161_v = z;
    }

    public void setPlayerPosition(double x, double y, double z, boolean onground) {
        PositionManager.mc.field_71439_g.field_70165_t = x;
        PositionManager.mc.field_71439_g.field_70163_u = y;
        PositionManager.mc.field_71439_g.field_70161_v = z;
        PositionManager.mc.field_71439_g.field_70122_E = onground;
    }

    public void setPositionPacket(double x, double y, double z, boolean onGround, boolean setPos, boolean noLagBack) {
        PositionManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, onGround));
        if (setPos) {
            PositionManager.mc.field_71439_g.func_70107_b(x, y, z);
            if (noLagBack) {
                this.updatePosition();
            }
        }
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

