package com.spadium.kassette.ui

import com.spadium.kassette.Kassette
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
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
            Kassette.Companion.logger.warn("please dont get called multiple times!!!!!")
            textRenderer = MinecraftClient.getInstance().textRenderer
            it.attachLayerBefore(
                IdentifiedLayer.HOTBAR_AND_BARS,
                MEDIA_LAYER,
                this::render
            )
        }
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        val color = -0x10000 // Red
        val targetColor = -0xff0100 // Green
        val marqueeVelocity: Int = 1
        val marqueeScrollThreshold: Int = 1
        val timeNaught = System.nanoTime()
        timeDelta = ((timeNaught - previousTime) / 1000000).toDouble()
        // we treat the marquee's position as a position-velocity thingy!!!
        // does it work? hell yeah! is there a better way to do it? possibly
        marqueePositionIndicator += (1 * (timeDelta / 1000))
//        print("$marqueePositionIndicator\n")
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
            "Albumasdiujasioduasiduasioduasiudas",
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
            marqueeCounter = if (marqueeCounter >= textToScroll.length - maxLength) 0 else marqueeCounter + 1
        }
        if (marqueeCounter >= spacingBetween + text.length + 1) {
            marqueeCounter = 0
        }

        var startIndex = marqueeCounter
        var endIndex = marqueeCounter + maxLength

        val scrolledText = textToScroll.substring(startIndex, endIndex)
        print("$scrolledText\n")
        print("MARQUEE - $marqueeCounter [$startIndex, $endIndex] @ ${java.time.LocalTime.now()}\n")
        this.drawText(textRenderer, scrolledText, x, y, color, shadow)
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