/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.alpha432.oyvey.event.events;

import java.util.UUID;
import me.alpha432.oyvey.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class ConnectionEvent
extends EventStage {
    private final UUID uuid;
    private final EntityPlayer entity;
    private final String name;

    public ConnectionEvent(int stage, UUID uuid, String name) {
        super(stage);
        this.uuid = uuid;
        this.name = name;
        this.entity = null;
    }

    public ConnectionEvent(int stage, EntityPlayer entity, UUID uuid, String name) {
        super(stage);
        this.entity = entity;
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}

