package me.decce.kerria.mixins;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = NativeImage.class, remap = false)
public interface NativeImageAccessor {
    @Accessor
    long getPixels();
}
