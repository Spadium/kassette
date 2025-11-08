package com.spadium.kassette.ui.overlays

import com.spadium.kassette.config.Config
import com.spadium.kassette.config.overlays.DefaultOverlayConfig
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.MarqueeTextManager
import net.minecraft.Util
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ARGB
import kotlin.math.floor
import kotlin.reflect.KProperty


class DefaultOverlay : OverlayTheme {
    private val scrollThreshold: Float = 1f
    private var timeDelta: Double = 0.0
    private var positionIndicator: Double = 0.0
    private var previousEndTime: Long = 0
    private var mediaInfo: MediaInfo = MediaManager.provider.info
    private var textRenderer: Font = Minecraft.getInstance().font

//    private var config = MainConfig.Instance
    private var config = Config.load<DefaultOverlayConfig>()
    private var isFancy = config.fancyText
    private var borderColor = ARGB.color(
        config.backgroundColor[3],
        config.borderColor[0],
        config.borderColor[1],
        config.borderColor[2]
    )
    private var backgroundColor = ARGB.color(
        config.backgroundColor[3],
        config.backgroundColor[0],
        config.backgroundColor[1],
        config.backgroundColor[2]
    )
    private var progressBarBg = ARGB.color(
        config.progressBackgroundColor[3],
        config.progressBackgroundColor[0],
        config.progressBackgroundColor[1],
        config.progressBackgroundColor[2]
    )
    private var progressBarFg = ARGB.color(
        config.progressForegroundColor[3],
        config.progressForegroundColor[0],
        config.progressForegroundColor[1],
        config.progressForegroundColor[2]
    )
    private lateinit var firstLineManager: MarqueeTextManager
    private lateinit var secondLineManager: MarqueeTextManager
    private lateinit var thirdLineManager: MarqueeTextManager

    constructor() {
//        Config.addListener(this::updateVariables)
        DefaultOverlayConfig.addListener(this::updateVariables)
    }

    private fun updateVariables(property: KProperty<*>, oldValue: DefaultOverlayConfig, newValue: DefaultOverlayConfig) {
        // avoid stuttering when we don't need to reload variables
        if (oldValue != newValue) {
            config = newValue
            borderColor = ARGB.color(
                config.backgroundColor[3],
                config.borderColor[0],
                config.borderColor[1],
                config.borderColor[2]
            )
            backgroundColor = ARGB.color(
                config.backgroundColor[3],
                config.backgroundColor[0],
                config.backgroundColor[1],
                config.backgroundColor[2]
            )
            progressBarBg = ARGB.color(
                config.progressBackgroundColor[3],
                config.progressBackgroundColor[0],
                config.progressBackgroundColor[1],
                config.progressBackgroundColor[2]
            )
            progressBarFg = ARGB.color(
                config.progressForegroundColor[3],
                config.progressForegroundColor[0],
                config.progressForegroundColor[1],
                config.progressForegroundColor[2]
            )
        }
    }

    private fun getFirstLine(): String {
        return "${mediaInfo.title} - ${mediaInfo.artist}"
    }

    private fun getSecondLine(): String {
        return mediaInfo.album
    }

    override fun render(context: GuiGraphics, tickCounter: DeltaTracker) {
        if (!::firstLineManager.isInitialized) {
            firstLineManager = MarqueeTextManager(context)
            secondLineManager = MarqueeTextManager(context)
            thirdLineManager = MarqueeTextManager(context)
        }
        mediaInfo = MediaManager.provider.info
        val startTime: Long = Util.getNanos()
        val artSize: Int = ((config.height.toFloat() - config.progressBarThickness) * (3f/4f)).toInt()

        // Delta-Time in seconds
        timeDelta = (startTime - previousEndTime).toDouble()  / (1000000000)
        positionIndicator += timeDelta / (1 / (if (isFancy) config.fancyTextSpeed else config.textSpeed).toDouble())
        val shouldScroll = (positionIndicator >= scrollThreshold)

        context.fill(
            0, 0, config.width, config.height, backgroundColor
        )
        context.blit(
            RenderPipelines.GUI_TEXTURED,
            ResourceLocation.parse("kassette:coverart"),
            8,
            (((config.height + config.progressBarThickness) / 2) - (artSize / 2)),
            0f, 0f,
            artSize, artSize,
            artSize, artSize
        )
        context.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            MediaManager.provider.info.state.texture,
            config.width - 17, 2 + (textRenderer.lineHeight * 3) + (config.lineSpacing * 2),
            textRenderer.lineHeight, textRenderer.lineHeight
        )

        firstLineManager.text = Component.literal(getFirstLine())
        firstLineManager.renderText(
            50, 2 + textRenderer.lineHeight, 0xFFFFFFFF.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
        )

        secondLineManager.text = Component.literal(getSecondLine())
        secondLineManager.renderText(
            50, 2 + (textRenderer.lineHeight * 2) + config.lineSpacing, 0xFFFFFFFF.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
        )
        thirdLineManager.text = Component.literal(MediaManager.provider.info.provider)
        thirdLineManager.renderText(
            50, 2 + (textRenderer.lineHeight * 3) + (config.lineSpacing * 2),
            0xFFAAAAAA.toInt(),
            true, 14, 3, shouldScroll
        )

        drawProgressBar(context)
        context.submitOutline(
            0, 0,
            config.width, config.height,
            borderColor
        )
        if (shouldScroll) {
            if (positionIndicator >= scrollThreshold && positionIndicator <= scrollThreshold*1.25) {
                positionIndicator -= scrollThreshold
            } else {
                positionIndicator = 0.0
            }
        }
        previousEndTime = startTime
    }

    private fun drawProgressBar(context: GuiGraphics) {
        val progress: Double = if (mediaInfo.maximumTime == 0L) {
            0.0
        } else {
            (mediaInfo.currentPosition.toDouble() / mediaInfo.maximumTime)
        }
        val progressBarWidth = floor((config.width - 1) * progress).toInt()
        // Progressbar background
        context.fill(
            1, 1, config.width - 1, config.progressBarThickness, progressBarBg
        )
        // Progressbar
        if (progressBarWidth > 0) {
            context.fill(
                1, 1, floor((config.width - 1) * progress).toInt(), config.progressBarThickness, progressBarFg
            )
        }
    }
}