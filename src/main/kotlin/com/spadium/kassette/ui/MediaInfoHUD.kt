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

private var timeDelta: Double = 0.0
private var positionIndicator: Double = 0.0
private var previousTime: Long = 0

class MediaInfoHUD {
    private var mediaManager = MediaManager.instance
    private var mediaInfo = mediaManager.info
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private lateinit var textRenderer: TextRenderer
    private lateinit var coverArt: NativeImageBackedTexture
    private val coverArtIdentifier = Identifier.of("kassette:coverart")
    private val config = Config.getInstance()
    private val hudConfig = config.hud
    val infoFirstLine = "${mediaInfo.title} - ${mediaInfo.artist}"
    val infoSecondLine = "${mediaInfo.album}"
    val isFancy = hudConfig.fancyText

    fun setup() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::render
        )
    }

    fun setupCoverArt() {
        val textureManager = MinecraftClient.getInstance().textureManager
        val coverImage = mediaInfo.coverArt

        coverArt = NativeImageBackedTexture(
            { "coverart" }, coverImage
        )
        coverArt.upload()
        textureManager.registerTexture(
            coverArtIdentifier,
            coverArt
        )
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
//        val xmlIS = MinecraftClient.getInstance().resourceManager.getResource(Identifier.of("kassette:gui_hud.xml")).get().inputStream
//        println(xmlIS.readAllBytes().toString(Charsets.UTF_8))
        if (!::textRenderer.isInitialized) {
            textRenderer = MinecraftClient.getInstance().textRenderer
            setupCoverArt()
            return
        }
        val scrollThreshold: Float = 1f
        val currentTime: Long = Util.getMeasuringTimeNano()

        // Delta-Time in seconds
        timeDelta = (currentTime - previousTime).toDouble()  / (1000000000)
        positionIndicator += timeDelta / (if (isFancy) hudConfig.fancyTextSpeed else hudConfig.textSpeed).toDouble()
        val maxWidth = MinecraftClient.getInstance().window.scaledWidth
        val maxHeight = MinecraftClient.getInstance().window.scaledHeight
        context.fill(
            0, 0, 128, 48, 0xFF000000.toInt()
        )
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette:coverart"),
            2, 2, 0f, 0f,
            32, 32,
            32, 32
        )

        textWrapper(
            context, textRenderer, infoFirstLine,
            50, 10, 0xFFFFFFFF.toInt(),
            true, 8, 3,
            (positionIndicator >= scrollThreshold),
            isFancy
        )
        textWrapper(
            context, textRenderer, infoSecondLine,
            50, 20, 0xFFFFFFFF.toInt(),
            true, 8, 3,
            (positionIndicator >= scrollThreshold), isFancy
        )
        drawProgressBar(context)
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

    private fun drawProgressBar(context: DrawContext) {
        val progress: Double = (mediaManager.info.currentPosition.toDouble() / mediaManager.info.maximumTime)
        // Progressbar background
        context.fill(
            0, 0, 100, 10, 0xFFFFFFFF.toInt()
        )
        // Progressbar
        context.fill(
            0, 0, (100 * progress).toInt(), 10, Colors.RED
        )
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