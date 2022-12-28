/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.gui.BossInfoClient
 *  net.minecraft.client.gui.GuiBossOverlay
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 *  net.minecraft.world.BossInfo
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Pre
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.event.entity.PlaySoundAtEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class NoRender
extends Module {
    private static NoRender INSTANCE = new NoRender();
    public Setting<Boolean> fire = this.register(new Setting<Boolean>("Fire", Boolean.valueOf(false), "Removes the portal overlay."));
    public Setting<Boolean> portal = this.register(new Setting<Boolean>("Portal", Boolean.valueOf(false), "Removes the portal overlay."));
    public Setting<Boolean> pumpkin = this.register(new Setting<Boolean>("Pumpkin", Boolean.valueOf(false), "Removes the pumpkin overlay."));
    public Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPop", Boolean.valueOf(false), "Removes the Totem overlay."));
    public Setting<Boolean> items = this.register(new Setting<Boolean>("Items", Boolean.valueOf(false), "Removes items on the ground."));
    public Setting<Boolean> nausea = this.register(new Setting<Boolean>("Nausea", Boolean.valueOf(false), "Removes Portal Nausea."));
    public Setting<Boolean> hurtcam = this.register(new Setting<Boolean>("HurtCam", Boolean.valueOf(false), "Removes shaking after taking damage."));
    public Setting<Fog> fog = this.register(new Setting<Fog>("Fog", Fog.NONE, "Removes Fog."));
    public Setting<Boolean> noWeather = this.register(new Setting<Boolean>("Weather", Boolean.valueOf(false), "AntiWeather"));
    public Setting<Boss> boss = this.register(new Setting<Boss>("BossBars", Boss.NONE, "Modifies the bossbars."));
    public Setting<Float> scale = this.register(new Setting<Object>("Scale", Float.valueOf(0.0f), Float.valueOf(0.5f), Float.valueOf(1.0f), v -> this.boss.getValue() == Boss.MINIMIZE || this.boss.getValue() != Boss.STACK, "Scale of the bars."));
    public Setting<Boolean> bats = this.register(new Setting<Boolean>("Bats", Boolean.valueOf(false), "Removes bats."));
    public Setting<NoArmor> noArmor = this.register(new Setting<NoArmor>("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
    public Setting<Boolean> glint = this.register(new Setting<Object>("Glint", Boolean.valueOf(false), v -> this.noArmor.getValue() != NoArmor.NONE));
    public Setting<Skylight> skylight = this.register(new Setting<Skylight>("Skylight", Skylight.NONE));
    public Setting<Boolean> barriers = this.register(new Setting<Boolean>("Barriers", Boolean.valueOf(false), "Barriers"));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", Boolean.valueOf(false), "Blocks"));
    public Setting<Boolean> advancements = this.register(new Setting<Boolean>("Advancements", false));
    public Setting<Boolean> pigmen = this.register(new Setting<Boolean>("Pigmen", false));
    public Setting<Boolean> timeChange = this.register(new Setting<Boolean>("TimeChange", false));
    public Setting<Integer> time = this.register(new Setting<Object>("Time", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(23000), v -> this.timeChange.getValue()));

    public NoRender() {
        super("NoRender", "Allows you to stop rendering stuff", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static NoRender getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoRender();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.items.getValue().booleanValue()) {
            NoRender.mc.field_71441_e.field_72996_f.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::func_70106_y);
        }
        if (this.noWeather.getValue().booleanValue() && NoRender.mc.field_71441_e.func_72896_J()) {
            NoRender.mc.field_71441_e.func_72894_k(0.0f);
        }
        if (this.timeChange.getValue().booleanValue()) {
            NoRender.mc.field_71441_e.func_72877_b((long)this.time.getValue().intValue());
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate & this.timeChange.getValue()) {
            event.setCanceled(true);
        }
    }

    public void doVoidFogParticles(int posX, int posY, int posZ) {
        int i = 32;
        Random random = new Random();
        ItemStack itemstack = NoRender.mc.field_71439_g.func_184614_ca();
        boolean flag = this.barriers.getValue() == false || NoRender.mc.field_71442_b.func_178889_l() == GameType.CREATIVE && !itemstack.func_190926_b() && itemstack.func_77973_b() == Item.func_150898_a((Block)Blocks.field_180401_cv);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 667; ++j) {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(int x, int y, int z, int offset, Random random, boolean holdingBarrier, BlockPos.MutableBlockPos pos) {
        int i = x + NoRender.mc.field_71441_e.field_73012_v.nextInt(offset) - NoRender.mc.field_71441_e.field_73012_v.nextInt(offset);
        int j = y + NoRender.mc.field_71441_e.field_73012_v.nextInt(offset) - NoRender.mc.field_71441_e.field_73012_v.nextInt(offset);
        int k = z + NoRender.mc.field_71441_e.field_73012_v.nextInt(offset) - NoRender.mc.field_71441_e.field_73012_v.nextInt(offset);
        pos.func_181079_c(i, j, k);
        IBlockState iblockstate = NoRender.mc.field_71441_e.func_180495_p((BlockPos)pos);
        iblockstate.func_177230_c().func_180655_c(iblockstate, (World)NoRender.mc.field_71441_e, (BlockPos)pos, random);
        if (!holdingBarrier && iblockstate.func_177230_c() == Blocks.field_180401_cv) {
            NoRender.mc.field_71441_e.func_175688_a(EnumParticleTypes.BARRIER, (double)((float)i + 0.5f), (double)((float)j + 0.5f), (double)((float)k + 0.5f), 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @SubscribeEvent
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        block7: {
            block8: {
                if (event.getType() != RenderGameOverlayEvent.ElementType.BOSSINFO || this.boss.getValue() == Boss.NONE) break block7;
                if (this.boss.getValue() != Boss.MINIMIZE) break block8;
                Map map = NoRender.mc.field_71456_v.func_184046_j().field_184060_g;
                if (map == null) {
                    return;
                }
                ScaledResolution scaledresolution = new ScaledResolution(mc);
                int i = scaledresolution.func_78326_a();
                int j = 12;
                for (Map.Entry entry : map.entrySet()) {
                    BossInfoClient info = (BossInfoClient)entry.getValue();
                    String text = info.func_186744_e().func_150254_d();
                    int k = (int)((float)i / this.scale.getValue().floatValue() / 2.0f - 91.0f);
                    GL11.glScaled((double)this.scale.getValue().floatValue(), (double)this.scale.getValue().floatValue(), (double)1.0);
                    if (!event.isCanceled()) {
                        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                        mc.func_110434_K().func_110577_a(GuiBossOverlay.field_184058_a);
                        NoRender.mc.field_71456_v.func_184046_j().func_184052_a(k, j, (BossInfo)info);
                        NoRender.mc.field_71466_p.func_175063_a(text, (float)i / this.scale.getValue().floatValue() / 2.0f - (float)(NoRender.mc.field_71466_p.func_78256_a(text) / 2), (float)(j - 9), 0xFFFFFF);
                    }
                    GL11.glScaled((double)(1.0 / (double)this.scale.getValue().floatValue()), (double)(1.0 / (double)this.scale.getValue().floatValue()), (double)1.0);
                    j += 10 + NoRender.mc.field_71466_p.field_78288_b;
                }
                break block7;
            }
            if (this.boss.getValue() != Boss.STACK) break block7;
            Map map = NoRender.mc.field_71456_v.func_184046_j().field_184060_g;
            HashMap to = new HashMap();
            for (Map.Entry entry2 : map.entrySet()) {
                Pair p;
                String s = ((BossInfoClient)entry2.getValue()).func_186744_e().func_150254_d();
                if (to.containsKey(s)) {
                    p = (Pair)to.get(s);
                    p = new Pair(p.getKey(), p.getValue() + 1);
                    to.put(s, p);
                    continue;
                }
                p = new Pair(entry2.getValue(), 1);
                to.put(s, p);
            }
            ScaledResolution scaledresolution2 = new ScaledResolution(mc);
            int l = scaledresolution2.func_78326_a();
            int m = 12;
            for (Map.Entry entry3 : to.entrySet()) {
                String text = (String)entry3.getKey();
                BossInfoClient info2 = (BossInfoClient)((Pair)entry3.getValue()).getKey();
                int a = (Integer)((Pair)entry3.getValue()).getValue();
                text = text + " x" + a;
                int k2 = (int)((float)l / this.scale.getValue().floatValue() / 2.0f - 91.0f);
                GL11.glScaled((double)this.scale.getValue().floatValue(), (double)this.scale.getValue().floatValue(), (double)1.0);
                if (!event.isCanceled()) {
                    GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    mc.func_110434_K().func_110577_a(GuiBossOverlay.field_184058_a);
                    NoRender.mc.field_71456_v.func_184046_j().func_184052_a(k2, m, (BossInfo)info2);
                    NoRender.mc.field_71466_p.func_175063_a(text, (float)l / this.scale.getValue().floatValue() / 2.0f - (float)(NoRender.mc.field_71466_p.func_78256_a(text) / 2), (float)(m - 9), 0xFFFFFF);
                }
                GL11.glScaled((double)(1.0 / (double)this.scale.getValue().floatValue()), (double)(1.0 / (double)this.scale.getValue().floatValue()), (double)1.0);
                m += 10 + NoRender.mc.field_71466_p.field_78288_b;
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<?> event) {
        if (this.bats.getValue().booleanValue() && event.getEntity() instanceof EntityBat) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundAtEntityEvent event) {
        if (this.bats.getValue().booleanValue() && event.getSound().equals((Object)SoundEvents.field_187740_w) || event.getSound().equals((Object)SoundEvents.field_187742_x) || event.getSound().equals((Object)SoundEvents.field_187743_y) || event.getSound().equals((Object)SoundEvents.field_189108_z) || event.getSound().equals((Object)SoundEvents.field_187744_z)) {
            event.setVolume(0.0f);
            event.setPitch(0.0f);
            event.setCanceled(true);
        }
    }

    static {
        INSTANCE = new NoRender();
    }

    public static class Pair<T, S> {
        private T key;
        private S value;

        public Pair(T key, S value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return this.key;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public S getValue() {
            return this.value;
        }

        public void setValue(S value) {
            this.value = value;
        }
    }

    public static enum NoArmor {
        NONE,
        ALL,
        HELMET;

    }

    public static enum Boss {
        NONE,
        REMOVE,
        STACK,
        MINIMIZE;

    }

    public static enum Fog {
        NONE,
        AIR,
        NOFOG;

    }

    public static enum Skylight {
        NONE,
        WORLD,
        ENTITY,
        ALL;

    }
}

