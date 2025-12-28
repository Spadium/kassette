package me.spadium.kassette.ui.screens.config

import me.spadium.kassette.config.MainConfig
import me.spadium.kassette.media.MediaManager
import me.spadium.kassette.ui.widgets.LayoutListWidget
import me.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ProvidersScreen: Screen {
    private val parent: Screen?
    private val layout = HeaderAndFooterLayout(this, 32)
    private lateinit var sections: LayoutListWidget

    constructor(parent: Screen?) : super(Component.translatable("kassette.config.providers.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addTitleHeader(title, font)

        val sectionButtons = LinearLayout.vertical().spacing(8)
        sectionButtons.addChild(
            KassetteUtils.createButtonToScreen(
                Component.translatable("kassette.config.button.currentprovider"),
                getScreenForProvider()
            )
        )
        sectionButtons.addChild(
            KassetteUtils.createButtonToScreen(
                Component.translatable("kassette.config.button.spotify"),
                SpotifyScreen(this)
            )
        )
        sectionButtons.arrangeElements()
        sections = LayoutListWidget(
            minecraft, sectionButtons,
            this, layout,
        )
        layout.addToContents(sections)

        layout.addToFooter(
            Button.builder(
                CommonComponents.GUI_DONE,
                { button -> onClose() }
            ).width(200).build()
        )

        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        repositionElements()
    }

    private fun getScreenForProvider(): Screen? {
        val provider = MediaManager.provider
        return when (provider::class.simpleName) {
            "SpotifyProvider" -> SpotifyScreen(this)
            else -> null
        }
    }

    override fun repositionElements() {
        sections.updateSize(width, layout)
        layout.arrangeElements()
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
//        clearAndInit()
    }

    override fun onClose() {
        MainConfig.Instance.save()
        this.minecraft!!.setScreen(parent)
    }
}