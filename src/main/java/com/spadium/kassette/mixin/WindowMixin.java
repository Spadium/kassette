package com.spadium.kassette.mixin;

import com.spadium.kassette.Kassette;
import com.mojang.blaze3d.platform.WindowEventHandler;
import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// for debugging purposes, was required once upon a time, but not anymore (very early in development)
@Mixin(value = Window.class, remap = false)
public class WindowMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J",
                    shift = At.Shift.BY, by = -3
            )
    )
    private static void enableDebugContext(WindowEventHandler eventHandler, ScreenManager screenManager, DisplayData settings, String fullscreenVideoMode, String title, CallbackInfo ci) {
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
    }
}
