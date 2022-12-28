/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.client.audio.SoundManager
 *  net.minecraftforge.fml.common.ObfuscationReflectionHelper
 */
package me.alpha432.oyvey.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ReloadSoundCommand
extends Command {
    public ReloadSoundCommand() {
        super("sound", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        try {
            SoundManager sndManager = (SoundManager)ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, (Object)mc.func_147118_V(), (String[])new String[]{"sndManager", "sndManager"});
            sndManager.func_148596_a();
            Command.sendMessage((Object)ChatFormatting.GREEN + "Reloaded Sound System.");
        }
        catch (Exception e) {
            System.out.println((Object)ChatFormatting.LIGHT_PURPLE + "Could not restart sound manager: " + e.toString());
            e.printStackTrace();
            Command.sendMessage((Object)ChatFormatting.LIGHT_PURPLE + "Couldnt Reload Sound System!");
        }
    }
}

