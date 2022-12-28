/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.lwjgl.util.glu.Project
 */
package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class}, priority=1001)
public class MixinEntityRenderer {
    @Shadow
    public float field_78530_s;
    @Shadow
    public double field_78503_V;
    @Shadow
    public double field_78502_W;
    @Shadow
    public double field_78509_X;
    @Shadow
    public int field_175084_ae;
    @Shadow
    public int field_175079_V;
    @Shadow
    public int field_78529_t;
    @Shadow
    public boolean field_175078_W;
    Minecraft mc = Minecraft.func_71410_x();

    @Shadow
    public void func_78467_g(float partialTicks) {
    }

    @Shadow
    public void func_78482_e(float partialTicks) {
    }

    @Shadow
    public void func_78475_f(float partialTicks) {
    }

    @Shadow
    public void func_180436_i() {
    }

    @Shadow
    public void func_175072_h() {
    }

    @Shadow
    public void func_78466_h(float partialTicks) {
    }

    @Shadow
    public void func_78468_a(int startCoords, float partialTicks) {
    }

    protected MixinEntityRenderer(RenderManager renderManager) {
    }

    @Inject(method={"updateLightmap"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffect(float ticks, CallbackInfo info) {
        if (NoRender.getInstance().hurtcam.getValue().booleanValue() && NoRender.getInstance().isOn()) {
            info.cancel();
        }
    }

    @Redirect(method={"setupCameraTransform"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onSetupCameraTransform(float fovy, float aspect, float zNear, float zFar) {
        PerspectiveEvent event = new PerspectiveEvent((float)this.mc.field_71443_c / (float)this.mc.field_71440_d);
        MinecraftForge.EVENT_BUS.post((Event)event);
        Project.gluPerspective((float)fovy, (float)event.getAspect(), (float)zNear, (float)zFar);
    }

    @Redirect(method={"renderWorldPass"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderWorldPass(float fovy, float aspect, float zNear, float zFar) {
        PerspectiveEvent event = new PerspectiveEvent((float)this.mc.field_71443_c / (float)this.mc.field_71440_d);
        MinecraftForge.EVENT_BUS.post((Event)event);
        Project.gluPerspective((float)fovy, (float)event.getAspect(), (float)zNear, (float)zFar);
    }

    @Redirect(method={"renderCloudsCheck"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderCloudsCheck(float fovy, float aspect, float zNear, float zFar) {
        PerspectiveEvent event = new PerspectiveEvent((float)this.mc.field_71443_c / (float)this.mc.field_71440_d);
        MinecraftForge.EVENT_BUS.post((Event)event);
        Project.gluPerspective((float)fovy, (float)event.getAspect(), (float)zNear, (float)zFar);
    }
}

