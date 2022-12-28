/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.Flex;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class NoHitBoxFix
extends Module {
    private static NoHitBoxFix INSTANCE = new NoHitBoxFix();
    public Setting<Boolean> pickaxe = this.register(new Setting<Boolean>("Pickaxe", true));
    public Setting<Boolean> crystal = this.register(new Setting<Boolean>("Crystal", true));
    public Setting<Boolean> gapple = this.register(new Setting<Boolean>("Gapple", true));

    public NoHitBoxFix() {
        super("NoHitBoxFix", "NoHitBox.", Module.Category.Flex, false, false, false);
        this.setInstance();
    }

    public static NoHitBoxFix getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NoHitBoxFix();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

