package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ErrorScreen: Screen {
    val layout = ThreePartsLayoutWidget(this, 32)
    val parent: Screen?
    var errors: MutableMap<String, Throwable>

    constructor(parent: Screen?, errors: MutableMap<String, Throwable>) : super(Text.translatable("kassette.error.title")) {
        this.parent = parent
        this.errors = errors
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val details = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))


        val footerButtons = layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(4))
        footerButtons.add(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(98).build()
        )
        footerButtons.add(
            ButtonWidget.builder(
                Text.literal("Clear"),
                { button -> }
            ).width(98).build()
        )

        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }

    override fun close() {
        client?.setScreen(parent)
    }
}