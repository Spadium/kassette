package com.spadium.kassette.ui.toasts

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.components.toasts.ToastManager
import net.minecraft.network.chat.Component

class WarningToast : Toast {
    private var visibility = Toast.Visibility.SHOW
    private val message: Component
    private val details: Component

    constructor(
        message: Component,
        details: Component = Component.empty()
    ) {
        this.message = message
        this.details = details
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
        if (details.string.isNullOrBlank()) {
            return 96
        }
        return 96
    }

    override fun render(
        context: GuiGraphics,
        textRenderer: Font,
        startTime: Long
    ) {
        context?.fill(
            0, 0, width(), height(), 0xFFDD9900.toInt()
        )

        context?.drawString(
            textRenderer!!, Component.literal("Warning"),
            4, 4, 0xFFFFFFFF.toInt(), false
        )
        context?.drawWordWrap(
            textRenderer!!, message,
            4, (4 + (textRenderer.lineHeight * 1.5)).toInt(), (width() - 16),
            0xFFFFFFFF.toInt(), false
        )
        context?.drawWordWrap(
            textRenderer!!, message,
            4, (4 + (textRenderer.lineHeight * 1.5)).toInt(), (width() - 16),
            0xFFFFFFFF.toInt(), false
        )

        context?.renderOutline(
            0, 0, width(), height(), 0xFFBBBBBB.toInt()
        )
    }
}