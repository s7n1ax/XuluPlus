/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.ItemTooltipEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.misc;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolTips
extends Module {
    private static final ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static ToolTips INSTANCE = new ToolTips();
    public Map<EntityPlayer, ItemStack> spiedPlayers = new ConcurrentHashMap<EntityPlayer, ItemStack>();
    public Map<EntityPlayer, Timer> playerTimers = new ConcurrentHashMap<EntityPlayer, Timer>();
    private int textRadarY = 0;

    public ToolTips() {
        super("ShulkerViewer", "Shows you the ShulkerInsides", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static ToolTips getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ToolTips();
        }
        return INSTANCE;
    }

    public static void displayInv(ItemStack stack, String name) {
        try {
            Item item = stack.func_77973_b();
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.field_145854_h = shulker.func_179223_d();
            entityBox.func_145834_a((World)ToolTips.mc.field_71441_e);
            ItemStackHelper.func_191283_b((NBTTagCompound)stack.func_77978_p().func_74775_l("BlockEntityTag"), (NonNullList)entityBox.field_190596_f);
            entityBox.func_145839_a(stack.func_77978_p().func_74775_l("BlockEntityTag"));
            entityBox.func_190575_a(name == null ? stack.func_82833_r() : name);
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                ToolTips.mc.field_71439_g.func_71007_a((IInventory)entityBox);
            }).start();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (ToolTips.fullNullCheck()) {
            return;
        }
        for (EntityPlayer player : ToolTips.mc.field_71441_e.field_73010_i) {
            if (player == null || !(player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox) || ToolTips.mc.field_71439_g == player) continue;
            ItemStack stack = player.func_184614_ca();
            this.spiedPlayers.put(player, stack);
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (ToolTips.fullNullCheck()) {
            return;
        }
        int x = -3;
        int y = 124;
        this.textRadarY = 0;
        for (EntityPlayer player : ToolTips.mc.field_71441_e.field_73010_i) {
            Timer playerTimer;
            if (this.spiedPlayers.get((Object)player) == null) continue;
            if (player.func_184614_ca() == null || !(player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox)) {
                playerTimer = this.playerTimers.get((Object)player);
                if (playerTimer == null) {
                    Timer timer = new Timer();
                    timer.reset();
                    this.playerTimers.put(player, timer);
                } else if (playerTimer.passedS(3.0)) {
                    continue;
                }
            } else if (player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get((Object)player)) != null) {
                playerTimer.reset();
                this.playerTimers.put(player, playerTimer);
            }
            ItemStack stack = this.spiedPlayers.get((Object)player);
            this.renderShulkerToolTip(stack, x, y, player.func_70005_c_());
            this.textRadarY = (y += 78) - 10 - 114 + 2;
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void makeTooltip(ItemTooltipEvent event) {
    }

    public void renderShulkerToolTip(ItemStack stack, int x, int y, String name) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound = stack.func_77978_p();
        if (tagCompound != null && tagCompound.func_150297_b("BlockEntityTag", 10) && (blockEntityTag = tagCompound.func_74775_l("BlockEntityTag")).func_150297_b("Items", 9)) {
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179147_l();
            GlStateManager.func_187428_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            mc.func_110434_K().func_110577_a(SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.func_179097_i();
            Color color = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 200);
            this.renderer.drawStringWithShadow(name == null ? stack.func_82833_r() : name, x + 8, y + 6, ColorUtil.toRGBA(color));
            GlStateManager.func_179126_j();
            RenderHelper.func_74520_c();
            GlStateManager.func_179091_B();
            GlStateManager.func_179142_g();
            GlStateManager.func_179145_e();
            NonNullList nonnulllist = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = x + i % 9 * 18 + 8;
                int iY = y + i / 9 * 18 + 18;
                ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                ToolTips.mc.func_175597_ag().field_178112_h.field_77023_b = 501.0f;
                RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
                RenderUtil.itemRender.func_180453_a(ToolTips.mc.field_71466_p, itemStack, iX, iY, null);
                ToolTips.mc.func_175597_ag().field_178112_h.field_77023_b = 0.0f;
            }
            GlStateManager.func_179140_f();
            GlStateManager.func_179084_k();
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

