package com.spadium.kassette.ui.widgets

import com.spadium.kassette.config.Config
import com.spadium.kassette.config.overlays.DefaultOverlayConfig
import com.spadium.kassette.ui.MarqueeTextManager
import net.minecraft.util.Util
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.network.chat.Component

class MarqueeTextWidget: StringWidget {
    private var hudConfig = Config.load<DefaultOverlayConfig>()
    var maxWidth: Int
    private var textManager = MarqueeTextManager()
    private var lastRenderTime: Long = 0L
    private var delta: Double = 0.0
    private var deltaAccumulator: Double = 0.0
    private val scrollThreshold = 1
    private val color = -1

    constructor(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        message: Component,
        textRenderer: Font?,
        maxWidth: Int
    ) : super(
        x,
        y,
        width,
        height,
        message,
        textRenderer!!
    ) {
        textManager.text = message
        this.maxWidth = maxWidth
    }

    constructor(message: Component, textRenderer: Font?, maxWidth: Int) : super(message, textRenderer!!) {
        textManager.text = message
        this.maxWidth = maxWidth
    }

    constructor(width: Int, height: Int, message: Component, textRenderer: Font?, maxWidth: Int) : super(
        width,
        height,
        message,
        textRenderer!!
    ) {
        textManager.text = message
        this.maxWidth = maxWidth
    }

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        delta = (Util.getNanos().toDouble() - lastRenderTime.toDouble()) / 1000000000
        deltaAccumulator += delta / (1 / (hudConfig.fancyTextSpeed).toDouble())
        val shouldScroll = (deltaAccumulator >= scrollThreshold)
        if (textManager.context != context) {
            textManager.context = context
        }
        // 5 is the size of  .
        textManager.renderText(x, y, color, true, width / 5, 3, shouldScroll)

        if (shouldScroll) {
            if (deltaAccumulator >= scrollThreshold && deltaAccumulator <= scrollThreshold*2) {
                deltaAccumulator -= scrollThreshold
            } else {
                deltaAccumulator = 0.0
            }
        }
        lastRenderTime = Util.getNanos()
    }

    override fun setWidth(width: Int) {
        if (maxWidth > width) {
            maxWidth = width
        }
        super.setWidth(width)
    }
}