/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickItem
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Keyboard
 */
package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.Flight;
import me.alpha432.oyvey.features.modules.movement.Phase;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown
extends Module {
    private static final KeyBinding[] keys = new KeyBinding[]{NoSlowDown.mc.field_71474_y.field_74351_w, NoSlowDown.mc.field_71474_y.field_74368_y, NoSlowDown.mc.field_71474_y.field_74370_x, NoSlowDown.mc.field_71474_y.field_74366_z, NoSlowDown.mc.field_71474_y.field_74314_A, NoSlowDown.mc.field_71474_y.field_151444_V};
    private static NoSlowDown INSTANCE = new NoSlowDown();
    public final Setting<Double> webHorizontalFactor = this.register(new Setting<Double>("WebMoved", 2.0, 0.0, 100.0));
    public final Setting<Double> webVerticalFactor = this.register(new Setting<Double>("WebVertical", 2.0, 0.0, 100.0));
    public Setting<Boolean> guiMove = this.register(new Setting<Boolean>("Gui", true));
    public Setting<Boolean> noSlow = this.register(new Setting<Boolean>("NoSlow", true));
    public Setting<Boolean> soulSand = this.register(new Setting<Boolean>("SoulSand", true));
    public Setting<Boolean> strict = this.register(new Setting<Boolean>("Strict(2b)", false));
    public Setting<Boolean> sneakPacket = this.register(new Setting<Boolean>("SneakPacket", false));
    public Setting<Boolean> endPortal = this.register(new Setting<Boolean>("EndSpoofed", false));
    public Setting<Boolean> webs = this.register(new Setting<Boolean>("Webs", false));
    private boolean sneaking;

    public NoSlowDown() {
        super("NoSlow", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static NoSlowDown getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoSlowDown();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.guiMove.getValue().booleanValue()) {
            for (KeyBinding bind : keys) {
                KeyBinding.func_74510_a((int)bind.func_151463_i(), (boolean)Keyboard.isKeyDown((int)bind.func_151463_i()));
            }
            if (NoSlowDown.mc.field_71462_r == null) {
                for (KeyBinding bind : keys) {
                    if (Keyboard.isKeyDown((int)bind.func_151463_i())) continue;
                    KeyBinding.func_74510_a((int)bind.func_151463_i(), (boolean)false);
                }
            }
        }
        if (this.webs.getValue().booleanValue() && OyVey.moduleManager.getModuleByClass(Flight.class).isDisabled() && OyVey.moduleManager.getModuleByClass(Phase.class).isDisabled() && NoSlowDown.mc.field_71439_g.field_70134_J) {
            NoSlowDown.mc.field_71439_g.field_70159_w *= this.webHorizontalFactor.getValue().doubleValue();
            NoSlowDown.mc.field_71439_g.field_70179_y *= this.webHorizontalFactor.getValue().doubleValue();
            NoSlowDown.mc.field_71439_g.field_70181_x *= this.webVerticalFactor.getValue().doubleValue();
        }
        NoSlowDown.mc.field_71439_g.func_184607_cu().func_77973_b();
        if (this.sneaking && !NoSlowDown.mc.field_71439_g.func_184587_cr() && this.sneakPacket.getValue().booleanValue()) {
            NoSlowDown.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)NoSlowDown.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            this.sneaking = false;
        }
    }

    @SubscribeEvent
    public void onUseItem(PlayerInteractEvent.RightClickItem event) {
        Item item = NoSlowDown.mc.field_71439_g.func_184586_b(event.getHand()).func_77973_b();
        if ((item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion && this.sneakPacket.getValue().booleanValue()) && !this.sneaking) {
            NoSlowDown.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)NoSlowDown.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            this.sneaking = true;
        }
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (this.noSlow.getValue().booleanValue() && NoSlowDown.mc.field_71439_g.func_184587_cr() && !NoSlowDown.mc.field_71439_g.func_184218_aH()) {
            event.getMovementInput().field_78902_a *= 5.0f;
            event.getMovementInput().field_192832_b *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.strict.getValue().booleanValue() && this.noSlow.getValue().booleanValue() && NoSlowDown.mc.field_71439_g.func_184587_cr() && !NoSlowDown.mc.field_71439_g.func_184218_aH()) {
            NoSlowDown.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(NoSlowDown.mc.field_71439_g.field_70165_t), Math.floor(NoSlowDown.mc.field_71439_g.field_70163_u), Math.floor(NoSlowDown.mc.field_71439_g.field_70161_v)), EnumFacing.DOWN));
        }
    }
}

