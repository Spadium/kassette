package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.util.KassetteUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.*
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class ConfigCategoryListWidget : ElementListWidget<ConfigCategoryListWidget.ConfigScreenListEntry> {
    val bannerLocation: Identifier = Identifier.of("kassette", "textures/gui/under_construction_banner.png")
    val buttonList: DirectionalLayoutWidget = DirectionalLayoutWidget.vertical().spacing(2)

    constructor(parent: ConfigScreen) : super(
        parent._client, parent.width,
        parent.layout.contentHeight, parent.layout.headerHeight, 184
    ) {
        buttonList.add(
            IconWidget.create(
                200, 50, bannerLocation,
                200, 50
            )
        )
        buttonList.add(
            ButtonWidget.builder(
                Text.translatable("kassette.config.button.openfile"),
                { button ->
                    Util.getOperatingSystem().open(FabricLoader.getInstance().configDir.resolve("kassette.json"))
                }
            ).width(200).build()
        )
        buttonList.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.providers"),
                ProvidersScreen(parent)
            )
        )
        buttonList.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.hud"),
                null
            )
        )
        buttonList.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.help"),
                HelpScreen(parent)
            )
        )
        buttonList.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.about"),
                AboutScreen(parent)
            )
        )

        if (!Kassette.errors.isEmpty()) {
            buttonList.add(
                KassetteUtils.createButtonToScreen(
                    Text.translatable("kassette.config.button.errors"),
                    ErrorScreen(parent, Kassette.errors)
                )
            )
        }
        buttonList.refreshPositions()
        addEntry(LayoutEntry(buttonList))
    }

    inner class LayoutEntry : ConfigScreenListEntry {
        val widget: LayoutWidget

        constructor(widget: LayoutWidget) {
            this.widget = widget
        }

        override fun render(
            context: DrawContext?,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickProgress: Float
        ) {
            widget.setPosition(x, y)
            widget.forEachChild {
                it.render(context, mouseX, mouseY, tickProgress)
            }
        }

        override fun children(): List<Element?>? {
            val children: MutableList<Element> = mutableListOf()
            widget.forEachChild { children.add(it) }
            return children
        }

        override fun selectableChildren(): List<Selectable?>? {
            val children: MutableList<Selectable> = mutableListOf()
            widget.forEachChild {
                if (it is Selectable) {
                    children.add(it)
                }
            }
            return children
        }
    }

    inner class ConfigCategory : ConfigScreenListEntry {
        val button: ButtonWidget

        constructor(text: Text, destination: Screen?) {
            this.button = KassetteUtils.createButtonToScreen(text, destination)
        }

        constructor(button: ButtonWidget) {
            this.button = button
        }

        override fun render(
            context: DrawContext?,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickProgress: Float
        ) {
            val buttonX = this@ConfigCategoryListWidget.scrollbarX - button.width - 10
            button.setPosition(buttonX, y + entryHeight + 15)
            button.render(context, mouseX, mouseY, tickProgress)
        }

        override fun selectableChildren(): List<Selectable?>? {
            return listOf(button)
        }

        override fun children(): List<Element?>? {
            return listOf(button)
        }
    }

    abstract inner class ConfigScreenListEntry : Entry<ConfigScreenListEntry>() {

    }
}