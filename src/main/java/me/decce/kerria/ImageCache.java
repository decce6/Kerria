package me.decce.kerria;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ImageCache {
    private final Long2ObjectMap<CachedNativeImage> cache = new Long2ObjectOpenHashMap<>();
    private final ConcurrentLinkedQueue<Long> removalQueue = new ConcurrentLinkedQueue<>();

    public CachedNativeImage tryGet(long pixels) {
        Long toRemove;
        while ((toRemove = removalQueue.poll()) != null) {
            long toRemoveValue = toRemove.longValue();
            var cached = cache.getOrDefault(toRemoveValue, null);
            if (cached != null) {
                cached.delete();
            }
            cache.remove(toRemoveValue);
        }
        return cache.getOrDefault(pixels, null);
    }

    public void put(long pixels, CachedNativeImage cached) {
        cache.put(pixels, cached);
    }

    public void remove(long pixels, long width, long height) {
        if (pixels != 0L) {
            long max = pixels + width * height * 4L;
            for (long realPixels = pixels; realPixels < max; realPixels += 4L) {
                removalQueue.add(realPixels);
            }
        }
    }
}
