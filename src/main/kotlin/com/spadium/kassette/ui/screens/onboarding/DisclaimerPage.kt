package com.spadium.kassette.ui.screens.onboarding

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.MultilineTextWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.text.Text

class DisclaimerPage: Screen(Text.translatable("kassette.onboarding.one.title")) {
    val layout = ThreePartsLayoutWidget(this)
    override fun init() {
        layout.addHeader(title, textRenderer)
        layout.addBody(
            MultilineTextWidget(
                Text.translatable("kassette.onboarding.one.alpha"),
                 textRenderer
            ).setMaxWidth(250)
        )
        val footerButtons = layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(4))
        footerButtons.add(
            ButtonWidget.builder(
                Text.translatable("kassette.onboarding.one.acknowledge"),
                { button -> client?.setScreen(TutorialPageOne()) }
            ).build()
        )
        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }
}