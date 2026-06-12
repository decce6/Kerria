package me.decce.kerria.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.decce.kerria.Kerria;
import org.spongepowered.asm.mixin.Mixin;

//? >=1.19.3 {
@Mixin(targets = "net.minecraft.client.renderer.texture.SpriteContents$AnimatedTexture")
//? } else >=1.17 {
/*@Mixin(targets = "net.minecraft.client.renderer.texture.TextureAtlasSprite$AnimatedTexture")
*///? } else {
/*@Mixin(net.minecraft.client.renderer.texture.TextureAtlasSprite.class)
*///? }
public class AnimatedTextureMixin {
    //? >=1.19.3 {
    @WrapMethod(method = "uploadFrame")
    private void kerria$upload(int i, int j, int k, Operation<Void> original) {
        Kerria.beginCache();
        Kerria.beginFastUpload();
        original.call(i, j, k);
        Kerria.endFastUpload();
        Kerria.endCache();
    }
    //? } else {
    
    /*//? >=1.17 {
    @WrapMethod(method = "uploadFrame(I)V")
    private void kerria$upload(int k, Operation<Void> original) {
    //? } else {
    /^@WrapMethod(method = "upload(I)V")
    private void kerria$upload(int k, Operation<Void> original) {
    ^///? }
        Kerria.beginCache();
        Kerria.beginFastUpload();
        original.call(k);
        Kerria.endFastUpload();
        Kerria.endCache();
    }
    *///? }
}
