/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.entity.item.EntityMinecartChest
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityDispenser
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityFurnace
 *  net.minecraft.tileentity.TileEntityHopper
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.Colors;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.UtilDeLaColorXulu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;

public class StorageESP
extends Module {
    private final Setting<Float> range = this.register(new Setting<Float>("RenderDistance", Float.valueOf(50.0f), Float.valueOf(1.0f), Float.valueOf(300.0f)));
    private final Setting<Boolean> colorSync = this.register(new Setting<Boolean>("NoOutOfView", false));
    private final Setting<Boolean> chest = this.register(new Setting<Boolean>("Chests", true));
    private final Setting<Boolean> dispenser = this.register(new Setting<Boolean>("Dispensers", false));
    private final Setting<Boolean> shulker = this.register(new Setting<Boolean>("Shulkers", true));
    private final Setting<Boolean> echest = this.register(new Setting<Boolean>("Echests", true));
    private final Setting<Boolean> furnace = this.register(new Setting<Boolean>("Furnaces", false));
    private final Setting<Boolean> hopper = this.register(new Setting<Boolean>("Hoppers", false));
    private final Setting<Boolean> cart = this.register(new Setting<Boolean>("Minecart", false));
    private final Setting<Boolean> frame = this.register(new Setting<Boolean>("Item Frames", false));
    private final Setting<Boolean> box = this.register(new Setting<Boolean>("BoxRender", false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("Saturation", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));

    public StorageESP() {
        super("Search", "Seeks out item storages of all kinds", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        int color;
        BlockPos pos;
        HashMap<BlockPos, Integer> positions = new HashMap<BlockPos, Integer>();
        for (TileEntity tileEntity : StorageESP.mc.field_71441_e.field_147482_g) {
            BlockPos blockPos;
            if (!(tileEntity instanceof TileEntityChest && this.chest.getValue() != false || tileEntity instanceof TileEntityDispenser && this.dispenser.getValue() != false || tileEntity instanceof TileEntityShulkerBox && this.shulker.getValue() != false || tileEntity instanceof TileEntityEnderChest && this.echest.getValue() != false || tileEntity instanceof TileEntityFurnace && this.furnace.getValue() != false) && (!(tileEntity instanceof TileEntityHopper) || !this.hopper.getValue().booleanValue())) continue;
            pos = tileEntity.func_174877_v();
            if (!(StorageESP.mc.field_71439_g.func_174818_b(blockPos) <= MathUtil.square(this.range.getValue().floatValue())) || (color = this.getTileEntityColor(tileEntity)) == -1) continue;
            positions.put(pos, color);
        }
        for (Entity entity : StorageESP.mc.field_71441_e.field_72996_f) {
            BlockPos blockPos;
            if ((!(entity instanceof EntityItemFrame) || !this.frame.getValue().booleanValue()) && (!(entity instanceof EntityMinecartChest) || !this.cart.getValue().booleanValue())) continue;
            pos = entity.func_180425_c();
            if (!(StorageESP.mc.field_71439_g.func_174818_b(blockPos) <= MathUtil.square(this.range.getValue().floatValue())) || (color = this.getEntityColor(entity)) == -1) continue;
            positions.put(pos, color);
        }
        for (Map.Entry entry : positions.entrySet()) {
            BlockPos blockPos = (BlockPos)entry.getKey();
            color = (Integer)entry.getValue();
            RenderUtil.drawBoxESP(blockPos, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(color), false, new Color(color), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    private int getTileEntityColor(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            return UtilDeLaColorXulu.Colors.BLUE;
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return UtilDeLaColorXulu.Colors.RED;
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return UtilDeLaColorXulu.Colors.PURPLE;
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return UtilDeLaColorXulu.Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityHopper) {
            return UtilDeLaColorXulu.Colors.DARK_RED;
        }
        if (tileEntity instanceof TileEntityDispenser) {
            return UtilDeLaColorXulu.Colors.ORANGE;
        }
        return -1;
    }

    private int getEntityColor(Entity entity) {
        if (entity instanceof EntityMinecartChest) {
            return UtilDeLaColorXulu.Colors.ORANGE;
        }
        if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).func_82335_i().func_77973_b() instanceof ItemShulkerBox) {
            return UtilDeLaColorXulu.Colors.YELLOW;
        }
        if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).func_82335_i().func_77973_b() instanceof ItemShulkerBox)) {
            return UtilDeLaColorXulu.Colors.ORANGE;
        }
        return -1;
    }
}

