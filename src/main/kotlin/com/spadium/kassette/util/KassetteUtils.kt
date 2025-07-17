package com.spadium.kassette.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class KassetteUtils {
    companion object {
        fun createButtonToScreen(message: Text, screen: Screen?): ButtonWidget {
            val button = ButtonWidget.builder(
                message,
                { button ->
                    if (screen != null) {
                        MinecraftClient.getInstance().setScreen(screen)
                    }
                }
            ).width(200).build()
            button.active = (screen != null)

            return button
        }

        inline fun createBooleanOptionButton(crossinline setter: (Boolean) -> Unit, value: Boolean): ButtonWidget {
            var valueToReturn = value

            val button = ButtonWidget.builder(
                Text.translatable("kassette.config.button.generic.boolean.$value"),
                { button ->
                    valueToReturn = !valueToReturn
                    setter(valueToReturn)
                    button.message = Text.translatable("kassette.config.button.generic.boolean.$valueToReturn")
                }
            ).width(100).build()
            return button
        }
    }
}