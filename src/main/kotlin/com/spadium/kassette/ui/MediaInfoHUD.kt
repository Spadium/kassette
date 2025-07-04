package com.spadium.kassette.ui

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.util.drawMarquee
import com.spadium.kassette.util.drawMarqueeFancy
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.ColorHelper
import kotlin.math.floor

private var timeDelta: Double = 0.0
private var positionIndicator: Double = 0.0
private var previousTime: Long = 0

class MediaInfoHUD {
    private var mediaInfo = MediaManager.info
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private var textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer
    private var coverArt: NativeImageBackedTexture = NativeImageBackedTexture(
        { "coverart" }, MediaManager.getDefaultCoverArt()
    )
    private val coverArtIdentifier = Identifier.of("kassette:coverart")
    private val config = Config.Instance
    private val hudConfig = config.hud
    private val isFancy = hudConfig.fancyText
    private val borderColor = ColorHelper.getArgb(
        hudConfig.backgroundColor[3],
        hudConfig.borderColor[0],
        hudConfig.borderColor[1],
        hudConfig.borderColor[2]
    )
    private val backgroundColor = ColorHelper.getArgb(
        hudConfig.backgroundColor[3],
        hudConfig.backgroundColor[0],
        hudConfig.backgroundColor[1],
        hudConfig.backgroundColor[2]
    )

    fun setup() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::render
        )
    }

    fun setupCoverArt() {
        val textureManager = MinecraftClient.getInstance().textureManager
        if (mediaInfo.coverArt != coverArt.image) {
            val coverImage = mediaInfo.coverArt
            coverArt.close()
            coverArt = NativeImageBackedTexture(
                { "coverart" }, coverImage
            )
            coverArt.setFilter(true, true)
            coverArt.upload()
            textureManager.registerTexture(
                coverArtIdentifier,
                coverArt
            )
        }
    }

    private fun getFirstLine(): String {
        return "${mediaInfo.title} - ${mediaInfo.artist}"
    }

    private fun getSecondLine(): String {
        return "${mediaInfo.album}"
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        setupCoverArt()
        val scrollThreshold: Float = 1f
        val currentTime: Long = Util.getMeasuringTimeNano()

        // Delta-Time in seconds
        timeDelta = (currentTime - previousTime).toDouble()  / (1000000000)
        positionIndicator += timeDelta / (1 / (if (isFancy) hudConfig.fancyTextSpeed else hudConfig.textSpeed).toDouble())
        val maxWidth = MinecraftClient.getInstance().window.scaledWidth
        val maxHeight = MinecraftClient.getInstance().window.scaledHeight
        context.fill(
            0, 0, hudConfig.width, hudConfig.height, backgroundColor
        )
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette:coverart"),
            2,
            ((hudConfig.height / 2) - (16)),
            0f, 0f,
            32, 32,
            32, 32
        )
        context.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            MediaManager.provider.state.texture,
            2, ((hudConfig.height / 2) + 8),
            8, 8
        )

        textWrapper(
            context, textRenderer, getFirstLine(),
            50, 2 + textRenderer.fontHeight, 0xFFFFFFFF.toInt(),
            true, 8, 3,
            (positionIndicator >= scrollThreshold),
            isFancy
        )
        textWrapper(
            context, textRenderer, getSecondLine(),
            50, 2 + (textRenderer.fontHeight * 2) + hudConfig.lineSpacing, 0xFFFFFFFF.toInt(),
            true, 8, 3,
            (positionIndicator >= scrollThreshold), isFancy
        )
        drawProgressBar(context)
        context.drawBorder(
            0, 0,
            hudConfig.width, hudConfig.height,
            borderColor
        )

        if (positionIndicator >= scrollThreshold) {
            positionIndicator = 0.0
        }
        previousTime = currentTime
    }

    private fun drawProgressBar(context: DrawContext) {
        val progress: Double = if (mediaInfo.maximumTime == 0L) {
            0.0
        } else {
            (mediaInfo.currentPosition.toDouble() / mediaInfo.maximumTime)
        }
        val progressBarWidth = floor((hudConfig.width - 1) * progress).toInt()
        // Progressbar background
        context.fill(
            1, 1, hudConfig.width - 1, hudConfig.progressBarThickness, Colors.WHITE
        )
        // Progressbar
        if (progressBarWidth > 0) {
            context.fill(
                1, 1, floor((hudConfig.width - 1) * progress).toInt(), hudConfig.progressBarThickness, Colors.RED
            )
        }
    }

    private fun textWrapper(
        context: DrawContext,
        textRenderer: TextRenderer,
        text: String,
        x: Int,
        y: Int,
        color: Int,
        shadow: Boolean,
        maxLength: Int,
        spacingBetween: Int,
        shouldScroll: Boolean,
        isFancy: Boolean
    ) {
        if (isFancy) {
            context.drawMarqueeFancy(
                textRenderer,
                text,
                x, y,
                color,
                shadow,
                maxLength, spacingBetween, shouldScroll
            )
        } else {
            context.drawMarquee(
                textRenderer,
                text,
                x, y,
                color,
                shadow,
                maxLength, spacingBetween, shouldScroll
            )
        }
    }
}