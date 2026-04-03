package me.decce.kerria.platform.neoforge;

//? neoforge {

/*import me.decce.kerria.Constants;
import me.decce.kerria.Kerria;
import me.decce.kerria.compat.embeddium.EmbeddiumCompat;
import net.neoforged.fml.ModList;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class NeoforgeEntrypoint {
	public NeoforgeEntrypoint() {
		Kerria.init();

		//? embeddium {
        if (ModList.get().isLoaded("embeddium")) {
            org.embeddedt.embeddium.api.OptionGUIConstructionEvent.BUS.addListener(EmbeddiumCompat::onSodiumPagesRegister);
        }
        //? }
	}
}
*///?}
