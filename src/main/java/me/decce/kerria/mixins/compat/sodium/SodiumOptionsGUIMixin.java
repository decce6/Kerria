package me.decce.kerria.mixins.compat.sodium;

import org.spongepowered.asm.mixin.Mixin;

//? if sodium {
import me.decce.kerria.compat.sodium.LegacySodiumPage;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(value = SodiumOptionsGUI.class, remap = false)
public class SodiumOptionsGUIMixin {
    @Shadow
    @Final
    private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void kerria$init(Screen prevScreen, CallbackInfo ci) {
        this.pages.add(new LegacySodiumPage());
    }
}
//? } else {
/*@Mixin(targets = "net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI")
public class SodiumOptionsGUIMixin {}
*///? }
