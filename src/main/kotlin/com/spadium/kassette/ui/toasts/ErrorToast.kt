package com.spadium.kassette.ui.toasts

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.components.toasts.ToastManager
import net.minecraft.client.gui.render.GuiRenderer
import net.minecraft.network.chat.Component

class ErrorToast: Toast {
    private var visibility = Toast.Visibility.SHOW
    private val message: String

    constructor(
        message: String
    ) {
        this.message = message
    }

    override fun getWantedVisibility(): Toast.Visibility {
        return visibility
    }

    override fun update(manager: ToastManager, time: Long) {
        if (time > 7500) {
            visibility = Toast.Visibility.HIDE
        }
    }

    override fun height(): Int {
        return 96
    }

    override fun render(
        context: GuiGraphics,
        textRenderer: Font,
        startTime: Long
    ) {
        context.fill(
            0, 0, this.width(), this.height(), 0xFF882222.toInt()
        )

        context.drawString(
            textRenderer, Component.literal("Error loading Kassette!"),
            8, 8, 0xFFFFFFFF.toInt(), false
        )
        context.drawWordWrap(
            textRenderer, Component.literal(message),
            8, 8 + (textRenderer.lineHeight * 2), (width() - 16),
            0xFFFFFFFF.toInt(), false
        )

        context.drawString(
            textRenderer, Component.literal("Check settings for more info"),
            8, (height() - textRenderer.lineHeight) - 8, 0xFFFFFFFF.toInt(), false
        )

        context.submitOutline(
            0, 0, width(), height(), 0xFFBBBBBB.toInt()
        )
    }
}