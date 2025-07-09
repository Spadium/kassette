package com.spadium.kassette.ui

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

class MarqueeTextManager {
    private var marqueeCounter = 0
    private var fancyOffset2 = 0
    private var spacingBetween: Int = 3

    var text: String = ""
    var textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer
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
            context.enableScissor(
                x, y,
                x + textRenderer.getWidth(" ".repeat(maxLength)),
                y + 8
            )
            context.drawText(textRenderer, scrolledText, x, y, color, shadow)
            context.disableScissor()
        }
    }
}