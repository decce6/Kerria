package me.decce.kerria.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import me.decce.kerria.CachedNativeImage;
import me.decce.kerria.Kerria;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL45C.*;

@Mixin(NativeImage.class)
public class NativeImageMixin {
    @Shadow
    @Final
    private long size;
    @Shadow
    @Final
    private NativeImage.Format format;
    @Shadow
    private long pixels;
    @Shadow
    @Final
    private int width;
    @Shadow
    @Final
    private int height;
    @Unique
    private int kerria$pbo;
    @Unique
    private CachedNativeImage kerria$cache;

    @Unique
    private int kerria$getPbo() {
        if (kerria$pbo == 0) {
            kerria$pbo = glCreateBuffers();
            glNamedBufferData(kerria$pbo, this.size, GL_DYNAMIC_COPY);
        }
        return kerria$pbo;
    }

    @WrapWithCondition(method = "_upload", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texSubImage2D(IIIIIIIIJ)V"))
    private boolean kerria$upload(int target, int level, int xOffset, int yOffset, int width, int height, int format, int type, long pixels,
                                  @Local(argsOnly = true, ordinal = 3) int unpackSkipPixels,
                                  @Local(argsOnly = true, ordinal = 4) int unpackSkipRows,
                                  @Local(argsOnly = true, ordinal = 0) boolean blur,
                                  @Local(argsOnly = true, ordinal = 1) boolean clamp,
                                  @Local(argsOnly = true, ordinal = 3) boolean autoClose) {
        if (!RenderSystem.isOnRenderThread() || !Kerria.isEnabled() || this.pixels == 0L) {
            return true;
        }

        long realPixels = pixels + 4L * unpackSkipPixels + 4L * unpackSkipRows * width;
        long currentSize = 4L * width * height;

        if (Kerria.shouldUseCache() && (!autoClose && !clamp && !blur && this.format == NativeImage.Format.RGBA)) {
            if (kerria$cache == null) {
                kerria$cache = new CachedNativeImage(this.pixels, this.width, this.height);
            }
            kerria$cache.use(unpackSkipPixels, unpackSkipRows, xOffset, yOffset, width, height, level);
            return false;
        }
        else if (Kerria.shouldUseFastUpload()) {
            var pbo = kerria$getPbo();
            if (Kerria.buffer().upload(realPixels, currentSize, pbo)) {
                GlStateManager._pixelStore(GL_UNPACK_SKIP_PIXELS, 0);
                GlStateManager._pixelStore(GL_UNPACK_SKIP_ROWS, 0);
                glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pbo);
                glTexSubImage2D(target, level, xOffset, yOffset, width, height, format, type, 0);
                glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);
                return false;
            }
            else {
                return true;
            }
        }
        return true;
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void kerria$close(CallbackInfo ci) {
        if (kerria$cache != null) {
            kerria$cache.delete();
            kerria$cache = null;
        }
    }
}
