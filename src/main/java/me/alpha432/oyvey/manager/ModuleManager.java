/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.input.Keyboard
 */
package me.alpha432.oyvey.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import me.alpha432.oyvey.features.modules.Flex.AdditionalInfo;
import me.alpha432.oyvey.features.modules.Flex.Anchor;
import me.alpha432.oyvey.features.modules.Flex.Animations;
import me.alpha432.oyvey.features.modules.Flex.AntiAFK;
import me.alpha432.oyvey.features.modules.Flex.AntiSwing;
import me.alpha432.oyvey.features.modules.Flex.BlockTweaks;
import me.alpha432.oyvey.features.modules.Flex.Bypass;
import me.alpha432.oyvey.features.modules.Flex.HitMarkers;
import me.alpha432.oyvey.features.modules.Flex.NoHitBoxFix;
import me.alpha432.oyvey.features.modules.Flex.NoLag;
import me.alpha432.oyvey.features.modules.Flex.Offhandimprove;
import me.alpha432.oyvey.features.modules.Flex.SelfShoot;
import me.alpha432.oyvey.features.modules.Flex.Trailing;
import me.alpha432.oyvey.features.modules.Flex.nametags;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.client.Colors;
import me.alpha432.oyvey.features.modules.client.Csgo;
import me.alpha432.oyvey.features.modules.client.Display;
import me.alpha432.oyvey.features.modules.client.FontMod;
import me.alpha432.oyvey.features.modules.client.GUIBlur;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.features.modules.client.NameChanger;
import me.alpha432.oyvey.features.modules.client.Overlay;
import me.alpha432.oyvey.features.modules.client.Timestamps;
import me.alpha432.oyvey.features.modules.combat.AntiTrap;
import me.alpha432.oyvey.features.modules.combat.AutoArmorPhob;
import me.alpha432.oyvey.features.modules.combat.AutoCity;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.modules.combat.AutoTrap;
import me.alpha432.oyvey.features.modules.combat.AutoWeb;
import me.alpha432.oyvey.features.modules.combat.BedBombPhob;
import me.alpha432.oyvey.features.modules.combat.Criticals;
import me.alpha432.oyvey.features.modules.combat.GodModule;
import me.alpha432.oyvey.features.modules.combat.HoleFiller;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.features.modules.combat.Offhand;
import me.alpha432.oyvey.features.modules.combat.SecondAutoCrystal;
import me.alpha432.oyvey.features.modules.combat.SelfFill;
import me.alpha432.oyvey.features.modules.combat.Selftrap;
import me.alpha432.oyvey.features.modules.combat.SilentXP;
import me.alpha432.oyvey.features.modules.combat.Surround;
import me.alpha432.oyvey.features.modules.misc.AutoElytra;
import me.alpha432.oyvey.features.modules.misc.AutoGG;
import me.alpha432.oyvey.features.modules.misc.Bow32k;
import me.alpha432.oyvey.features.modules.misc.BuildHeight;
import me.alpha432.oyvey.features.modules.misc.ChatModifier;
import me.alpha432.oyvey.features.modules.misc.DonkeyNotifier;
import me.alpha432.oyvey.features.modules.misc.ExtraTab;
import me.alpha432.oyvey.features.modules.misc.FakeKick;
import me.alpha432.oyvey.features.modules.misc.MCF;
import me.alpha432.oyvey.features.modules.misc.MobOwner;
import me.alpha432.oyvey.features.modules.misc.NoHandShake;
import me.alpha432.oyvey.features.modules.misc.NoHitBox;
import me.alpha432.oyvey.features.modules.misc.PearlNotify;
import me.alpha432.oyvey.features.modules.misc.Swing;
import me.alpha432.oyvey.features.modules.misc.ToolTips;
import me.alpha432.oyvey.features.modules.misc.Tracker;
import me.alpha432.oyvey.features.modules.movement.ElytraFlightPhob;
import me.alpha432.oyvey.features.modules.movement.Flight;
import me.alpha432.oyvey.features.modules.movement.IceSpeed;
import me.alpha432.oyvey.features.modules.movement.LongJump;
import me.alpha432.oyvey.features.modules.movement.NoSlowDown;
import me.alpha432.oyvey.features.modules.movement.NoVoid;
import me.alpha432.oyvey.features.modules.movement.PacketFly;
import me.alpha432.oyvey.features.modules.movement.Phase;
import me.alpha432.oyvey.features.modules.movement.ReverseStep;
import me.alpha432.oyvey.features.modules.movement.Scaffold;
import me.alpha432.oyvey.features.modules.movement.Speed;
import me.alpha432.oyvey.features.modules.movement.SprintFuture;
import me.alpha432.oyvey.features.modules.movement.Step;
import me.alpha432.oyvey.features.modules.movement.StepTwo;
import me.alpha432.oyvey.features.modules.movement.StrafePhob;
import me.alpha432.oyvey.features.modules.movement.TickShift;
import me.alpha432.oyvey.features.modules.movement.Velocity;
import me.alpha432.oyvey.features.modules.movement.YPort;
import me.alpha432.oyvey.features.modules.player.FakePlayer;
import me.alpha432.oyvey.features.modules.player.FastPlace;
import me.alpha432.oyvey.features.modules.player.Freecam;
import me.alpha432.oyvey.features.modules.player.LiquidInteract;
import me.alpha432.oyvey.features.modules.player.MCP;
import me.alpha432.oyvey.features.modules.player.MultiTask;
import me.alpha432.oyvey.features.modules.player.Reach;
import me.alpha432.oyvey.features.modules.player.Replenish;
import me.alpha432.oyvey.features.modules.player.Speedmine;
import me.alpha432.oyvey.features.modules.player.TpsSync;
import me.alpha432.oyvey.features.modules.render.ArrowESP;
import me.alpha432.oyvey.features.modules.render.Aspect;
import me.alpha432.oyvey.features.modules.render.BlockHighlight;
import me.alpha432.oyvey.features.modules.render.BreakingESP;
import me.alpha432.oyvey.features.modules.render.BurrowESP;
import me.alpha432.oyvey.features.modules.render.Chams;
import me.alpha432.oyvey.features.modules.render.ChorusLanding;
import me.alpha432.oyvey.features.modules.render.ChorusPredict;
import me.alpha432.oyvey.features.modules.render.CombatLogger;
import me.alpha432.oyvey.features.modules.render.ESP;
import me.alpha432.oyvey.features.modules.render.GlintModify;
import me.alpha432.oyvey.features.modules.render.HandChams;
import me.alpha432.oyvey.features.modules.render.HoleESP;
import me.alpha432.oyvey.features.modules.render.ItemChams;
import me.alpha432.oyvey.features.modules.render.NoRender;
import me.alpha432.oyvey.features.modules.render.OneChunk;
import me.alpha432.oyvey.features.modules.render.PopChams;
import me.alpha432.oyvey.features.modules.render.PortalViewerYippie;
import me.alpha432.oyvey.features.modules.render.ShaderChams;
import me.alpha432.oyvey.features.modules.render.ShadersXuluOwo;
import me.alpha432.oyvey.features.modules.render.Skeleton;
import me.alpha432.oyvey.features.modules.render.SkyColor;
import me.alpha432.oyvey.features.modules.render.SmallShield;
import me.alpha432.oyvey.features.modules.render.StorageESP;
import me.alpha432.oyvey.features.modules.render.Trajectories;
import me.alpha432.oyvey.features.modules.render.ViewModel;
import me.alpha432.oyvey.features.modules.render.Wireframe;
import me.alpha432.oyvey.util.Util;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager
extends Feature {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        this.modules.add(new AdditionalInfo());
        this.modules.add(new SelfShoot());
        this.modules.add(new HitMarkers());
        this.modules.add(new NoHitBoxFix());
        this.modules.add(new Anchor());
        this.modules.add(new Animations());
        this.modules.add(new AntiAFK());
        this.modules.add(new AntiSwing());
        this.modules.add(new BlockTweaks());
        this.modules.add(new Bypass());
        this.modules.add(new nametags());
        this.modules.add(new NoLag());
        this.modules.add(new Offhandimprove());
        this.modules.add(new Trailing());
        this.modules.add(new ClickGui());
        this.modules.add(new Csgo());
        this.modules.add(new Display());
        this.modules.add(new FontMod());
        this.modules.add(new GUIBlur());
        this.modules.add(new HUD());
        this.modules.add(new NameChanger());
        this.modules.add(new Overlay());
        this.modules.add(new Colors());
        this.modules.add(new Timestamps());
        this.modules.add(new AutoArmorPhob());
        this.modules.add(new AutoCrystal());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoWeb());
        this.modules.add(new Criticals());
        this.modules.add(new GodModule());
        this.modules.add(new HoleFiller());
        this.modules.add(new Killaura());
        this.modules.add(new Offhand());
        this.modules.add(new SecondAutoCrystal());
        this.modules.add(new SelfFill());
        this.modules.add(new Selftrap());
        this.modules.add(new SilentXP());
        this.modules.add(new Surround());
        this.modules.add(new AntiTrap());
        this.modules.add(new AutoCity());
        this.modules.add(new BedBombPhob());
        this.modules.add(new AutoGG());
        this.modules.add(new BuildHeight());
        this.modules.add(new ChatModifier());
        this.modules.add(new DonkeyNotifier());
        this.modules.add(new ExtraTab());
        this.modules.add(new FakeKick());
        this.modules.add(new MCF());
        this.modules.add(new NoHandShake());
        this.modules.add(new NoHitBox());
        this.modules.add(new PearlNotify());
        this.modules.add(new Swing());
        this.modules.add(new ToolTips());
        this.modules.add(new Tracker());
        this.modules.add(new Bow32k());
        this.modules.add(new MobOwner());
        this.modules.add(new AutoElytra());
        this.modules.add(new Flight());
        this.modules.add(new NoVoid());
        this.modules.add(new PacketFly());
        this.modules.add(new ReverseStep());
        this.modules.add(new Scaffold());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new StepTwo());
        this.modules.add(new YPort());
        this.modules.add(new IceSpeed());
        this.modules.add(new Velocity());
        this.modules.add(new Phase());
        this.modules.add(new LongJump());
        this.modules.add(new StrafePhob());
        this.modules.add(new ElytraFlightPhob());
        this.modules.add(new NoSlowDown());
        this.modules.add(new TickShift());
        this.modules.add(new SprintFuture());
        this.modules.add(new FakePlayer());
        this.modules.add(new FastPlace());
        this.modules.add(new Freecam());
        this.modules.add(new LiquidInteract());
        this.modules.add(new MCP());
        this.modules.add(new MultiTask());
        this.modules.add(new Reach());
        this.modules.add(new Replenish());
        this.modules.add(new Speedmine());
        this.modules.add(new TpsSync());
        this.modules.add(new ArrowESP());
        this.modules.add(new Aspect());
        this.modules.add(new BlockHighlight());
        this.modules.add(new BreakingESP());
        this.modules.add(new BurrowESP());
        this.modules.add(new Chams());
        this.modules.add(new ChorusPredict());
        this.modules.add(new ESP());
        this.modules.add(new HandChams());
        this.modules.add(new NoRender());
        this.modules.add(new PopChams());
        this.modules.add(new Skeleton());
        this.modules.add(new SkyColor());
        this.modules.add(new SmallShield());
        this.modules.add(new Trajectories());
        this.modules.add(new ViewModel());
        this.modules.add(new Wireframe());
        this.modules.add(new ChorusLanding());
        this.modules.add(new GlintModify());
        this.modules.add(new ShadersXuluOwo());
        this.modules.add(new PortalViewerYippie());
        this.modules.add(new CombatLogger());
        this.modules.add(new StorageESP());
        this.modules.add(HoleESP.getInstance());
        this.modules.add(OneChunk.getInstance());
        this.modules.add(ShaderChams.getInstance());
        this.modules.add(ItemChams.getInstance());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<String> getModules() {
        ArrayList<String> allModules = new ArrayList<String>();
        for (Module module : this.modules) {
            allModules.add(module.getName());
        }
        return allModules;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add((Module)module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus)MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void onRenderHand(RenderHandEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRenderHand(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(((EventBus)MinecraftForge.EVENT_BUS)::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.field_71462_r instanceof OyVeyGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
    extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + (Object)ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.WHITE + module.getDisplayInfo() + (Object)ChatFormatting.GRAY + "]" : "");
                    module.offset = (float)ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float)ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.field_71441_e == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float)ModuleManager.this.renderer.getStringWidth(text) && Util.mc.field_71441_e != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = OyVey.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + (Object)ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.WHITE + module.getDisplayInfo() + (Object)ChatFormatting.GRAY + "]" : "");
                    module.offset = (float)ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float)ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.field_71441_e == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float)ModuleManager.this.renderer.getStringWidth(text) && Util.mc.field_71441_e != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}

