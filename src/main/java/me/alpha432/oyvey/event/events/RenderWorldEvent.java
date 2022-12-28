/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;

public class RenderWorldEvent
extends EventStage {
    private final float partialTicks;

    public RenderWorldEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

