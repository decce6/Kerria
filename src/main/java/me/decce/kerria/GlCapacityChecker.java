package me.decce.kerria;

import org.lwjgl.opengl.GL;

public class GlCapacityChecker {
    public final boolean opengl45;
    public final boolean copyImage;
    public final boolean bufferStorage;
    public final boolean textureStorage;
    public final boolean directStateAccess;

    public final boolean supportsFastUpload;
    public final boolean supportsTextureCache;

    public GlCapacityChecker() {
        var capacity = GL.getCapabilities();
        opengl45 = capacity.OpenGL45;
        copyImage = capacity.GL_ARB_copy_image;
        bufferStorage = capacity.GL_ARB_buffer_storage;
        //? >=1.20.1 {
        textureStorage = capacity.GL_ARB_texture_storage || capacity.GL_EXT_texture_storage;
        //? } else {
        /*textureStorage = capacity.GL_ARB_texture_storage;
        *///? }
        directStateAccess = capacity.GL_ARB_direct_state_access || capacity.GL_EXT_direct_state_access;

        supportsFastUpload = opengl45 || (directStateAccess && bufferStorage);
        supportsTextureCache = opengl45 || (directStateAccess && copyImage && textureStorage);
        if (!supportsFastUpload) {
            Kerria.LOGGER.info("Fast Texture Upload is not supported (OpenGL45={}, DirectStateAccess={}, BufferStorage={})", opengl45, directStateAccess, bufferStorage);
        }
        if (!supportsTextureCache) {
            Kerria.LOGGER.info("Animated Texture Cache is not supported (OpenGL45={}, DirectStateAccess={}, CopyImage={}, TextureStorage={})", opengl45, directStateAccess, copyImage, textureStorage);
        }
    }
}
