package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.config.Config
import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ConfigScreen : Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this, 64, 32)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val sectionButtons = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.providers"),
                ProvidersScreen(this)
            )
        )
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.hud"),
                null
            )
        )
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
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
        Config.Instance.save()
        this.client!!.setScreen(parent)
    }
}