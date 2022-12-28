/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketKeepAlive
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.mixin.mixins.IC00HandshakePhob;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PingBypassPhob
extends Module {
    private static PingBypassPhob instance;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final Timer pingTimer = new Timer();
    private final List<Long> pingList = new ArrayList<Long>();
    public Setting<String> ip = this.register(new Setting<String>("PhobosIP", "0.0.0.0.0"));
    public Setting<String> port = this.register(new Setting<String>("Port", "0").setRenderName(true));
    public Setting<String> serverIP = this.register(new Setting<String>("ServerIP", "AnarchyHvH.eu"));
    public Setting<Boolean> noFML = this.register(new Setting<Boolean>("RemoveFML", false));
    public Setting<Boolean> getName = this.register(new Setting<Boolean>("GetName", false));
    public Setting<Boolean> average = this.register(new Setting<Boolean>("Average", false));
    public Setting<Boolean> clear = this.register(new Setting<Boolean>("ClearPings", false));
    public Setting<Boolean> oneWay = this.register(new Setting<Boolean>("OneWay", false));
    public Setting<Integer> delay = this.register(new Setting<Integer>("KeepAlives", 10, 1, 50));
    private long currentPing;
    private long serverPing;
    private StringBuffer name;
    private long averagePing;
    private String serverPrefix = "idk";

    public PingBypassPhob() {
        super("PingBypass", "x", Module.Category.CLIENT, false, false, true);
        instance = this;
    }

    public static PingBypassPhob getInstance() {
        if (instance == null) {
            instance = new PingBypassPhob();
        }
        return instance;
    }

    public String getPlayerName() {
        if (this.name == null) {
            return null;
        }
        return this.name.toString();
    }

    public String getServerPrefix() {
        return this.serverPrefix;
    }

    @Override
    public void onLogout() {
        this.averagePing = 0L;
        this.currentPing = 0L;
        this.serverPing = 0L;
        this.pingList.clear();
        this.connected.set(false);
        this.name = null;
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat)event.getPacket();
            if (packet.field_148919_a.func_150260_c().startsWith("@Clientprefix")) {
                this.serverPrefix = packet.field_148919_a.func_150254_d().replace("@Clientprefix", "");
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.func_147114_u() != null && this.isConnected()) {
            if (this.getName.getValue().booleanValue()) {
                mc.func_147114_u().func_147297_a((Packet)new CPacketChatMessage("@Servername"));
                this.getName.setValue(false);
            }
            if (this.serverPrefix.equalsIgnoreCase("idk") && PingBypassPhob.mc.field_71441_e != null) {
                mc.func_147114_u().func_147297_a((Packet)new CPacketChatMessage("@Servergetprefix"));
            }
            if (this.pingTimer.passedMs(this.delay.getValue() * 1000)) {
                mc.func_147114_u().func_147297_a((Packet)new CPacketKeepAlive(100L));
                this.pingTimer.reset();
            }
            if (this.clear.getValue().booleanValue()) {
                this.pingList.clear();
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketKeepAlive alive;
        if (event.getPacket() instanceof SPacketChat) {
            SPacketChat packetChat = (SPacketChat)event.getPacket();
            if (packetChat.func_148915_c().func_150254_d().startsWith("@Client")) {
                this.name = new StringBuffer(TextUtil.stripColor(packetChat.func_148915_c().func_150254_d().replace("@Client", "")));
                event.setCanceled(true);
            }
        } else if (event.getPacket() instanceof SPacketKeepAlive && (alive = (SPacketKeepAlive)event.getPacket()).func_149134_c() > 0L && alive.func_149134_c() < 1000L) {
            this.serverPing = alive.func_149134_c();
            this.currentPing = this.oneWay.getValue() != false ? this.pingTimer.getPassedTimeMs() / 2L : this.pingTimer.getPassedTimeMs();
            this.pingList.add(this.currentPing);
            this.averagePing = this.getAveragePing();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        IC00HandshakePhob packet;
        if (event.getPacket() instanceof C00Handshake && (packet = (IC00HandshakePhob)event.getPacket()).getIp().equals(this.ip.getValue())) {
            packet.setIp(this.serverIP.getValue());
            System.out.println(packet.getIp());
            this.connected.set(true);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.averagePing + "ms";
    }

    private long getAveragePing() {
        if (!this.average.getValue().booleanValue() || this.pingList.isEmpty()) {
            return this.currentPing;
        }
        int full = 0;
        for (long i : this.pingList) {
            full = (int)((long)full + i);
        }
        return full / this.pingList.size();
    }

    public boolean isConnected() {
        return this.connected.get();
    }

    public int getPort() {
        int result;
        try {
            result = Integer.parseInt(this.port.getValue());
        }
        catch (NumberFormatException e) {
            return -1;
        }
        return result;
    }

    public long getServerPing() {
        return this.serverPing;
    }
}

