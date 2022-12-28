/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  org.lwjgl.input.Mouse
 */
package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public class MCF
extends Module {
    private boolean clicked = false;

    public MCF() {
        super("MiddleClick", "Middleclick Friends.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown((int)2)) {
            if (!this.clicked && MCF.mc.field_71462_r == null) {
                this.onClick();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void onClick() {
        Entity entity;
        RayTraceResult result = MCF.mc.field_71476_x;
        if (result != null && result.field_72313_a == RayTraceResult.Type.ENTITY && (entity = result.field_72308_g) instanceof EntityPlayer) {
            if (OyVey.friendManager.isFriend(entity.func_70005_c_())) {
                OyVey.friendManager.removeFriend(entity.func_70005_c_());
                Command.sendMessage((Object)ChatFormatting.RED + entity.func_70005_c_() + (Object)ChatFormatting.RED + " has been unfriended.");
            } else {
                OyVey.friendManager.addFriend(entity.func_70005_c_());
                Command.sendMessage((Object)ChatFormatting.AQUA + entity.func_70005_c_() + (Object)ChatFormatting.AQUA + " has been friended.");
            }
        }
        this.clicked = true;
    }
}

