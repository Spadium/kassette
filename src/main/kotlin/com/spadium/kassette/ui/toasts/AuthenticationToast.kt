package com.spadium.kassette.ui.toasts

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.ToastManager

class AuthenticationToast: Toast {
    override fun getVisibility(): Toast.Visibility? {
        return Toast.Visibility.SHOW
    }

    override fun update(manager: ToastManager?, time: Long) {
    }

    override fun draw(
        context: DrawContext?,
        textRenderer: TextRenderer?,
        startTime: Long
    ) {
        context?.fill(0, 0, 128, 32, 0xFFFFFFFF.toInt())
    }
}