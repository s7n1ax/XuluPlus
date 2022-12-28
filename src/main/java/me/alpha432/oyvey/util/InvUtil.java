/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.inventory.GuiCrafting
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAir
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.alpha432.oyvey.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InvUtil
implements Util {
    public static void switchToHotbarSlot(int slot, boolean silent) {
        if (InvUtil.mc.field_71439_g.field_71071_by.field_70461_c == slot || slot < 0) {
            return;
        }
        if (silent) {
            InvUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            InvUtil.mc.field_71442_b.func_78765_e();
        } else {
            InvUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            InvUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            InvUtil.mc.field_71442_b.func_78765_e();
        }
    }

    public static void switchToHotbarSlot(Class clazz, boolean silent) {
        int slot = InvUtil.findHotbarBlock(clazz);
        if (slot > -1) {
            InvUtil.switchToHotbarSlot(slot, silent);
        }
    }

    public static boolean isNull(ItemStack stack) {
        return stack == null || stack.func_77973_b() instanceof ItemAir;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InvUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (clazz.isInstance((Object)stack.func_77973_b())) {
                return i;
            }
            if (!(stack.func_77973_b() instanceof ItemBlock) || !clazz.isInstance((Object)((ItemBlock)stack.func_77973_b()).func_179223_d())) continue;
            return i;
        }
        return -1;
    }

    public static int findAnyBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InvUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InvUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || ((ItemBlock)stack.func_77973_b()).func_179223_d() != blockIn) continue;
            return i;
        }
        return -1;
    }

    public static int getItemHotbar(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = InvUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)item) != Item.func_150891_b((Item)input)) continue;
            return i;
        }
        return -1;
    }

    public static int findStackInventory(Item input) {
        return InvUtil.findStackInventory(input, false);
    }

    public static int findStackInventory(Item input, boolean withHotbar) {
        int i;
        int n = i = withHotbar ? 0 : 9;
        while (i < 36) {
            Item item = InvUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)input) == Item.func_150891_b((Item)item)) {
                return i + (i < 9 ? 36 : 0);
            }
            ++i;
        }
        return -1;
    }

    public static int findItemInventorySlot(Item item, boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().func_77973_b() != item || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static List<Integer> findEmptySlots(boolean withXCarry) {
        ArrayList<Integer> outPut = new ArrayList<Integer>();
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().field_190928_g && entry.getValue().func_77973_b() != Items.field_190931_a) continue;
            outPut.add(entry.getKey());
        }
        if (withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InvUtil.mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
                ItemStack craftingStack = craftingSlot.func_75211_c();
                if (!craftingStack.func_190926_b() && craftingStack.func_77973_b() != Items.field_190931_a) continue;
                outPut.add(i);
            }
        }
        return outPut;
    }

    public static int findInventoryBlock(Class clazz, boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!InvUtil.isBlock(entry.getValue().func_77973_b(), clazz) || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static int findInventoryWool(boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().func_77973_b() instanceof ItemBlock)) continue;
            ItemBlock wool = (ItemBlock)entry.getValue().func_77973_b();
            if (wool.func_179223_d().field_149764_J != Material.field_151580_n || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static int findEmptySlot() {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().func_190926_b()) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static boolean isBlock(Item item, Class clazz) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).func_179223_d();
            return clazz.isInstance((Object)block);
        }
        return false;
    }

    public static void confirmSlot(int slot) {
        InvUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
        InvUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        InvUtil.mc.field_71442_b.func_78765_e();
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        if (InvUtil.mc.field_71462_r instanceof GuiCrafting) {
            return InvUtil.fuckYou3arthqu4kev2(10, 45);
        }
        return InvUtil.getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)InvUtil.mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
        }
        return fullInventorySlots;
    }

    private static Map<Integer, ItemStack> fuckYou3arthqu4kev2(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)InvUtil.mc.field_71439_g.field_71070_bA.func_75138_a().get(current));
        }
        return fullInventorySlots;
    }

    public static boolean[] switchItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode2, Class clazz) {
        boolean[] switchedItemSwitched = new boolean[]{switchedItem, false};
        switch (mode2) {
            case NORMAL: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.findHotbarBlock(clazz), false);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    InvUtil.switchToHotbarSlot(lastHotbarSlot, false);
                    switchedItemSwitched[0] = false;
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case SILENT: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.findHotbarBlock(clazz), true);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    switchedItemSwitched[0] = false;
                    OyVey.inventoryManager.recoverSilent(lastHotbarSlot);
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case NONE: {
                switchedItemSwitched[1] = back || InvUtil.mc.field_71439_g.field_71071_by.field_70461_c == InvUtil.findHotbarBlock(clazz);
            }
        }
        return switchedItemSwitched;
    }

    public static boolean[] switchItemToItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode2, Item item) {
        boolean[] switchedItemSwitched = new boolean[]{switchedItem, false};
        switch (mode2) {
            case NORMAL: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.getItemHotbar(item), false);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    InvUtil.switchToHotbarSlot(lastHotbarSlot, false);
                    switchedItemSwitched[0] = false;
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case SILENT: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.getItemHotbar(item), true);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    switchedItemSwitched[0] = false;
                    OyVey.inventoryManager.recoverSilent(lastHotbarSlot);
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case NONE: {
                switchedItemSwitched[1] = back || InvUtil.mc.field_71439_g.field_71071_by.field_70461_c == InvUtil.getItemHotbar(item);
            }
        }
        return switchedItemSwitched;
    }

    public static boolean holdingItem(Class clazz) {
        ItemStack stack = InvUtil.mc.field_71439_g.func_184614_ca();
        boolean result = InvUtil.isInstanceOf(stack, clazz);
        if (!result) {
            InvUtil.mc.field_71439_g.func_184592_cb();
            result = InvUtil.isInstanceOf(stack, clazz);
        }
        return result;
    }

    public static boolean isInstanceOf(ItemStack stack, Class clazz) {
        if (stack == null) {
            return false;
        }
        Item item = stack.func_77973_b();
        if (clazz.isInstance((Object)item)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            Block block = Block.func_149634_a((Item)item);
            return clazz.isInstance((Object)block);
        }
        return false;
    }

    public static int getEmptyXCarry() {
        for (int i = 1; i < 5; ++i) {
            Slot craftingSlot = (Slot)InvUtil.mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
            ItemStack craftingStack = craftingSlot.func_75211_c();
            if (!craftingStack.func_190926_b() && craftingStack.func_77973_b() != Items.field_190931_a) continue;
            return i;
        }
        return -1;
    }

    public static boolean isSlotEmpty(int i) {
        Slot slot = (Slot)InvUtil.mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
        ItemStack stack = slot.func_75211_c();
        return stack.func_190926_b();
    }

    public static int convertHotbarToInv(int input) {
        return 36 + input;
    }

    public static boolean areStacksCompatible(ItemStack stack1, ItemStack stack2) {
        if (!stack1.func_77973_b().equals((Object)stack2.func_77973_b())) {
            return false;
        }
        if (stack1.func_77973_b() instanceof ItemBlock && stack2.func_77973_b() instanceof ItemBlock) {
            Block block1 = ((ItemBlock)stack1.func_77973_b()).func_179223_d();
            Block block2 = ((ItemBlock)stack2.func_77973_b()).func_179223_d();
            if (!block1.field_149764_J.equals((Object)block2.field_149764_J)) {
                return false;
            }
        }
        if (!stack1.func_82833_r().equals(stack2.func_82833_r())) {
            return false;
        }
        return stack1.func_77952_i() == stack2.func_77952_i();
    }

    public static EntityEquipmentSlot getEquipmentFromSlot(int slot) {
        if (slot == 5) {
            return EntityEquipmentSlot.HEAD;
        }
        if (slot == 6) {
            return EntityEquipmentSlot.CHEST;
        }
        if (slot == 7) {
            return EntityEquipmentSlot.LEGS;
        }
        return EntityEquipmentSlot.FEET;
    }

    public static int findArmorSlot(EntityEquipmentSlot type2, boolean binding) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            boolean cursed;
            ItemStack s = Minecraft.func_71410_x().field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c();
            if (s.func_77973_b() == Items.field_190931_a || !(s.func_77973_b() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)s.func_77973_b();
            if (armor.field_77881_a != type2) continue;
            float currentDamage = armor.field_77879_b + EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_180310_c, (ItemStack)s);
            boolean bl = cursed = binding && EnchantmentHelper.func_190938_b((ItemStack)s);
            if (!(currentDamage > damage) || cursed) continue;
            damage = currentDamage;
            slot = i;
        }
        return slot;
    }

    public static int findArmorSlot(EntityEquipmentSlot type2, boolean binding, boolean withXCarry) {
        int slot = InvUtil.findArmorSlot(type2, binding);
        if (slot == -1 && withXCarry) {
            float damage = 0.0f;
            for (int i = 1; i < 5; ++i) {
                boolean cursed;
                Slot craftingSlot = (Slot)InvUtil.mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
                ItemStack craftingStack = craftingSlot.func_75211_c();
                if (craftingStack.func_77973_b() == Items.field_190931_a || !(craftingStack.func_77973_b() instanceof ItemArmor)) continue;
                ItemArmor armor = (ItemArmor)craftingStack.func_77973_b();
                if (armor.field_77881_a != type2) continue;
                float currentDamage = armor.field_77879_b + EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_180310_c, (ItemStack)craftingStack);
                boolean bl = cursed = binding && EnchantmentHelper.func_190938_b((ItemStack)craftingStack);
                if (!(currentDamage > damage) || cursed) continue;
                damage = currentDamage;
                slot = i;
            }
        }
        return slot;
    }

    public static int findItemInventorySlot(Item item, boolean offHand, boolean withXCarry) {
        int slot = InvUtil.findItemInventorySlot(item, offHand);
        if (slot == -1 && withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InvUtil.mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
                ItemStack craftingStack = craftingSlot.func_75211_c();
                if (craftingStack.func_77973_b() == Items.field_190931_a || craftingStack.func_77973_b() != item) continue;
                slot = i;
            }
        }
        return slot;
    }

    public static int findBlockSlotInventory(Class<BlockShulkerBox> clazz, boolean offHand, boolean withXCarry) {
        int slot = InvUtil.findInventoryBlock(clazz, offHand);
        if (slot == -1 && withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InvUtil.mc.field_71439_g.field_71069_bz.field_75151_b.get(i);
                ItemStack craftingStack = craftingSlot.func_75211_c();
                if (craftingStack.func_77973_b() == Items.field_190931_a) continue;
                Item craftingStackItem = craftingStack.func_77973_b();
                if (clazz.isInstance((Object)craftingStackItem)) {
                    slot = i;
                    continue;
                }
                if (!(craftingStackItem instanceof ItemBlock) || !clazz.isInstance((Object)((ItemBlock)craftingStackItem).func_179223_d())) continue;
                slot = i;
            }
        }
        return slot;
    }

    public static class Task {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;

        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }

        public Task(int slot) {
            this.slot = slot;
            this.quickClick = false;
            this.update = false;
        }

        public Task(int slot, boolean quickClick) {
            this.slot = slot;
            this.quickClick = quickClick;
            this.update = false;
        }

        public void run() {
            if (this.update) {
                Util.mc.field_71442_b.func_78765_e();
            }
            if (this.slot != -1) {
                Util.mc.field_71442_b.func_187098_a(Util.mc.field_71439_g.field_71069_bz.field_75152_c, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)Util.mc.field_71439_g);
            }
        }

        public boolean isSwitching() {
            return !this.update;
        }
    }

    public static enum Switch {
        NORMAL,
        SILENT,
        NONE;

    }
}

