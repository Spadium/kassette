package com.spadium.kassette.ui

import com.spadium.kassette.ui.config.AboutScreen
import com.spadium.kassette.ui.config.ProvidersScreen
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.*
import net.minecraft.client.gui.widget.ButtonWidget.PressAction
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import java.awt.Button

class ConfigScreen : Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this, 64, 32)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        val directionalLayout = layout.addHeader(DirectionalLayoutWidget.vertical().spacing(8))
        directionalLayout.add(
            TextWidget(title, textRenderer),
            Positioner::alignHorizontalCenter
        )

        val sectionButtons = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))
        sectionButtons.add(
            createButtonToScreen(
                Text.translatable("kassette.config.button.providers"),
                ProvidersScreen(this)
            )
        )
        sectionButtons.add(
            createButtonToScreen(
                Text.translatable("kassette.config.button.about"),
                AboutScreen(this)
            )
        )

        layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(200).build()
        )


        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.refreshPositions()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)

    }

    override fun close() {
        this.client!!.setScreen(this.parent)
    }

    private fun createButtonToScreen(message: Text, screen: Screen): ButtonWidget {
        return ButtonWidget.builder(
            message,
            { button -> client!!.setScreen(screen) }
        ).width(200).build()
    }
}