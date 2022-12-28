/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.world.World
 *  net.minecraft.world.chunk.Chunk
 */
package me.alpha432.oyvey.mixin.mixins;

import com.google.common.base.Predicate;
import java.util.List;
import me.alpha432.oyvey.features.modules.misc.Tracker;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={World.class})
public class MixinWorld {
    @Redirect(method={"getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"))
    public <T extends Entity> void getEntitiesOfTypeWithinAABBHook(Chunk chunk, Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> filter) {
        try {
            chunk.func_177430_a(entityClass, aabb, listToFill, filter);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Inject(method={"onEntityAdded"}, at={@At(value="HEAD")})
    private void onEntityAdded(Entity entityIn, CallbackInfo ci) {
        if (Tracker.getInstance().isOn()) {
            Tracker.getInstance().onSpawnEntity(entityIn);
        }
    }
}

