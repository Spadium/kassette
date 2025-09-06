package com.spadium.kassette.mixin;

import com.spadium.kassette.media.CoverUpdater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "TAIL"))
    private static void updateCoversOnRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        CoverUpdater.setupCoverArt();
    }
}
