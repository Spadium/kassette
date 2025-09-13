package com.spadium.kassette.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.texture.NativeImage
import net.minecraft.text.Text
import kotlin.math.floor

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
        // could be more efficient, but i dont really care right now
        fun getAverageColor(image: NativeImage): Int {
            val redMask   = 0x00FF0000
            val greenMask = 0x0000FF00
            val blueMask  = 0x000000FF
            var rowCount = 0
            var sigmaR = 0
            var sigmaG = 0
            var sigmaB = 0
            val width = image.width
            val height = image.height
            val arr = image.copyPixelsArgb()
            for (y in 0..(height-1)) {
                rowCount = y
                for (x in 0..(width-1)) {
                    val color = arr[(rowCount * (width-1)) + x]
                    sigmaR += color.and(redMask) ushr 16
                    sigmaG += color.and(greenMask) ushr 8
                    sigmaB += color.and(blueMask)
                }
            }
            val avgR = floor(sigmaR.toFloat() / arr.size.toFloat()).toInt()
            val avgG = floor(sigmaG.toFloat() / arr.size.toFloat()).toInt()
            val avgB = floor(sigmaB.toFloat() / arr.size.toFloat()).toInt()
            val avgColor: Int = 0xFF000000.toInt().or((avgR shl 16)).or(avgG shl 8).or(avgB)
            return avgColor
        }
    }
}