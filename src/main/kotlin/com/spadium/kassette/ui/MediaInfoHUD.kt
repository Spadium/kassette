package com.spadium.kassette.ui

import com.spadium.kassette.mixin.DrawContextAccessor
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.util.Identifier
import net.minecraft.util.Util

private var timeDelta: Double = 0.0
private var marqueePositionIndicator: Double = 0.0
private var previousTime: Long = 0

class MediaInfoHUD {
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private lateinit var textRenderer: TextRenderer

    constructor()

    fun setup() {
        HudLayerRegistrationCallback.EVENT.register {
            textRenderer = MinecraftClient.getInstance().textRenderer
            it.attachLayerBefore(
                IdentifiedLayer.HOTBAR_AND_BARS,
                MEDIA_LAYER,
                this::render
            )
        }
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        val marqueeVelocity: Float = 1f
        val marqueeScrollThreshold: Float = 1f
        val timeNaught = Util.getMeasuringTimeNano()
        timeDelta = (timeNaught - previousTime).toDouble()

        // we use a simple algebraic kinematic to help us scroll the marquee!
        // does it work? hell yeah! is there a better way to do it? definitely
        marqueePositionIndicator += (marqueeVelocity * (timeDelta / (1000 * 1000000)))

        context.fill(
            0, 0, 100, 48, 0xFF000000.toInt()
        )
        context.drawMarquee(
            textRenderer,
            "Artist - Title",
            50, 10,
            0xFFFFFFFF.toInt(),
            true,
            8, 3, (marqueePositionIndicator >= marqueeScrollThreshold)
        )
        context.drawText(
            textRenderer,
            "Album",
            50, 20,
            0xFFFFFFFF.toInt(),
            true
        )
        context.drawBorder(
            0, 0,
            100, 48,
            0xFF00FF00.toInt()
        )

        if (marqueePositionIndicator >= marqueeScrollThreshold) {
            marqueePositionIndicator = 0.0
        }
        previousTime = timeNaught
    }
}


//TODO: Cleanup later
private var marqueeCounter = 0

// simple marquee, might add a "fancy" one later
private fun DrawContext.drawMarquee(
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
            marqueeCounter = if (marqueeCounter >= spacingBetween + text.length) 0 else marqueeCounter + 1
        }

        val startIndex = marqueeCounter
        val endIndex = marqueeCounter + maxLength

        val scrolledText = textToScroll.substring(startIndex, endIndex)
        this.drawText(textRenderer, scrolledText, x, y, color, shadow)
    }
}

// fancy marquee, not for actual use right now
private fun DrawContext.drawMarqueeFancy(
    textRenderer: TextRenderer,
    text: String,
    x: Int,
    y: Int,
    color: Int,
    shadow: Boolean,
    maxLength: Int,
    spacingBetween: Int,
    offset: Float
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
        this.draw {
            textRenderer.draw(
                textToScroll, (x + offset), y.toFloat(),
                color, shadow, this.matrices.peek().positionMatrix,
                it,
                TextRenderer.TextLayerType.NORMAL,
                0,
                0xF000F0
            )
        }
    }
}

private fun DrawContext.drawShortened(
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