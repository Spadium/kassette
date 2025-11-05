package com.spadium.kassette.ui.screens.onboarding

import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class TutorialPageOne: Screen(Component.translatable("kassette.onboarding.two.title")) {
    val layout = HeaderAndFooterLayout(this)
    override fun init() {
        layout.addTitleHeader(title, font)
        layout.addToContents(
            MultiLineTextWidget(
                Component.translatable("kassette.onboarding.two.section.one"),
                font
            ).setMaxWidth(250)
        )
        val footerButtons = layout.addToFooter(LinearLayout.horizontal().spacing(4))
        footerButtons.addChild(
            Button.builder(
                CommonComponents.GUI_PROCEED,
                { button -> minecraft?.setScreen(TutorialPageTwo()) }
            ).build()
        )
        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        layout.arrangeElements()
    }
}