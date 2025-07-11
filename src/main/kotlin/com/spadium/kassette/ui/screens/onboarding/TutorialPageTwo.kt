package com.spadium.kassette.ui.screens.onboarding

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.MediaManager
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.MultilineTextWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class TutorialPageTwo: Screen {
    val layout = ThreePartsLayoutWidget(this)

    constructor(): super(Text.translatable("kassette.onboarding.three.title")) {
        Config.Instance.firstRun = false
        Config.Instance.save()
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        layout.addBody(
            MultilineTextWidget(
                Text.translatable("kassette.onboarding.three.section.config"),
                textRenderer
            ).setMaxWidth(250)
        )
        val footerButtons = layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(4))
        footerButtons.add(
            ButtonWidget.builder(
                ScreenTexts.PROCEED,
                { button ->
                    Config.Instance = Config.load()
                    if (MediaManager.provider is AccountMediaProvider) {
                        (MediaManager.provider as AccountMediaProvider).initiateLogin(false)
                        close()
                    }
                }
            ).build()
        )
        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }
}