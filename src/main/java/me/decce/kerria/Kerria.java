package me.decce.kerria;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import me.decce.kerria.platform.Platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//? fabric {
import me.decce.kerria.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.decce.kerria.platform.neoforge.NeoforgePlatform;
*///?} forge {
/*import me.decce.kerria.platform.forge.ForgePlatform;
*///? }

public class Kerria {
	public static final Logger LOGGER = LogManager.getLogger();

	static KerriaConfig config;

	private static int fastUpload;
	private static int cached;
	private static GlCapacityChecker capacity;
	private static final Platform PLATFORM = createPlatformInstance();

	private static UnifiedBuffer buffer;

	public static void init() {
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
		return fastUpload > 0 && getCapacity().supportsFastUpload && getConfig().fastUpload;
	}

	public static boolean shouldUseCache() {
		return cached > 0 && getCapacity().supportsTextureCache && getConfig().cache;
	}

	public static void recreateBuffer() {
		if (buffer == null) {
			return;
		}
		buffer.delete();
		buffer = new UnifiedBuffer();
	}

	public static void runOnRenderThread(RenderCall renderCall) {
		if (RenderSystem.isOnRenderThread()) {
			renderCall.execute();
		}
		else {
			RenderSystem.recordRenderCall(renderCall);
		}
	}

	public static Platform platform() {
		return PLATFORM;
	}

	public static boolean isEnabled() {
		return getConfig().enabled;
	}

	public static GlCapacityChecker getCapacity() {
		if (capacity == null) {
			capacity = new GlCapacityChecker();
		}
		return capacity;
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

	public static KerriaConfig getConfig() {
		if (config == null) {
			KerriaConfig.reload();
			config.save();
		}
		return config;
	}
}
