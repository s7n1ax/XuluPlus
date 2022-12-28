/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.handshake.client.C00Handshake
 */
package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={C00Handshake.class})
public interface IC00HandshakePhob {
    @Accessor(value="ip")
    public String getIp();

    @Accessor(value="ip")
    public void setIp(String var1);
}

