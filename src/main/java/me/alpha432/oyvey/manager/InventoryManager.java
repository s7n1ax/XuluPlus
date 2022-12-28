/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.util.Util;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryManager
implements Util {
    public int currentPlayerItem;
    private int recoverySlot = -1;

    public void update() {
        if (this.recoverySlot != -1) {
            InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.recoverySlot == 8 ? 7 : this.recoverySlot + 1));
            InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.recoverySlot));
            int i = InventoryManager.mc.field_71439_g.field_71071_by.field_70461_c = this.recoverySlot;
            if (i != this.currentPlayerItem) {
                this.currentPlayerItem = i;
                InventoryManager.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.currentPlayerItem));
            }
            this.recoverySlot = -1;
        }
    }

    public void recoverSilent(int slot) {
        this.recoverySlot = slot;
    }
}

