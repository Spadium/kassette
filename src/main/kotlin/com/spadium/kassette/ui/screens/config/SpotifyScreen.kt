package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.media.spotify.SpotifyProvider
import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
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
    private val layout = ThreePartsLayoutWidget(this, 32)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)

        val gridLayout = GridWidget()
        val gridAdder = gridLayout.createAdder(2)
        gridLayout.mainPositioner.margin(4, 4, 4, 0)
            .alignHorizontalCenter().alignVerticalCenter()
        val loginButton = gridAdder.add(
            ButtonWidget.builder(
                Text.translatable("kassette.config.button.login"),
                { button ->
                    if (MediaManager.provider is SpotifyProvider) {
                        (MediaManager.provider as SpotifyProvider).initiateLogin(false)
                    }
                }
            ).width(200).build(), 2
        )
        if (MediaManager.provider is SpotifyProvider && (MediaManager.provider as SpotifyProvider).isAuthenticated) {
            loginButton.active = false
            loginButton.message = Text.translatable("kassette.config.button.login.loggedin")
        } else if (MediaManager.provider !is SpotifyProvider) {
            loginButton.active = false
            loginButton.setTooltip(Tooltip.of(Text.translatable("kassette.config.provider.error.inactive", "Spotify")))
        }
        val bypassRateLimitText = gridAdder.add(TextWidget(Text.translatable("kassette.config.option.spotify.ratelimit"), textRenderer))
        bypassRateLimitText.width = 100
        val bypassRateLimitButton = gridAdder.add(
            ButtonWidget.builder(
                Text.translatable("kassette.config.button.generic.boolean.${Config.Instance.providers.spotify.ignoreRateLimits}"),
                { button ->
                    Config.Instance.providers.spotify.ignoreRateLimits = !Config.Instance.providers.spotify.ignoreRateLimits
                    button.message = Text.translatable("kassette.config.button.generic.boolean.${Config.Instance.providers.spotify.ignoreRateLimits}")
                }
            ).width(100).build()
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
        Config.Instance.save()
        this.client!!.setScreen(parent)

    }
}