package com.spadium.kassette.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class KassetteUtils {
    companion object {
        fun createButtonToScreen(message: Text, screen: Screen): ButtonWidget {
            return ButtonWidget.builder(
                message,
                { button -> MinecraftClient.getInstance().setScreen(screen) }
            ).width(200).build()
        }
    }
}