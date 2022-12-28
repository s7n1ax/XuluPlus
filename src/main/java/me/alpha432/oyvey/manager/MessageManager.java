/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 */
package me.alpha432.oyvey.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MessageManager {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static String prefix = (Object)TextFormatting.LIGHT_PURPLE + "[" + (Object)TextFormatting.DARK_PURPLE + "Xulu+" + (Object)TextFormatting.LIGHT_PURPLE + "]" + (Object)TextFormatting.RESET;

    public static void sendClientMessage(String message, boolean forcePermanent) {
        if (MessageManager.mc.field_71439_g != null) {
            try {
                TextComponentString e = new TextComponentString(prefix + " " + message);
                int i = forcePermanent ? 0 : 12076;
                MessageManager.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)e, i);
            }
            catch (NullPointerException nullpointerexception) {
                nullpointerexception.printStackTrace();
            }
        }
    }
}

