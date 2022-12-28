/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.oyvey.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoWeb
extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 250));
    private final Setting<Integer> blocksPerPlace = this.register(new Setting<Integer>("Blockspertick", 8, 1, 30));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("Packetplace", false));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Autodisable", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    private final Setting<Boolean> lowerbody = this.register(new Setting<Boolean>("Feet", true));
    private final Setting<Boolean> upperBody = this.register(new Setting<Boolean>("Face", false));
    private final Timer timer = new Timer();
    public EntityPlayer target;
    private boolean didPlace = false;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    private boolean smartRotate = false;
    private BlockPos startPos = null;

    public AutoWeb() {
        super("AutoWeeb", "Traps Weebs For The StepBro Genghius", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (AutoWeb.fullNullCheck()) {
            return;
        }
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.field_71439_g);
        this.lastHotbarSlot = AutoWeb.mc.field_71439_g.field_71071_by.field_70461_c;
    }

    @Override
    public void onTick() {
        this.smartRotate = false;
        this.doTrap();
    }

    @Override
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.func_70005_c_();
        }
        return null;
    }

    @Override
    public void onDisable() {
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }

    private void doTrap() {
        if (this.check()) {
            return;
        }
        this.doWebTrap();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void doWebTrap() {
        List<Vec3d> placeTargets = this.getPlacements();
        this.placeList(placeTargets);
    }

    private List<Vec3d> getPlacements() {
        ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        Vec3d baseVec = this.target.func_174791_d();
        if (this.lowerbody.getValue().booleanValue()) {
            list.add(baseVec);
        }
        if (this.upperBody.getValue().booleanValue()) {
            list.add(baseVec.func_72441_c(0.0, 1.0, 0.0));
        }
        return list;
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoWeb.mc.field_71439_g.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), AutoWeb.mc.field_71439_g.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue());
            if (placeability != 3 && placeability != 1) continue;
            this.placeBlock(position);
        }
    }

    private boolean check() {
        isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (this.isOff()) {
            return true;
        }
        if (this.disable.getValue().booleanValue() && !this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)AutoWeb.mc.field_71439_g))) {
            this.disable();
            return true;
        }
        if (obbySlot == -1) {
            Command.sendMessage("[" + this.getDisplayName() + "] " + (Object)ChatFormatting.LIGHT_PURPLE + "No Webs in hotbar disabling...");
            this.toggle();
            return true;
        }
        if (AutoWeb.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && AutoWeb.mc.field_71439_g.field_71071_by.field_70461_c != obbySlot) {
            this.lastHotbarSlot = AutoWeb.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(10.0);
        return this.target == null || !this.timer.passedMs(this.delay.getValue().intValue());
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoWeb.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, range) || player.field_70134_J || OyVey.speedManager.getPlayerSpeed(player) > 30.0) continue;
            if (target == null) {
                target = player;
                distance = AutoWeb.mc.field_71439_g.func_70068_e((Entity)player);
                continue;
            }
            if (!(AutoWeb.mc.field_71439_g.func_70068_e((Entity)player) < distance)) continue;
            target = player;
            distance = AutoWeb.mc.field_71439_g.func_70068_e((Entity)player);
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerPlace.getValue() && AutoWeb.mc.field_71439_g.func_174818_b(pos) <= MathUtil.square(6.0) && this.switchItem(false)) {
            isPlacing = true;
            this.isSneaking = this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking);
            this.didPlace = true;
            ++this.placements;
        }
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, InventoryUtil.Switch.NORMAL, BlockWeb.class);
        this.switchedItem = value[0];
        return value[1];
    }
}

