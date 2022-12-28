/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD
extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ItemStack totem = new ItemStack(Items.field_190929_cY);
    private static RenderItem itemRender;
    private static HUD INSTANCE;
    private final Setting<Boolean> grayNess = this.register(new Setting<Boolean>("gray", Boolean.TRUE));
    private final Setting<Boolean> renderingUp = this.register(new Setting<Boolean>("renderingup", Boolean.FALSE, "Orientation of the HUD-Elements."));
    private final Setting<Boolean> waterMark = this.register(new Setting<Boolean>("watermark", Boolean.FALSE, "displays watermark"));
    private final Setting<Boolean> waterMark2 = this.register(new Setting<Boolean>("welcomer", Boolean.FALSE, "displays watermark"));
    private final Setting<Boolean> waterMark3 = this.register(new Setting<Boolean>("totemstext", Boolean.FALSE, "displays watermark"));
    public Setting<Integer> waterMarkY = this.register(new Setting<Object>("watermarkposy", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(20), v -> this.waterMark.getValue()));
    public Setting<Integer> waterMark2Y = this.register(new Setting<Object>("welcomerposy", Integer.valueOf(11), Integer.valueOf(0), Integer.valueOf(100), v -> this.waterMark2.getValue()));
    public Setting<Integer> waterMark3Y = this.register(new Setting<Object>("totemstexty", Integer.valueOf(59), Integer.valueOf(0), Integer.valueOf(100), v -> this.waterMark3.getValue()));
    private final Setting<Boolean> arrayList = this.register(new Setting<Boolean>("ArrayList", Boolean.FALSE, "Lists the active modules."));
    private final Setting<Boolean> pvp = this.register(new Setting<Boolean>("info", false));
    private final Setting<Boolean> performance = this.register(new Setting<Boolean>("performanceinfo", false));
    private final Setting<Boolean> coords = this.register(new Setting<Boolean>("coords", Boolean.FALSE, "Your current coordinates"));
    private final Setting<Boolean> direction = this.register(new Setting<Boolean>("direction", Boolean.FALSE, "The Direction you are facing."));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("armor", Boolean.FALSE, "ArmorHUD"));
    private final Setting<Boolean> totems = this.register(new Setting<Boolean>("totems", Boolean.FALSE, "TotemHUD"));
    private final Setting<Boolean> greeter = this.register(new Setting<Boolean>("AlternativeWatermark", Boolean.FALSE, "The time"));
    private final Setting<Boolean> speed = this.register(new Setting<Boolean>("speed", Boolean.FALSE, "Your Speed"));
    private final Setting<Boolean> potions = this.register(new Setting<Boolean>("potions", Boolean.FALSE, "Your Speed"));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("ping", Boolean.FALSE, "Your response time to the server."));
    private final Setting<Boolean> tps = this.register(new Setting<Boolean>("tps", Boolean.FALSE, "Ticks per second of the server."));
    private final Setting<Boolean> fps = this.register(new Setting<Boolean>("fps", Boolean.FALSE, "Your frames per second."));
    private final Setting<Boolean> lag = this.register(new Setting<Boolean>("lagnotifier", Boolean.FALSE, "The time"));
    private final Timer timer = new Timer();
    private final Map<String, Integer> players = new HashMap<String, Integer>();
    public Setting<String> command = this.register(new Setting<String>("command", "Xulu+"));
    public Setting<TextUtil.Color> bracketColor = this.register(new Setting<TextUtil.Color>("bracketcolor", TextUtil.Color.LIGHT_PURPLE));
    public Setting<TextUtil.Color> commandColor = this.register(new Setting<TextUtil.Color>("namecolor", TextUtil.Color.DARK_PURPLE));
    public Setting<Boolean> rainbowPrefix = this.register(new Setting<Boolean>("rainbowprefix", false));
    public Setting<Integer> rainbowSpeed = this.register(new Setting<Object>("prefixspeed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbowPrefix.getValue()));
    public Setting<String> commandBracket = this.register(new Setting<String>("bracket1", "["));
    public Setting<String> commandBracket2 = this.register(new Setting<String>("nracket2", "]"));
    public Setting<Boolean> notifyToggles = this.register(new Setting<Boolean>("chatnotify", Boolean.FALSE, "notifys in chat"));
    public Setting<Boolean> magenDavid = this.register(new Setting<Boolean>("icon", Boolean.FALSE, "nigged"));
    public Setting<Integer> animationHorizontalTime = this.register(new Setting<Object>("animationhtime", Integer.valueOf(500), Integer.valueOf(1), Integer.valueOf(1000), v -> this.arrayList.getValue()));
    public Setting<Integer> animationVerticalTime = this.register(new Setting<Object>("animationvtime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> this.arrayList.getValue()));
    public Setting<RenderingMode> renderingMode = this.register(new Setting<RenderingMode>("ordering", RenderingMode.ABC));
    public Setting<Boolean> time = this.register(new Setting<Boolean>("time", Boolean.FALSE, "The time"));
    public Setting<Integer> lagTime = this.register(new Setting<Integer>("lagtime", 1000, 0, 2000));
    private int color;
    public float hue;
    private boolean shouldIncrement;
    private int hitMarkerTimer;

    public HUD() {
        super("HUD", "nice HUD with many functions", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static HUD getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUD();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        String str1;
        String fpsText;
        ArrayList effects;
        int i;
        String grayString;
        String str;
        int j;
        float f;
        char[] stringToCharArray;
        int[] arrayOfInt;
        String string;
        if (HUD.fullNullCheck()) {
            return;
        }
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.waterMark.getValue().booleanValue()) {
            string = "Xulu+" + (Object)ChatFormatting.LIGHT_PURPLE + "v" + "1.0.6-DEV" + (Object)ChatFormatting.WHITE + " | " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.BOLD + Minecraft.field_71470_ab + (Object)ChatFormatting.WHITE + " Frames | " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.BOLD + OyVey.serverManager.getPing() + (Object)ChatFormatting.WHITE + " Ms";
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMarkY.getValue().intValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = string.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMarkY.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(string, 2.0f, this.waterMarkY.getValue().intValue(), this.color, true);
            }
        }
        if (this.pvp.getValue().booleanValue()) {
            this.renderPvpInfo();
        }
        if (this.performance.getValue().booleanValue()) {
            this.renderPerformanceInfo();
        }
        if (this.waterMark2.getValue().booleanValue()) {
            string = "Xulu+ Getting Cracked With Da Homie " + HUD.mc.field_71439_g.getDisplayNameString();
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMark2Y.getValue().intValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = string.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMark2Y.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(string, 2.0f, this.waterMark2Y.getValue().intValue(), this.color, true);
            }
        }
        if (this.waterMark3.getValue().booleanValue()) {
            string = "" + (Object)ChatFormatting.BOLD + (Object)ChatFormatting.WHITE + HUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum() + (Object)ChatFormatting.RESET + " totems left";
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMark3Y.getValue().intValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = string.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMark3Y.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(string, 2.0f, this.waterMark3Y.getValue().intValue(), this.color, true);
            }
        }
        int[] counter1 = new int[]{1};
        int n = j = HUD.mc.field_71462_r instanceof GuiChat && this.renderingUp.getValue() == false ? 14 : 0;
        if (this.arrayList.getValue().booleanValue()) {
            if (this.renderingUp.getValue().booleanValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                        String str2 = OyVey.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str2, width - 2 - this.renderer.getStringWidth(str2), 2 + j * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        counter1[0] = counter1[0] + 1;
                    }
                } else {
                    for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                        Module module = OyVey.moduleManager.sortedModules.get(k);
                        str = module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + (module.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.LIGHT_PURPLE + module.getDisplayInfo() + (Object)ChatFormatting.DARK_PURPLE + "]" : "");
                        this.renderer.drawString(str, width - 2 - this.renderer.getStringWidth(str), 2 + j * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        counter1[0] = counter1[0] + 1;
                    }
                }
            } else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); ++k) {
                    String str3 = OyVey.moduleManager.sortedModulesABC.get(k);
                    this.renderer.drawString(str3, width - 2 - this.renderer.getStringWidth(str3), height - (j += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); ++k) {
                    Module module = OyVey.moduleManager.sortedModules.get(k);
                    str = module.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + (module.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.WHITE + module.getDisplayInfo() + (Object)ChatFormatting.LIGHT_PURPLE + "]" : "");
                    this.renderer.drawString(str, width - 2 - this.renderer.getStringWidth(str), height - (j += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        String string2 = grayString = this.grayNess.getValue() != false ? String.valueOf((Object)ChatFormatting.GRAY) : "";
        int n2 = HUD.mc.field_71462_r instanceof GuiChat && this.renderingUp.getValue() != false ? 13 : (i = this.renderingUp.getValue() != false ? -2 : 0);
        if (this.renderingUp.getValue().booleanValue()) {
            if (this.potions.getValue().booleanValue()) {
                effects = new ArrayList(Minecraft.func_71410_x().field_71439_g.func_70651_bq());
                for (PotionEffect potionEffect : effects) {
                    String str4 = OyVey.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str4, width - this.renderer.getStringWidth(str4) - 2, height - 2 - (i += 10), potionEffect.func_188419_a().func_76401_j(), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                str = grayString + "Speed " + (Object)ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                str = grayString + "Time " + (Object)ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                str = grayString + "TPS " + (Object)ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            fpsText = grayString + "FPS " + (Object)ChatFormatting.WHITE + Minecraft.field_71470_ab;
            str1 = grayString + "Ping " + (Object)ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, width - this.renderer.getStringWidth(str1) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, width - this.renderer.getStringWidth(str1) - 2, height - 2 - (i += 10), ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (this.potions.getValue().booleanValue()) {
                effects = new ArrayList(Minecraft.func_71410_x().field_71439_g.func_70651_bq());
                for (PotionEffect potionEffect : effects) {
                    String str5 = OyVey.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str5, width - this.renderer.getStringWidth(str5) - 2, 2 + i++ * 10, potionEffect.func_188419_a().func_76401_j(), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                str = grayString + "Speed " + (Object)ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                str = grayString + "Time " + (Object)ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                str = grayString + "TPS " + (Object)ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            fpsText = grayString + "FPS " + (Object)ChatFormatting.WHITE + Minecraft.field_71470_ab;
            str1 = grayString + "Ping " + (Object)ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, width - this.renderer.getStringWidth(str1) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, width - this.renderer.getStringWidth(str1) - 2, 2 + i++ * 10, ClickGui.getInstance().rainbow.getValue().booleanValue() ? (ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        boolean inHell = HUD.mc.field_71441_e.func_180494_b(HUD.mc.field_71439_g.func_180425_c()).func_185359_l().equals("hell");
        int posX = (int)HUD.mc.field_71439_g.field_70165_t;
        int posY = (int)HUD.mc.field_71439_g.field_70163_u;
        int posZ = (int)HUD.mc.field_71439_g.field_70161_v;
        float nether = !inHell ? 0.125f : 8.0f;
        int hposX = (int)(HUD.mc.field_71439_g.field_70165_t * (double)nether);
        int hposZ = (int)(HUD.mc.field_71439_g.field_70161_v * (double)nether);
        i = HUD.mc.field_71462_r instanceof GuiChat ? 14 : 0;
        String coordinates = (Object)ChatFormatting.WHITE + "xyz " + (Object)ChatFormatting.RESET + (inHell ? posX + ", " + posY + ", " + posZ + (Object)ChatFormatting.WHITE + " [" + (Object)ChatFormatting.RESET + hposX + ", " + hposZ + (Object)ChatFormatting.LIGHT_PURPLE + "]" + (Object)ChatFormatting.RESET : posX + ", " + posY + ", " + posZ + (Object)ChatFormatting.LIGHT_PURPLE + " [" + (Object)ChatFormatting.RESET + hposX + ", " + hposZ + (Object)ChatFormatting.LIGHT_PURPLE + "]");
        String direction = this.direction.getValue() != false ? OyVey.rotationManager.getDirection4D(false) : "";
        String coords = this.coords.getValue() != false ? coordinates : "";
        i += 10;
        if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
            String rainbowCoords;
            String string3 = this.coords.getValue() != false ? "xyz " + (inHell ? posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]" : posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : (rainbowCoords = "");
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, height - i - 11, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, height - i, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            } else {
                int[] counter2 = new int[]{1};
                char[] stringToCharArray2 = direction.toCharArray();
                float s = 0.0f;
                for (char c : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c), 2.0f + s, height - i - 11, ColorUtil.rainbow(counter2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += (float)this.renderer.getStringWidth(String.valueOf(c));
                    counter2[0] = counter2[0] + 1;
                }
                int[] counter3 = new int[]{1};
                char[] stringToCharArray22 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (char c : stringToCharArray22) {
                    this.renderer.drawString(String.valueOf(c), 2.0f + u, height - i, ColorUtil.rainbow(counter3[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += (float)this.renderer.getStringWidth(String.valueOf(c));
                    counter3[0] = counter3[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(direction, 2.0f, height - i - 11, this.color, true);
            this.renderer.drawString(coords, 2.0f, height - i, this.color, true);
        }
        if (this.armor.getValue().booleanValue()) {
            this.renderArmorHUD(true);
        }
        if (this.totems.getValue().booleanValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue().booleanValue()) {
            this.renderGreeter();
        }
        if (this.lag.getValue().booleanValue()) {
            this.renderLag();
        }
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        int width = this.renderer.scaledWidth;
        String text = " Client: Xulu+ Build: v1.0.6-DEV Player: ";
        if (this.greeter.getValue().booleanValue()) {
            text = text + HUD.mc.field_71439_g.getDisplayNameString();
        }
        if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            } else {
                int[] counter1 = new int[]{1};
                char[] stringToCharArray = text.toCharArray();
                float i = 0.0f;
                for (char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f + i, 2.0f, ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    i += (float)this.renderer.getStringWidth(String.valueOf(c));
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.color, true);
        }
    }

    public void renderLag() {
        int width = this.renderer.scaledWidth;
        if (OyVey.serverManager.isServerNotResponding()) {
            String text = (Object)ChatFormatting.LIGHT_PURPLE + "server lagging for " + MathUtil.round((float)OyVey.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }

    public void renderTotemHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int totems = HUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += HUD.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            int i = width / 2;
            boolean iteration = false;
            int y = height - 55 - (HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f() ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(totem, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", x + 19 - 2 - this.renderer.getStringWidth(totems + ""), y + 9, 0xFFFFFF);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }

    public void renderArmorHUD(boolean percent) {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        GlStateManager.func_179098_w();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f() ? 10 : 0);
        for (ItemStack is : HUD.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(is, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, is, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
            if (!percent) continue;
            int dmg = 0;
            int itemDurability = is.func_77958_k() - is.func_77952_i();
            float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
            float red = 1.0f - green;
            dmg = percent ? 100 - (int)(red * 100.0f) : itemDurability;
            this.renderer.drawStringWithShadow(dmg + "", x + 8 - this.renderer.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
    }

    public void renderPvpInfo() {
        float f;
        char[] stringToCharArray;
        int[] arrayOfInt;
        String caOn = (Object)ChatFormatting.WHITE + "CA:" + (Object)ChatFormatting.GREEN + " ON";
        String caOff = (Object)ChatFormatting.WHITE + "CA:" + (Object)ChatFormatting.DARK_RED + " OFF";
        String suOn = (Object)ChatFormatting.WHITE + "SR:" + (Object)ChatFormatting.GREEN + " ON";
        String suOff = (Object)ChatFormatting.WHITE + "SR:" + (Object)ChatFormatting.DARK_RED + " OFF";
        String speOn = (Object)ChatFormatting.WHITE + "SP:" + (Object)ChatFormatting.GREEN + " ON";
        String speOff = (Object)ChatFormatting.WHITE + "SP:" + (Object)ChatFormatting.DARK_RED + " OFF";
        String steOn = (Object)ChatFormatting.WHITE + "ST:" + (Object)ChatFormatting.GREEN + " ON";
        String steOff = (Object)ChatFormatting.WHITE + "ST:" + (Object)ChatFormatting.DARK_RED + " OFF";
        String hlOn = (Object)ChatFormatting.WHITE + "HF:" + (Object)ChatFormatting.GREEN + " ON";
        String hlOff = (Object)ChatFormatting.WHITE + "HF:" + (Object)ChatFormatting.DARK_RED + " OFF";
        if (OyVey.moduleManager.getModuleByName("CrystalAura[X+Silent]").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOn, 4.0f, 20.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = caOn.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 20.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(caOn, 4.0f, 20.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("Surround").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(suOn, 4.0f, 30.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = suOn.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 30.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(suOn, 4.0f, 30.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("Speed").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(speOn, 4.0f, 40.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = speOn.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 40.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(speOn, 4.0f, 40.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("Step").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(steOn, 4.0f, 50.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = steOn.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 50.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(steOn, 4.0f, 50.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("HoleFiller").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(hlOn, 4.0f, 60.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = hlOn.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 60.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(hlOn, 4.0f, 60.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("CrystalAura[X+Silent]").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOff, 4.0f, 20.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = caOff.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 20.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(caOff, 4.0f, 20.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("Surround").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(suOff, 4.0f, 30.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = suOff.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 30.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(suOff, 4.0f, 30.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("Speed").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(speOff, 4.0f, 40.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = speOff.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 40.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(speOff, 4.0f, 40.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("Step").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(steOff, 4.0f, 50.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = steOff.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 50.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(steOff, 4.0f, 50.0f, this.color, true);
            }
        }
        if (OyVey.moduleManager.getModuleByName("HoleFiller").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(hlOff, 4.0f, 60.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                } else {
                    arrayOfInt = new int[]{1};
                    stringToCharArray = hlOff.toCharArray();
                    f = 0.0f;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 4.0f + f, 60.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += (float)this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(hlOff, 4.0f, 60.0f, this.color, true);
            }
        }
    }

    public void renderPerformanceInfo() {
        float f;
        char[] stringToCharArray;
        int[] arrayOfInt;
        String fps = (Object)ChatFormatting.WHITE + "FPS: " + (Object)ChatFormatting.RESET + Minecraft.field_71470_ab;
        String ping = (Object)ChatFormatting.WHITE + "Ping: " + (Object)ChatFormatting.RESET + OyVey.serverManager.getPing();
        String tps = (Object)ChatFormatting.WHITE + "TPS: " + (Object)ChatFormatting.RESET + OyVey.serverManager.getTPS();
        if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(fps, 4.0f, 70.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            } else {
                arrayOfInt = new int[]{1};
                stringToCharArray = fps.toCharArray();
                f = 0.0f;
                for (char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), 4.0f + f, 70.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    f += (float)this.renderer.getStringWidth(String.valueOf(c));
                    arrayOfInt[0] = arrayOfInt[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(fps, 4.0f, 70.0f, this.color, true);
        }
        if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(ping, 4.0f, 80.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            } else {
                arrayOfInt = new int[]{1};
                stringToCharArray = ping.toCharArray();
                f = 0.0f;
                for (char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), 4.0f + f, 80.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    f += (float)this.renderer.getStringWidth(String.valueOf(c));
                    arrayOfInt[0] = arrayOfInt[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(ping, 4.0f, 80.0f, this.color, true);
        }
        if (ClickGui.getInstance().rainbow.getValue().booleanValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(tps, 4.0f, 90.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            } else {
                arrayOfInt = new int[]{1};
                stringToCharArray = tps.toCharArray();
                f = 0.0f;
                for (char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), 4.0f + f, 90.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    f += (float)this.renderer.getStringWidth(String.valueOf(c));
                    arrayOfInt[0] = arrayOfInt[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(tps, 4.0f, 90.0f, this.color, true);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    @Override
    public void onLoad() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && this.equals(event.getSetting().getFeature())) {
            OyVey.commandManager.setClientMessage(this.getCommandMessage());
        }
    }

    public String getCommandMessage() {
        if (this.rainbowPrefix.getPlannedValue().booleanValue()) {
            StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
            stringBuilder.insert(0, "\u00a7+");
            stringBuilder.append("\u00a7r");
            return stringBuilder.toString();
        }
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }

    public String getRainbowCommandMessage() {
        StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
        stringBuilder.insert(0, "\u00a7+");
        stringBuilder.append("\u00a7r");
        return stringBuilder.toString();
    }

    public String getRawCommandMessage() {
        return this.commandBracket.getValue() + this.command.getValue() + this.commandBracket2.getValue();
    }

    public void drawTextRadar(int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (Map.Entry<String, Integer> player : this.players.entrySet()) {
                String text = player.getKey() + " ";
                int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, y, this.color, true);
                y += textheight;
            }
        }
    }

    static {
        INSTANCE = new HUD();
    }

    public static enum RenderingMode {
        Length,
        ABC;

    }
}

