/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class TpsSync
extends Module {
    private static TpsSync INSTANCE = new TpsSync();
    public Setting<Boolean> attack = this.register(new Setting<Boolean>("attack", Boolean.FALSE));
    public Setting<Boolean> mining = this.register(new Setting<Boolean>("mine", Boolean.TRUE));

    public TpsSync() {
        super("TPSsync", "Syncs your client with the TPS.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static TpsSync getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TpsSync();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

