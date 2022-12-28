/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketSetPassengers
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.PushEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freecam
extends Module {
    private static Freecam INSTANCE = new Freecam();
    public Setting<Double> speed = this.register(new Setting<Double>("speed", 0.5, 0.1, 5.0));
    public Setting<Boolean> view = this.register(new Setting<Boolean>("3d", false));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("packet", true));
    public Setting<Boolean> disable = this.register(new Setting<Boolean>("logout/off", true));
    public Setting<Boolean> legit = this.register(new Setting<Boolean>("legit", false));
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;

    public Freecam() {
        super("Freecam", "Look around freely.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static Freecam getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Freecam();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck()) {
            this.oldBoundingBox = Freecam.mc.field_71439_g.func_174813_aQ();
            Freecam.mc.field_71439_g.func_174826_a(new AxisAlignedBB(Freecam.mc.field_71439_g.field_70165_t, Freecam.mc.field_71439_g.field_70163_u, Freecam.mc.field_71439_g.field_70161_v, Freecam.mc.field_71439_g.field_70165_t, Freecam.mc.field_71439_g.field_70163_u, Freecam.mc.field_71439_g.field_70161_v));
            if (Freecam.mc.field_71439_g.func_184187_bx() != null) {
                this.riding = Freecam.mc.field_71439_g.func_184187_bx();
                Freecam.mc.field_71439_g.func_184210_p();
            }
            this.entity = new EntityOtherPlayerMP((World)Freecam.mc.field_71441_e, Freecam.mc.field_71449_j.func_148256_e());
            this.entity.func_82149_j((Entity)Freecam.mc.field_71439_g);
            this.entity.field_70177_z = Freecam.mc.field_71439_g.field_70177_z;
            this.entity.field_70759_as = Freecam.mc.field_71439_g.field_70759_as;
            this.entity.field_71071_by.func_70455_b(Freecam.mc.field_71439_g.field_71071_by);
            Freecam.mc.field_71441_e.func_73027_a(69420, (Entity)this.entity);
            this.position = Freecam.mc.field_71439_g.func_174791_d();
            this.yaw = Freecam.mc.field_71439_g.field_70177_z;
            this.pitch = Freecam.mc.field_71439_g.field_70125_A;
            Freecam.mc.field_71439_g.field_70145_X = true;
        }
    }

    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            Freecam.mc.field_71439_g.func_174826_a(this.oldBoundingBox);
            if (this.riding != null) {
                Freecam.mc.field_71439_g.func_184205_a(this.riding, true);
            }
            if (this.entity != null) {
                Freecam.mc.field_71441_e.func_72900_e((Entity)this.entity);
            }
            if (this.position != null) {
                Freecam.mc.field_71439_g.func_70107_b(this.position.field_72450_a, this.position.field_72448_b, this.position.field_72449_c);
            }
            Freecam.mc.field_71439_g.field_70177_z = this.yaw;
            Freecam.mc.field_71439_g.field_70125_A = this.pitch;
            Freecam.mc.field_71439_g.field_70145_X = false;
        }
    }

    @Override
    public void onUpdate() {
        Freecam.mc.field_71439_g.field_70145_X = true;
        Freecam.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        Freecam.mc.field_71439_g.field_70747_aH = this.speed.getValue().floatValue();
        double[] dir = MathUtil.directionSpeed(this.speed.getValue());
        if (Freecam.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || Freecam.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
            Freecam.mc.field_71439_g.field_70159_w = dir[0];
            Freecam.mc.field_71439_g.field_70179_y = dir[1];
        } else {
            Freecam.mc.field_71439_g.field_70159_w = 0.0;
            Freecam.mc.field_71439_g.field_70179_y = 0.0;
        }
        Freecam.mc.field_71439_g.func_70031_b(false);
        if (this.view.getValue().booleanValue() && !Freecam.mc.field_71474_y.field_74311_E.func_151470_d() && !Freecam.mc.field_71474_y.field_74314_A.func_151470_d()) {
            Freecam.mc.field_71439_g.field_70181_x = this.speed.getValue() * -MathUtil.degToRad(Freecam.mc.field_71439_g.field_70125_A) * (double)Freecam.mc.field_71439_g.field_71158_b.field_192832_b;
        }
        if (Freecam.mc.field_71474_y.field_74314_A.func_151470_d()) {
            EntityPlayerSP player = Freecam.mc.field_71439_g;
            player.field_70181_x += this.speed.getValue().doubleValue();
        }
        if (Freecam.mc.field_71474_y.field_74311_E.func_151470_d()) {
            EntityPlayerSP player2 = Freecam.mc.field_71439_g;
            player2.field_70181_x -= this.speed.getValue().doubleValue();
        }
    }

    @Override
    public void onLogout() {
        if (this.disable.getValue().booleanValue()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (this.legit.getValue().booleanValue() && this.entity != null && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
            packetPlayer.field_149479_a = this.entity.field_70165_t;
            packetPlayer.field_149477_b = this.entity.field_70163_u;
            packetPlayer.field_149478_c = this.entity.field_70161_v;
            return;
        }
        if (this.packet.getValue().booleanValue()) {
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCanceled(true);
            }
        } else if (!(event.getPacket() instanceof CPacketUseEntity || event.getPacket() instanceof CPacketPlayerTryUseItem || event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock || event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketVehicleMove || event.getPacket() instanceof CPacketChatMessage || event.getPacket() instanceof CPacketKeepAlive)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSetPassengers packet;
        Entity riding;
        if (event.getPacket() instanceof SPacketSetPassengers && (riding = Freecam.mc.field_71441_e.func_73045_a((packet = (SPacketSetPassengers)event.getPacket()).func_186972_b())) != null && riding == this.riding) {
            this.riding = null;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet2 = (SPacketPlayerPosLook)event.getPacket();
            if (this.packet.getValue().booleanValue()) {
                if (this.entity != null) {
                    this.entity.func_70080_a(packet2.func_148932_c(), packet2.func_148928_d(), packet2.func_148933_e(), packet2.func_148931_f(), packet2.func_148930_g());
                }
                this.position = new Vec3d(packet2.func_148932_c(), packet2.func_148928_d(), packet2.func_148933_e());
                Freecam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(packet2.func_186965_f()));
                event.setCanceled(true);
            } else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
}

