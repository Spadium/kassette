package com.spadium.kassette.ui.config

import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.Positioner
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ProvidersScreen: Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this, 64, 32)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)

        val sectionButtons = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.spotify"),
                SpotifyScreen(this)
            )
        )

        layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(200).build()
        )


        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }

    override fun close() {
        this.client!!.setScreen(this.parent)
    }
}