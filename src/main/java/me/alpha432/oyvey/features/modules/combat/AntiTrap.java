/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiTrap
extends Module {
    public static Set<BlockPos> placedPos = new HashSet<BlockPos>();
    private final Setting<Integer> coolDown = this.register(new Setting<Integer>("CoolDown", 400, 0, 1000));
    private final Setting<InventoryUtil.Switch> switchMode = this.register(new Setting<InventoryUtil.Switch>("Switch", InventoryUtil.Switch.NORMAL));
    private final Vec3d[] surroundTargets = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, 1.0)};
    public Setting<Rotate> rotate = this.register(new Setting<Rotate>("Rotate", Rotate.NORMAL));
    public Setting<Boolean> sortY = this.register(new Setting<Boolean>("SortY", true));
    private int lastHotbarSlot = -1;
    private boolean switchedItem;
    private boolean offhand = false;

    public AntiTrap() {
        super("AntiTrap", "Places a crystal to prevent you getting trapped.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        if (AntiTrap.fullNullCheck()) {
            return;
        }
        this.switchItem(true);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (!AntiTrap.fullNullCheck() && event.getStage() == 0) {
            this.doAntiTrap();
        }
    }

    public void doAntiTrap() {
        this.offhand = AntiTrap.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        boolean bl = this.offhand;
        if (!this.offhand && InventoryUtil.findHotbarBlock(ItemEndCrystal.class) == -1) {
            this.disable();
            return;
        }
        this.lastHotbarSlot = AntiTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        ArrayList<Vec3d> targets = new ArrayList<Vec3d>();
        Collections.addAll(targets, BlockUtil.convertVec3ds(AntiTrap.mc.field_71439_g.func_174791_d(), this.surroundTargets));
        EntityPlayer closestPlayer = EntityUtil.getClosestEnemy(6.0);
        if (closestPlayer != null) {
            targets.sort((vec3d, vec3d2) -> Double.compare(closestPlayer.func_70092_e(vec3d2.field_72450_a, vec3d2.field_72448_b, vec3d2.field_72449_c), closestPlayer.func_70092_e(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c)));
            if (this.sortY.getValue().booleanValue()) {
                targets.sort(Comparator.comparingDouble(vec3d -> vec3d.field_72448_b));
            }
        }
        for (Vec3d vec3d3 : targets) {
            BlockPos pos = new BlockPos(vec3d3);
            if (!BlockUtil.canPlaceCrystal(pos)) continue;
            this.placeCrystal(pos);
            this.disable();
            break;
        }
    }

    private void placeCrystal(BlockPos pos) {
        boolean mainhand = AntiTrap.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP;
        boolean bl = mainhand;
        if (!(mainhand || this.offhand || this.switchItem(false))) {
            this.disable();
            return;
        }
        RayTraceResult result = AntiTrap.mc.field_71441_e.func_72933_a(new Vec3d(AntiTrap.mc.field_71439_g.field_70165_t, AntiTrap.mc.field_71439_g.field_70163_u + (double)AntiTrap.mc.field_71439_g.func_70047_e(), AntiTrap.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        EnumFacing facing = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        float[] angle = MathUtil.calcAngle(AntiTrap.mc.field_71439_g.func_174824_e(Util.mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
        switch (this.rotate.getValue()) {
            case NONE: {
                break;
            }
            case NORMAL: {
                OyVey.rotationManager.setPlayerRotations(angle[0], angle[1]);
                break;
            }
            case PACKET: {
                AntiTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angle[0], (float)MathHelper.func_180184_b((int)((int)angle[1]), (int)360), AntiTrap.mc.field_71439_g.field_70122_E));
            }
        }
        placedPos.add(pos);
        AntiTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        AntiTrap.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
    }

    private boolean switchItem(boolean back) {
        if (this.offhand) {
            return true;
        }
        boolean[] value = InventoryUtil.switchItemToItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), Items.field_185158_cP);
        this.switchedItem = value[0];
        return value[1];
    }

    public static enum Rotate {
        NONE,
        NORMAL,
        PACKET;

    }
}

