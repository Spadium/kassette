package me.spadium.kassette.mixin;

import me.spadium.kassette.media.CoverUpdater;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "TAIL"))
    private static void updateCoversOnRender(DeltaTracker tickCounter, boolean tick, CallbackInfo ci) {
        CoverUpdater.setupCoverArt();
    }
}
