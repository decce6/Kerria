package me.decce.kerria;

import org.lwjgl.opengl.GL13C;

public class GlStateTracker {
    private static int activeTexture;
    private static final int[] textures = new int[12];

    public static void activeTexture(int i) {
        activeTexture = i - GL13C.GL_TEXTURE0;
    }

    public static void bindTexture(int i) {
        textures[activeTexture] = i;
    }

    public static int getCurrentlyBoundTexture() {
        return textures[activeTexture];
    }
}
