package com.spadium.kassette.ui.toasts

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.ToastManager
import net.minecraft.text.Text
import net.minecraft.util.Colors

class ErrorToast: Toast {
    private var visibility = Toast.Visibility.SHOW
    private val message: String

    constructor(
        message: String
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
            0, 0, width, 64, 0xFF882222.toInt()
        )

        context?.drawText(
            textRenderer!!, Text.literal("Error loading Kassette!"),
            8, 8, 0xFFFFFFFF.toInt(), false
        )
        context?.drawWrappedText(
            textRenderer!!, Text.literal(message),
            8, 8 + (textRenderer.fontHeight * 2), (width - 16),
            0xFFFFFFFF.toInt(), false
        )

        context?.drawText(
            textRenderer!!, Text.literal("Check settings for more info"),
            8, (64 - textRenderer.fontHeight) - 8, 0xFFFFFFFF.toInt(), false
        )

        context?.drawBorder(
            0, 0, width, 64, 0xFFBBBBBB.toInt()
        )
    }
}