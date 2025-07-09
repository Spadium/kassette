package com.spadium.kassette.ui

import com.spadium.kassette.config.Config
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
    This class contains logic related to marquee text rendering, we use this to fix a bug
    that occurred in older versions where both lines would reset their positions if only one
    line needed to reset its own position
 */
class MarqueeTextManager {
    private var marqueeCounter = 0
    private var fancyOffset = 0
    private var spacingBetween: Int = 3
    private var textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer

    var text: String = ""
    var context: DrawContext

    constructor(context: DrawContext) {
        this.context = context
    }

    fun renderText(
        x: Int,
        y: Int,
        color: Int,
        shadow: Boolean,
        maxLength: Int,
        spacingBetween: Int,
        shouldScroll: Boolean
    ) {
        if (Config.Instance.hud.fancyText) {
            renderFancyText(
                x, y, color, shadow, maxLength, spacingBetween, shouldScroll
            )
        } else {
            renderSimpleText(
                x, y, color, shadow, maxLength, spacingBetween, shouldScroll
            )
        }
    }

    private fun renderSimpleText(
        x: Int,
        y: Int,
        color: Int,
        shadow: Boolean,
        maxLength: Int,
        spacingBetween: Int,
        shouldScroll: Boolean
    ) {
        var spacing: String = ""
        for (i in 0..spacingBetween) {
            spacing += " "
        }

        val textToScroll: String = "$text$spacing$text$spacing$text"

        if (text.length <= maxLength) {
            // Don't bother scrolling when the text can fit within the maximum length before scrolling
            context.drawText(
                textRenderer, text,
                x, y, color, shadow
            )
        } else {
            if (shouldScroll) {
                marqueeCounter = if (marqueeCounter >= spacingBetween + text.length) 0 else marqueeCounter + 1
            }

            val startIndex = marqueeCounter
            val endIndex = marqueeCounter + maxLength

            val scrolledText = textToScroll.substring(startIndex, endIndex)
            context.drawText(textRenderer, scrolledText, x, y, color, shadow)
        }
    }

    private fun renderFancyText(
        x: Int,
        y: Int,
        color: Int,
        shadow: Boolean,
        maxLength: Int,
        spacingBetween: Int,
        shouldScroll: Boolean
    ) {
        var spacing: String = ""
        for (i in 0..spacingBetween) {
            spacing += " "
        }

        val textToScroll: String = "$text$spacing$text$spacing"

        if (text.length <= maxLength) {
            // Don't bother scrolling when the text can fit within the maximum length before scrolling
            context.drawText(
                textRenderer, text,
                x, y, color, shadow
            )
        } else {
            if (shouldScroll) {
                fancyOffset = if (fancyOffset >= textRenderer.getWidth("$text$spacing")) 0 else fancyOffset + 1
            }
            // very janky but i dont care
            context.enableScissor(
                x, y,
                x + textRenderer.getWidth(" ".repeat(maxLength)),
                y + 8
            )
            context.drawText(textRenderer, textToScroll, x - fancyOffset, y, color, shadow)
            context.disableScissor()
        }
    }
}