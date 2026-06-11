package me.decce.kerria;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import static org.lwjgl.opengl.GL45C.*;

public class CachedNativeImage {
    private final int glId;

    public CachedNativeImage(long pixels, int width, int height) {
        GlStateManager._pixelStore(GL_UNPACK_ROW_LENGTH, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_PIXELS, 0);
        GlStateManager._pixelStore(GL_UNPACK_SKIP_ROWS, 0);
        glId = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(glId, 1, GL_RGBA8, width, height);
        glTextureSubImage2D(glId, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public void delete() {
        Kerria.runOnRenderThread(() -> {
            glDeleteTextures(glId);
        });
    }

    public void use(int srcX, int srcY, int destX, int destY, int width, int height, int level) {
        var dest = GlStateTracker.getCurrentlyBoundTexture();
        glCopyImageSubData(
                glId, GL_TEXTURE_2D, 0, srcX, srcY, 0,
                dest, GL_TEXTURE_2D, level, destX, destY, 0,
                width, height, 1);
    }
}
