package me.decce.kerria;

import me.decce.kerria.platform.Platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import me.decce.kerria.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.decce.kerria.platform.neoforge.NeoforgePlatform;
*///?} forge {
/*import me.decce.kerria.platform.forge.ForgePlatform;
*///? }

public class Kerria {
	public static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_ID);

	public static KerriaConfig config;

	private static ImageCache cache;
	private static int fastUpload;
	private static int cached;
	private static GlCapacityChecker capacity;
	private static final Platform PLATFORM = createPlatformInstance();

	private static UnifiedBuffer buffer;

	public static void init() {
		KerriaConfig.reload();
		config.save();
	}

	public static ImageCache cache() {
		if (cache == null) {
			cache = new ImageCache();
		}
		return cache;

	}

	public static UnifiedBuffer buffer() {
		if (buffer == null) {
			buffer = new UnifiedBuffer();
		}
		return buffer;
	}

	public static void beginFastUpload() {
		fastUpload++;
	}

	public static void endFastUpload() {
		fastUpload--;
	}

	public static void beginCache() {
		cached++;
	}

	public static void endCache() {
		cached--;
	}

	public static boolean shouldUseFastUpload() {
		return fastUpload > 0;
	}

	public static boolean shouldUseCache() {
		return cached > 0;
	}

	public static void recreateBuffer() {
		if (buffer == null) {
			return;
		}
		buffer.delete();
		buffer = new UnifiedBuffer();
	}

	public static void recreateCache() {
		if (cache != null) {
			cache.resize();
		}
	}

	public static Platform platform() {
		return PLATFORM;
	}

	// Must only be called from the render thread - fails if no GL context is present on the current thread
	public static boolean isEnabled() {
		if (capacity == null) {
			capacity = new GlCapacityChecker();
		}
		return config.enabled;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		*///?}
	}
}
