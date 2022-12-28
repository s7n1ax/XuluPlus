/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.alpha432.oyvey.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopManager
extends Feature {
    private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    private final Set<EntityPlayer> toAnnounce = new HashSet<EntityPlayer>();

    public void onTotemPop(EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals((Object)TotemPopManager.mc.field_71439_g)) {
            this.toAnnounce.add(player);
        }
    }

    public void onDeath(EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals((Object)TotemPopManager.mc.field_71439_g)) {
            int playerNumber = 0;
            for (char character : player.func_70005_c_().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            this.toAnnounce.remove((Object)player);
        }
        this.resetPops(player);
    }

    public void onLogout(EntityPlayer player, boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }

    public void onOwnLogout(boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }

    public void clearList() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    }

    public void resetPops(EntityPlayer player) {
        this.setTotemPops(player, 0);
    }

    public void popTotem(EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(EntityPlayer player, int amount) {
        this.poplist.put(player, amount);
    }

    public int getTotemPops(EntityPlayer player) {
        Integer pops = this.poplist.get((Object)player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }

    public String getTotemPopString(EntityPlayer player) {
        return "\u00a7f" + (this.getTotemPops(player) <= 0 ? "" : "-" + this.getTotemPops(player) + " ");
    }
}

