package com.spadium.kassette.ui.screens.onboarding

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.MultilineTextWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class TutorialPageOne: Screen(Text.translatable("kassette.onboarding.two.title")) {
    val layout = ThreePartsLayoutWidget(this)
    override fun init() {
        layout.addHeader(title, textRenderer)
        layout.addBody(
            MultilineTextWidget(
                Text.translatable("kassette.onboarding.two.section.one"),
                textRenderer
            ).setMaxWidth(250)
        )
        val footerButtons = layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(4))
        footerButtons.add(
            ButtonWidget.builder(
                ScreenTexts.PROCEED,
                { button -> client?.setScreen(TutorialPageTwo()) }
            ).build()
        )
        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }
}