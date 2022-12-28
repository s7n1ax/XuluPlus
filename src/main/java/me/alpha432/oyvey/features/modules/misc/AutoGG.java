/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.DeathEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.manager.FileManager;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoGG
extends Module {
    private static final String path = "Xuluplus/autoEZ.txt";
    private final Setting<Boolean> onOwnDeath = this.register(new Setting<Boolean>("OwnDeath", false));
    private final Setting<Boolean> greentext = this.register(new Setting<Boolean>("Greentext", false));
    private final Setting<Boolean> loadFiles = this.register(new Setting<Boolean>("LoadFiles", false));
    private final Setting<Integer> targetResetTimer = this.register(new Setting<Integer>("Reset", 30, 0, 90));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 10, 0, 30));
    private final Setting<Boolean> test = this.register(new Setting<Boolean>("Test", false));
    private final Timer timer = new Timer();
    private final Timer cooldownTimer = new Timer();
    public Map<EntityPlayer, Integer> targets = new ConcurrentHashMap<EntityPlayer, Integer>();
    public List<String> messages = new ArrayList<String>();
    public EntityPlayer cauraTarget;
    private boolean cooldown;

    public AutoGG() {
        super("AutoEZ", "Automatically GGs", Module.Category.MISC, true, false, false);
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        this.loadMessages();
        this.timer.reset();
        this.cooldownTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.loadFiles.getValue().booleanValue()) {
            this.loadMessages();
            Command.sendMessage("[AutoGG] Loaded messages.");
            this.loadFiles.setValue(false);
        }
        if (AutoCrystal.target != null && this.cauraTarget != AutoCrystal.target) {
            this.cauraTarget = AutoCrystal.target;
        }
        if (this.test.getValue().booleanValue()) {
            this.announceDeath((EntityPlayer)AutoGG.mc.field_71439_g);
            this.test.setValue(false);
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passedS(this.delay.getValue().intValue()) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (AutoCrystal.target != null) {
            this.targets.put(AutoCrystal.target, (int)(this.timer.getPassedTimeMs() / 1000L));
        }
        this.targets.replaceAll((p, v) -> (int)(this.timer.getPassedTimeMs() / 1000L));
        for (EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get((Object)player) <= this.targetResetTimer.getValue()) continue;
            this.targets.remove((Object)player);
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onEntityDeath(DeathEvent event) {
        if (this.targets.containsKey((Object)event.player) && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
            this.targets.remove((Object)event.player);
        }
        if (event.player == this.cauraTarget && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
        if (event.player == AutoGG.mc.field_71439_g && this.onOwnDeath.getValue().booleanValue()) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !OyVey.friendManager.isFriend(event.getEntityPlayer())) {
            this.targets.put((EntityPlayer)event.getTarget(), 0);
        }
    }

    @SubscribeEvent
    public void onSendAttackPacket(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet.func_149564_a((World)AutoGG.mc.field_71441_e) instanceof EntityPlayer && !OyVey.friendManager.isFriend((EntityPlayer)Objects.requireNonNull(packet.func_149564_a((World)AutoGG.mc.field_71441_e)))) {
            this.targets.put((EntityPlayer)packet.func_149564_a((World)AutoGG.mc.field_71441_e), 0);
        }
    }

    public void loadMessages() {
        this.messages = FileManager.readTextFileAllLines(path);
    }

    public String getRandomMessage() {
        this.loadMessages();
        Random rand = new Random();
        if (this.messages.size() == 0) {
            return "[player] Got Fucking Ezed By XuluPlus";
        }
        if (this.messages.size() == 1) {
            return this.messages.get(0);
        }
        return this.messages.get(MathUtil.clamp(rand.nextInt(this.messages.size()), 0, this.messages.size() - 1));
    }

    public void announceDeath(EntityPlayer target) {
        AutoGG.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage((this.greentext.getValue() != false ? ">" : "") + this.getRandomMessage().replaceAll("[player]", target.getDisplayNameString())));
    }
}

