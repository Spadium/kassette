package com.spadium.kassette.ui.screens.onboarding

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class DisclaimerPage: Screen(Component.translatable("kassette.onboarding.one.title")) {
    val layout = HeaderAndFooterLayout(this)
    override fun init() {
        layout.addTitleHeader(title, font)
        layout.addToContents(
            MultiLineTextWidget(
                Component.translatable("kassette.onboarding.one.alpha"),
                font
            ).setMaxWidth(250)
        )
        val footerButtons = layout.addToFooter(LinearLayout.horizontal().spacing(4))
        footerButtons.addChild(
            Button.builder(
                Component.translatable("kassette.onboarding.one.acknowledge"),
                { button -> minecraft?.setScreen(TutorialPageOne()) }
            ).build()
        )
        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        layout.arrangeElements()
    }
}