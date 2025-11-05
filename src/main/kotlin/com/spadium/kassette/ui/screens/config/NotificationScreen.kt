package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.util.ModNotification
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class NotificationScreen: Screen {
    val layout = HeaderAndFooterLayout(this, 32)
    val parent: Screen?
    var notifications: MutableList<ModNotification>

    constructor(parent: Screen?, notifications: MutableList<ModNotification>) : super(Component.translatable("kassette.notifications.title")) {
        this.parent = parent
        this.notifications = notifications
    }

    override fun init() {
        layout.addTitleHeader(title, font)
        val details = layout.addToContents(LinearLayout.vertical().spacing(8))


        val footerButtons = layout.addToFooter(LinearLayout.horizontal().spacing(4))
        footerButtons.addChild(
            Button.builder(
                CommonComponents.GUI_DONE,
                { button -> this@NotificationScreen.onClose() }
            ).width(98).build()
        )
        footerButtons.addChild(
            Button.builder(
                Component.literal("Clear"),
                { button -> }
            ).width(98).build()
        )

        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        layout.arrangeElements()
    }

    override fun onClose() {
        minecraft?.setScreen(parent)
    }
}