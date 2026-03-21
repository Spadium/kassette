package me.spadium.kassette.ui.toasts

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.components.toasts.ToastManager

class AuthenticationToast: Toast {
    override fun getWantedVisibility(): Toast.Visibility {
        return Toast.Visibility.SHOW
    }

    override fun update(manager: ToastManager, time: Long) {
    }

    override fun render(
        context: GuiGraphics,
        textRenderer: Font,
        startTime: Long
    ) {
        context?.fill(0, 0, 128, 32, 0xFFFFFFFF.toInt())
    }
}