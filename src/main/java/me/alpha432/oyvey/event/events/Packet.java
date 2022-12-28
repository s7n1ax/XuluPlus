/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.alpha432.oyvey.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Packet
extends Event {
    private Object packet;
    private Type type;

    public Packet(Object packet, Type type2) {
        this.packet = packet;
        this.type = type2;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    public void setType(Type type2) {
        this.type = type2;
    }

    public Object getPacket() {
        return this.packet;
    }

    public Type getType() {
        return this.type;
    }

    public static enum Type {
        INCOMING,
        OUTGOING;

    }
}

