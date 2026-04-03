package me.decce.kerria.platform.fabric;

//? fabric {

import me.decce.kerria.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements Platform {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}
}
//?}
