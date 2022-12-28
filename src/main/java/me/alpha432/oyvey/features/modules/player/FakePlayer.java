/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.world.World
 */
package me.alpha432.oyvey.features.modules.player;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class FakePlayer
extends Module {
    public static final String[][] phobosInfo = new String[][]{{"8af022c8-b926-41a0-8b79-2b544ff00fcf", "ddevil69gfg", "3", "0"}, {"0aa3b04f-786a-49c8-bea9-025ee0dd1e85", "yippie", "-3", "0"}, {"19bf3f1f-fe06-4c86-bea5-3dad5df89714", "Zocker160", "0", "-3"}, {"e47d6571-99c2-415b-955e-c4bc7b55941b", "Genghius", "0", "3"}, {"b01f9bc1-cb7c-429a-b178-93d771f00926", "Strox", "6", "0"}, {"b232930c-c28a-4e10-8c90-f152235a65c5", "Burak", "-6", "0"}, {"ace08461-3db3-4579-98d3-390a67d5645b", "CodeX", "0", "-6"}, {"5bead5b0-3bab-460d-af1d-7929950f40c2", "uexv", "0", "6"}, {"78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "fckme", "0", "9"}, {"78ee2bd6-64c4-45f0-96e5-0b6747ba7382", "alphamnky", "0", "-9"}};
    private static final String[] fitInfo = new String[]{"fdee323e-7f0c-4c15-8d1c-0f277442342a", "ddevil69gfg"};
    private static FakePlayer INSTANCE = new FakePlayer();
    private final List<EntityOtherPlayerMP> fakeEntities = new ArrayList<EntityOtherPlayerMP>();
    private final Setting<Boolean> copyInv = this.register(new Setting<Boolean>("InventoryKeep", true));
    public Setting<Boolean> multi = this.register(new Setting<Boolean>("Change", false));
    private final Setting<Integer> players = this.register(new Setting<Object>("Gamers", 1, 1, 9, v -> this.multi.getValue(), "Amount of other players."));
    public List<Integer> fakePlayerIdList = new ArrayList<Integer>();

    public FakePlayer() {
        super("FakePlayer", "Spawns in a fake player", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static FakePlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakePlayer();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        if (FakePlayer.fullNullCheck()) {
            this.disable();
            return;
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        if (this.multi.getValue().booleanValue()) {
            int amount = 0;
            int entityId = -101;
            for (String[] data : phobosInfo) {
                this.addFakePlayer(data[0], data[1], entityId, Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                if (++amount >= this.players.getValue()) {
                    return;
                }
                entityId -= amount;
            }
        } else {
            this.addFakePlayer(fitInfo[0], fitInfo[1], -100, 0, 0);
        }
    }

    @Override
    public void onDisable() {
        if (FakePlayer.fullNullCheck()) {
            return;
        }
        for (int id : this.fakePlayerIdList) {
            FakePlayer.mc.field_71441_e.func_73028_b(id);
        }
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
        GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.field_71441_e, profile);
        fakePlayer.func_82149_j((Entity)FakePlayer.mc.field_71439_g);
        fakePlayer.field_70165_t += (double)offsetX;
        fakePlayer.field_70161_v += (double)offsetZ;
        if (this.copyInv.getValue().booleanValue()) {
            for (PotionEffect potionEffect : OyVey.potionManager.getOwnPotions()) {
                fakePlayer.func_70690_d(potionEffect);
            }
            fakePlayer.field_71071_by.func_70455_b(FakePlayer.mc.field_71439_g.field_71071_by);
        }
        fakePlayer.func_70606_j(FakePlayer.mc.field_71439_g.func_110143_aJ() + FakePlayer.mc.field_71439_g.func_110139_bj());
        this.fakeEntities.add(fakePlayer);
        FakePlayer.mc.field_71441_e.func_73027_a(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }
}

