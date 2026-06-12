package me.decce.kerria.mixins;

import me.decce.kerria.Kerria;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? >=1.19.3 {
@Mixin(targets = "net.minecraft.client.renderer.texture.SpriteContents$InterpolationData")
//? } else {
/*@Mixin(targets = "net.minecraft.client.renderer.texture.TextureAtlasSprite$InterpolationData")
*///? }
public class InterpolationDataMixin {
    @Inject(method = "uploadInterpolatedFrame", at = @At("HEAD"))
    private void kerria$upload$head(CallbackInfo ci) {
        Kerria.beginFastUpload();
    }

    @Inject(method = "uploadInterpolatedFrame", at = @At("RETURN"))
    private void kerria$upload$return(CallbackInfo ci) {
        Kerria.endFastUpload();
    }
}
