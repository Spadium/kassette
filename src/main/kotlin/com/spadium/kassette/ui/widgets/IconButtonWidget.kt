package com.spadium.kassette.ui.widgets

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.util.function.Supplier

class IconButtonWidget: ButtonWidget {
    val icon: Identifier
    val iconWidth: Int
    val iconHeight: Int
    val defaultNarrationSupplier: NarrationSupplier =
        NarrationSupplier { textSupplier: Supplier<MutableText?>? -> textSupplier!!.get() }

    constructor(
        x: Int, y: Int,
        height: Int, width: Int, iconHeight: Int, iconWidth: Int,
        icon: Identifier, onClick: PressAction,
        narrationSupplier: NarrationSupplier = NarrationSupplier { textSupplier: Supplier<MutableText?>? -> textSupplier!!.get() }
    ) : super(x, y, width, height, Text.empty(), onClick, narrationSupplier) {
        this.icon = icon
        this.iconHeight = iconHeight
        this.iconWidth = iconWidth
    }

    constructor(
        x: Int, y: Int, size: Int, iconSize: Int, icon: Identifier, onClick: PressAction
    ) : this(x, y, size, size, iconSize, iconSize, icon, onClick)

    override fun renderWidget(
        context: DrawContext?,
        mouseX: Int,
        mouseY: Int,
        deltaTicks: Float
    ) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks)
        context?.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            icon, x, y, 0f, 0f, iconHeight, iconWidth, iconHeight, iconHeight
        )
    }

    inner class Builder {
        var width: Int = 20
        var height: Int = 20
        var onPress: PressAction = PressAction {}
        val icon: Identifier
        constructor(icon: Identifier) {
            this.icon = icon
        }
    }
}