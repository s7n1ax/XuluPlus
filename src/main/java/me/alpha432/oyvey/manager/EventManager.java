/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketPlayerListItem$AddPlayerData
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 *  org.lwjgl.input.Keyboard
 */
package me.alpha432.oyvey.manager;

import com.google.common.base.Strings;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ConnectionEvent;
import me.alpha432.oyvey.event.events.DeathEvent;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.event.events.TotemPopEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class EventManager
extends Feature {
    private final Timer logoutTimer = new Timer();
    private final AtomicBoolean tickOngoing = new AtomicBoolean(false);

    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }

    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!EventManager.fullNullCheck() && event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving().equals((Object)EventManager.mc.field_71439_g)) {
            OyVey.inventoryManager.update();
            OyVey.moduleManager.onUpdate();
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                OyVey.moduleManager.sortModules(true);
            } else {
                OyVey.moduleManager.sortModulesABC();
            }
        }
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        OyVey.moduleManager.onLogin();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        OyVey.moduleManager.onLogout();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        OyVey.moduleManager.onTick();
        for (EntityPlayer player : EventManager.mc.field_71441_e.field_73010_i) {
            if (player == null || player.func_110143_aJ() > 0.0f) continue;
            MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            OyVey.speedManager.updateValues();
            OyVey.rotationManager.updateRotations();
            OyVey.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            OyVey.rotationManager.restoreRotations();
            OyVey.positionManager.restorePosition();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketEntityStatus packet;
        if (event.getStage() != 0) {
            return;
        }
        OyVey.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).func_149160_c() == 35 && packet.func_149161_a((World)EventManager.mc.field_71441_e) instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)EventManager.mc.field_71441_e);
            MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && !EventManager.fullNullCheck() && this.logoutTimer.passedS(1.0)) {
            packet = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet.func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet.func_179768_b())) {
                return;
            }
            packet.func_179767_a().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty((String)data.func_179962_a().getName()) || data.func_179962_a().getId() != null).forEach(arg_0 -> EventManager.lambda$onPacketReceive$1((SPacketPlayerListItem)packet, arg_0));
        }
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            OyVey.serverManager.update();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.field_71424_I.func_76320_a("Xulu+");
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        OyVey.moduleManager.onRender3D(render3dEvent);
        EventManager.mc.field_71424_I.func_76319_b();
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        OyVey.moduleManager.onRenderHand(event);
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            OyVey.textManager.updateResolution();
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            OyVey.moduleManager.onRender2D(render2DEvent);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            OyVey.moduleManager.onKeyPressed(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                if (event.getMessage().length() > 1) {
                    OyVey.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage((Object)ChatFormatting.LIGHT_PURPLE + "An error occurred while running this command. Check the log!");
            }
        }
    }

    private static /* synthetic */ void lambda$onPacketReceive$1(SPacketPlayerListItem packet, SPacketPlayerListItem.AddPlayerData data) {
        UUID id = data.func_179962_a().getId();
        switch (packet.func_179768_b()) {
            case ADD_PLAYER: {
                String name = data.func_179962_a().getName();
                MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id, name));
                break;
            }
            case REMOVE_PLAYER: {
                EntityPlayer entity = EventManager.mc.field_71441_e.func_152378_a(id);
                if (entity != null) {
                    String logoutName = entity.func_70005_c_();
                    MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity, id, logoutName));
                    break;
                }
                MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id, null));
            }
        }
    }
}

