package com.spadium.kassette.ui.toasts

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.ToastManager
import net.minecraft.text.Text

class WarningToast : Toast {
    private var visibility = Toast.Visibility.SHOW
    private val message: Text

    constructor(
        message: Text
    ) {
        this.message = message
    }

    override fun getVisibility(): Toast.Visibility? {
        return visibility
    }

    override fun update(manager: ToastManager?, time: Long) {
        if (time > 7500) {
            visibility = Toast.Visibility.HIDE
        }
    }

    override fun draw(
        context: DrawContext?,
        textRenderer: TextRenderer?,
        startTime: Long
    ) {
        context?.fill(
            0, 0, width, height, 0xFFDD9900.toInt()
        )

        context?.drawText(
            textRenderer!!, Text.literal("Warning"),
            4, 4, 0xFFFFFFFF.toInt(), false
        )
        context?.drawWrappedText(
            textRenderer!!, message,
            4, (4 + (textRenderer.fontHeight * 1.5)).toInt(), (width - 16),
            0xFFFFFFFF.toInt(), false
        )

        context?.drawBorder(
            0, 0, width, height, 0xFFBBBBBB.toInt()
        )
    }
}