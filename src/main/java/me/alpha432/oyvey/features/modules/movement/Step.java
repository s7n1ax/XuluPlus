/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.material.Material
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Step
extends Module {
    private static Step instance;
    final double[] twoFiveOffset = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
    private final double[] oneblockPositions = new double[]{0.42, 0.75};
    private final double[] twoblockPositions = new double[]{0.4, 0.75, 0.5, 0.41, 0.83, 1.16, 1.41, 1.57, 1.58, 1.42};
    private final double[] futurePositions = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
    private final double[] fourBlockPositions = new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43, 1.78, 1.63, 1.51, 1.9, 2.21, 2.45, 2.43, 2.78, 2.63, 2.51, 2.9, 3.21, 3.45, 3.43};
    public Setting<Boolean> vanilla = this.register(new Setting<Boolean>("Vanilla", false));
    public Setting<Float> stepHeightVanilla = this.register(new Setting<Object>("VHeight", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.vanilla.getValue()));
    public Setting<Integer> stepHeight = this.register(new Setting<Object>("Height", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(5), v -> this.vanilla.getValue() == false));
    public Setting<Boolean> small = this.register(new Setting<Object>("Offset", Boolean.valueOf(false), v -> this.stepHeight.getValue() > 1 && this.vanilla.getValue() == false));
    public Setting<Boolean> spoof = this.register(new Setting<Object>("Spoof", Boolean.valueOf(true), v -> this.vanilla.getValue() == false));
    public Setting<Integer> ticks = this.register(new Setting<Object>("Delay", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(25), v -> this.spoof.getValue() != false && this.vanilla.getValue() == false));
    public Setting<Boolean> turnOff = this.register(new Setting<Object>("Disable", Boolean.valueOf(false), v -> this.vanilla.getValue() == false));
    public Setting<Boolean> check = this.register(new Setting<Object>("Check", Boolean.valueOf(true), v -> this.vanilla.getValue() == false));
    private double[] selectedPositions = new double[0];
    private int packets;

    public Step() {
        super("Step", "Makes you step up blocks", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static Step getInstance() {
        if (instance == null) {
            instance = new Step();
        }
        return instance;
    }

    @Override
    public void onToggle() {
        Step.mc.field_71439_g.field_70138_W = 0.6f;
    }

    @Override
    public void onUpdate() {
        if (this.vanilla.getValue().booleanValue()) {
            Step.mc.field_71439_g.field_70138_W = this.stepHeightVanilla.getValue().floatValue();
            return;
        }
        switch (this.stepHeight.getValue()) {
            case 1: {
                this.selectedPositions = this.oneblockPositions;
                break;
            }
            case 2: {
                this.selectedPositions = this.small.getValue() != false ? this.twoblockPositions : this.futurePositions;
                break;
            }
            case 3: {
                this.selectedPositions = this.twoFiveOffset;
            }
            case 4: {
                this.selectedPositions = this.fourBlockPositions;
            }
        }
        if (Step.mc.field_71439_g.field_70123_F && Step.mc.field_71439_g.field_70122_E) {
            ++this.packets;
        }
        AxisAlignedBB bb = Step.mc.field_71439_g.func_174813_aQ();
        if (this.check.getValue().booleanValue()) {
            for (int x = MathHelper.func_76128_c((double)bb.field_72340_a); x < MathHelper.func_76128_c((double)(bb.field_72336_d + 1.0)); ++x) {
                for (int z = MathHelper.func_76128_c((double)bb.field_72339_c); z < MathHelper.func_76128_c((double)(bb.field_72334_f + 1.0)); ++z) {
                    Block block = Step.mc.field_71441_e.func_180495_p(new BlockPos((double)x, bb.field_72337_e + 1.0, (double)z)).func_177230_c();
                    if (block instanceof BlockAir) continue;
                    return;
                }
            }
        }
        if (Step.mc.field_71439_g.field_70122_E && !Step.mc.field_71439_g.func_70055_a(Material.field_151586_h) && !Step.mc.field_71439_g.func_70055_a(Material.field_151587_i) && Step.mc.field_71439_g.field_70124_G && Step.mc.field_71439_g.field_70143_R == 0.0f && !Step.mc.field_71474_y.field_74314_A.field_74513_e && Step.mc.field_71439_g.field_70123_F && !Step.mc.field_71439_g.func_70617_f_() && (this.packets > this.selectedPositions.length - 2 || this.spoof.getValue().booleanValue() && this.packets > this.ticks.getValue())) {
            for (double position : this.selectedPositions) {
                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + position, Step.mc.field_71439_g.field_70161_v, true));
            }
            Step.mc.field_71439_g.func_70107_b(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + this.selectedPositions[this.selectedPositions.length - 1], Step.mc.field_71439_g.field_70161_v);
            this.packets = 0;
            if (this.turnOff.getValue().booleanValue()) {
                this.disable();
            }
        }
    }
}

