package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.config.providers.SpotifyConfig
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.media.spotify.SpotifyProvider
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class SpotifyScreen: Screen {
    private val parent: Screen?
    private val layout = HeaderAndFooterLayout(this, 32)

    constructor(parent: Screen?) : super(Component.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addTitleHeader(title, font)

        val gridLayout = GridLayout()
        val gridAdder = gridLayout.createRowHelper(2)
        gridLayout.defaultCellSetting().padding(4, 4, 4, 0)
            .alignHorizontallyCenter().alignVerticallyMiddle()
        val loginButton = gridAdder.addChild(
            Button.builder(
                Component.translatable("kassette.config.button.login"),
                { button ->
                    if (MediaManager.provider is SpotifyProvider) {
                        (MediaManager.provider as SpotifyProvider).initiateLogin(false)
                    }
                }
            ).width(200).build(), 2
        )
        if (MediaManager.provider is SpotifyProvider && (MediaManager.provider as SpotifyProvider).isAuthenticated) {
            loginButton.active = false
            loginButton.message = Component.translatable("kassette.config.button.login.loggedin")
        } else if (MediaManager.provider !is SpotifyProvider) {
            loginButton.active = false
            loginButton.setTooltip(Tooltip.create(Component.translatable("kassette.config.provider.error.inactive", "Spotify")))
        }
        val bypassRateLimitText = gridAdder.addChild(StringWidget(Component.translatable("kassette.config.option.spotify.ratelimit"), font))
        bypassRateLimitText.width = 100
        val bypassRateLimitButton = gridAdder.addChild(
            Button.builder(
                Component.translatable("kassette.config.button.generic.boolean.${MainConfig.Instance.providers.spotify.ignoreRateLimits}"),
                { button ->
                    MainConfig.Instance.providers.spotify.ignoreRateLimits = !MainConfig.Instance.providers.spotify.ignoreRateLimits
                    button.message = Component.translatable("kassette.config.button.generic.boolean.${MainConfig.Instance.providers.spotify.ignoreRateLimits}")
                    rebuildWidgets()
                }
            ).width(100).build()
        )

        layout.addToContents(gridLayout)

        layout.addToFooter(
            Button.builder(
                CommonComponents.GUI_DONE,
                { button -> onClose() }
            ).width(200).build()
        )

        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        gridLayout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        gridLayout.arrangeElements()
        layout.arrangeElements()
    }

    private fun onLogin() {
        rebuildWidgets()
    }

    override fun onClose() {
        SpotifyConfig.Instance.save()
        this.minecraft!!.setScreen(parent)

    }
}