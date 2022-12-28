/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleToggleEvent
extends Event {
    public Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public static class Disable
    extends ModuleToggleEvent {
        public Disable(Module module) {
            super(module);
        }

        public Module getModule() {
            return this.module;
        }
    }

    public static class Enable
    extends ModuleToggleEvent {
        public Enable(Module module) {
            super(module);
        }

        public Module getModule() {
            return this.module;
        }
    }
}

