package com.spadium.kassette.ui.widgets

import com.spadium.kassette.media.MediaManager
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput

import kotlin.math.floor

class ProgressBarWidget : AbstractWidget {
    var currentValue: Long = 1L
    var maxValue: Long = 2L
    var backgroundColor: Int = 0xFFFFFFFF.toInt()
    var foregroundColor: Int = 0xFFAAFF22.toInt()

    constructor(x: Int, y: Int, width: Int, height: Int, currentValue: Long, maxValue: Long) : super(
        x,
        y,
        width,
        height,
        null
    ) {
        this.currentValue = currentValue
        this.maxValue = maxValue
    }

    constructor(
        width: Int, height: Int, currentValue: Long, maxValue: Long
    ) : super (0, 0, width, height, null) {
        this.currentValue = currentValue
        this.maxValue = maxValue
    }

    constructor(currentValue: Long, maxValue: Long) : super(0, 0, 0, 0, null) {
        this.currentValue = currentValue
        this.maxValue = maxValue
    }

    override fun renderWidget(
        context: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {
        val mediaInfo = MediaManager.provider.info

        val progress: Double = if (mediaInfo.maximumTime == 0L) {
            0.0
        } else {
            (mediaInfo.currentPosition.toDouble() / mediaInfo.maximumTime)
        }
        val progressBarWidth = floor(width * progress).toInt()

        context?.fill(x, y, x + width, y + height, backgroundColor)
        context?.fill(x, y, x + progressBarWidth, y + height, foregroundColor)
    }

    override fun updateWidgetNarration(builder: NarrationElementOutput) {
        builder.add(NarratedElementType.TITLE, "Progressbar at $currentValue out of $maxValue")
    }
}