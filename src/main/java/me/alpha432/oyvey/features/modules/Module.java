/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.client.event.RenderHandEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package me.alpha432.oyvey.features.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.features.setting.Bind;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Module
extends Feature {
    private static final Logger LOGGER = LogManager.getLogger(Module.class);
    private final String description;
    private final Category category;
    public Setting<Boolean> enabled = this.register(new Setting<Boolean>("ON", false));
    public Setting<Boolean> drawn = this.register(new Setting<Boolean>("drawn", true));
    public Setting<Bind> bind = this.register(new Setting<Bind>("keybind", new Bind(-1)));
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;

    public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name);
        this.displayName = this.register(new Setting<String>("displayname", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onLoad() {
        LOGGER.info(this.getDisplayName() + " loaded");
    }

    public void onUnload() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onRenderHand(RenderHandEvent event) {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(Boolean.TRUE);
        this.onToggle();
        this.onEnable();
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + (Object)ChatFormatting.BOLD + " " + this.getDisplayName() + (Object)ChatFormatting.RESET + (Object)ChatFormatting.LIGHT_PURPLE + " ON.");
            Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
        }
        LOGGER.info(this.getDisplayName() + " enabled");
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
    }

    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
        this.enabled.setValue(false);
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + (Object)ChatFormatting.BOLD + " " + this.getDisplayName() + (Object)ChatFormatting.RESET + (Object)ChatFormatting.RED + " OFF.");
            Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
        }
        LOGGER.info(this.getDisplayName() + " disabled");
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = OyVey.moduleManager.getModuleByDisplayName(name);
        Module originalModule = OyVey.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage((Object)ChatFormatting.RED + "a module of this name already exists.");
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean listening() {
        return this.hasListener && this.isOn() || this.alwaysListening;
    }

    public String getFullArrayString() {
        return this.getDisplayName() + (Object)ChatFormatting.LIGHT_PURPLE + (this.getDisplayInfo() != null ? " [" + (Object)ChatFormatting.DARK_PURPLE + this.getDisplayInfo() + (Object)ChatFormatting.LIGHT_PURPLE + "]" : "");
    }

    public static enum Category {
        COMBAT("[Combat]"),
        MISC("[Misc]"),
        RENDER("[Render]"),
        MOVEMENT("[Movement]"),
        PLAYER("[Player]"),
        CLIENT("[Eclient]"),
        Flex("[Flex]");

        private final String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

