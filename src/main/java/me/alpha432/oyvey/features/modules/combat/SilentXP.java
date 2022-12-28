/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.world.World
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Bind;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.InvUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class SilentXP
extends Module {
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.MIDDLECLICK));
    public Setting<Boolean> antiFriend = this.register(new Setting<Boolean>("AntiFR", true));
    public Setting<Bind> key = this.register(new Setting<Bind>("Key", new Bind(-1), v -> this.mode.getValue() != Mode.MIDDLECLICK));
    public Setting<Boolean> groundOnly = this.register(new Setting<Boolean>("Belowhorizon", false));
    private boolean last;
    private boolean on;

    public SilentXP() {
        super("SilentXP", "Silently mends your armor", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (SilentXP.fullNullCheck()) {
            return;
        }
        switch (this.mode.getValue()) {
            case PRESS: {
                if (!this.key.getValue().isDown()) break;
                this.throwXP(false);
                break;
            }
            case TOGGLE: {
                if (!this.toggled()) break;
                this.throwXP(false);
                break;
            }
            default: {
                if (this.groundOnly.getValue().booleanValue() && SilentXP.mc.field_71439_g.field_70125_A < 0.0f) {
                    return;
                }
                if (!Mouse.isButtonDown((int)2)) break;
                this.throwXP(true);
            }
        }
    }

    private boolean toggled() {
        if (this.key.getValue().getKey() == -1) {
            return false;
        }
        if (!Keyboard.isKeyDown((int)this.key.getValue().getKey())) {
            this.last = true;
        } else {
            if (Keyboard.isKeyDown((int)this.key.getValue().getKey()) && this.last && !this.on) {
                this.last = false;
                this.on = true;
                return this.on;
            }
            if (Keyboard.isKeyDown((int)this.key.getValue().getKey()) && this.last && this.on) {
                this.last = false;
                this.on = false;
                return this.on;
            }
        }
        return this.on;
    }

    private void throwXP(boolean mcf) {
        boolean offhand;
        RayTraceResult result;
        if (mcf && this.antiFriend.getValue().booleanValue() && (result = SilentXP.mc.field_71476_x) != null && result.field_72313_a == RayTraceResult.Type.ENTITY && result.field_72308_g instanceof EntityPlayer) {
            return;
        }
        int xpSlot = InvUtil.findHotbarBlock(ItemExpBottle.class);
        boolean bl = offhand = SilentXP.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151062_by;
        if (xpSlot != -1 || offhand) {
            int oldslot = SilentXP.mc.field_71439_g.field_71071_by.field_70461_c;
            if (!offhand) {
                InvUtil.switchToHotbarSlot(xpSlot, false);
            }
            SilentXP.mc.field_71442_b.func_187101_a((EntityPlayer)SilentXP.mc.field_71439_g, (World)SilentXP.mc.field_71441_e, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InvUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }

    public static enum Mode {
        MIDDLECLICK,
        TOGGLE,
        PRESS;

    }
}

