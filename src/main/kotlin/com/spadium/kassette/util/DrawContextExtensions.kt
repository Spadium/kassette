package com.spadium.kassette.util

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext

private var marqueeCounter = 0
private var fancyOffset2 = 0

fun DrawContext.drawMarquee(
    textRenderer: TextRenderer,
    text: String,
    x: Int,
    y: Int,
    color: Int,
    shadow: Boolean,
    maxLength: Int,
    spacingBetween: Int,
    shouldScroll: Boolean
) {

}

// fancy marquee, needs more improvements but is good for general usage
fun DrawContext.drawMarqueeFancy(
    textRenderer: TextRenderer,
    text: String,
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
        this.drawText(
            textRenderer, text,
            x, y, color, shadow
        )
    } else {
        if (shouldScroll) {
            fancyOffset2 = if (fancyOffset2 >= textRenderer.getWidth("$text$spacing")) 0 else fancyOffset2 + 1
        }
        // very janky but i dont care
        enableScissor(
            x, y,
            x + textRenderer.getWidth(" ".repeat(maxLength)),
            y + 8
        )
        drawText(textRenderer, textToScroll, x - fancyOffset2, y, color, shadow)
        disableScissor()
    }
}

fun DrawContext.drawShortened(
    textRenderer: TextRenderer,
    text: String,
    x: Int,
    y: Int,
    color: Int,
    shadow: Boolean,
    maxLength: Int,
) {
    if (text.length <= maxLength) {
        // Don't bother scrolling when the text can fit within the maximum length before scrolling
        this.drawText(
            textRenderer, text,
            x, y, color, shadow
        )
    } else {
        val textAfterShortening = "${text.substring(maxLength - 3)}..."
        this.drawText(textRenderer, textAfterShortening, x, y, color, shadow)
    }
}
