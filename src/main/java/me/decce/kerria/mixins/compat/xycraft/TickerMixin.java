package me.decce.kerria.mixins.compat.xycraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

//? xycraft {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.decce.kerria.Kerria;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tv.soaryn.xycraft.core.client.render.texture.CloudFX;

@Pseudo
//? 1.21.1 {
@Mixin(value = CloudFX.Ticker.class, remap = false)
//? } else {
/^@Mixin(targets = "tv.soaryn.xycraft.core.client.render.texture.CloudFX$1", remap = false)
^///? }
public class TickerMixin {
    //? >=1.21.1{
    @WrapMethod(method = "tickAndUpload")
    private void kerria$tickAndUpload(int x, int y, Operation<Void> original) {
        Kerria.beginFastUpload();
        original.call(x, y);
        Kerria.endFastUpload();
    }
    //? } else {
    /^@WrapMethod(method = "m_245385_")
    private void kerria$tickAndUpload(Operation<Void> original) {
        Kerria.beginFastUpload();
        original.call();
        Kerria.endFastUpload();
    }
    ^///? }
}
*///? }else {
@Pseudo
@Mixin(targets = "")
public class TickerMixin {}
//? }
