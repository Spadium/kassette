package com.spadium.kassette.ui.overlays

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.config.overlays.DefaultOverlayConfig
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.MarqueeTextManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.math.ColorHelper
import kotlin.math.floor
import kotlin.reflect.KProperty


class DefaultOverlay : OverlayTheme {
    private val scrollThreshold: Float = 1f
    private var timeDelta: Double = 0.0
    private var positionIndicator: Double = 0.0
    private var previousEndTime: Long = 0
    private var mediaInfo: MediaInfo = MediaManager.provider.info
    private var textRenderer: TextRenderer = MinecraftClient.getInstance().textRenderer

    private var config = MainConfig.Instance
    private var hudConfig = MainConfig.load<DefaultOverlayConfig>()
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
//        Config.addListener(this::updateVariables)
    }

    private fun updateVariables(property: KProperty<*>, oldValue: MainConfig, newValue: MainConfig) {
        // avoid stuttering when we don't need to reload variables
        if (oldValue != newValue) {
            config = newValue
//            hudConfig = config.overlays.default
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
    }

    private fun getFirstLine(): String {
        return "${mediaInfo.title} - ${mediaInfo.artist}"
    }

    private fun getSecondLine(): String {
        return mediaInfo.album
    }

    override fun render(context: DrawContext, tickCounter: RenderTickCounter) {
        if (!::firstLineManager.isInitialized) {
            firstLineManager = MarqueeTextManager(context)
            secondLineManager = MarqueeTextManager(context)
            thirdLineManager = MarqueeTextManager(context)
        }
        mediaInfo = MediaManager.provider.info
        val startTime: Long = Util.getMeasuringTimeNano()
        val artSize: Int = ((hudConfig.height.toFloat() - hudConfig.progressBarThickness) * (3f/4f)).toInt()

        // Delta-Time in seconds
        timeDelta = (startTime - previousEndTime).toDouble()  / (1000000000)
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

        firstLineManager.text = Text.literal(getFirstLine())
        firstLineManager.renderText(
            50, 2 + textRenderer.fontHeight, 0xFFFFFFFF.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
        )

        secondLineManager.text = Text.literal(getSecondLine())
        secondLineManager.renderText(
            50, 2 + (textRenderer.fontHeight * 2) + hudConfig.lineSpacing, 0xFFFFFFFF.toInt(),
            true, 14, 3, (positionIndicator >= scrollThreshold)
        )
        thirdLineManager.text = Text.literal(MediaManager.provider.info.provider)
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
            positionIndicator -= scrollThreshold
        }
        previousEndTime = startTime
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