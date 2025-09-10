package com.spadium.kassette.ui.widgets

import com.spadium.kassette.media.MediaManager
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.screen.narration.NarrationPart
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.text.Text
import kotlin.math.floor

class ProgressBarWidget : ClickableWidget {
    var currentValue: Long = 1L
    var maxValue: Long = 2L

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
        context: DrawContext?,
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

        context?.fill(x, y, x + width, y + height, 0xFFFFFFFF.toInt())
        context?.fill(x, y, x + progressBarWidth, y + height, 0xFFAAFF22.toInt())
    }

    override fun appendClickableNarrations(builder: NarrationMessageBuilder?) {
        builder?.put(NarrationPart.TITLE, "Progressbar at x out of y")
    }
}