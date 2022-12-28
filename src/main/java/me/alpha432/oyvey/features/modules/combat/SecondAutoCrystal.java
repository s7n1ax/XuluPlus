/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.Util;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SecondAutoCrystal
extends Module {
    private final Timer placeTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private final Timer preditTimer = new Timer();
    private final Timer manualTimer = new Timer();
    private final Setting<Integer> attackFactor = this.register(new Setting<Integer>("PredictDelay", 0, 0, 200));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("Boxalpha", 125, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("Linewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    public Setting<Boolean> sound = this.register(new Setting<Boolean>("Sequential", true));
    public Setting<Boolean> place = this.register(new Setting<Boolean>("Place", true));
    public Setting<Float> placeDelay = this.register(new Setting<Float>("Delays", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(300.0f)));
    public Setting<Float> placeRange = this.register(new Setting<Float>("Range", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> explode = this.register(new Setting<Boolean>("Break", true));
    public Setting<Boolean> packetBreak = this.register(new Setting<Boolean>("BreakPackets[MAX]", true));
    public Setting<Boolean> predicts = this.register(new Setting<Boolean>("Infered", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Float> breakDelay = this.register(new Setting<Float>("Delays", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(300.0f)));
    public Setting<Float> breakRange = this.register(new Setting<Float>("Brange", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Float> breakWallRange = this.register(new Setting<Float>("Breakwallrange", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> opPlace = this.register(new Setting<Boolean>("1.13 place", true));
    public Setting<Boolean> suicide = this.register(new Setting<Boolean>("AntiSelfKill", true));
    public Setting<Boolean> autoswitch = this.register(new Setting<Boolean>("Autoswitch", true));
    public Setting<SwitchMode> switchmode = this.register(new Setting<SwitchMode>("Awitchmode", SwitchMode.Normal, v -> this.autoswitch.getValue()));
    public Setting<Boolean> silentSwitch = this.register(new Setting<Boolean>("Silent", Boolean.valueOf(true), v -> this.switchmode.getValue() == SwitchMode.Silent));
    public Setting<Boolean> ignoreUseAmount = this.register(new Setting<Boolean>("UseAmount[OverLook]", true));
    public Setting<Integer> wasteAmount = this.register(new Setting<Integer>("UseAmount", 4, 1, 5));
    public Setting<Boolean> facePlaceSword = this.register(new Setting<Boolean>("SwordFP", true));
    public Setting<Float> targetRange = this.register(new Setting<Float>("EnemyRange", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(12.0f)));
    public Setting<Float> minDamage = this.register(new Setting<Float>("MinDMG", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    public Setting<Float> facePlace = this.register(new Setting<Float>("FaceHP", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(36.0f)));
    public Setting<Float> breakMaxSelfDamage = this.register(new Setting<Float>("Breakmaxself", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(12.0f)));
    public Setting<Float> breakMinDmg = this.register(new Setting<Float>("Breakmindmg", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Float> minArmor = this.register(new Setting<Float>("MinArmor", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(80.0f)));
    public Setting<SwingMode> swingMode = this.register(new Setting<SwingMode>("Swing", SwingMode.MainHand));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", true));
    public Setting<Boolean> renderDmg = this.register(new Setting<Boolean>("renderdmg", true));
    public Setting<Boolean> box = this.register(new Setting<Boolean>("box", true));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("outline", true));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("8-red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("8.8-green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("8-blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("8-alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    EntityEnderCrystal crystal;
    private EntityLivingBase target;
    private BlockPos pos;
    private int hotBarSlot;
    private boolean armor;
    private boolean armorTarget;
    private int crystalCount;
    private int predictWait;
    private int predictPackets;
    private boolean packetCalc;
    private float yaw = 0.0f;
    private EntityLivingBase realTarget;
    private int predict;
    private float pitch = 0.0f;
    private boolean rotating = false;

    public SecondAutoCrystal() {
        super("CrystalAura[Simple]", "Slightly Improved[Choose your fav]", Module.Category.COMBAT, true, false, false);
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.func_177958_n();
        int cy = loc.func_177956_o();
        int cz = loc.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f = sphere ? (float)cy + r : (float)(cy + h);
                    float f2 = f;
                    if (!((float)y < f)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.field_149476_e = this.yaw;
            packet.field_149473_f = this.pitch;
            this.rotating = false;
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=true)
    public void onSoundPacket(PacketEvent.Receive event) {
        SPacketSoundEffect packet2;
        if (AutoCrystal.fullNullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketSoundEffect && this.sound.getValue().booleanValue() && (packet2 = (SPacketSoundEffect)event.getPacket()).func_186977_b() == SoundCategory.BLOCKS && packet2.func_186978_a() == SoundEvents.field_187539_bB) {
            ArrayList entities = new ArrayList(SecondAutoCrystal.mc.field_71441_e.field_72996_f);
            int size = entities.size();
            for (int i = 0; i < size; ++i) {
                Entity entity = (Entity)entities.get(i);
                if (!(entity instanceof EntityEnderCrystal) || !(entity.func_70092_e(packet2.func_149207_d(), packet2.func_149211_e(), packet2.func_149210_f()) < 36.0)) continue;
                entity.func_70106_y();
            }
        }
    }

    private void rotateTo(Entity entity) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(SecondAutoCrystal.mc.field_71439_g.func_174824_e(Util.mc.func_184121_ak()), entity.func_174791_d());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(SecondAutoCrystal.mc.field_71439_g.func_174824_e(Util.mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    @Override
    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.predictWait = 0;
        this.hotBarSlot = -1;
        this.pos = null;
        this.crystal = null;
        this.predict = 0;
        this.predictPackets = 1;
        this.target = null;
        this.packetCalc = false;
        this.realTarget = null;
        this.armor = false;
        this.armorTarget = false;
    }

    @Override
    public void onDisable() {
        this.rotating = false;
    }

    @Override
    public void onTick() {
        this.onCrystal();
    }

    @Override
    public String getDisplayInfo() {
        if (this.realTarget != null) {
            return this.realTarget.func_70005_c_();
        }
        return null;
    }

    public void onCrystal() {
        if (SecondAutoCrystal.mc.field_71441_e == null || SecondAutoCrystal.mc.field_71439_g == null) {
            return;
        }
        this.realTarget = null;
        this.manualBreaker();
        this.crystalCount = 0;
        if (!this.ignoreUseAmount.getValue().booleanValue()) {
            for (Entity crystal : SecondAutoCrystal.mc.field_71441_e.field_72996_f) {
                if (!(crystal instanceof EntityEnderCrystal) || !this.IsValidCrystal(crystal)) continue;
                boolean count = false;
                double damage = this.calculateDamage((double)this.target.func_180425_c().func_177958_n() + 0.5, (double)this.target.func_180425_c().func_177956_o() + 1.0, (double)this.target.func_180425_c().func_177952_p() + 0.5, (Entity)this.target);
                if (damage >= (double)this.minDamage.getValue().floatValue()) {
                    count = true;
                }
                if (!count) continue;
                ++this.crystalCount;
            }
        }
        this.hotBarSlot = -1;
        if (SecondAutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_185158_cP) {
            int crystalSlot;
            int n = crystalSlot = SecondAutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP ? SecondAutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (SecondAutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() != Items.field_185158_cP) continue;
                    crystalSlot = l;
                    this.hotBarSlot = l;
                    break;
                }
            }
            if (crystalSlot == -1) {
                this.pos = null;
                this.target = null;
                return;
            }
        }
        if (SecondAutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && SecondAutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_185158_cP) {
            this.pos = null;
            this.target = null;
            return;
        }
        if (this.target == null) {
            this.target = this.getTarget();
        }
        if (this.target == null) {
            this.crystal = null;
            return;
        }
        if (this.target.func_70032_d((Entity)SecondAutoCrystal.mc.field_71439_g) > 12.0f) {
            this.crystal = null;
            this.target = null;
        }
        this.crystal = SecondAutoCrystal.mc.field_71441_e.field_72996_f.stream().filter(this::IsValidCrystal).map(p_Entity -> (EntityEnderCrystal)p_Entity).min(Comparator.comparing(p_Entity -> Float.valueOf(this.target.func_70032_d((Entity)p_Entity)))).orElse(null);
        if (this.crystal != null && this.explode.getValue().booleanValue() && this.breakTimer.passedMs(this.breakDelay.getValue().longValue())) {
            this.breakTimer.reset();
            if (this.packetBreak.getValue().booleanValue()) {
                this.rotateTo((Entity)this.crystal);
                SecondAutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity((Entity)this.crystal));
            } else {
                this.rotateTo((Entity)this.crystal);
                SecondAutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)SecondAutoCrystal.mc.field_71439_g, (Entity)this.crystal);
            }
            if (this.swingMode.getValue() == SwingMode.MainHand) {
                SecondAutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            } else if (this.swingMode.getValue() == SwingMode.OffHand) {
                SecondAutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.OFF_HAND);
            }
        }
        if (this.placeTimer.passedMs(this.placeDelay.getValue().longValue()) && this.place.getValue().booleanValue()) {
            this.placeTimer.reset();
            double damage = 0.5;
            for (BlockPos blockPos : this.placePostions(this.placeRange.getValue().floatValue())) {
                double d;
                double targetRange;
                if (blockPos == null || this.target == null || !SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(blockPos)).isEmpty() || (targetRange = this.target.func_70011_f((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p())) > (double)this.targetRange.getValue().floatValue() || this.target.field_70128_L || this.target.func_110143_aJ() + this.target.func_110139_bj() <= 0.0f) continue;
                double targetDmg = this.calculateDamage((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 1.0, (double)blockPos.func_177952_p() + 0.5, (Entity)this.target);
                this.armor = false;
                for (ItemStack is : this.target.func_184193_aE()) {
                    float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
                    float red = 1.0f - green;
                    int dmg = 100 - (int)(red * 100.0f);
                    if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
                    this.armor = true;
                }
                if (targetDmg < (double)this.minDamage.getValue().floatValue() && (this.facePlaceSword.getValue() != false ? this.target.func_110139_bj() + this.target.func_110143_aJ() > this.facePlace.getValue().floatValue() : SecondAutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword || this.target.func_110139_bj() + this.target.func_110143_aJ() > this.facePlace.getValue().floatValue()) && (this.facePlaceSword.getValue() == false ? SecondAutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword || !this.armor : !this.armor)) continue;
                double selfDmg = this.calculateDamage((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 1.0, (double)blockPos.func_177952_p() + 0.5, (Entity)SecondAutoCrystal.mc.field_71439_g);
                if (d + (this.suicide.getValue() != false ? 2.0 : 0.5) >= (double)(SecondAutoCrystal.mc.field_71439_g.func_110143_aJ() + SecondAutoCrystal.mc.field_71439_g.func_110139_bj()) && selfDmg >= targetDmg && targetDmg < (double)(this.target.func_110143_aJ() + this.target.func_110139_bj()) || !(damage < targetDmg)) continue;
                this.pos = blockPos;
                damage = targetDmg;
            }
            if (damage == 0.5) {
                this.pos = null;
                this.target = null;
                this.realTarget = null;
                return;
            }
            this.realTarget = this.target;
            if (this.hotBarSlot != -1 && this.autoswitch.getValue().booleanValue() && !SecondAutoCrystal.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t) && this.switchmode.getValue() == SwitchMode.Normal && !this.silentSwitch.getValue().booleanValue()) {
                SecondAutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = this.hotBarSlot;
            }
            int slot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
            int old = Util.mc.field_71439_g.field_71071_by.field_70461_c;
            EnumHand hand = null;
            if (this.switchmode.getValue() == SwitchMode.Silent && slot != -1) {
                if (Util.mc.field_71439_g.func_184587_cr() && this.silentSwitch.getValue().booleanValue()) {
                    hand = Util.mc.field_71439_g.func_184600_cs();
                }
                Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            }
            if (!this.ignoreUseAmount.getValue().booleanValue()) {
                int crystalLimit = this.wasteAmount.getValue();
                if (this.crystalCount >= crystalLimit) {
                    return;
                }
                if (damage < (double)this.minDamage.getValue().floatValue()) {
                    crystalLimit = 1;
                }
                if (this.crystalCount < crystalLimit && this.pos != null) {
                    this.rotateToPos(this.pos);
                    SecondAutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, SecondAutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            } else if (this.pos != null) {
                this.rotateToPos(this.pos);
                SecondAutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, SecondAutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            if (this.switchmode.getValue() == SwitchMode.Silent && slot != -1) {
                if (this.silentSwitch.getValue().booleanValue() && hand != null) {
                    Util.mc.field_71439_g.func_184598_c(hand);
                }
                Util.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(old));
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).func_148993_l() == 51 && this.predicts.getValue().booleanValue() && this.preditTimer.passedMs(this.attackFactor.getValue().longValue()) && this.predicts.getValue().booleanValue() && this.explode.getValue().booleanValue() && this.packetBreak.getValue().booleanValue() && this.target != null) {
            if (!this.isPredicting(packet)) {
                return;
            }
            CPacketUseEntity predict = new CPacketUseEntity();
            predict.field_149567_a = packet.func_149001_c();
            predict.field_149566_b = CPacketUseEntity.Action.ATTACK;
            SecondAutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)predict);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.pos != null && this.render.getValue().booleanValue() && this.target != null) {
            RenderUtil.drawBoxESP(this.pos, ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.outline.getValue(), ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
            if (this.renderDmg.getValue().booleanValue()) {
                double renderDamage = this.calculateDamage((double)this.pos.func_177958_n() + 0.5, (double)this.pos.func_177956_o() + 1.0, (double)this.pos.func_177952_p() + 0.5, (Entity)this.target);
                RenderUtil.drawText(this.pos, (Math.floor(renderDamage) == renderDamage ? Integer.valueOf((int)renderDamage) : String.format("%.1f", renderDamage)) + "");
            }
        }
    }

    private boolean isPredicting(SPacketSpawnObject packet) {
        double d;
        BlockPos packPos = new BlockPos(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e());
        if (SecondAutoCrystal.mc.field_71439_g.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) > (double)this.breakRange.getValue().floatValue()) {
            return false;
        }
        if (!this.canSeePos(packPos) && SecondAutoCrystal.mc.field_71439_g.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) > (double)this.breakWallRange.getValue().floatValue()) {
            return false;
        }
        double targetDmg = this.calculateDamage(packet.func_186880_c() + 0.5, packet.func_186882_d() + 1.0, packet.func_186881_e() + 0.5, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)SecondAutoCrystal.mc.field_71439_g) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = this.calculateDamage(packet.func_186880_c() + 0.5, packet.func_186882_d() + 1.0, packet.func_186881_e() + 0.5, (Entity)SecondAutoCrystal.mc.field_71439_g);
        double d2 = d = this.suicide.getValue() != false ? 2.0 : 0.5;
        if (selfDmg + d < (double)(SecondAutoCrystal.mc.field_71439_g.func_110143_aJ() + SecondAutoCrystal.mc.field_71439_g.func_110139_bj()) && targetDmg >= (double)(this.target.func_110139_bj() + this.target.func_110143_aJ())) {
            return true;
        }
        this.armorTarget = false;
        for (ItemStack is : this.target.func_184193_aE()) {
            float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
            this.armorTarget = true;
        }
        if (targetDmg >= (double)this.breakMinDmg.getValue().floatValue() && selfDmg <= (double)this.breakMaxSelfDamage.getValue().floatValue()) {
            return true;
        }
        return EntityUtil.isInHole((Entity)this.target) && this.target.func_110143_aJ() + this.target.func_110139_bj() <= this.facePlace.getValue().floatValue();
    }

    private boolean IsValidCrystal(Entity p_Entity) {
        double d;
        if (p_Entity == null) {
            return false;
        }
        if (!(p_Entity instanceof EntityEnderCrystal)) {
            return false;
        }
        if (this.target == null) {
            return false;
        }
        if (p_Entity.func_70032_d((Entity)SecondAutoCrystal.mc.field_71439_g) > this.breakRange.getValue().floatValue()) {
            return false;
        }
        if (!SecondAutoCrystal.mc.field_71439_g.func_70685_l(p_Entity) && p_Entity.func_70032_d((Entity)SecondAutoCrystal.mc.field_71439_g) > this.breakWallRange.getValue().floatValue()) {
            return false;
        }
        if (this.target.field_70128_L || this.target.func_110143_aJ() + this.target.func_110139_bj() <= 0.0f) {
            return false;
        }
        double targetDmg = this.calculateDamage((double)p_Entity.func_180425_c().func_177958_n() + 0.5, (double)p_Entity.func_180425_c().func_177956_o() + 1.0, (double)p_Entity.func_180425_c().func_177952_p() + 0.5, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)SecondAutoCrystal.mc.field_71439_g) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = this.calculateDamage((double)p_Entity.func_180425_c().func_177958_n() + 0.5, (double)p_Entity.func_180425_c().func_177956_o() + 1.0, (double)p_Entity.func_180425_c().func_177952_p() + 0.5, (Entity)SecondAutoCrystal.mc.field_71439_g);
        double d2 = d = this.suicide.getValue() != false ? 2.0 : 0.5;
        if (selfDmg + d < (double)(SecondAutoCrystal.mc.field_71439_g.func_110143_aJ() + SecondAutoCrystal.mc.field_71439_g.func_110139_bj()) && targetDmg >= (double)(this.target.func_110139_bj() + this.target.func_110143_aJ())) {
            return true;
        }
        this.armorTarget = false;
        for (ItemStack is : this.target.func_184193_aE()) {
            float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
            this.armorTarget = true;
        }
        if (targetDmg >= (double)this.breakMinDmg.getValue().floatValue() && selfDmg <= (double)this.breakMaxSelfDamage.getValue().floatValue()) {
            return true;
        }
        return EntityUtil.isInHole((Entity)this.target) && this.target.func_110143_aJ() + this.target.func_110139_bj() <= this.facePlace.getValue().floatValue();
    }

    EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        for (EntityPlayer entity : SecondAutoCrystal.mc.field_71441_e.field_73010_i) {
            if (SecondAutoCrystal.mc.field_71439_g == null || SecondAutoCrystal.mc.field_71439_g.field_70128_L || entity.field_70128_L || entity == SecondAutoCrystal.mc.field_71439_g || OyVey.friendManager.isFriend(entity.func_70005_c_()) || entity.func_70032_d((Entity)SecondAutoCrystal.mc.field_71439_g) > 12.0f) continue;
            this.armorTarget = false;
            for (ItemStack is : entity.func_184193_aE()) {
                float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
                this.armorTarget = true;
            }
            if (EntityUtil.isInHole((Entity)entity) && entity.func_110139_bj() + entity.func_110143_aJ() > this.facePlace.getValue().floatValue() && !this.armorTarget && this.minDamage.getValue().floatValue() > 2.2f) continue;
            if (closestPlayer == null) {
                closestPlayer = entity;
                continue;
            }
            if (!(closestPlayer.func_70032_d((Entity)SecondAutoCrystal.mc.field_71439_g) > entity.func_70032_d((Entity)SecondAutoCrystal.mc.field_71439_g))) continue;
            closestPlayer = entity;
        }
        return closestPlayer;
    }

    private void manualBreaker() {
        RayTraceResult result;
        if (this.manualTimer.passedMs(200L) && SecondAutoCrystal.mc.field_71474_y.field_74313_G.func_151470_d() && SecondAutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao && SecondAutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151153_ao && SecondAutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151031_f && SecondAutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151062_by && (result = SecondAutoCrystal.mc.field_71476_x) != null) {
            if (result.field_72313_a.equals((Object)RayTraceResult.Type.ENTITY)) {
                Entity entity = result.field_72308_g;
                if (entity instanceof EntityEnderCrystal) {
                    if (this.packetBreak.getValue().booleanValue()) {
                        SecondAutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
                    } else {
                        SecondAutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)SecondAutoCrystal.mc.field_71439_g, entity);
                    }
                    this.manualTimer.reset();
                }
            } else if (result.field_72313_a.equals((Object)RayTraceResult.Type.BLOCK)) {
                BlockPos mousePos = new BlockPos((double)SecondAutoCrystal.mc.field_71476_x.func_178782_a().func_177958_n(), (double)SecondAutoCrystal.mc.field_71476_x.func_178782_a().func_177956_o() + 1.0, (double)SecondAutoCrystal.mc.field_71476_x.func_178782_a().func_177952_p());
                for (Entity target : SecondAutoCrystal.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(mousePos))) {
                    if (!(target instanceof EntityEnderCrystal)) continue;
                    if (this.packetBreak.getValue().booleanValue()) {
                        SecondAutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(target));
                    } else {
                        SecondAutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)SecondAutoCrystal.mc.field_71439_g, target);
                    }
                    this.manualTimer.reset();
                }
            }
        }
    }

    private boolean canSeePos(BlockPos pos) {
        return SecondAutoCrystal.mc.field_71441_e.func_147447_a(new Vec3d(SecondAutoCrystal.mc.field_71439_g.field_70165_t, SecondAutoCrystal.mc.field_71439_g.field_70163_u + (double)SecondAutoCrystal.mc.field_71439_g.func_70047_e(), SecondAutoCrystal.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p()), false, true, false) == null;
    }

    private NonNullList<BlockPos> placePostions(float placeRange) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)SecondAutoCrystal.getSphere(new BlockPos(Math.floor(SecondAutoCrystal.mc.field_71439_g.field_70165_t), Math.floor(SecondAutoCrystal.mc.field_71439_g.field_70163_u), Math.floor(SecondAutoCrystal.mc.field_71439_g.field_70161_v)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> this.canPlaceCrystal((BlockPos)pos, true)).collect(Collectors.toList()));
        return positions;
    }

    private boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (!this.opPlace.getValue().booleanValue()) {
                if (SecondAutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && SecondAutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (SecondAutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || SecondAutoCrystal.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (Entity entity : SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
                for (Entity entity : SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (SecondAutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && SecondAutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (SecondAutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : SecondAutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.func_70011_f(posX, posY, posZ) / 12.0;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)SecondAutoCrystal.mc.field_71441_e, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    private float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.func_94539_a((Explosion)explosion);
            damage = CombatRules.func_189427_a((float)damage, (float)ep.func_70658_aO(), (float)((float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
            int k = 0;
            try {
                k = EnchantmentHelper.func_77508_a((Iterable)ep.func_184193_aE(), (DamageSource)ds);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.func_76131_a((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(MobEffects.field_76429_m)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
        return damage;
    }

    private float getDamageMultiplied(float damage) {
        int diff = SecondAutoCrystal.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static enum SwitchMode {
        Normal,
        Silent;

    }

    public static enum SwingMode {
        MainHand,
        OffHand,
        None;

    }
}

