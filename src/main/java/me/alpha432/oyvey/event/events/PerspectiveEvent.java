/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;

public class PerspectiveEvent
extends EventStage {
    private float aspect;

    public PerspectiveEvent(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return this.aspect;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }
}

