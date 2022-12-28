/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
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
import net.minecraft.util.math.Vec3d;

public class Surround
extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Blockspertick", 12, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 250));
    private final Setting<Boolean> noGhost = this.register(new Setting<Boolean>("Packetplace", false));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("Center", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Surround.fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.field_71439_g);
        if (this.center.getValue().booleanValue()) {
            OyVey.positionManager.setPositionPacket((double)this.startPos.func_177958_n() + 0.5, this.startPos.func_177956_o(), (double)this.startPos.func_177952_p() + 0.5, true, true, true);
        }
        this.retries.clear();
        this.retryTimer.reset();
    }

    @Override
    public void onTick() {
        this.doFeetPlace();
    }

    @Override
    public void onDisable() {
        if (Surround.nullCheck()) {
            return;
        }
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }

    @Override
    public String getDisplayInfo() {
        switch (this.isSafe) {
            case 0: {
                return (Object)ChatFormatting.RED + "Unsafe";
            }
            case 1: {
                return (Object)ChatFormatting.YELLOW + "Safe";
            }
        }
        return (Object)ChatFormatting.GREEN + "Safe";
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtil.isSafe((Entity)Surround.mc.field_71439_g, 0, true)) {
            this.isSafe = 0;
            this.placeBlocks(Surround.mc.field_71439_g.func_174791_d(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.field_71439_g, 0, true), true, false, false);
        } else if (!EntityUtil.isSafe((Entity)Surround.mc.field_71439_g, -1, false)) {
            this.isSafe = 1;
            this.placeBlocks(Surround.mc.field_71439_g.func_174791_d(), EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.field_71439_g, -1, false), false, false, true);
        } else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d;
                array[i] = vec3d = iterator.next();
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, true), true, false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray((Entity)Surround.mc.field_71439_g, 0, true)) {
                if (!vec3d.equals((Object)pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.field_71439_g.func_174791_d().func_178787_e(vec3ds[0].func_178787_e(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        boolean gotHelp = true;
        block5: for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            BlockPos position = new BlockPos(pos).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get((Object)position) == null || this.retries.get((Object)position) < 4) {
                        this.placeBlock(position);
                        this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
                        this.retryTimer.reset();
                        continue block5;
                    }
                    if (OyVey.speedManager.getSpeedKpH() != 0.0 || isExtending || this.extenders >= 1) continue block5;
                    this.placeBlocks(Surround.mc.field_71439_g.func_174791_d().func_178787_e(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(Surround.mc.field_71439_g.func_174791_d().func_178787_e(vec3d), 0, true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    continue block5;
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block5;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) continue block5;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (Surround.nullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (this.obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + (Object)ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && Surround.mc.field_71439_g.field_71071_by.field_70461_c != this.obbySlot && Surround.mc.field_71439_g.field_71071_by.field_70461_c != echestSlot) {
            this.lastHotbarSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.field_71439_g))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            int originalSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            isPlacing = true;
            Surround.mc.field_71439_g.field_71071_by.field_70461_c = obbySlot == -1 ? eChestSot : obbySlot;
            Surround.mc.field_71442_b.func_78765_e();
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.field_71439_g.field_71071_by.field_70461_c = originalSlot;
            Surround.mc.field_71442_b.func_78765_e();
            this.didPlace = true;
            ++this.placements;
        }
    }
}

