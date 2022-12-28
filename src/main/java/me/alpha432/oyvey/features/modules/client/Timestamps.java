/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Timestamps
extends Module {
    public Timestamps() {
        super("ChatTime", "Prefixes chat messages with the time", Module.Category.CLIENT, true, false, false);
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String strDate = dateFormatter.format(date);
        TextComponentString time = new TextComponentString((Object)ChatFormatting.LIGHT_PURPLE + "[" + (Object)ChatFormatting.DARK_PURPLE + strDate + (Object)ChatFormatting.LIGHT_PURPLE + "]" + (Object)ChatFormatting.RESET + " ");
        event.setMessage(time.func_150257_a(event.getMessage()));
    }
}

