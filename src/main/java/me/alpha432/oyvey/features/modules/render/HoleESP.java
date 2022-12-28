/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.ColorSetting;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.HoleUtil;
import me.alpha432.oyvey.util.OyColor;
import me.alpha432.oyvey.util.OyPair;
import me.alpha432.oyvey.util.PlayerUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleESP
extends Module {
    private static HoleESP INSTANCE;
    public Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 1, 20));
    public Setting<Boolean> hideOwn = this.register(new Setting<Boolean>("Hide Own", false));
    public Setting<RenderMode> mode = this.register(new Setting<RenderMode>("Render", RenderMode.Pretty));
    public ColorSetting bedrockColor = new ColorSetting("Bedrock Color", new OyColor(0, 255, 0, 100));
    public ColorSetting bedrockColor2 = new ColorSetting("Bedrock Outline", new OyColor(0, 255, 0, 100));
    public ColorSetting obsidianColor = new ColorSetting("Obsidian Color", new OyColor(255, 0, 0, 100));
    public ColorSetting obsidianColor2 = new ColorSetting("Obsidian Outline", new OyColor(255, 0, 0, 100));
    public Setting<Double> lineWidth = this.register(new Setting<Double>("Line Width", Double.valueOf(1.0), Double.valueOf(-1.0), Double.valueOf(2.0), s -> this.mode.equals((Object)RenderMode.Outline)));
    public Setting<Double> Height = this.register(new Setting<Double>("Height", Double.valueOf(1.0), Double.valueOf(-1.0), Double.valueOf(2.0), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<Boolean> invertFill = this.register(new Setting<Boolean>("Invert Fill", Boolean.valueOf(false), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<Boolean> invertLine = this.register(new Setting<Boolean>("Invert Line", Boolean.valueOf(false), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<RmodeEnum> RMode = this.register(new Setting<RmodeEnum>("Color Mode", RmodeEnum.Rainbow));
    public Setting<SinModeEnum> SinMode = this.register(new Setting<SinModeEnum>("Sine Mode", SinModeEnum.Special, s -> this.RMode.equals((Object)RmodeEnum.Sin)));
    public Setting<Integer> RDelay = this.register(new Setting<Integer>("Rainbow Delay", 500, 0, 2500));
    public Setting<Integer> FillUp = this.register(new Setting<Integer>("Fill Up", Integer.valueOf(80), Integer.valueOf(0), Integer.valueOf(255), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<Integer> FillDown = this.register(new Setting<Integer>("Fill Down", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<Integer> LineFillUp = this.register(new Setting<Integer>("Liner Fill Up", Integer.valueOf(80), Integer.valueOf(0), Integer.valueOf(255), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<Integer> LineFillDown = this.register(new Setting<Integer>("Liner Fill Down", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), s -> this.mode.equals((Object)RenderMode.Fade)));
    public Setting<CustomHolesEnum> customHoles = this.register(new Setting<CustomHolesEnum>("Show", CustomHolesEnum.Single));
    private final ConcurrentHashMap<BlockPos, OyPair<OyColor, Boolean>> holes = new ConcurrentHashMap();

    private HoleESP() {
        super("HoleESP", "Shows safe spots.", Module.Category.RENDER, false, false, false);
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.holes.isEmpty()) {
            return;
        }
        this.holes.forEach((arg_0, arg_1) -> this.renderHoles(arg_0, arg_1));
    }

    @Override
    public void onUpdate() {
        if (HoleESP.nullCheck()) {
            return;
        }
        this.holes.clear();
        int range = this.range.getValue();
        HashSet<BlockPos> possibleHoles = new HashSet<BlockPos>();
        List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), range, range, false, true, 0);
        for (BlockPos pos : blockPosList) {
            if (!EntityUtil.posEqualsBlock(pos, Blocks.field_150350_a).booleanValue() || EntityUtil.posEqualsBlock(pos.func_177982_a(0, -1, 0), Blocks.field_150350_a).booleanValue() || !EntityUtil.posEqualsBlock(pos.func_177982_a(0, 1, 0), Blocks.field_150350_a).booleanValue() || !EntityUtil.posEqualsBlock(pos.func_177982_a(0, 2, 0), Blocks.field_150350_a).booleanValue()) continue;
            possibleHoles.add(pos);
        }
        for (BlockPos pos : possibleHoles) {
            OyPair<OyColor, Boolean> p;
            boolean safe;
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType == HoleUtil.HoleType.NONE) continue;
            HoleUtil.BlockSafety holeSafety = holeInfo.getSafety();
            AxisAlignedBB centreBlocks = holeInfo.getCentre();
            if (centreBlocks == null) continue;
            OyColor colour = holeSafety == HoleUtil.BlockSafety.UNBREAKABLE ? this.bedrockColor.getValue() : this.obsidianColor.getValue();
            boolean bl = safe = holeSafety == HoleUtil.BlockSafety.UNBREAKABLE;
            if (this.customHoles.equals((Object)CustomHolesEnum.Custom) && (holeType == HoleUtil.HoleType.CUSTOM || holeType == HoleUtil.HoleType.DOUBLE)) {
                p = new OyPair<OyColor, Boolean>(colour, safe);
                this.holes.put(pos, p);
                continue;
            }
            if (this.customHoles.equals((Object)CustomHolesEnum.Double) && holeType == HoleUtil.HoleType.DOUBLE) {
                p = new OyPair<OyColor, Boolean>(colour, safe);
                this.holes.put(pos, p);
                continue;
            }
            if (holeType != HoleUtil.HoleType.SINGLE) continue;
            p = new OyPair<OyColor, Boolean>(colour, safe);
            this.holes.put(pos, p);
        }
    }

    private void renderHoles(BlockPos hole, OyPair<OyColor, Boolean> pair) {
        boolean safe = pair.getValue();
        if (this.hideOwn.getValue().booleanValue() && hole.equals((Object)PlayerUtil.getPlayerPos())) {
            return;
        }
        if (!this.mode.equals((Object)RenderMode.Gradient) && !this.mode.equals((Object)RenderMode.Fade)) {
            boolean outline = false;
            boolean solid = false;
            switch (this.mode.getValue()) {
                case Pretty: {
                    outline = true;
                    solid = true;
                    break;
                }
                case Solid: {
                    outline = false;
                    solid = true;
                    break;
                }
                case Outline: {
                    outline = true;
                    solid = false;
                }
            }
            RenderUtil.drawBoxESP(hole, (Color)(safe ? this.bedrockColor.getValue() : this.obsidianColor.getValue()), (Color)(safe ? this.bedrockColor2.getValue() : this.obsidianColor2.getValue()), this.lineWidth.getValue(), outline, solid, Float.valueOf((float)(this.Height.getValue() - 1.0)));
        } else if (this.mode.equals((Object)RenderMode.Gradient)) {
            RenderUtil.drawGlowBox(hole, this.Height.getValue() - 1.0, Float.valueOf(this.lineWidth.getValue().floatValue()), safe ? this.bedrockColor.getValue() : this.obsidianColor.getValue(), safe ? this.bedrockColor2.getValue() : this.obsidianColor2.getValue());
        } else {
            RenderUtil.drawOpenGradientBox(hole, this.invertFill.getValue() == false ? this.getGColor(safe, false, false) : this.getGColor(safe, true, false), this.invertFill.getValue() == false ? this.getGColor(safe, true, false) : this.getGColor(safe, false, false), 0.0);
            RenderUtil.drawGradientBlockOutline(hole, this.invertLine.getValue() != false ? this.getGColor(safe, false, true) : this.getGColor(safe, true, true), this.invertLine.getValue() != false ? this.getGColor(safe, true, true) : this.getGColor(safe, false, true), 2.0f, 0.0);
        }
    }

    private Color getGColor(boolean safe, boolean top, boolean line) {
        ColorUtil.type type2 = null;
        switch (this.SinMode.getValue()) {
            case Special: {
                type2 = ColorUtil.type.SPECIAL;
                break;
            }
            case Saturation: {
                type2 = ColorUtil.type.SATURATION;
                break;
            }
            case Brightness: {
                type2 = ColorUtil.type.BRIGHTNESS;
            }
        }
        Color rVal = !safe ? (this.obsidianColor.getRainbow() ? (this.RMode.equals((Object)RmodeEnum.Rainbow) ? (top ? ColorUtil.releasedDynamicRainbow(0, line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue()) : ColorUtil.releasedDynamicRainbow(this.RDelay.getValue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue())) : (this.SinMode.equals((Object)SinModeEnum.Hue) ? (top ? ColorUtil.getSinState(this.obsidianColor.getColor(), this.obsidianColor2.getColor(), 1000.0, line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue()) : ColorUtil.getSinState(this.obsidianColor.getColor(), this.obsidianColor2.getColor(), (double)this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue())) : (top ? ColorUtil.getSinState(this.obsidianColor.getColor(), 1000.0, line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue(), type2) : ColorUtil.getSinState(this.obsidianColor.getColor(), this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue(), type2)))) : (top ? new OyColor(this.obsidianColor.getColor().getRed(), this.obsidianColor.getColor().getGreen(), this.obsidianColor.getColor().getBlue(), line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue()) : new OyColor(this.obsidianColor.getColor().getRed(), this.obsidianColor.getColor().getGreen(), this.obsidianColor.getColor().getBlue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue()))) : (this.bedrockColor.getRainbow() ? (this.RMode.equals((Object)RmodeEnum.Rainbow) ? (top ? ColorUtil.releasedDynamicRainbow(0, line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue()) : ColorUtil.releasedDynamicRainbow(this.RDelay.getValue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue())) : (this.SinMode.equals((Object)SinModeEnum.Hue) ? (top ? ColorUtil.getSinState(this.bedrockColor.getColor(), this.bedrockColor2.getColor(), 1000.0, line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue()) : ColorUtil.getSinState(this.bedrockColor.getColor(), this.bedrockColor2.getColor(), (double)this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue())) : (top ? ColorUtil.getSinState(this.bedrockColor.getColor(), 1000.0, line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue(), type2) : ColorUtil.getSinState(this.bedrockColor.getColor(), this.RDelay.getValue().intValue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue(), type2)))) : (top ? new OyColor(this.bedrockColor.getColor().getRed(), this.bedrockColor.getColor().getGreen(), this.bedrockColor.getColor().getBlue(), line ? this.LineFillUp.getValue().intValue() : this.FillUp.getValue().intValue()) : new OyColor(this.bedrockColor.getColor().getRed(), this.bedrockColor.getColor().getGreen(), this.bedrockColor.getColor().getBlue(), line ? this.LineFillDown.getValue().intValue() : this.FillDown.getValue().intValue())));
        return rVal;
    }

    @Override
    public String getDisplayInfo() {
        return "" + this.holes.size();
    }

    public static enum RmodeEnum {
        Rainbow,
        Sin;

    }

    public static enum SinModeEnum {
        Special,
        Hue,
        Saturation,
        Brightness;

    }

    public static enum CustomHolesEnum {
        Single,
        Double,
        Custom;

    }

    public static enum RenderMode {
        Pretty,
        Solid,
        Outline,
        Gradient,
        Fade;

    }
}

