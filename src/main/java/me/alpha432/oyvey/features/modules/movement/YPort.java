/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.Step;
import me.alpha432.oyvey.features.modules.movement.StepTwo;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil2;
import me.alpha432.oyvey.util.MotionUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.EntityLivingBase;

public class YPort
extends Module {
    public Setting<Boolean> useTimer = this.register(new Setting<Boolean>("Usetimer", false));
    private final Setting<Double> yPortSpeed = this.register(new Setting<Double>("Speed", 0.1, 0.0, 1.0));
    public Setting<Boolean> stepyport = this.register(new Setting<Boolean>("Step", true));
    private Timer timer = new Timer();
    private float stepheight = 2.0f;

    public YPort() {
        super("Yport", "Yports you", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onDisable() {
        this.timer.reset();
        EntityUtil2.resetTimer();
    }

    @Override
    public void onUpdate() {
        if (YPort.mc.field_71439_g.func_70093_af() || YPort.mc.field_71439_g.func_70090_H() || YPort.mc.field_71439_g.func_180799_ab() || YPort.mc.field_71439_g.func_70617_f_() || OyVey.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        if (YPort.mc.field_71439_g == null || YPort.mc.field_71441_e == null) {
            this.disable();
            return;
        }
        this.handleYPortSpeed();
        if ((!YPort.mc.field_71439_g.func_70617_f_() || YPort.mc.field_71439_g.func_70090_H() || YPort.mc.field_71439_g.func_180799_ab()) && this.stepyport.getValue().booleanValue()) {
            Step.mc.field_71439_g.field_70138_W = this.stepheight;
            StepTwo.mc.field_71439_g.field_70138_W = this.stepheight;
        }
    }

    @Override
    public void onToggle() {
        Step.mc.field_71439_g.field_70138_W = 0.6f;
        StepTwo.mc.field_71439_g.field_70138_W = 0.6f;
        YPort.mc.field_71439_g.field_70181_x = -3.0;
    }

    private void handleYPortSpeed() {
        if (!MotionUtil.isMoving((EntityLivingBase)YPort.mc.field_71439_g) || YPort.mc.field_71439_g.func_70090_H() && YPort.mc.field_71439_g.func_180799_ab() || YPort.mc.field_71439_g.field_70123_F) {
            return;
        }
        if (YPort.mc.field_71439_g.field_70122_E) {
            if (this.useTimer.getValue().booleanValue()) {
                EntityUtil2.setTimer(1.15f);
            }
            YPort.mc.field_71439_g.func_70664_aZ();
            MotionUtil.setSpeed((EntityLivingBase)YPort.mc.field_71439_g, MotionUtil.getBaseMoveSpeed() + this.yPortSpeed.getValue());
        } else {
            YPort.mc.field_71439_g.field_70181_x = -1.0;
            EntityUtil2.resetTimer();
        }
    }
}

