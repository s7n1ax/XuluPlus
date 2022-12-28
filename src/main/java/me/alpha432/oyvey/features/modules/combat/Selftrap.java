/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Selftrap
extends Module {
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Blockspertick", 8, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 250));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotated", true));
    private final Setting<Integer> disableTime = this.register(new Setting<Integer>("Disabletime", 200, 50, 300));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Toggle", true));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("Packetplace", false));
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();
    private int blocksThisTick = 0;
    private boolean isSneaking;
    private boolean hasOffhand = false;

    public Selftrap() {
        super("Selftrap", "Useful for swordfights", Module.Category.COMBAT, true, false, true);
    }

    @Override
    public void onEnable() {
        if (Selftrap.fullNullCheck()) {
            this.disable();
        }
        this.offTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.isOn() && (this.blocksPerTick.getValue() != 1 || !this.rotate.getValue().booleanValue())) {
            this.doHoleFill();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.isOn() && event.getStage() == 0 && this.blocksPerTick.getValue() == 1 && this.rotate.getValue().booleanValue()) {
            this.doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.retries.clear();
        this.hasOffhand = false;
    }

    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        for (BlockPos position : this.getPositions()) {
            int placeability = BlockUtil.isPositionPlaceable(position, false);
            if (placeability == 1 && (this.retries.get((Object)position) == null || this.retries.get((Object)position) < 4)) {
                this.placeBlock(position);
                this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
            }
            if (placeability != 3) continue;
            this.placeBlock(position);
        }
    }

    private List<BlockPos> getPositions() {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        positions.add(new BlockPos(Selftrap.mc.field_71439_g.field_70165_t, Selftrap.mc.field_71439_g.field_70163_u + 2.0, Selftrap.mc.field_71439_g.field_70161_v));
        int placeability = BlockUtil.isPositionPlaceable((BlockPos)positions.get(0), false);
        switch (placeability) {
            case 0: {
                return new ArrayList<BlockPos>();
            }
            case 3: {
                return positions;
            }
            case 1: {
                if (BlockUtil.isPositionPlaceable(positions.get(0), false, false) == 3) {
                    return positions;
                }
            }
            case 2: {
                positions.add(new BlockPos(Selftrap.mc.field_71439_g.field_70165_t + 1.0, Selftrap.mc.field_71439_g.field_70163_u + 1.0, Selftrap.mc.field_71439_g.field_70161_v));
                positions.add(new BlockPos(Selftrap.mc.field_71439_g.field_70165_t + 1.0, Selftrap.mc.field_71439_g.field_70163_u + 2.0, Selftrap.mc.field_71439_g.field_70161_v));
            }
        }
        positions.sort(Comparator.comparingDouble(Vec3i::func_177956_o));
        return positions;
    }

    private void placeBlock(BlockPos pos) {
        if (this.blocksThisTick < this.blocksPerTick.getValue()) {
            boolean smartRotate = this.blocksPerTick.getValue() == 1 && this.rotate.getValue() != false;
            int originalSlot = Selftrap.mc.field_71439_g.field_71071_by.field_70461_c;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            Selftrap.mc.field_71439_g.field_71071_by.field_70461_c = obbySlot == -1 ? eChestSot : obbySlot;
            Selftrap.mc.field_71442_b.func_78765_e();
            this.isSneaking = smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking);
            Selftrap.mc.field_71439_g.field_71071_by.field_70461_c = originalSlot;
            Selftrap.mc.field_71442_b.func_78765_e();
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }

    private boolean check() {
        if (Selftrap.fullNullCheck()) {
            this.disable();
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.blocksThisTick = 0;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (!EntityUtil.isSafe((Entity)Selftrap.mc.field_71439_g)) {
            this.offTimer.reset();
            return true;
        }
        if (this.disable.getValue().booleanValue() && this.offTimer.passedMs(this.disableTime.getValue().intValue())) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }
}

