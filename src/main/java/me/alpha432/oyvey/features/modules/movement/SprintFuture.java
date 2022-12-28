/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SprintFuture
extends Module {
    private static SprintFuture INSTANCE = new SprintFuture();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.LEGIT));

    public SprintFuture() {
        super("SprintFuture", "Modifies sprinting", Module.Category.MOVEMENT, false, false, false);
        this.setInstance();
    }

    public static SprintFuture getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SprintFuture();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSprint(MoveEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (SprintFuture.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || SprintFuture.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if (!SprintFuture.mc.field_71474_y.field_74351_w.func_151470_d() && !SprintFuture.mc.field_71474_y.field_74368_y.func_151470_d() && !SprintFuture.mc.field_71474_y.field_74370_x.func_151470_d() && !SprintFuture.mc.field_71474_y.field_74366_z.func_151470_d() || SprintFuture.mc.field_71439_g.func_70093_af() || SprintFuture.mc.field_71439_g.field_70123_F || (float)SprintFuture.mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0f) break;
                SprintFuture.mc.field_71439_g.func_70031_b(true);
                break;
            }
            case LEGIT: {
                if (!SprintFuture.mc.field_71474_y.field_74351_w.func_151470_d() || SprintFuture.mc.field_71439_g.func_70093_af() || SprintFuture.mc.field_71439_g.func_184587_cr() || SprintFuture.mc.field_71439_g.field_70123_F || (float)SprintFuture.mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0f || SprintFuture.mc.field_71462_r != null) break;
                SprintFuture.mc.field_71439_g.func_70031_b(true);
            }
        }
    }

    @Override
    public void onDisable() {
        if (!SprintFuture.nullCheck()) {
            SprintFuture.mc.field_71439_g.func_70031_b(false);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        LEGIT,
        RAGE;

    }
}

