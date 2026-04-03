package me.decce.kerria;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.mojang.blaze3d.systems.RenderSystem;

import java.time.Duration;

public class ImageCache {
    private final Cache<Long, CachedNativeImage> cache;
    private int cacheSize;

    public ImageCache() {
        cacheSize = Kerria.config.minCacheSize;
        this.cache = Caffeine.newBuilder()
                .executor(Runnable::run)
                .expireAfterAccess(Duration.ofSeconds(60))
                .maximumSize(cacheSize)
                .removalListener(this::onCacheRemoval)
                .build();
    }

    private void onCacheRemoval(Long info, CachedNativeImage image, RemovalCause cause) {
        if (cause == RemovalCause.SIZE && cacheSize < Kerria.config.maxCacheSize) {
            var newSize = Math.min((int) (cacheSize * 1.4), Kerria.config.maxCacheSize);
            cache.policy().eviction().ifPresent(eviction -> {
                Kerria.LOGGER.info("Increased image cache size from {} to {}", cacheSize, newSize);
                cacheSize = newSize;
                eviction.setMaximum(newSize);
            });
        }

        if (RenderSystem.isOnRenderThread() && image != null) {
            image.delete();
        }
    }

    public void resize() {
        int newSize = this.cacheSize;
        if (newSize > Kerria.config.maxCacheSize) {
            newSize = Kerria.config.maxCacheSize;
        }
        if (newSize < Kerria.config.minCacheSize) {
            newSize = Kerria.config.minCacheSize;
        }
        int finalNewSize = newSize;
        if (this.cacheSize != newSize) {
            cache.policy().eviction().ifPresent(eviction -> {
                Kerria.LOGGER.info("Resized image cache from {} to {}", cacheSize, finalNewSize);
                eviction.setMaximum(finalNewSize);
                this.cacheSize = finalNewSize;
            });
        }
    }

    public CachedNativeImage tryGet(long pixels) {
        var cached = cache.getIfPresent(pixels);
        if (cached == null) {
            return null;
        }
        return cached;
    }

    public void put(long info, CachedNativeImage cached) {
        cache.put(info, cached);
    }
}
