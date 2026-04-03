package me.decce.kerria.platform.fabric;

//? fabric {

import me.decce.kerria.Kerria;
import net.fabricmc.api.ClientModInitializer;

public class FabricEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Kerria.init();
	}

}
//?}
