package com.spadium.kassette.ui

import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.util.drawMarqueeFancy
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

private var timeDelta: Double = 0.0
private var positionIndicator: Double = 0.0
private var previousTime: Long = 0
private val mediaManager = MediaManager.instance

class MediaInfoHUD {

    private val MEDIA_LAYER: Identifier = Identifier.of("kassette", "media-layer")
    private lateinit var textRenderer: TextRenderer
    private lateinit var coverArt: NativeImageBackedTexture
    private val coverArtIdentifier = Identifier.of("kassette:coverart")

    constructor() {

    }

    fun setup() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.HOTBAR, MEDIA_LAYER,
            this::render
        )
    }

    fun setupCoverArt() {
        val textureManager = MinecraftClient.getInstance().textureManager

        if (textureManager.getTexture(coverArtIdentifier) != null) {
            textureManager.destroyTexture(coverArtIdentifier)
        }
        coverArt = NativeImageBackedTexture(
            { "coverart" } , mediaManager.info.coverArt
        )
        coverArt.upload()

        MinecraftClient.getInstance().textureManager.registerTexture(
            coverArtIdentifier,
            coverArt
        )
    }

    private fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        if (!::textRenderer.isInitialized) {
            textRenderer = MinecraftClient.getInstance().textRenderer
            setupCoverArt()
        }

        val speedFactor: Float = 5f
        val scrollThreshold: Float = 1f
        val currentTime: Long = Util.getMeasuringTimeNano()
        // Delta-Time in seconds
        timeDelta = (currentTime - previousTime).toDouble()  / (1000000000)
        positionIndicator += timeDelta / (1 / speedFactor)

        context.fill(
            0, 0, 100, 48, 0xFF000000.toInt()
        )
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette:coverart"), 2, 2, 0f, 0f, 32, 32, 32, 32
        )
//        context.drawMarquee(
//            textRenderer,
//            "Artist - Title",
//            50, 10,
//            0xFFFFFFFF.toInt(),
//            true,
//            8, 3, (positionIndicator >= scrollThreshold)
//        )
        context.drawMarqueeFancy(
            textRenderer,
            "${mediaManager.info.title} - ${mediaManager.info.artist}",
            50, 10,
            0xFFFFFFFF.toInt(),
            true,
            8, 3, (positionIndicator >= scrollThreshold)
        )
        context.drawText(
            textRenderer,
            mediaManager.info.album,
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
