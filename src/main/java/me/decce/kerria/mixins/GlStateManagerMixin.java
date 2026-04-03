package me.decce.kerria.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.blaze3d.platform.GlStateManager;
import me.decce.kerria.GlStateTracker;
import org.lwjgl.opengl.GL11C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GlStateManager.class, remap = false)
public class GlStateManagerMixin {
    @Inject(method = "_activeTexture", at = @At("RETURN"))
    private static void kerria$_activeTexture(int unit, CallbackInfo ci) {
        GlStateTracker.activeTexture(unit);
    }

    @Inject(method = "_bindTexture", at = @At("RETURN"))
    private static void kerria$_bindTexture(int texture, CallbackInfo ci) {
        GlStateTracker.bindTexture(texture);
    }

    @Inject(method = "_texImage2D", at = @At("HEAD"))
    private static void kerria$_texImage2D(CallbackInfo ci, @Local(argsOnly = true, ordinal = 2) LocalIntRef internalFormat) {
        // glCopyImageSubData demands strictly identical internal formats.
        // Using non-sized internal format seems to be legacy and there should be no practical difference between GL_RGBA and GL_RGBA8.
        if (internalFormat.get() == GL11C.GL_RGBA) {
            internalFormat.set(GL11C.GL_RGBA8);
        }
    }
}
