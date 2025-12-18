package com.spadium.kassette.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.spadium.kassette.ui.screens.config.ConfigScreen;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public class TemporaryConfigPathMixin extends Screen {
    protected TemporaryConfigPathMixin(Component title) {
        super(title);
    }

    @Shadow
    private Button openScreenButton(Component text, Supplier<Screen> screenSupplier) {
        throw new AssertionError();
    }

    @Inject(
        method = "init",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addToContents(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"
        )
    )
    private void createKassetteConfigButton(CallbackInfo ci, @Local LocalRef<Button> button, @Local GridLayout.RowHelper rowHelper) {
        button.set(
                rowHelper.addChild(openScreenButton(Component.literal("Kassette Config"), () -> new ConfigScreen((OptionsScreen)(Object)this) ))
        );
    }
}
