/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.subsettings;

import java.util.function.Predicate;
import me.alpha432.oyvey.features.setting.Setting;

public final class BoolSetting
extends Setting {
    private final boolean defaultValue;
    private boolean parent;
    private boolean value;

    public BoolSetting(String name, boolean defaultValue, boolean parent) {
        super(name, defaultValue);
        this.parent = parent;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public BoolSetting(String name, boolean defaultValue, boolean parent, Predicate<Boolean> visible) {
        super(name, Boolean.valueOf(defaultValue), visible);
        this.parent = parent;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean isParent() {
        return this.parent;
    }

    public void toggle() {
        this.value = !this.value;
    }
}

