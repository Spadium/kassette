package me.spadium.kassette.ui.screens.onboarding

import me.spadium.kassette.config.Config
import me.spadium.kassette.config.MainConfig
import me.spadium.kassette.media.AccountMediaProvider
import me.spadium.kassette.media.MediaManager
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.MultiLineTextWidget
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class TutorialPageTwo: Screen {
    val layout = HeaderAndFooterLayout(this)

    constructor(): super(Component.translatable("kassette.onboarding.three.title")) {
        MainConfig.Instance.firstRun = false
        MainConfig.Instance.save()
    }

    override fun init() {
        layout.addTitleHeader(title, font)
        layout.addToContents(
            MultiLineTextWidget(
                Component.translatable("kassette.onboarding.three.section.config"),
                font
            ).setMaxWidth(250)
        )
        val footerButtons = layout.addToFooter(LinearLayout.horizontal().spacing(4))
        footerButtons.addChild(
            Button.builder(
                CommonComponents.GUI_PROCEED,
                { button ->
                    MainConfig.Instance = Config.load()
                    if (MediaManager.provider is AccountMediaProvider) {
                        (MediaManager.provider as AccountMediaProvider).initiateLogin(false)
                        this@TutorialPageTwo.onClose()
                    }
                }
            ).build()
        )
        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        layout.arrangeElements()
    }
}