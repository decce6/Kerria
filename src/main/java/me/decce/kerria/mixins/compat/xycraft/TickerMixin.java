package me.decce.kerria.mixins.compat.xycraft;

import org.spongepowered.asm.mixin.Mixin;

//? xycraft {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.decce.kerria.Kerria;
import org.spongepowered.asm.mixin.Pseudo;
import tv.soaryn.xycraft.core.client.render.texture.CloudFX;

@Pseudo
@Mixin(value = CloudFX.Ticker.class, remap = false)
public class TickerMixin {
    @WrapMethod(method = "tickAndUpload")
    private void kerria$tickAndUpload(int jim, int bob, Operation<Void> original) {
        Kerria.beginFastUpload();
        original.call(jim, bob);
        Kerria.endFastUpload();
    }
}
*///? }else {
@Mixin(targets = {})
public class TickerMixin {}
//? }
