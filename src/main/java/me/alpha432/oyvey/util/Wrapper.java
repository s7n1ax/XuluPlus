/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 *  org.lwjgl.input.Keyboard
 */
package me.alpha432.oyvey.util;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
    public static Minecraft mc = Minecraft.func_71410_x();
    public static FontRenderer fontRenderer = Wrapper.mc.field_71466_p;
    public static FontRenderer fr = Minecraft.func_71410_x().field_71466_p;
    public static volatile Wrapper INSTANCE = new Wrapper();

    public static EntityPlayerSP getPlayer() {
        return Wrapper.getMinecraft().field_71439_g;
    }

    public static Minecraft getMinecraft() {
        return mc;
    }

    public static World getWorld() {
        return Wrapper.getMinecraft().field_71441_e;
    }

    public static int getKey(String keyname) {
        return Keyboard.getKeyIndex((String)keyname.toUpperCase());
    }

    public Minecraft mc() {
        return Minecraft.func_71410_x();
    }

    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().field_71439_g;
    }

    public WorldClient world() {
        return Wrapper.INSTANCE.mc().field_71441_e;
    }

    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().field_71474_y;
    }

    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().field_71466_p;
    }

    public void sendPacket(Packet packet) {
        this.player().field_71174_a.func_147297_a(packet);
    }

    public PlayerControllerMP controller() {
        return Wrapper.INSTANCE.mc().field_71442_b;
    }

    public void swingArm() {
        this.player().func_184609_a(EnumHand.MAIN_HAND);
    }

    public void attack(Entity entity) {
        this.controller().func_78764_a((EntityPlayer)this.player(), entity);
    }

    public void copy(String content) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
    }
}

