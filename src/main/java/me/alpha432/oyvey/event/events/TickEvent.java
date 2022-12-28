/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;

public class TickEvent
extends EventStage {
    private final int stage;

    public TickEvent(int stage) {
        this.stage = stage;
    }

    @Override
    public final int getStage() {
        return this.stage;
    }
}

