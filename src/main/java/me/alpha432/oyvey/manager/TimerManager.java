/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;

public class TimerManager
extends Feature {
    private float timer = 1.0f;

    public void init() {
    }

    public void unload() {
        this.timer = 1.0f;
    }

    public void update() {
    }

    public void setTimer(float timer) {
        if (timer > 0.0f) {
            this.timer = timer;
        }
    }

    public float getTimer() {
        return this.timer;
    }

    @Override
    public void reset() {
        this.timer = 1.0f;
    }
}

