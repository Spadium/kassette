package com.spadium.kassette.ui

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import net.minecraft.util.Util

private var timeDelta: Double = 0.0
private var positionIndicator: Double = 0.0
private var previousTime: Long = 0

class MediaInfoHUD {
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private lateinit var textRenderer: TextRenderer
    private lateinit var coverArt: NativeImageBackedTexture
    constructor() {

    }

    fun setup() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::render
        )
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        if (!::textRenderer.isInitialized)
            textRenderer = MinecraftClient.getInstance().textRenderer

        val speedFactor: Float = 1f
        val scrollThreshold: Float = 1f
        val currentTime = Util.getMeasuringTimeNano()
        // Delta-Time in seconds
        timeDelta = (currentTime - previousTime).toDouble()  / (1000000000)
        positionIndicator += timeDelta / speedFactor

        context.fill(
            0, 0, 100, 48, 0xFF000000.toInt()
        )
        context.drawMarquee(
            textRenderer,
            "Artist - Title",
            50, 10,
            0xFFFFFFFF.toInt(),
            true,
            8, 3, (positionIndicator >= scrollThreshold)
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

        if (positionIndicator >= scrollThreshold) {
            positionIndicator = 0.0
        }
        previousTime = currentTime
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

    val textToScroll: String = "$text$spacing$text$spacing$text"

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
    TODO("update to 1.21.6")
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
//        this.draw {
//            textRenderer.draw(
//                textToScroll, (x + offset), y.toFloat(),
//                color, shadow, this.matrices.peek().positionMatrix,
//                it,
//                TextRenderer.TextLayerType.NORMAL,
//                0,
//                0xF000F0
//            )
//        }
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