/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.potion.PotionUtils
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.util.List;
import java.util.Objects;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.SeconddaryInventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.PotionUtils;

public class SelfShoot
extends Module {
    private final Setting<Integer> tickDelay = this.register(new Setting<Integer>("TickDelay", 3, 0, 8));

    public SelfShoot() {
        super("SelfShoot", "shoots arrows at you", Module.Category.Flex, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (SelfShoot.mc.field_71439_g != null) {
            List<Integer> arrowSlots;
            if (SelfShoot.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && SelfShoot.mc.field_71439_g.func_184587_cr() && SelfShoot.mc.field_71439_g.func_184612_cw() >= this.tickDelay.getValue()) {
                SelfShoot.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(SelfShoot.mc.field_71439_g.field_71109_bG, -90.0f, SelfShoot.mc.field_71439_g.field_70122_E));
                SelfShoot.mc.field_71442_b.func_78766_c((EntityPlayer)SelfShoot.mc.field_71439_g);
            }
            if ((arrowSlots = SeconddaryInventoryUtil.getItemInventory(Items.field_185167_i)).get(0) == -1) {
                return;
            }
            int speedSlot = -1;
            int strengthSlot = -1;
            for (Integer slot : arrowSlots) {
                if (PotionUtils.func_185191_c((ItemStack)SelfShoot.mc.field_71439_g.field_71071_by.func_70301_a(slot.intValue())).getRegistryName().func_110623_a().contains("swiftness")) {
                    speedSlot = slot;
                    continue;
                }
                if (!Objects.requireNonNull(PotionUtils.func_185191_c((ItemStack)SelfShoot.mc.field_71439_g.field_71071_by.func_70301_a(slot.intValue())).getRegistryName()).func_110623_a().contains("strength")) continue;
                strengthSlot = slot;
            }
        }
    }

    @Override
    public void onEnable() {
    }

    private int findBow() {
        return SeconddaryInventoryUtil.getItemHotbar((Item)Items.field_151031_f);
    }
}

