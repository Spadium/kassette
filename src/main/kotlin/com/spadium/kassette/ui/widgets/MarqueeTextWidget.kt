package com.spadium.kassette.ui.widgets

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.config.overlays.DefaultOverlayConfig
import com.spadium.kassette.ui.MarqueeTextManager
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text
import net.minecraft.util.Util

class MarqueeTextWidget: TextWidget {
    private var hudConfig = MainConfig.load<DefaultOverlayConfig>()
    var maxWidth: Int
    private var textManager = MarqueeTextManager()
    private var lastRenderTime: Long = 0L
    private var delta: Double = 0.0
    private var deltaAccumulator: Double = 0.0
    private val scrollThreshold = 1

    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        message: Text,
        textRenderer: TextRenderer?,
        maxWidth: Int
    ) : super(
        x,
        y,
        width,
        height,
        message,
        textRenderer
    ) {
        textManager.text = message
        this.maxWidth = maxWidth
    }

    constructor(message: Text, textRenderer: TextRenderer?, maxWidth: Int) : super(message, textRenderer) {
        textManager.text = message
        this.maxWidth = maxWidth
    }

    constructor(width: Int, height: Int, message: Text, textRenderer: TextRenderer?, maxWidth: Int) : super(
        width,
        height,
        message,
        textRenderer
    ) {
        textManager.text = message
        this.maxWidth = maxWidth
    }

    override fun renderWidget(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        delta = (Util.getMeasuringTimeNano().toDouble() - lastRenderTime.toDouble()) / 1000000000
        deltaAccumulator += delta / (1 / (hudConfig.fancyTextSpeed).toDouble())
        val shouldScroll = (deltaAccumulator >= scrollThreshold)
        if (textManager.context != context) {
            textManager.context = context
        }
        // 5 is the size of  .
        textManager.renderText(x, y, textColor, true, width / 5, 3, shouldScroll)

        if (shouldScroll) {
            if (deltaAccumulator >= scrollThreshold && deltaAccumulator <= scrollThreshold*2) {
                deltaAccumulator -= scrollThreshold
            } else {
                deltaAccumulator = 0.0
            }
        }
        lastRenderTime = Util.getMeasuringTimeNano()
    }

    override fun setWidth(width: Int) {
        if (maxWidth > width) {
            maxWidth = width
        }
        super.setWidth(width)
    }
}