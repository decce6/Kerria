package me.decce.kerria;

import com.mojang.blaze3d.platform.NativeImage;
import me.decce.kerria.mixins.NativeImageAccessor;

import static org.lwjgl.opengl.GL45C.*;

public class CachedNativeImage {
    private final int glId;
    private final int width;
    private final int height;
    private boolean deleted;

    public CachedNativeImage(NativeImage image, int width, int height) {
        glId = glCreateTextures(GL_TEXTURE_2D);
        var accessor = (NativeImageAccessor)(Object) image;
        var pixels = accessor.getPixels();
        this.width = width;
        this.height = height;
        glTextureStorage2D(glId, 1, GL_RGBA8, width, height);
        glTextureSubImage2D(glId, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public void delete() {
        if (!this.deleted) {
            glDeleteTextures(glId);
            deleted = true;
        }
    }

    public boolean use(int x, int y, int width, int height, int level) {
        if (this.width != width || this.height != height || this.deleted) {
            return false;
        }
        var dest = GlStateTracker.getCurrentlyBoundTexture();
        glCopyImageSubData(
                glId, GL_TEXTURE_2D, 0,0, 0, 0,
                dest, GL_TEXTURE_2D, level, x, y, 0,
                width, height, 1);
        return true;
    }
}
