/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 */
package me.alpha432.oyvey.features.modules.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.event.events.MoveEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.movement.StrafePhob;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.Util;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Flight
extends Module {
    private static Flight INSTANCE = new Flight();
    private final Fly flySwitch = new Fly();
    private final List<CPacketPlayer> packets = new ArrayList<CPacketPlayer>();
    private final Timer delayTimer = new Timer();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Modes", Mode.PACKET));
    public Setting<Boolean> better = this.register(new Setting<Object>("Better", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Format> format = this.register(new Setting<Object>("Format", (Object)Format.DAMAGE, v -> this.mode.getValue() == Mode.DAMAGE));
    public Setting<PacketMode> type = this.register(new Setting<Object>("Type", (Object)PacketMode.Y, v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Boolean> phase = this.register(new Setting<Object>("Phase", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKET && this.better.getValue() != false));
    public Setting<Float> speed = this.register(new Setting<Object>("Speed", Float.valueOf(0.1f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.DESCEND || this.mode.getValue() == Mode.DAMAGE, "The speed."));
    public Setting<Boolean> noKick = this.register(new Setting<Object>("NoKick", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKET || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.DAMAGE));
    public Setting<Boolean> noClip = this.register(new Setting<Object>("NoClip", Boolean.FALSE, v -> this.mode.getValue() == Mode.DAMAGE));
    public Setting<Boolean> groundSpoof = this.register(new Setting<Object>("GroundSpoof", Boolean.FALSE, v -> this.mode.getValue() == Mode.SPOOF));
    public Setting<Boolean> antiGround = this.register(new Setting<Object>("AntiGround", Boolean.TRUE, v -> this.mode.getValue() == Mode.SPOOF));
    public Setting<Integer> cooldown = this.register(new Setting<Object>("Cooldown", Integer.valueOf(1), v -> this.mode.getValue() == Mode.DESCEND));
    public Setting<Boolean> ascend = this.register(new Setting<Object>("Ascend", Boolean.FALSE, v -> this.mode.getValue() == Mode.DESCEND));
    private int teleportId;
    private int counter;
    private double moveSpeed;
    private double lastDist;
    private int level;

    public Flight() {
        super("Flight", "Makes you fly.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static Flight getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Flight();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (Flight.fullNullCheck() || this.mode.getValue() != Mode.DESCEND) {
            return;
        }
        if (event.phase == TickEvent.Phase.END) {
            if (!Flight.mc.field_71439_g.func_184613_cA()) {
                if (this.counter < 1) {
                    this.counter += this.cooldown.getValue().intValue();
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u, Flight.mc.field_71439_g.field_70161_v, false));
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u - 0.03, Flight.mc.field_71439_g.field_70161_v, true));
                } else {
                    --this.counter;
                }
            }
        } else {
            Flight.mc.field_71439_g.field_70181_x = this.ascend.getValue() != false ? (double)this.speed.getValue().floatValue() : (double)(-this.speed.getValue().floatValue());
        }
    }

    @Override
    public void onEnable() {
        CPacketPlayer.Position bounds;
        if (Flight.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue() == Mode.PACKET) {
            this.teleportId = 0;
            this.packets.clear();
            bounds = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, 0.0, Flight.mc.field_71439_g.field_70161_v, Flight.mc.field_71439_g.field_70122_E);
            this.packets.add((CPacketPlayer)bounds);
            Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
        }
        if (this.mode.getValue() == Mode.CREATIVE) {
            Flight.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
            if (Flight.mc.field_71439_g.field_71075_bZ.field_75098_d) {
                return;
            }
            Flight.mc.field_71439_g.field_71075_bZ.field_75101_c = true;
        }
        if (this.mode.getValue() == Mode.SPOOF) {
            this.flySwitch.enable();
        }
        if (this.mode.getValue() == Mode.DAMAGE) {
            this.level = 0;
            if (this.format.getValue() == Format.PACKET && Flight.mc.field_71441_e != null) {
                this.teleportId = 0;
                this.packets.clear();
                bounds = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u <= 10.0 ? 255.0 : 1.0, Flight.mc.field_71439_g.field_70161_v, Flight.mc.field_71439_g.field_70122_E);
                this.packets.add((CPacketPlayer)bounds);
                Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() == Mode.DAMAGE) {
            if (this.format.getValue() == Format.DAMAGE) {
                if (event.getStage() == 0) {
                    Flight.mc.field_71439_g.field_70181_x = 0.0;
                    double motionY = 0.42f;
                    if (Flight.mc.field_71439_g.field_70122_E) {
                        if (Flight.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                            motionY += (double)((float)(Objects.requireNonNull(Flight.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j)).func_76458_c() + 1) * 0.1f);
                        }
                        Flight.mc.field_71439_g.field_70181_x = motionY;
                        OyVey.positionManager.setPlayerPosition(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70181_x, Flight.mc.field_71439_g.field_70161_v);
                        this.moveSpeed *= 2.149;
                    }
                }
                if (Flight.mc.field_71439_g.field_70173_aa % 2 == 0) {
                    Flight.mc.field_71439_g.func_70107_b(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u + MathUtil.getRandom(1.2354235325235235E-14, 1.2354235325235233E-13), Flight.mc.field_71439_g.field_70161_v);
                }
                if (Flight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Flight.mc.field_71439_g.field_70181_x += (double)(this.speed.getValue().floatValue() / 2.0f);
                }
                if (Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Flight.mc.field_71439_g.field_70181_x -= (double)(this.speed.getValue().floatValue() / 2.0f);
                }
            }
            if (this.format.getValue() == Format.NORMAL) {
                double d = Flight.mc.field_71474_y.field_74314_A.func_151470_d() ? (double)this.speed.getValue().floatValue() : (Flight.mc.field_71439_g.field_70181_x = Flight.mc.field_71474_y.field_74311_E.func_151470_d() ? (double)(-this.speed.getValue().floatValue()) : 0.0);
                if (this.noKick.getValue().booleanValue() && Flight.mc.field_71439_g.field_70173_aa % 5 == 0) {
                    OyVey.positionManager.setPlayerPosition(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u - 0.03125, Flight.mc.field_71439_g.field_70161_v, true);
                }
                double[] dir = EntityUtil.forward(this.speed.getValue().floatValue());
                Flight.mc.field_71439_g.field_70159_w = dir[0];
                Flight.mc.field_71439_g.field_70179_y = dir[1];
            }
            if (this.format.getValue() == Format.PACKET) {
                if (this.teleportId <= 0) {
                    CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u <= 10.0 ? 255.0 : 1.0, Flight.mc.field_71439_g.field_70161_v, Flight.mc.field_71439_g.field_70122_E);
                    this.packets.add((CPacketPlayer)bounds);
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
                    return;
                }
                Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                double posY = -1.0E-8;
                if (!Flight.mc.field_71474_y.field_74314_A.func_151470_d() && !Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    if (EntityUtil.isMoving()) {
                        for (double x = 0.0625; x < (double)this.speed.getValue().floatValue(); x += 0.262) {
                            double[] dir = EntityUtil.forward(x);
                            Flight.mc.field_71439_g.func_70016_h(dir[0], posY, dir[1]);
                            this.move(dir[0], posY, dir[1]);
                        }
                    }
                } else if (Flight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    for (int i = 0; i <= 3; ++i) {
                        Flight.mc.field_71439_g.func_70016_h(0.0, Flight.mc.field_71439_g.field_70173_aa % 20 == 0 ? (double)-0.04f : (double)(0.062f * (float)i), 0.0);
                        this.move(0.0, Flight.mc.field_71439_g.field_70173_aa % 20 == 0 ? (double)-0.04f : (double)(0.062f * (float)i), 0.0);
                    }
                } else if (Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    for (int i = 0; i <= 3; ++i) {
                        Flight.mc.field_71439_g.func_70016_h(0.0, posY - 0.0625 * (double)i, 0.0);
                        this.move(0.0, posY - 0.0625 * (double)i, 0.0);
                    }
                }
            }
            if (this.format.getValue() == Format.SLOW) {
                double posX = Flight.mc.field_71439_g.field_70165_t;
                double posY = Flight.mc.field_71439_g.field_70163_u;
                double posZ = Flight.mc.field_71439_g.field_70161_v;
                boolean ground = Flight.mc.field_71439_g.field_70122_E;
                Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                if (!Flight.mc.field_71474_y.field_74314_A.func_151470_d() && !Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    double[] dir = EntityUtil.forward(0.0625);
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
                    Flight.mc.field_71439_g.func_70634_a(posX + dir[0], posY, posZ + dir[1]);
                } else if (Flight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, posY + 0.0625, posZ, ground));
                    Flight.mc.field_71439_g.func_70634_a(posX, posY + 0.0625, posZ);
                } else if (Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, posY - 0.0625, posZ, ground));
                    Flight.mc.field_71439_g.func_70634_a(posX, posY - 0.0625, posZ);
                }
                Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX + Flight.mc.field_71439_g.field_70159_w, Flight.mc.field_71439_g.field_70163_u <= 10.0 ? 255.0 : 1.0, posZ + Flight.mc.field_71439_g.field_70179_y, ground));
            }
            if (this.format.getValue() == Format.DELAY) {
                if (this.delayTimer.passedMs(1000L)) {
                    this.delayTimer.reset();
                }
                if (this.delayTimer.passedMs(600L)) {
                    Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                    return;
                }
                if (this.teleportId <= 0) {
                    CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u <= 10.0 ? 255.0 : 1.0, Flight.mc.field_71439_g.field_70161_v, Flight.mc.field_71439_g.field_70122_E);
                    this.packets.add((CPacketPlayer)bounds);
                    Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
                    return;
                }
                Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                double posY = -1.0E-8;
                if (!Flight.mc.field_71474_y.field_74314_A.func_151470_d() && !Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    if (EntityUtil.isMoving()) {
                        double[] dir = EntityUtil.forward(0.2);
                        Flight.mc.field_71439_g.func_70016_h(dir[0], posY, dir[1]);
                        this.move(dir[0], posY, dir[1]);
                    }
                } else if (Flight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    Flight.mc.field_71439_g.func_70016_h(0.0, (double)0.062f, 0.0);
                    this.move(0.0, 0.062f, 0.0);
                } else if (Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Flight.mc.field_71439_g.func_70016_h(0.0, 0.0625, 0.0);
                    this.move(0.0, 0.0625, 0.0);
                }
            }
            if (this.noClip.getValue().booleanValue()) {
                Flight.mc.field_71439_g.field_70145_X = true;
            }
        }
        if (event.getStage() == 0) {
            if (this.mode.getValue() == Mode.CREATIVE) {
                Flight.mc.field_71439_g.field_71075_bZ.func_75092_a(this.speed.getValue().floatValue());
                Flight.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
                if (Flight.mc.field_71439_g.field_71075_bZ.field_75098_d) {
                    return;
                }
                Flight.mc.field_71439_g.field_71075_bZ.field_75101_c = true;
            }
            if (this.mode.getValue() == Mode.VANILLA) {
                Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                Flight.mc.field_71439_g.field_70747_aH = this.speed.getValue().floatValue();
                if (this.noKick.getValue().booleanValue() && Flight.mc.field_71439_g.field_70173_aa % 4 == 0) {
                    Flight.mc.field_71439_g.field_70181_x = -0.04f;
                }
                double[] dir = MathUtil.directionSpeed(this.speed.getValue().floatValue());
                if (Flight.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || Flight.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
                    Flight.mc.field_71439_g.field_70159_w = dir[0];
                    Flight.mc.field_71439_g.field_70179_y = dir[1];
                } else {
                    Flight.mc.field_71439_g.field_70159_w = 0.0;
                    Flight.mc.field_71439_g.field_70179_y = 0.0;
                }
                if (Flight.mc.field_71474_y.field_74314_A.func_151470_d()) {
                    double d = this.noKick.getValue().booleanValue() ? (Flight.mc.field_71439_g.field_70173_aa % 20 == 0 ? (double)-0.04f : (double)this.speed.getValue().floatValue()) : (Flight.mc.field_71439_g.field_70181_x = (Flight.mc.field_71439_g.field_70181_x = Flight.mc.field_71439_g.field_70181_x + (double)this.speed.getValue().floatValue()));
                }
                if (Flight.mc.field_71474_y.field_74311_E.func_151470_d()) {
                    Flight.mc.field_71439_g.field_70181_x -= (double)this.speed.getValue().floatValue();
                }
            }
            if (this.mode.getValue() == Mode.PACKET && !this.better.getValue().booleanValue()) {
                this.doNormalPacketFly();
            }
            if (this.mode.getValue() == Mode.PACKET && this.better.getValue().booleanValue()) {
                this.doBetterPacketFly();
            }
        }
    }

    private void doNormalPacketFly() {
        if (this.teleportId <= 0) {
            CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, 0.0, Flight.mc.field_71439_g.field_70161_v, Flight.mc.field_71439_g.field_70122_E);
            this.packets.add((CPacketPlayer)bounds);
            Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
            return;
        }
        Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        if (Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, 0.0, -0.0625)).isEmpty()) {
            double ySpeed = Flight.mc.field_71474_y.field_74314_A.func_151470_d() ? (this.noKick.getValue().booleanValue() ? (Flight.mc.field_71439_g.field_70173_aa % 20 == 0 ? (double)-0.04f : (double)0.062f) : (double)0.062f) : (Flight.mc.field_71474_y.field_74311_E.func_151470_d() ? -0.062 : (Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, -0.0625, -0.0625)).isEmpty() ? (Flight.mc.field_71439_g.field_70173_aa % 4 == 0 ? (double)(this.noKick.getValue() != false ? -0.04f : 0.0f) : 0.0) : 0.0));
            double[] directionalSpeed = MathUtil.directionSpeed(this.speed.getValue().floatValue());
            if (Flight.mc.field_71474_y.field_74314_A.func_151470_d() || Flight.mc.field_71474_y.field_74311_E.func_151470_d() || Flight.mc.field_71474_y.field_74351_w.func_151470_d() || Flight.mc.field_71474_y.field_74368_y.func_151470_d() || Flight.mc.field_71474_y.field_74366_z.func_151470_d() || Flight.mc.field_71474_y.field_74370_x.func_151470_d()) {
                if (directionalSpeed[0] != 0.0 || directionalSpeed[1] != 0.0) {
                    if (Flight.mc.field_71439_g.field_71158_b.field_78901_c && (Flight.mc.field_71439_g.field_70702_br != 0.0f || Flight.mc.field_71439_g.field_191988_bg != 0.0f)) {
                        Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.field_71439_g.func_70016_h(0.0, ySpeed * (double)i, 0.0);
                            this.move(0.0, ySpeed * (double)i, 0.0);
                        }
                    } else if (Flight.mc.field_71439_g.field_71158_b.field_78901_c) {
                        Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.field_71439_g.func_70016_h(0.0, ySpeed * (double)i, 0.0);
                            this.move(0.0, ySpeed * (double)i, 0.0);
                        }
                    } else {
                        for (int i = 0; i <= 2; ++i) {
                            Flight.mc.field_71439_g.func_70016_h(directionalSpeed[0] * (double)i, ySpeed * (double)i, directionalSpeed[1] * (double)i);
                            this.move(directionalSpeed[0] * (double)i, ySpeed * (double)i, directionalSpeed[1] * (double)i);
                        }
                    }
                }
            } else if (this.noKick.getValue().booleanValue() && Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                Flight.mc.field_71439_g.func_70016_h(0.0, Flight.mc.field_71439_g.field_70173_aa % 2 == 0 ? (double)0.04f : (double)-0.04f, 0.0);
                this.move(0.0, Flight.mc.field_71439_g.field_70173_aa % 2 == 0 ? (double)0.04f : (double)-0.04f, 0.0);
            }
        }
    }

    private void doBetterPacketFly() {
        if (this.teleportId <= 0) {
            CPacketPlayer.Position bounds = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t, 10000.0, Flight.mc.field_71439_g.field_70161_v, Flight.mc.field_71439_g.field_70122_E);
            this.packets.add((CPacketPlayer)bounds);
            Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
            return;
        }
        Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        if (Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, 0.0, -0.0625)).isEmpty()) {
            double ySpeed = Flight.mc.field_71474_y.field_74314_A.func_151470_d() ? (this.noKick.getValue().booleanValue() ? (Flight.mc.field_71439_g.field_70173_aa % 20 == 0 ? (double)-0.04f : (double)0.062f) : (double)0.062f) : (Flight.mc.field_71474_y.field_74311_E.func_151470_d() ? -0.062 : (Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, -0.0625, -0.0625)).isEmpty() ? (Flight.mc.field_71439_g.field_70173_aa % 4 == 0 ? (double)(this.noKick.getValue() != false ? -0.04f : 0.0f) : 0.0) : 0.0));
            double[] directionalSpeed = MathUtil.directionSpeed(this.speed.getValue().floatValue());
            if (Flight.mc.field_71474_y.field_74314_A.func_151470_d() || Flight.mc.field_71474_y.field_74311_E.func_151470_d() || Flight.mc.field_71474_y.field_74351_w.func_151470_d() || Flight.mc.field_71474_y.field_74368_y.func_151470_d() || Flight.mc.field_71474_y.field_74366_z.func_151470_d() || Flight.mc.field_71474_y.field_74370_x.func_151470_d()) {
                if (directionalSpeed[0] != 0.0 || directionalSpeed[1] != 0.0) {
                    if (Flight.mc.field_71439_g.field_71158_b.field_78901_c && (Flight.mc.field_71439_g.field_70702_br != 0.0f || Flight.mc.field_71439_g.field_191988_bg != 0.0f)) {
                        Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.field_71439_g.func_70016_h(0.0, ySpeed * (double)i, 0.0);
                            this.move(0.0, ySpeed * (double)i, 0.0);
                        }
                    } else if (Flight.mc.field_71439_g.field_71158_b.field_78901_c) {
                        Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
                        this.move(0.0, 0.0, 0.0);
                        for (int i = 0; i <= 3; ++i) {
                            Flight.mc.field_71439_g.func_70016_h(0.0, ySpeed * (double)i, 0.0);
                            this.move(0.0, ySpeed * (double)i, 0.0);
                        }
                    } else {
                        for (int i = 0; i <= 2; ++i) {
                            Flight.mc.field_71439_g.func_70016_h(directionalSpeed[0] * (double)i, ySpeed * (double)i, directionalSpeed[1] * (double)i);
                            this.move(directionalSpeed[0] * (double)i, ySpeed * (double)i, directionalSpeed[1] * (double)i);
                        }
                    }
                }
            } else if (this.noKick.getValue().booleanValue() && Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(-0.0625, -0.0625, -0.0625)).isEmpty()) {
                Flight.mc.field_71439_g.func_70016_h(0.0, Flight.mc.field_71439_g.field_70173_aa % 2 == 0 ? (double)0.04f : (double)-0.04f, 0.0);
                this.move(0.0, Flight.mc.field_71439_g.field_70173_aa % 2 == 0 ? (double)0.04f : (double)-0.04f, 0.0);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.SPOOF) {
            if (Flight.fullNullCheck()) {
                return;
            }
            if (!Flight.mc.field_71439_g.field_71075_bZ.field_75101_c) {
                this.flySwitch.disable();
                this.flySwitch.enable();
                Flight.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
            }
            Flight.mc.field_71439_g.field_71075_bZ.func_75092_a(0.05f * this.speed.getValue().floatValue());
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.CREATIVE && Flight.mc.field_71439_g != null) {
            Flight.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
            Flight.mc.field_71439_g.field_71075_bZ.func_75092_a(0.05f);
            if (Flight.mc.field_71439_g.field_71075_bZ.field_75098_d) {
                return;
            }
            Flight.mc.field_71439_g.field_71075_bZ.field_75101_c = false;
        }
        if (this.mode.getValue() == Mode.SPOOF) {
            this.flySwitch.disable();
        }
        if (this.mode.getValue() == Mode.DAMAGE) {
            OyVey.timerManager.reset();
            Flight.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
            this.moveSpeed = StrafePhob.getBaseMoveSpeed();
            this.lastDist = 0.0;
            if (this.noClip.getValue().booleanValue()) {
                Flight.mc.field_71439_g.field_70145_X = false;
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (event.getStage() == 0 && this.mode.getValue() == Mode.DAMAGE && this.format.getValue() == Format.DAMAGE) {
            double forward = Flight.mc.field_71439_g.field_71158_b.field_192832_b;
            double strafe = Flight.mc.field_71439_g.field_71158_b.field_78902_a;
            float yaw = Flight.mc.field_71439_g.field_70177_z;
            if (forward == 0.0 && strafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            if (forward != 0.0 && strafe != 0.0) {
                forward *= Math.sin(0.7853981633974483);
                strafe *= Math.cos(0.7853981633974483);
            }
            if (this.level != 1 || Flight.mc.field_71439_g.field_191988_bg == 0.0f && Flight.mc.field_71439_g.field_70702_br == 0.0f) {
                if (this.level == 2) {
                    ++this.level;
                } else if (this.level == 3) {
                    ++this.level;
                    double difference = (Flight.mc.field_71439_g.field_70173_aa % 2 == 0 ? -0.05 : 0.1) * (this.lastDist - StrafePhob.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                } else {
                    if (Flight.mc.field_71441_e.func_184144_a((Entity)Flight.mc.field_71439_g, Flight.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Flight.mc.field_71439_g.field_70181_x, 0.0)).size() > 0 || Flight.mc.field_71439_g.field_70124_G) {
                        this.level = 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
            } else {
                this.level = 2;
                double boost = Flight.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) ? 1.86 : 2.05;
                this.moveSpeed = boost * StrafePhob.getBaseMoveSpeed() - 0.01;
            }
            this.moveSpeed = Math.max(this.moveSpeed, StrafePhob.getBaseMoveSpeed());
            double mx = -Math.sin(Math.toRadians(yaw));
            double mz = Math.cos(Math.toRadians(yaw));
            event.setX(forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
            event.setZ(forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0) {
            CPacketPlayer packet;
            if (this.mode.getValue() == Mode.PACKET) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                    event.setCanceled(true);
                }
                if (event.getPacket() instanceof CPacketPlayer) {
                    packet = (CPacketPlayer)event.getPacket();
                    if (this.packets.contains((Object)packet)) {
                        this.packets.remove((Object)packet);
                        return;
                    }
                    event.setCanceled(true);
                }
            }
            if (this.mode.getValue() == Mode.SPOOF) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (!(this.groundSpoof.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayer && Flight.mc.field_71439_g.field_71075_bZ.field_75100_b)) {
                    return;
                }
                packet = (CPacketPlayer)event.getPacket();
                if (!packet.field_149480_h) {
                    return;
                }
                AxisAlignedBB range = Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(0.0, -Flight.mc.field_71439_g.field_70163_u, 0.0).func_191195_a(0.0, (double)(-Flight.mc.field_71439_g.field_70131_O), 0.0);
                List collisionBoxes = Flight.mc.field_71439_g.field_70170_p.func_184144_a((Entity)Flight.mc.field_71439_g, range);
                AtomicReference<Double> newHeight = new AtomicReference<Double>(0.0);
                collisionBoxes.forEach(box -> newHeight.set(Math.max((Double)newHeight.get(), box.field_72337_e)));
                packet.field_149477_b = newHeight.get();
                packet.field_149474_g = true;
            }
            if (this.mode.getValue() == Mode.DAMAGE && (this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY)) {
                if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
                    event.setCanceled(true);
                }
                if (event.getPacket() instanceof CPacketPlayer) {
                    packet = (CPacketPlayer)event.getPacket();
                    if (this.packets.contains((Object)packet)) {
                        this.packets.remove((Object)packet);
                        return;
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0) {
            SPacketPlayerPosLook packet;
            if (this.mode.getValue() == Mode.PACKET) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (event.getPacket() instanceof SPacketPlayerPosLook) {
                    packet = (SPacketPlayerPosLook)event.getPacket();
                    if (Flight.mc.field_71439_g.func_70089_S() && Flight.mc.field_71441_e.func_175667_e(new BlockPos(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u, Flight.mc.field_71439_g.field_70161_v)) && !(Flight.mc.field_71462_r instanceof GuiDownloadTerrain)) {
                        if (this.teleportId <= 0) {
                            this.teleportId = packet.func_186965_f();
                        } else {
                            event.setCanceled(true);
                        }
                    }
                }
            }
            if (this.mode.getValue() == Mode.SPOOF) {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (!(this.antiGround.getValue().booleanValue() && event.getPacket() instanceof SPacketPlayerPosLook && Flight.mc.field_71439_g.field_71075_bZ.field_75100_b)) {
                    return;
                }
                packet = (SPacketPlayerPosLook)event.getPacket();
                double oldY = Flight.mc.field_71439_g.field_70163_u;
                Flight.mc.field_71439_g.func_70107_b(packet.field_148940_a, packet.field_148938_b, packet.field_148939_c);
                AxisAlignedBB range = Flight.mc.field_71439_g.func_174813_aQ().func_72321_a(0.0, (double)(256.0f - Flight.mc.field_71439_g.field_70131_O) - Flight.mc.field_71439_g.field_70163_u, 0.0).func_191195_a(0.0, (double)Flight.mc.field_71439_g.field_70131_O, 0.0);
                List collisionBoxes = Flight.mc.field_71439_g.field_70170_p.func_184144_a((Entity)Flight.mc.field_71439_g, range);
                AtomicReference<Double> newY = new AtomicReference<Double>(256.0);
                collisionBoxes.forEach(box -> newY.set(Math.min((Double)newY.get(), box.field_72338_b - (double)Flight.mc.field_71439_g.field_70131_O)));
                packet.field_148938_b = Math.min(oldY, newY.get());
            }
            if (this.mode.getValue() == Mode.DAMAGE && (this.format.getValue() == Format.PACKET || this.format.getValue() == Format.DELAY) && event.getPacket() instanceof SPacketPlayerPosLook) {
                packet = (SPacketPlayerPosLook)event.getPacket();
                if (Flight.mc.field_71439_g.func_70089_S() && Flight.mc.field_71441_e.func_175667_e(new BlockPos(Flight.mc.field_71439_g.field_70165_t, Flight.mc.field_71439_g.field_70163_u, Flight.mc.field_71439_g.field_70161_v)) && !(Flight.mc.field_71462_r instanceof GuiDownloadTerrain)) {
                    if (this.teleportId <= 0) {
                        this.teleportId = packet.func_186965_f();
                    } else {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && this.isEnabled() && !event.getSetting().equals(this.enabled)) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.PACKET && this.better.getValue().booleanValue() && this.phase.getValue().booleanValue()) {
            event.setCanceled(true);
        }
    }

    private void move(double x, double y, double z) {
        CPacketPlayer.Position pos = new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, Flight.mc.field_71439_g.field_70163_u + y, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
        this.packets.add((CPacketPlayer)pos);
        Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)pos);
        CPacketPlayer.Position bounds = this.better.getValue() != false ? this.createBoundsPacket(x, y, z) : new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, 0.0, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
        this.packets.add((CPacketPlayer)bounds);
        Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)bounds);
        ++this.teleportId;
        Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId - 1));
        Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId));
        Flight.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportId + 1));
    }

    private CPacketPlayer createBoundsPacket(double x, double y, double z) {
        switch (this.type.getValue()) {
            case Up: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, 10000.0, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
            }
            case Down: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, -10000.0, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
            }
            case Zero: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, 0.0, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
            }
            case Y: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, Flight.mc.field_71439_g.field_70163_u + y <= 10.0 ? 255.0 : 1.0, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
            }
            case X: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x + 75.0, Flight.mc.field_71439_g.field_70163_u + y, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
            }
            case Z: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, Flight.mc.field_71439_g.field_70163_u + y, Flight.mc.field_71439_g.field_70161_v + z + 75.0, Flight.mc.field_71439_g.field_70122_E);
            }
            case XZ: {
                return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x + 75.0, Flight.mc.field_71439_g.field_70163_u + y, Flight.mc.field_71439_g.field_70161_v + z + 75.0, Flight.mc.field_71439_g.field_70122_E);
            }
        }
        return new CPacketPlayer.Position(Flight.mc.field_71439_g.field_70165_t + x, 2000.0, Flight.mc.field_71439_g.field_70161_v + z, Flight.mc.field_71439_g.field_70122_E);
    }

    private static class Fly {
        private Fly() {
        }

        protected void enable() {
            Util.mc.func_152344_a(() -> {
                if (Util.mc.field_71439_g == null || Util.mc.field_71439_g.field_71075_bZ == null) {
                    return;
                }
                Util.mc.field_71439_g.field_71075_bZ.field_75101_c = true;
                Util.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
            });
        }

        protected void disable() {
            Util.mc.func_152344_a(() -> {
                if (Util.mc.field_71439_g == null || Util.mc.field_71439_g.field_71075_bZ == null) {
                    return;
                }
                PlayerCapabilities gmCaps = new PlayerCapabilities();
                Util.mc.field_71442_b.func_178889_l().func_77147_a(gmCaps);
                PlayerCapabilities capabilities = Util.mc.field_71439_g.field_71075_bZ;
                capabilities.field_75101_c = gmCaps.field_75101_c;
                capabilities.field_75100_b = gmCaps.field_75101_c && capabilities.field_75100_b;
                capabilities.func_75092_a(gmCaps.func_75093_a());
            });
        }
    }

    public static enum Mode {
        CREATIVE,
        VANILLA,
        PACKET,
        SPOOF,
        DESCEND,
        DAMAGE;

    }

    public static enum Format {
        DAMAGE,
        SLOW,
        DELAY,
        NORMAL,
        PACKET;

    }

    private static enum PacketMode {
        Up,
        Down,
        Zero,
        Y,
        X,
        Z,
        XZ;

    }
}

