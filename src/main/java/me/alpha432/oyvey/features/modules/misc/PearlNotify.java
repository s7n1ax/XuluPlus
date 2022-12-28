/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.UUID;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

public class PearlNotify
extends Module {
    private final HashMap<EntityPlayer, UUID> list = new HashMap();
    private Entity enderPearl;
    private boolean flag;

    public PearlNotify() {
        super("PearlNotify", "Notify pearl throws.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        this.flag = true;
    }

    @Override
    public void onUpdate() {
        if (PearlNotify.mc.field_71441_e == null || PearlNotify.mc.field_71439_g == null) {
            return;
        }
        this.enderPearl = null;
        for (Object e : PearlNotify.mc.field_71441_e.field_72996_f) {
            if (!(e instanceof EntityEnderPearl)) continue;
            this.enderPearl = e;
            break;
        }
        if (this.enderPearl == null) {
            this.flag = true;
            return;
        }
        EntityPlayer closestPlayer = null;
        for (EntityPlayer entity : PearlNotify.mc.field_71441_e.field_73010_i) {
            if (closestPlayer == null) {
                closestPlayer = entity;
                continue;
            }
            if (closestPlayer.func_70032_d(this.enderPearl) <= entity.func_70032_d(this.enderPearl)) continue;
            closestPlayer = entity;
        }
        if (closestPlayer == PearlNotify.mc.field_71439_g) {
            this.flag = false;
        }
        if (closestPlayer != null && this.flag) {
            String faceing = this.enderPearl.func_174811_aO().toString();
            if (faceing.equals("west")) {
                faceing = "east";
            } else if (faceing.equals("east")) {
                faceing = "west";
            }
            Command.sendMessage(OyVey.friendManager.isFriend(closestPlayer.func_70005_c_()) ? (Object)ChatFormatting.AQUA + closestPlayer.func_70005_c_() + (Object)ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!" : (Object)ChatFormatting.RED + closestPlayer.func_70005_c_() + (Object)ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!");
            this.flag = false;
        }
    }
}

