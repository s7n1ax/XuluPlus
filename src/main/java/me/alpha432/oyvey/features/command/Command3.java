/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentBase
 *  net.minecraft.util.text.TextComponentString
 */
package me.alpha432.oyvey.features.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;

public abstract class Command3
extends Feature {
    protected String name;
    protected String[] commands;

    public Command3(String name) {
        super(name);
        this.name = name;
        this.commands = new String[]{""};
    }

    public Command3(String name, String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }

    public static void sendMessage(String message, boolean notification) {
        Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + " \u00a7r" + message);
        if (notification) {
            OyVey.notificationManager.addNotification(message, 3000L);
        }
    }

    public static void sendMessage(String message) {
        Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + " \u00a7r" + message);
    }

    public static void sendSilentMessage(String message) {
        if (Command.nullCheck()) {
            return;
        }
        Command.mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(message));
    }

    public static void sendOverwriteMessage(String message, int id, boolean notification) {
        TextComponentString component = new TextComponentString(message);
        Command.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)component, id);
        if (notification) {
            OyVey.notificationManager.addNotification(message, 3000L);
        }
    }

    public static void sendRainbowMessage(String message) {
        StringBuilder stringBuilder = new StringBuilder(message);
        stringBuilder.insert(0, "\u00a7+");
        Command.mc.field_71439_g.func_145747_a((ITextComponent)new ChatMessage(stringBuilder.toString()));
    }

    public static String getCommandPrefix() {
        return OyVey.commandManager.getPrefix();
    }

    public abstract void execute(String[] var1);

    @Override
    public String getName() {
        return this.name;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public static class ChatMessage
    extends TextComponentBase {
        private final String text;

        public ChatMessage(String text) {
            Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher matcher = pattern.matcher(text);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                String replacement = "\u00a7" + matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }

        public String func_150261_e() {
            return this.text;
        }

        public ITextComponent func_150259_f() {
            return new ChatMessage(this.text);
        }
    }
}

