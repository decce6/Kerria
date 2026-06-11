package me.decce.kerria.platform.forge;

//? forge {
/*import me.decce.kerria.Constants;
import me.decce.kerria.Kerria;
import net.minecraftforge.fml.common.Mod;

//? if embeddium {
/^import net.minecraftforge.fml.ModList;
import me.decce.kerria.compat.embeddium.EmbeddiumCompat;
^///? }

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        Kerria.init();

        //? embeddium {
        /^if (ModList.get().isLoaded("embeddium")) {
            org.embeddedt.embeddium.api.OptionGUIConstructionEvent.BUS.addListener(EmbeddiumCompat::onSodiumPagesRegister);
        }
        ^///? }
    }
}
*///? }
