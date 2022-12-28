/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 */
package me.alpha432.oyvey.features.modules.player;

import java.util.ArrayList;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Replenish
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 10));
    private final Setting<Integer> gapStack = this.register(new Setting<Integer>("Gapples", 1, 50, 64));
    private final Setting<Integer> xpStackAt = this.register(new Setting<Integer>("Crystals", 1, 50, 64));
    private final Timer timer = new Timer();
    private final ArrayList<Item> Hotbar = new ArrayList();

    public Replenish() {
        super("Replenish", "Replenishes your hotbar", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onEnable() {
        if (Replenish.fullNullCheck()) {
            return;
        }
        this.Hotbar.clear();
        for (int l_I = 0; l_I < 9; ++l_I) {
            ItemStack l_Stack = Replenish.mc.field_71439_g.field_71071_by.func_70301_a(l_I);
            if (!l_Stack.func_190926_b() && !this.Hotbar.contains((Object)l_Stack.func_77973_b())) {
                this.Hotbar.add(l_Stack.func_77973_b());
                continue;
            }
            this.Hotbar.add(Items.field_190931_a);
        }
    }

    @Override
    public void onUpdate() {
        if (Replenish.mc.field_71462_r != null) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValue() * 1000)) {
            return;
        }
        for (int l_I = 0; l_I < 9; ++l_I) {
            if (!this.RefillSlotIfNeed(l_I)) continue;
            this.timer.reset();
            return;
        }
    }

    private boolean RefillSlotIfNeed(int p_Slot) {
        ItemStack l_Stack = Replenish.mc.field_71439_g.field_71071_by.func_70301_a(p_Slot);
        if (l_Stack.func_190926_b() || l_Stack.func_77973_b() == Items.field_190931_a) {
            return false;
        }
        if (!l_Stack.func_77985_e()) {
            return false;
        }
        if (l_Stack.func_190916_E() >= l_Stack.func_77976_d()) {
            return false;
        }
        if (l_Stack.func_77973_b().equals((Object)Items.field_151153_ao) && l_Stack.func_190916_E() >= this.gapStack.getValue()) {
            return false;
        }
        if (l_Stack.func_77973_b().equals((Object)Items.field_151062_by) && l_Stack.func_190916_E() > this.xpStackAt.getValue()) {
            return false;
        }
        for (int l_I = 9; l_I < 36; ++l_I) {
            ItemStack l_Item = Replenish.mc.field_71439_g.field_71071_by.func_70301_a(l_I);
            if (l_Item.func_190926_b() || !this.CanItemBeMergedWith(l_Stack, l_Item)) continue;
            Replenish.mc.field_71442_b.func_187098_a(Replenish.mc.field_71439_g.field_71069_bz.field_75152_c, l_I, 0, ClickType.QUICK_MOVE, (EntityPlayer)Replenish.mc.field_71439_g);
            Replenish.mc.field_71442_b.func_78765_e();
            return true;
        }
        return false;
    }

    private boolean CanItemBeMergedWith(ItemStack p_Source, ItemStack p_Target) {
        return p_Source.func_77973_b() == p_Target.func_77973_b() && p_Source.func_82833_r().equals(p_Target.func_82833_r());
    }
}

