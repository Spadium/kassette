package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.widgets.LayoutListWidget
import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ProvidersScreen: Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this, 32)
    private lateinit var sections: LayoutListWidget

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)

        val sectionButtons = DirectionalLayoutWidget.vertical().spacing(8)
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.currentprovider"),
                getScreenForProvider()
            )
        )
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.spotify"),
                SpotifyScreen(this)
            )
        )
        sectionButtons.refreshPositions()
        sections = LayoutListWidget(
            client, sectionButtons,
            this, layout,
        )
        layout.addBody(sections)

        layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(200).build()
        )

        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        refreshWidgetPositions()
    }

    private fun getScreenForProvider(): Screen? {
        val provider = MediaManager.provider
        return when (provider::class.simpleName) {
            "SpotifyProvider" -> SpotifyScreen(this)
            else -> null
        }
    }

    override fun refreshWidgetPositions() {
        sections.position(width, layout)
        layout.refreshPositions()
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
//        clearAndInit()
    }

    override fun close() {
        MainConfig.Instance.save()
        this.client!!.setScreen(parent)
    }
}