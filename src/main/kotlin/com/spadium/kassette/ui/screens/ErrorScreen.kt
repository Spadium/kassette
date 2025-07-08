package com.spadium.kassette.ui.screens

import com.spadium.kassette.ui.screens.config.AboutScreen
import com.spadium.kassette.ui.screens.config.ProvidersScreen
import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ErrorScreen: Screen {
    val layout = ThreePartsLayoutWidget(this)

    constructor(parent: Screen?) : super(Text.translatable("kassette.error.title"))

    override fun init() {
        layout.addHeader(title, textRenderer)
        val sectionButtons = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))
        sectionButtons.add(
            KassetteUtils.Companion.createButtonToScreen(
                Text.translatable("kassette.config.button.providers"),
                ProvidersScreen(this)
            )
        )
        sectionButtons.add(
            KassetteUtils.Companion.createButtonToScreen(
                Text.translatable("kassette.config.button.about"),
                AboutScreen(this)
            )
        )

        val footerButtons = layout.addFooter(DirectionalLayoutWidget.horizontal())
        footerButtons.add(
            ButtonWidget.builder(
                ScreenTexts.CONTINUE,
                { button -> close() }
            ).width(200).build()
        )

        footerButtons.add(
            ButtonWidget.builder(
                ScreenTexts.CANCEL,
                { button -> close() }
            ).width(200).build()
        )

        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }
}