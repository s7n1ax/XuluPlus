/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.util;

@FunctionalInterface
public interface SafeRunnable
extends Runnable {
    public void runSafely() throws Throwable;

    @Override
    default public void run() {
        try {
            this.runSafely();
        }
        catch (Throwable t) {
            this.handle(t);
        }
    }

    default public void handle(Throwable t) {
        t.printStackTrace();
    }
}

