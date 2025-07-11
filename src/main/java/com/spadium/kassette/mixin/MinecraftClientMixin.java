package com.spadium.kassette.mixin;

import com.spadium.kassette.config.Config;
import com.spadium.kassette.ui.screens.onboarding.DisclaimerPage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "createInitScreens", at = @At(value = "RETURN"))
    private static void addOnboardingToInit(List<Function<Runnable, Screen>> list, CallbackInfoReturnable<Boolean> cir) {
        if (Config.Companion.getInstance().getFirstRun()) {
            list.add(
                    onClose -> (new DisclaimerPage())
            );
        }
    }
}
