package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.util.ModNotification
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class NotificationScreen: Screen {
    val layout = ThreePartsLayoutWidget(this, 32)
    val parent: Screen?
    var notifications: MutableList<ModNotification>

    constructor(parent: Screen?, notifications: MutableList<ModNotification>) : super(Text.translatable("kassette.notifications.title")) {
        this.parent = parent
        this.notifications = notifications
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val details = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))


        val footerButtons = layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(4))
        footerButtons.add(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(98).build()
        )
        footerButtons.add(
            ButtonWidget.builder(
                Text.literal("Clear"),
                { button -> }
            ).width(98).build()
        )

        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }

    override fun close() {
        client?.setScreen(parent)
    }
}