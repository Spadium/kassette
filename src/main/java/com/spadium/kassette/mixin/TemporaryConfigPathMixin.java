package com.spadium.kassette.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.spadium.kassette.ui.screens.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.Supplier;

@Debug(export = true)
@Mixin(OptionsScreen.class)
public abstract class TemporaryConfigPathMixin extends Screen {
    protected TemporaryConfigPathMixin(Component title) {
        super(title);
    }

    @Shadow
    private Button openScreenButton(Component text, Supplier<Screen> screenSupplier) {
        throw new AssertionError();
    }

    @ModifyArgs(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    ordinal = 8,
                    target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"
            )
    )
    private void modifyTelemetryButtonCreationArgs(Args args) {
        args.set(0, openScreenButton(Component.literal("Kassette Config"), () -> new ConfigScreen((OptionsScreen)(Object)this)));;
    }

    @Redirect(
            method = "init()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;allowsTelemetry()Z")
    )
    private boolean overrideTelemetryCheck(Minecraft client) {
        return true;
    }
}
