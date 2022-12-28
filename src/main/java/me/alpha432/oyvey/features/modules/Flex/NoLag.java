/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.Flex;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoLag
extends Module {
    private static final HashSet<Object> BLACKLIST = Sets.newHashSet((Object[])((Object[])new SoundEvent[]{SoundEvents.field_187719_p, SoundEvents.field_191258_p, SoundEvents.field_187716_o, SoundEvents.field_187725_r, SoundEvents.field_187722_q, SoundEvents.field_187713_n, SoundEvents.field_187728_s}));
    private static NoLag instance;
    public Setting<Boolean> crystals = this.register(new Setting<Boolean>("C-AntiLag", true));
    public Setting<Boolean> armor = this.register(new Setting<Boolean>("A-AntiLag", true));
    public Setting<Float> soundRange = this.register(new Setting<Float>("LagDisableRange", Float.valueOf(12.0f), Float.valueOf(0.0f), Float.valueOf(12.0f)));

    public NoLag() {
        super("-Procrastinate", "Tries to Disable Lags", Module.Category.Flex, true, false, false);
        instance = this;
    }

    public static NoLag getInstance() {
        if (instance == null) {
            instance = new NoLag();
        }
        return instance;
    }

    public static void removeEntities(SPacketSoundEffect packet, float range) {
        BlockPos pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
        ArrayList<Entity> toRemove = new ArrayList<Entity>();
        if (NoLag.fullNullCheck()) {
            return;
        }
        for (Entity entity : NoLag.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityEnderCrystal) || entity.func_174818_b(pos) > MathUtil.square(range)) continue;
            toRemove.add(entity);
        }
        for (Entity entity : toRemove) {
            entity.func_70106_y();
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event != null && event.getPacket() != null && NoLag.mc.field_71439_g != null && NoLag.mc.field_71441_e != null && event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (this.crystals.getValue().booleanValue() && packet.func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB && (AutoCrystal.getInstance().isOff() || !AutoCrystal.getInstance().sound.getValue().booleanValue() && AutoCrystal.getInstance().threadMode.getValue() != AutoCrystal.ThreadMode.SOUND)) {
                NoLag.removeEntities(packet, this.soundRange.getValue().floatValue());
            }
            if (BLACKLIST.contains((Object)packet.func_186978_a()) && this.armor.getValue().booleanValue()) {
                event.setCanceled(true);
            }
        }
    }
}

