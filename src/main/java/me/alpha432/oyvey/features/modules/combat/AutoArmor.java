/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemStack
 */
package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Module {
    private final Setting<Integer> delay = this.register(new Setting<Integer>("delays", 50, 0, 500));
    private final Setting<Boolean> curse = this.register(new Setting<Boolean>("vanishing", false));
    private final Setting<Boolean> mendingTakeOff = this.register(new Setting<Boolean>("automend", false));
    private final Setting<Integer> closestEnemy = this.register(new Setting<Object>("enemy", Integer.valueOf(8), Integer.valueOf(1), Integer.valueOf(20), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> repair = this.register(new Setting<Object>("repair%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.mendingTakeOff.getValue()));
    private final Setting<Integer> actions = this.register(new Setting<Integer>("packets", 3, 1, 12));
    private final Timer timer = new Timer();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final List<Integer> doneSlots = new ArrayList<Integer>();
    boolean flag;

    public AutoArmor() {
        super("AutoArmor", "Puts Armor on you and successfully mending it", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onLogin() {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.flag = false;
    }

    @Override
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }

    @Override
    public void onTick() {
        if (AutoArmor.fullNullCheck() || AutoArmor.mc.field_71462_r instanceof GuiContainer && !(AutoArmor.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        if (this.taskList.isEmpty()) {
            int slot;
            ItemStack feet;
            int slot2;
            ItemStack legging;
            int slot3;
            ItemStack chest;
            int slot4;
            if (this.mendingTakeOff.getValue().booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmor.mc.field_71474_y.field_74313_G.func_151470_d() && AutoArmor.mc.field_71441_e.field_73010_i.stream().noneMatch(e -> e != AutoArmor.mc.field_71439_g && !OyVey.friendManager.isFriend(e.func_70005_c_()) && AutoArmor.mc.field_71439_g.func_70032_d((Entity)e) <= (float)this.closestEnemy.getValue().intValue()) && !this.flag) {
                int goods;
                int dam;
                int takeOff = 0;
                for (Map.Entry<Integer, ItemStack> armorSlot : this.getArmor().entrySet()) {
                    ItemStack stack = armorSlot.getValue();
                    float percent = (float)this.repair.getValue().intValue() / 100.0f;
                    dam = Math.round((float)stack.func_77958_k() * percent);
                    if (dam >= (goods = stack.func_77958_k() - stack.func_77952_i())) continue;
                    ++takeOff;
                }
                if (takeOff == 4) {
                    this.flag = true;
                }
                if (!this.flag) {
                    ItemStack itemStack1 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
                    if (!itemStack1.field_190928_g) {
                        int goods2;
                        float percent = (float)this.repair.getValue().intValue() / 100.0f;
                        int dam2 = Math.round((float)itemStack1.func_77958_k() * percent);
                        if (dam2 < (goods2 = itemStack1.func_77958_k() - itemStack1.func_77952_i())) {
                            this.takeOffSlot(5);
                        }
                    }
                    ItemStack itemStack2 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
                    if (!itemStack2.field_190928_g) {
                        int goods3;
                        float percent = (float)this.repair.getValue().intValue() / 100.0f;
                        int dam3 = Math.round((float)itemStack2.func_77958_k() * percent);
                        if (dam3 < (goods3 = itemStack2.func_77958_k() - itemStack2.func_77952_i())) {
                            this.takeOffSlot(6);
                        }
                    }
                    ItemStack itemStack3 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
                    if (!itemStack3.field_190928_g) {
                        float percent = (float)this.repair.getValue().intValue() / 100.0f;
                        dam = Math.round((float)itemStack3.func_77958_k() * percent);
                        if (dam < (goods = itemStack3.func_77958_k() - itemStack3.func_77952_i())) {
                            this.takeOffSlot(7);
                        }
                    }
                    ItemStack itemStack4 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
                    if (!itemStack4.field_190928_g) {
                        int goods4;
                        float percent = (float)this.repair.getValue().intValue() / 100.0f;
                        int dam4 = Math.round((float)itemStack4.func_77958_k() * percent);
                        if (dam4 < (goods4 = itemStack4.func_77958_k() - itemStack4.func_77952_i())) {
                            this.takeOffSlot(8);
                        }
                    }
                }
                return;
            }
            this.flag = false;
            ItemStack helm = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
            if (helm.func_77973_b() == Items.field_190931_a && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(5, slot4);
            }
            if ((chest = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(6, slot3);
            }
            if ((legging = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(7, slot2);
            }
            if ((feet = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue(), true)) != -1) {
                this.getSlotOn(8, slot);
            }
        }
        if (this.timer.passedMs((int)((float)this.delay.getValue().intValue() * OyVey.serverManager.getTpsFactor()))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.getValue(); ++i) {
                    InventoryUtil.Task task = this.taskList.poll();
                    if (task == null) continue;
                    task.run();
                }
            }
            this.timer.reset();
        }
    }

    private void takeOffSlot(int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (int i : InventoryUtil.findEmptySlots(true)) {
                if (this.doneSlots.contains(target)) continue;
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                this.taskList.add(new InventoryUtil.Task(slot));
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    private void getSlotOn(int slot, int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object)target);
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    private Map<Integer, ItemStack> getArmor() {
        return this.getInventorySlots(5, 8);
    }

    private Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)AutoArmor.mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
}

