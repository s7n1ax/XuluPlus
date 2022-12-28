/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiConfirmOpenLink
 *  net.minecraft.client.gui.GuiControls
 *  net.minecraft.client.gui.GuiCustomizeSkin
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreenOptionsSounds
 *  net.minecraft.client.gui.GuiVideoSettings
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.GuiModList
 */
package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.Util;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

public class GUIBlur
extends Module
implements Util {
    public GUIBlur() {
        super("XuluBlur", "Blurs Your Gui like in xulu", Module.Category.CLIENT, true, false, false);
    }

    @Override
    public void onDisable() {
        if (GUIBlur.mc.field_71441_e != null) {
            GUIBlur.mc.field_71460_t.func_147706_e().func_148021_a();
        }
    }

    @Override
    public void onUpdate() {
        if (GUIBlur.mc.field_71441_e != null) {
            if (ClickGui.getInstance().isEnabled() || GUIBlur.mc.field_71462_r instanceof GuiContainer || GUIBlur.mc.field_71462_r instanceof GuiChat || GUIBlur.mc.field_71462_r instanceof GuiConfirmOpenLink || GUIBlur.mc.field_71462_r instanceof GuiEditSign || GUIBlur.mc.field_71462_r instanceof GuiGameOver || GUIBlur.mc.field_71462_r instanceof GuiOptions || GUIBlur.mc.field_71462_r instanceof GuiIngameMenu || GUIBlur.mc.field_71462_r instanceof GuiVideoSettings || GUIBlur.mc.field_71462_r instanceof GuiScreenOptionsSounds || GUIBlur.mc.field_71462_r instanceof GuiControls || GUIBlur.mc.field_71462_r instanceof GuiCustomizeSkin || GUIBlur.mc.field_71462_r instanceof GuiModList) {
                if (OpenGlHelper.field_148824_g && mc.func_175606_aa() instanceof EntityPlayer) {
                    if (GUIBlur.mc.field_71460_t.func_147706_e() != null) {
                        GUIBlur.mc.field_71460_t.func_147706_e().func_148021_a();
                    }
                    try {
                        GUIBlur.mc.field_71460_t.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (GUIBlur.mc.field_71460_t.func_147706_e() != null && GUIBlur.mc.field_71462_r == null) {
                    GUIBlur.mc.field_71460_t.func_147706_e().func_148021_a();
                }
            } else if (GUIBlur.mc.field_71460_t.func_147706_e() != null) {
                GUIBlur.mc.field_71460_t.func_147706_e().func_148021_a();
            }
        }
    }
}

