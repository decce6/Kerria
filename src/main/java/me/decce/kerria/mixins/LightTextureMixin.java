package me.decce.kerria.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import me.decce.kerria.Kerria;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @WrapOperation(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;upload()V"))
    private void kerria$upload(DynamicTexture instance, Operation<Void> original) {
        if (!RenderSystem.isOnRenderThread() || !Kerria.getConfig().fastLightTextureUpload) {
            original.call(instance);
        }
        else {
            Kerria.beginFastUpload();
            original.call(instance);
            Kerria.endFastUpload();
        }
    }
}
