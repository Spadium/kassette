package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.ui.screens.config.ConfigCategoryListWidget
import com.spadium.kassette.util.KassetteUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Overlay
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.ClickableWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.LayoutWidget
import net.minecraft.client.gui.widget.ScrollableLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.client.gui.widget.Widget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class ConfigScreen : Screen {
    val parent: Screen?
    val layout = ThreePartsLayoutWidget(this)
    internal val _client: MinecraftClient?
        get() {
            return this.client
        }

    private lateinit var sections: ConfigCategoryListWidget

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        sections = ConfigCategoryListWidget(this)
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
        layout.refreshPositions()
    }

    override fun refreshWidgetPositions() {
        layout.refreshPositions()
        sections.position(width, layout)
    }

    override fun close() {
        Config.Instance.save()
        this.client!!.setScreen(parent)
    }
}