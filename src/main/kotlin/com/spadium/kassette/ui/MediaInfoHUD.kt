package com.spadium.kassette.ui

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.util.ImageUtils
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

private var timeDelta: Double = 0.0
private var positionIndicator: Double = 0.0
private var previousTime: Long = 0

class MediaInfoHUD {
    private var mediaManager = MediaManager
    private var mediaInfo = mediaManager.info
    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private lateinit var textRenderer: TextRenderer
    private lateinit var coverArt: NativeImageBackedTexture
    private val coverArtIdentifier = Identifier.of("kassette:coverart")
    private val config = Config.Instance
    private val hudConfig = config.hud
    val isFancy = hudConfig.fancyText
    val borderColor = ColorHelper.getArgb(
        hudConfig.backgroundColor[3],
        hudConfig.borderColor[0],
        hudConfig.borderColor[1],
        hudConfig.borderColor[2]
    )
    val backgroundColor = ColorHelper.getArgb(
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
        val coverImage = if (mediaInfo?.coverArt != null) {
            mediaInfo!!.coverArt
        } else {
            ImageUtils.loadGenericImage(
                javaClass.getResourceAsStream("/assets/kassette/placeholder.jpg")!!.readAllBytes()
            )
        }

        coverArt = NativeImageBackedTexture(
            { "coverart" }, coverImage
        )
        coverArt.upload()
        textureManager.registerTexture(
            coverArtIdentifier,
            coverArt
        )
    }

    private fun getFirstLine(): String {
        return if (mediaInfo != null) {
            "${mediaInfo!!.title} - ${mediaInfo!!.artist}"
        } else {
            "Nothing Currently Playing"
        }
    }

    private fun getSecondLine(): String {
        return if (mediaInfo != null) {
            mediaInfo!!.artist
        } else {
            "No Information"
        }
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
        positionIndicator += timeDelta / (1 / (if (isFancy) hudConfig.fancyTextSpeed else hudConfig.textSpeed).toDouble())
        val maxWidth = MinecraftClient.getInstance().window.scaledWidth
        val maxHeight = MinecraftClient.getInstance().window.scaledHeight
        context.fill(
            0, 0, hudConfig.width, hudConfig.height, backgroundColor
        )
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette:coverart"),
            2, 2, 0f, 0f,
            32, 32,
            32, 32
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
            50, 2 + + textRenderer.fontHeight * 2, 0xFFFFFFFF.toInt(),
            true, 8, 3,
            (positionIndicator >= scrollThreshold), isFancy
        )
        drawProgressBar(context)
        context.drawBorder(
            0, 0,
            100, 48,
            borderColor
        )

        if (positionIndicator >= scrollThreshold) {
            positionIndicator = 0.0
        }
        previousTime = currentTime
    }

    private fun drawProgressBar(context: DrawContext) {
        val progress: Double = if (mediaManager.info?.currentPosition == null || mediaManager.info?.maximumTime == null) {
            0.0
        } else {
            (mediaManager.info?.currentPosition!!.toDouble() / mediaManager.info?.maximumTime!!)
        }

        // Progressbar background
        context.fill(
            0, 0, 100, 10, -1
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