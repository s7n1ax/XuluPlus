/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.layers.LayerRenderer
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.alpha432.oyvey.event.events;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderEntityLayerEvent
extends Event {
    public EntityLivingBase entity;
    public LayerRenderer<?> layer;

    public RenderEntityLayerEvent(EntityLivingBase entity, LayerRenderer<?> layer) {
        this.entity = entity;
        this.layer = layer;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public void setEntity(EntityLivingBase entityLivingBase) {
        this.entity = entityLivingBase;
    }

    public LayerRenderer<?> getLayer() {
        return this.layer;
    }

    public void setLayer(LayerRenderer<?> layerRenderer) {
        this.layer = layerRenderer;
    }

    public EntityLivingBase component1() {
        return this.entity;
    }

    public LayerRenderer<?> component2() {
        return this.layer;
    }

    public RenderEntityLayerEvent copy(EntityLivingBase entity, LayerRenderer<?> layer) {
        return new RenderEntityLayerEvent(entity, layer);
    }

    public static RenderEntityLayerEvent copy$default(RenderEntityLayerEvent renderEntityLayerEvent, EntityLivingBase entityLivingBase, LayerRenderer layerRenderer, int n, Object object) {
        if ((n & 1) != 0) {
            entityLivingBase = renderEntityLayerEvent.entity;
        }
        if ((n & 2) != 0) {
            layerRenderer = renderEntityLayerEvent.layer;
        }
        return renderEntityLayerEvent.copy(entityLivingBase, layerRenderer);
    }

    public String toString() {
        return "RenderEntityLayerEvent(entity=" + (Object)this.entity + ", layer=" + this.layer + ')';
    }

    public int hashCode() {
        int result2 = this.entity.hashCode();
        result2 = result2 * 31 + this.layer.hashCode();
        return result2;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RenderEntityLayerEvent)) {
            return false;
        }
        RenderEntityLayerEvent renderEntityLayerEvent = (RenderEntityLayerEvent)((Object)other);
        return this.entity == renderEntityLayerEvent.entity && this.layer == renderEntityLayerEvent.layer;
    }
}

