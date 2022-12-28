/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.alpha432.oyvey.features.modules.movement;

import java.text.DecimalFormat;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.Speed;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class StepTwo
extends Module {
    private static StepTwo instance;
    Setting<Double> height = this.register(new Setting<Double>("height", 2.5, 0.5, 2.5));
    Setting<Mode> mode = this.register(new Setting<Mode>("mode", Mode.Vanilla));
    private int ticks = 0;

    public StepTwo() {
        super("Step[Rewr]", "s", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static StepTwo getInstance() {
        if (instance == null) {
            instance = new StepTwo();
        }
        return instance;
    }

    @Override
    public void onToggle() {
        StepTwo.mc.field_71439_g.field_70138_W = 0.6f;
    }

    @Override
    public void onUpdate() {
        if (StepTwo.mc.field_71441_e == null || StepTwo.mc.field_71439_g == null) {
            return;
        }
        if (StepTwo.mc.field_71439_g.func_70090_H() || StepTwo.mc.field_71439_g.func_180799_ab() || StepTwo.mc.field_71439_g.func_70617_f_() || StepTwo.mc.field_71474_y.field_74314_A.func_151470_d()) {
            return;
        }
        if (OyVey.moduleManager.getModuleByClass(Speed.class).isEnabled()) {
            return;
        }
        if (this.mode.getValue() == Mode.Normal) {
            double[] dir = StepTwo.forward(0.1);
            boolean twofive = false;
            boolean two = false;
            boolean onefive = false;
            boolean one = false;
            if (StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.6, dir[1])).isEmpty() && !StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.4, dir[1])).isEmpty()) {
                twofive = true;
            }
            if (StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.1, dir[1])).isEmpty() && !StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.9, dir[1])).isEmpty()) {
                two = true;
            }
            if (StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.6, dir[1])).isEmpty() && !StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.4, dir[1])).isEmpty()) {
                onefive = true;
            }
            if (StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.0, dir[1])).isEmpty() && !StepTwo.mc.field_71441_e.func_184144_a((Entity)StepTwo.mc.field_71439_g, StepTwo.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 0.6, dir[1])).isEmpty()) {
                one = true;
            }
            if (StepTwo.mc.field_71439_g.field_70123_F && (StepTwo.mc.field_71439_g.field_191988_bg != 0.0f || StepTwo.mc.field_71439_g.field_70702_br != 0.0f) && StepTwo.mc.field_71439_g.field_70122_E) {
                int i;
                if (one && this.height.getValue() >= 1.0) {
                    double[] oneOffset = new double[]{0.42, 0.753};
                    for (i = 0; i < oneOffset.length; ++i) {
                        StepTwo.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + oneOffset[i], StepTwo.mc.field_71439_g.field_70161_v, StepTwo.mc.field_71439_g.field_70122_E));
                    }
                    StepTwo.mc.field_71439_g.func_70107_b(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + 1.0, StepTwo.mc.field_71439_g.field_70161_v);
                    this.ticks = 1;
                }
                if (onefive && this.height.getValue() >= 1.5) {
                    double[] oneFiveOffset = new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
                    for (i = 0; i < oneFiveOffset.length; ++i) {
                        StepTwo.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + oneFiveOffset[i], StepTwo.mc.field_71439_g.field_70161_v, StepTwo.mc.field_71439_g.field_70122_E));
                    }
                    StepTwo.mc.field_71439_g.func_70107_b(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + 1.5, StepTwo.mc.field_71439_g.field_70161_v);
                    this.ticks = 1;
                }
                if (two && this.height.getValue() >= 2.0) {
                    double[] twoOffset = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
                    for (i = 0; i < twoOffset.length; ++i) {
                        StepTwo.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + twoOffset[i], StepTwo.mc.field_71439_g.field_70161_v, StepTwo.mc.field_71439_g.field_70122_E));
                    }
                    StepTwo.mc.field_71439_g.func_70107_b(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + 2.0, StepTwo.mc.field_71439_g.field_70161_v);
                    this.ticks = 2;
                }
                if (twofive && this.height.getValue() >= 2.5) {
                    double[] twoFiveOffset = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
                    for (i = 0; i < twoFiveOffset.length; ++i) {
                        StepTwo.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + twoFiveOffset[i], StepTwo.mc.field_71439_g.field_70161_v, StepTwo.mc.field_71439_g.field_70122_E));
                    }
                    StepTwo.mc.field_71439_g.func_70107_b(StepTwo.mc.field_71439_g.field_70165_t, StepTwo.mc.field_71439_g.field_70163_u + 2.5, StepTwo.mc.field_71439_g.field_70161_v);
                    this.ticks = 2;
                }
            }
        }
        if (this.mode.getValue() == Mode.Vanilla) {
            DecimalFormat df = new DecimalFormat("#");
            StepTwo.mc.field_71439_g.field_70138_W = Float.parseFloat(df.format(this.height.getValue()));
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onDisable() {
        StepTwo.mc.field_71439_g.field_70138_W = 0.5f;
    }

    public static double[] forward(double speed) {
        float forward = StepTwo.mc.field_71439_g.field_71158_b.field_192832_b;
        float side = StepTwo.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = StepTwo.mc.field_71439_g.field_70126_B + (StepTwo.mc.field_71439_g.field_70177_z - StepTwo.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static enum Mode {
        Vanilla,
        Normal;

    }
}

