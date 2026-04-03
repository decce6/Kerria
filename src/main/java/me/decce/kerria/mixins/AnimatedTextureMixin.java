package me.decce.kerria.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.decce.kerria.Kerria;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.renderer.texture.SpriteContents$AnimatedTexture")
public class AnimatedTextureMixin {
    @WrapMethod(method = "uploadFrame")
    private void kerria$upload(int i, int j, int k, Operation<Void> original) {
        Kerria.beginCache();
        Kerria.beginFastUpload();
        original.call(i, j, k);
        Kerria.endFastUpload();
        Kerria.endCache();
    }
}
