/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.movement;

import io.netty.util.internal.ConcurrentSet;
import java.util.Set;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Phase
extends Module {
    private static Phase INSTANCE = new Phase();
    private final Set<CPacketPlayer> packets = new ConcurrentSet();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.PACKETFLY));
    public Setting<PacketFlyMode> type = this.register(new Setting<Object>("Types", (Object)PacketFlyMode.SETBACK, v -> this.mode.getValue() == Mode.PACKETFLY));
    public Setting<Integer> yMove = this.register(new Setting<Object>("Ylvl", 625, 1, 1000, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK, "YMovement speed."));
    public Setting<Boolean> extra = this.register(new Setting<Object>("AdditionalPacket", Boolean.TRUE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Integer> offset = this.register(new Setting<Object>("Offset", 1337, -1337, 1337, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.extra.getValue() != false, "Up speed."));
    public Setting<Boolean> fallPacket = this.register(new Setting<Object>("FallOffset", Boolean.TRUE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> teleporter = this.register(new Setting<Object>("Tp", Boolean.TRUE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> boundingBox = this.register(new Setting<Object>("BoundingBox", Boolean.TRUE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Integer> teleportConfirm = this.register(new Setting<Object>("Confirmed", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> ultraPacket = this.register(new Setting<Object>("MultiPacketSend", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> updates = this.register(new Setting<Object>("Updates", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> setOnMove = this.register(new Setting<Object>("SetMove", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> cliperino = this.register(new Setting<Object>("NotThroughWalls", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.setOnMove.getValue() != false));
    public Setting<Boolean> scanPackets = this.register(new Setting<Object>("ScanPackets", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> resetConfirm = this.register(new Setting<Object>("Reset", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> posLook = this.register(new Setting<Object>("POSLook", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> cancel = this.register(new Setting<Object>("Cancel", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false));
    public Setting<Boolean> cancelType = this.register(new Setting<Object>("SetYaw", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false && this.cancel.getValue() != false));
    public Setting<Boolean> onlyY = this.register(new Setting<Object>("OnlyY", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false));
    public Setting<Integer> cancelPacket = this.register(new Setting<Object>("Packets", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(20), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false));
    private boolean teleport = true;
    private int teleportIds;
    private int posLookPackets;

    public Phase() {
        super("Phase", "Lets You Phase Through Blocks", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static Phase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Phase();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.packets.clear();
        this.posLookPackets = 0;
        if (Phase.mc.field_71439_g != null) {
            if (this.resetConfirm.getValue().booleanValue()) {
                this.teleportIds = 0;
            }
            Phase.mc.field_71439_g.field_70145_X = false;
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.setOnMove.getValue().booleanValue() && this.type.getValue() == PacketFlyMode.SETBACK && event.getStage() == 0 && !mc.func_71356_B() && this.mode.getValue() == Mode.PACKETFLY) {
            event.setX(Phase.mc.field_71439_g.field_70159_w);
            event.setY(Phase.mc.field_71439_g.field_70181_x);
            event.setZ(Phase.mc.field_71439_g.field_70179_y);
            if (this.cliperino.getValue().booleanValue()) {
                Phase.mc.field_71439_g.field_70145_X = true;
            }
        }
        if (this.type.getValue() == PacketFlyMode.NONE || event.getStage() != 0 || mc.func_71356_B() || this.mode.getValue() != Mode.PACKETFLY) {
            return;
        }
        if (!this.boundingBox.getValue().booleanValue() && !this.updates.getValue().booleanValue()) {
            this.doPhase(event);
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 1 && this.type.getValue() != PacketFlyMode.NONE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMove(UpdateWalkingPlayerEvent event) {
        if (Phase.fullNullCheck() || event.getStage() != 0 || this.type.getValue() != PacketFlyMode.SETBACK || this.mode.getValue() != Mode.PACKETFLY) {
            return;
        }
        if (this.boundingBox.getValue().booleanValue()) {
            this.doBoundingBox();
        } else if (this.updates.getValue().booleanValue()) {
            this.doPhase(null);
        }
    }

    private void doPhase(MoveEvent event) {
        if (this.type.getValue() == PacketFlyMode.SETBACK && !this.boundingBox.getValue().booleanValue()) {
            double[] dirSpeed = this.getMotion(this.teleport ? (double)this.yMove.getValue().intValue() / 10000.0 : (double)(this.yMove.getValue() - 1) / 10000.0);
            double posX = Phase.mc.field_71439_g.field_70165_t + dirSpeed[0];
            double posY = Phase.mc.field_71439_g.field_70163_u + (Phase.mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? (double)this.yMove.getValue().intValue() / 10000.0 : (double)(this.yMove.getValue() - 1) / 10000.0) : 1.0E-8) - (Phase.mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? (double)this.yMove.getValue().intValue() / 10000.0 : (double)(this.yMove.getValue() - 1) / 10000.0) : 2.0E-8);
            double posZ = Phase.mc.field_71439_g.field_70161_v + dirSpeed[1];
            CPacketPlayer.PositionRotation packetPlayer = new CPacketPlayer.PositionRotation(posX, posY, posZ, Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, false);
            this.packets.add((CPacketPlayer)packetPlayer);
            Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)packetPlayer);
            if (this.teleportConfirm.getValue() != 3) {
                Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds - 1));
                ++this.teleportIds;
            }
            if (this.extra.getValue().booleanValue()) {
                CPacketPlayer.PositionRotation packet = new CPacketPlayer.PositionRotation(Phase.mc.field_71439_g.field_70165_t, (double)this.offset.getValue().intValue() + Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v, Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, true);
                this.packets.add((CPacketPlayer)packet);
                Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet);
            }
            if (this.teleportConfirm.getValue() != 1) {
                Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds + 1));
                ++this.teleportIds;
            }
            if (this.ultraPacket.getValue().booleanValue()) {
                CPacketPlayer.PositionRotation packet2 = new CPacketPlayer.PositionRotation(posX, posY, posZ, Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, false);
                this.packets.add((CPacketPlayer)packet2);
                Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet2);
            }
            if (this.teleportConfirm.getValue() == 4) {
                Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportIds));
                ++this.teleportIds;
            }
            if (this.fallPacket.getValue().booleanValue()) {
                Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Phase.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            Phase.mc.field_71439_g.func_70107_b(posX, posY, posZ);
            boolean bl = this.teleport = this.teleporter.getValue() == false || !this.teleport;
            if (event != null) {
                event.setX(0.0);
                event.setY(0.0);
                event.setX(0.0);
            } else {
                Phase.mc.field_71439_g.field_70159_w = 0.0;
                Phase.mc.field_71439_g.field_70181_x = 0.0;
                Phase.mc.field_71439_g.field_70179_y = 0.0;
            }
        }
    }

    private void doBoundingBox() {
        double[] dirSpeed = this.getMotion(this.teleport ? (double)0.0225f : (double)0.0224f);
        Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(Phase.mc.field_71439_g.field_70165_t + dirSpeed[0], Phase.mc.field_71439_g.field_70163_u + (Phase.mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? 0.0625 : 0.0624) : 1.0E-8) - (Phase.mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? 0.0625 : 0.0624) : 2.0E-8), Phase.mc.field_71439_g.field_70161_v + dirSpeed[1], Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, false));
        Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(Phase.mc.field_71439_g.field_70165_t, -1337.0, Phase.mc.field_71439_g.field_70161_v, Phase.mc.field_71439_g.field_70177_z, Phase.mc.field_71439_g.field_70125_A, true));
        Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Phase.mc.field_71439_g, CPacketEntityAction.Action.START_FALL_FLYING));
        Phase.mc.field_71439_g.func_70107_b(Phase.mc.field_71439_g.field_70165_t + dirSpeed[0], Phase.mc.field_71439_g.field_70163_u + (Phase.mc.field_71474_y.field_74314_A.func_151470_d() ? (this.teleport ? 0.0625 : 0.0624) : 1.0E-8) - (Phase.mc.field_71474_y.field_74311_E.func_151470_d() ? (this.teleport ? 0.0625 : 0.0624) : 2.0E-8), Phase.mc.field_71439_g.field_70161_v + dirSpeed[1]);
        this.teleport = !this.teleport;
        Phase.mc.field_71439_g.field_70179_y = 0.0;
        Phase.mc.field_71439_g.field_70181_x = 0.0;
        Phase.mc.field_71439_g.field_70159_w = 0.0;
        Phase.mc.field_71439_g.field_70145_X = this.teleport;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.posLook.getValue().booleanValue() && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            if (Phase.mc.field_71439_g.func_70089_S() && Phase.mc.field_71441_e.func_175667_e(new BlockPos(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v)) && !(Phase.mc.field_71462_r instanceof GuiDownloadTerrain)) {
                if (this.teleportIds <= 0) {
                    this.teleportIds = packet.func_186965_f();
                }
                if (this.cancel.getValue().booleanValue() && this.cancelType.getValue().booleanValue()) {
                    packet.field_148936_d = Phase.mc.field_71439_g.field_70177_z;
                    packet.field_148937_e = Phase.mc.field_71439_g.field_70125_A;
                    return;
                }
                if (!(!this.cancel.getValue().booleanValue() || this.posLookPackets < this.cancelPacket.getValue() || this.onlyY.getValue().booleanValue() && (Phase.mc.field_71474_y.field_74351_w.func_151470_d() || Phase.mc.field_71474_y.field_74366_z.func_151470_d() || Phase.mc.field_71474_y.field_74370_x.func_151470_d() || Phase.mc.field_71474_y.field_74368_y.func_151470_d()))) {
                    this.posLookPackets = 0;
                    event.setCanceled(true);
                }
                ++this.posLookPackets;
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Send event) {
        if (this.scanPackets.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
            if (this.packets.contains((Object)packetPlayer)) {
                this.packets.remove((Object)packetPlayer);
            } else {
                event.setCanceled(true);
            }
        }
    }

    private double[] getMotion(double speed) {
        float moveForward = Phase.mc.field_71439_g.field_71158_b.field_192832_b;
        float moveStrafe = Phase.mc.field_71439_g.field_71158_b.field_78902_a;
        float rotationYaw = Phase.mc.field_71439_g.field_70126_B + (Phase.mc.field_71439_g.field_70177_z - Phase.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    public static enum Mode {
        PACKETFLY;

    }

    public static enum PacketFlyMode {
        NONE,
        SETBACK;

    }
}

