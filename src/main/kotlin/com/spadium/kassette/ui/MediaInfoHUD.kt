package com.spadium.kassette.ui

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.ColorHelper
import kotlin.math.floor
import kotlin.reflect.KProperty


class MediaInfoHUD {
    private var timeDelta: Double = 0.0
    private var positionIndicator: Double = 0.0
    private var previousTime: Long = 0
    private var mediaInfo: MediaInfo = MediaManager.provider.info
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private var textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer
    private var coverArt: NativeImageBackedTexture = NativeImageBackedTexture(
        { "coverart" }, MediaManager.getDefaultCoverArt()
    )
    private var largeCoverArt: NativeImageBackedTexture = NativeImageBackedTexture(
        { "coverart_large" }, MediaManager.getDefaultCoverArt()
    )
    private val coverArtIdentifier = Identifier.of("kassette:coverart")
    private val largeCoverArtIdentifier = Identifier.of("kassette:coverart_large")
    private var config = Config.Instance
    private var hudConfig = config.hud
    private var isFancy = hudConfig.fancyText
    private var borderColor = ColorHelper.getArgb(
        hudConfig.backgroundColor[3],
        hudConfig.borderColor[0],
        hudConfig.borderColor[1],
        hudConfig.borderColor[2]
    )
    private var backgroundColor = ColorHelper.getArgb(
        hudConfig.backgroundColor[3],
        hudConfig.backgroundColor[0],
        hudConfig.backgroundColor[1],
        hudConfig.backgroundColor[2]
    )
    private var progressBarBg = ColorHelper.getArgb(
        hudConfig.progressBackgroundColor[3],
        hudConfig.progressBackgroundColor[0],
        hudConfig.progressBackgroundColor[1],
        hudConfig.progressBackgroundColor[2]
    )
    private var progressBarFg = ColorHelper.getArgb(
        hudConfig.progressForegroundColor[3],
        hudConfig.progressForegroundColor[0],
        hudConfig.progressForegroundColor[1],
        hudConfig.progressForegroundColor[2]
    )
    private lateinit var firstLineManager: MarqueeTextManager
    private lateinit var secondLineManager: MarqueeTextManager
    private lateinit var thirdLineManager: MarqueeTextManager

    constructor() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::render
        )
        Config.addListener(this::updateVariables)
    }

    private fun updateVariables(property: KProperty<*>, oldValue: Config, newValue: Config) {
        config = Config.Instance
        hudConfig = config.hud
        borderColor = ColorHelper.getArgb(
            hudConfig.backgroundColor[3],
            hudConfig.borderColor[0],
            hudConfig.borderColor[1],
            hudConfig.borderColor[2]
        )
        backgroundColor = ColorHelper.getArgb(
            hudConfig.backgroundColor[3],
            hudConfig.backgroundColor[0],
            hudConfig.backgroundColor[1],
            hudConfig.backgroundColor[2]
        )
        progressBarBg = ColorHelper.getArgb(
            hudConfig.progressBackgroundColor[3],
            hudConfig.progressBackgroundColor[0],
            hudConfig.progressBackgroundColor[1],
            hudConfig.progressBackgroundColor[2]
        )
        progressBarFg = ColorHelper.getArgb(
            hudConfig.progressForegroundColor[3],
            hudConfig.progressForegroundColor[0],
            hudConfig.progressForegroundColor[1],
            hudConfig.progressForegroundColor[2]
        )
    }

    private fun setupCoverArt() {
        if (MediaManager.provider.getMedia().coverArt != coverArt.image) {
            val textureManager = MinecraftClient.getInstance().textureManager
            val coverImage = MediaManager.provider.getMedia().coverArt
            coverArt.close()
            coverArt = NativeImageBackedTexture(
                { "coverart" }, coverImage
            )
            coverArt.setFilter(true, true)
            coverArt.upload()

            largeCoverArt.close()
            largeCoverArt = NativeImageBackedTexture(
                { "coverart_large" }, coverImage
            )
            largeCoverArt.setFilter(false, false)
            largeCoverArt.upload()

            textureManager.registerTexture(coverArtIdentifier, coverArt)
            textureManager.registerTexture(largeCoverArtIdentifier, largeCoverArt)
        }
    }

    private fun getFirstLine(): String {
        return "${mediaInfo.title} - ${mediaInfo.artist}"
    }

    private fun getSecondLine(): String {
        return mediaInfo.album
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        if (!::firstLineManager.isInitialized) {
            firstLineManager = MarqueeTextManager(context)
            secondLineManager = MarqueeTextManager(context)
            thirdLineManager = MarqueeTextManager(context)
        }
        mediaInfo = MediaManager.provider.info
        setupCoverArt()
        val scrollThreshold: Float = 1f
        val currentTime: Long = Util.getMeasuringTimeNano()
        val artSize: Int = ((hudConfig.height.toFloat() - hudConfig.progressBarThickness) * (3f/4f)).toInt()

        // Delta-Time in seconds
        timeDelta = (currentTime - previousTime).toDouble()  / (1000000000)
        positionIndicator += timeDelta / (1 / (if (isFancy) hudConfig.fancyTextSpeed else hudConfig.textSpeed).toDouble())

        context.fill(
            0, 0, hudConfig.width, hudConfig.height, backgroundColor
        )
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette:coverart"),
            8,
            (((hudConfig.height + hudConfig.progressBarThickness) / 2) - (artSize / 2)),
            0f, 0f,
            artSize, artSize,
            artSize, artSize
        )
        context.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            MediaManager.provider.info.state.texture,
            hudConfig.width - 17, 2 + (textRenderer.fontHeight * 3) + (hudConfig.lineSpacing * 2),
            textRenderer.fontHeight, textRenderer.fontHeight
        )

        firstLineManager.text = getFirstLine()
        firstLineManager.renderText(
            50, 2 + textRenderer.fontHeight, 0xFFFFFFFF.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
        )

        secondLineManager.text = getSecondLine()
        secondLineManager.renderText(
            50, 2 + (textRenderer.fontHeight * 2) + hudConfig.lineSpacing, 0xFFFFFFFF.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
        )
        thirdLineManager.text = MediaManager.provider.info.provider
        thirdLineManager.renderText(
            50, 2 + (textRenderer.fontHeight * 3) + (hudConfig.lineSpacing * 2),
            0xFFAAAAAA.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
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
            1, 1, hudConfig.width - 1, hudConfig.progressBarThickness, progressBarBg
        )
        // Progressbar
        if (progressBarWidth > 0) {
            context.fill(
                1, 1, floor((hudConfig.width - 1) * progress).toInt(), hudConfig.progressBarThickness, progressBarFg
            )
        }
    }
}