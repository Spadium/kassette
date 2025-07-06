package com.spadium.kassette.ui.widgets

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.screen.narration.NarrationPart
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Supplier

class IconButtonWidget: PressableWidget {
    val icon: Identifier
    val iconWidth: Int
    val iconHeight: Int
    val defaultNarrationSupplier: ButtonWidget.NarrationSupplier =
        ButtonWidget.NarrationSupplier { textSupplier: Supplier<MutableText?>? -> textSupplier!!.get() }
    val onClick: () -> Unit

    constructor(
        x: Int, y: Int,
        height: Int, width: Int, iconHeight: Int, iconWidth: Int,
        icon: Identifier, onClick: () -> Unit,
    ) : super(x, y, width, height, Text.empty()) {
        this.icon = icon
        this.iconHeight = iconHeight
        this.iconWidth = iconWidth
        this.onClick = onClick
    }

    constructor(
        x: Int, y: Int, size: Int, iconSize: Int, icon: Identifier, onClick: () -> Unit
    ) : this(x, y, size, size, iconSize, iconSize, icon, onClick)

    override fun onPress() {
        this.onClick
    }

    override fun renderWidget(
        context: DrawContext?,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {
        context?.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            icon, x, y, 0f, 0f, iconHeight, iconWidth, iconHeight, iconHeight
        )
        super.renderWidget(context, mouseX, mouseY, deltaTicks)
    }

    override fun appendClickableNarrations(builder: NarrationMessageBuilder?) {
        builder?.put(NarrationPart.TITLE, "IconButton")
    }

    inner class Builder {
        var width: Int = 20
        var height: Int = 20
        var onPress: ButtonWidget.PressAction = ButtonWidget.PressAction {}
        val icon: Identifier
        constructor(icon: Identifier) {
            this.icon = icon
        }
    }
}