package me.decce.kerria;

import org.lwjgl.opengl.GL;

public class GlCapacityChecker {
    public final boolean supported;

    public GlCapacityChecker() {
        var capacity = GL.getCapabilities();
        supported = capacity.OpenGL45;
    }
}
