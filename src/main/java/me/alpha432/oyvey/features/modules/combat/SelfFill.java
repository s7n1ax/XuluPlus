/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BurrowUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class SelfFill
extends Module {
    private final Setting<Integer> offset = this.register(new Setting<Integer>("Offset", 3, -10, 10));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OBBY));
    private BlockPos originalPos;
    private int oldSlot = -1;
    Block returnBlock = null;

    public SelfFill() {
        super("Burrow", "There you have it genghius ):", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.originalPos = new BlockPos(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u, SelfFill.mc.field_71439_g.field_70161_v);
        switch (this.mode.getValue()) {
            case OBBY: {
                this.returnBlock = Blocks.field_150451_bX;
                break;
            }
            case ECHEST: {
                this.returnBlock = Blocks.field_150477_bB;
                break;
            }
            case EABypass: {
                this.returnBlock = Blocks.field_150486_ae;
            }
        }
        if (SelfFill.mc.field_71441_e.func_180495_p(new BlockPos(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u, SelfFill.mc.field_71439_g.field_70161_v)).func_177230_c().equals((Object)this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = SelfFill.mc.field_71439_g.field_71071_by.field_70461_c;
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case OBBY: {
                if (BurrowUtil.findHotbarBlock(BlockObsidian.class) != -1) break;
                Command.sendMessage("No Redstone left");
                this.disable();
                return;
            }
            case ECHEST: {
                if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) != -1) break;
                Command.sendMessage("No e-chests");
                this.disable();
                return;
            }
            case EABypass: {
                if (BurrowUtil.findHotbarBlock(BlockChest.class) != -1) break;
                Command.sendMessage("No chest");
                this.disable();
                return;
            }
        }
        BurrowUtil.switchToSlot(this.mode.getValue() == Mode.OBBY ? BurrowUtil.findHotbarBlock(BlockObsidian.class) : (this.mode.getValue() == Mode.ECHEST ? BurrowUtil.findHotbarBlock(BlockEnderChest.class) : BurrowUtil.findHotbarBlock(BlockChest.class)));
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u + 0.41999998688698, SelfFill.mc.field_71439_g.field_70161_v, true));
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u + 0.7531999805211997, SelfFill.mc.field_71439_g.field_70161_v, true));
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u + 1.00133597911214, SelfFill.mc.field_71439_g.field_70161_v, true));
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u + 1.16610926093821, SelfFill.mc.field_71439_g.field_70161_v, true));
        BurrowUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(SelfFill.mc.field_71439_g.field_70165_t, SelfFill.mc.field_71439_g.field_70163_u + (double)this.offset.getValue().intValue(), SelfFill.mc.field_71439_g.field_70161_v, false));
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfFill.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        SelfFill.mc.field_71439_g.func_70095_a(false);
        BurrowUtil.switchToSlot(this.oldSlot);
        this.toggle();
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : SelfFill.mc.field_71441_e.field_72996_f) {
            if (entity.equals((Object)SelfFill.mc.field_71439_g) || entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public static enum Mode {
        OBBY,
        ECHEST,
        EABypass;

    }
}

