package com.spadium.kassette.ui.config

import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.GridWidget
import net.minecraft.client.gui.widget.Positioner
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Util

class SpotifyScreen: Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this, 64, 32)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)

        val gridLayout = GridWidget()
        val gridAdder = gridLayout.createAdder(2)
        gridLayout.mainPositioner.margin(4, 4, 4, 0)
        gridAdder.add(
            ButtonWidget.builder(
                Text.translatable("kassette.config.login"),
                { button -> Util.getOperatingSystem().open("https://www.google.com") }
            ).width(50 ).build(),
            1
        )
        layout.addBody(gridLayout)

        layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(200).build()
        )

        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        gridLayout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        gridLayout.refreshPositions()
        layout.refreshPositions()
    }

    override fun close() {
        this.client!!.setScreen(this.parent)
    }
}