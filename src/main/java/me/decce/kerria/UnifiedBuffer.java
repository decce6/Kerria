package me.decce.kerria;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL45C.*;

public class UnifiedBuffer {
    public static final int MAPPING_FLAGS = GL_MAP_WRITE_BIT | GL_MAP_READ_BIT | GL_MAP_PERSISTENT_BIT | GL_MAP_COHERENT_BIT;
    public static final int STORAGE_FLAGS = GL_DYNAMIC_STORAGE_BIT | GL_MAP_WRITE_BIT | GL_MAP_READ_BIT | GL_MAP_PERSISTENT_BIT | GL_MAP_COHERENT_BIT;

    private int size;
    private long position;
    private int id;
    private ByteBuffer buffer;
    private long address;
    private long fence;
    private boolean shouldInsertFence;
    
    public UnifiedBuffer() {
        id = glCreateBuffers();
        this.size = Kerria.config.bufferSize;
        glNamedBufferStorage(id, size, STORAGE_FLAGS);
        buffer = glMapNamedBufferRange(id, 0, size, MAPPING_FLAGS);
        if (buffer == null) {
            throw new RuntimeException("Failed to map persistent buffer");
        }
        this.address = MemoryUtil.memAddress(buffer);
        this.shouldInsertFence = true;
    }

    public void delete() {
        if (fence != 0) {
            glDeleteSync(fence);
            fence = 0;
        }
        if (id != 0) {
            glDeleteBuffers(id);
            id = 0;
            buffer = null;
        }
    }

    private void waitSync() {
        if (fence != 0) {
            glClientWaitSync(fence, GL_SYNC_FLUSH_COMMANDS_BIT, Long.MAX_VALUE);
            glDeleteSync(fence);
            fence = 0;
        }
    }
    
    public boolean upload(long address, long size, int dest) {
        if (size > this.size) {
            return false;
        }
        if (position + size > this.size) {
            kerria$rotate();
        }
        MemoryUtil.memCopy(address, this.address + position, size);
        // glFlushMappedNamedBufferRange(id, position, size);
        glCopyNamedBufferSubData(id, dest, position, 0, size);
        position += size;
        if (shouldInsertFence) {
            fence = glFenceSync(GL_SYNC_GPU_COMMANDS_COMPLETE, 0);
            shouldInsertFence = false;
        }
        return true;
    }

    private void kerria$rotate() {
        waitSync();
        position = 0; //TODO fence
        shouldInsertFence = true;
    }
}
