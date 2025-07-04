package com.spadium.kassette.mixin;

import com.spadium.kassette.util.ImageUtils;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Mixin(NativeImage.class)
public class NativeImageMixin {
    @Redirect(
            method = "read(Lnet/minecraft/client/texture/NativeImage$Format;Ljava/nio/ByteBuffer;)Lnet/minecraft/client/texture/NativeImage;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/PngMetadata;validate(Ljava/nio/ByteBuffer;)V")
    )
    private static void validateRedirector(ByteBuffer buf) {
        ByteOrder order = buf.order();
        ImageUtils.validateImage(buf,order);

    }
}
