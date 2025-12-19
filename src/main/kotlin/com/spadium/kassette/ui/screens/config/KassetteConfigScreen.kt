package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.ui.widgets.LayoutListWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class KassetteConfigScreen : Screen {
    private val parent: Screen?
    private val layout = HeaderAndFooterLayout(this, 32)
    private lateinit var sections: LayoutListWidget

    constructor(parent: Screen?) : super(Component.translatable("kassette.config.kassette.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addTitleHeader(title, font)

        val sectionButtons = LinearLayout.vertical().spacing(8)



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

    override fun repositionElements() {
        sections.updateSize(width, layout)
        layout.arrangeElements()
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    override fun onClose() {
        MainConfig.Instance.save()
        this.minecraft.setScreen(parent)
    }
}