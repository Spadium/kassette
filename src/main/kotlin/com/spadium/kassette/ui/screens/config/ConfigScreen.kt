package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.util.KassetteUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.Overlay
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.ElementListWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.ScrollableLayoutWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class ConfigScreen : Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this)

    private lateinit var sections: ConfigCategoryListWidget

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        sections = ConfigCategoryListWidget()
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

    inner class ConfigCategoryListWidget: ElementListWidget<ConfigScreenListEntry> {
        private val entries: List<ConfigScreenListEntry> = listOf(
            BannerImage(),
            ConfigCategory(
                ButtonWidget.builder(
                    Text.translatable("kassette.config.button.openfile"),
                    { button -> Util.getOperatingSystem().open(FabricLoader.getInstance().configDir.resolve("kassette.json")) }
                ).width(200).build()
            ),
            ConfigCategory(
                Text.translatable("kassette.config.button.providers"),
                ProvidersScreen(this@ConfigScreen)
            ),
            ConfigCategory(
                Text.translatable("kassette.config.button.hud"),
                null
            ),
            ConfigCategory(
                Text.translatable("kassette.config.button.about"),
                AboutScreen(this@ConfigScreen)
            ),
            ConfigCategory(
                Text.translatable("kassette.config.button.help"),
                HelpScreen(this@ConfigScreen)
            )
        )

        constructor() : super(
            this@ConfigScreen.client, this@ConfigScreen.width,
            this@ConfigScreen.layout.contentHeight, this@ConfigScreen.layout.headerHeight, 25
        ) {
            entries.forEach { entry ->
                addEntry(entry)
            }

            if (!Kassette.errors.isEmpty()) {
                addEntry(
                    ConfigCategory(
                        Text.translatable("kassette.config.button.errors"),
                        ErrorScreen(this@ConfigScreen, Kassette.errors)
                    )
                )
            }
        }

        inner class BannerImage : ConfigScreenListEntry() {
            val bannerLocation: Identifier = Identifier.of("kassette", "textures/gui/under_construction_banner.png")
            val banner: IconWidget = IconWidget.create(
                200, 50, bannerLocation,
                200, 50
            )

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
//            val bannerX = (this@ConfigCategoryListWidget.width / 2) - (banner.width / 2)
                val bannerX = this@ConfigCategoryListWidget.scrollbarX - banner.width - 10
                banner.setPosition(bannerX, y)
                banner.render(context, mouseX, mouseY, tickProgress)
            }

            override fun selectableChildren(): List<Selectable?>? {
                return listOf(banner)
            }

            override fun children(): List<Element?>? {
                return listOf(banner)
            }

        }

        inner class ConfigCategory: ConfigScreenListEntry {
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
    }

    abstract inner class ConfigScreenListEntry: ElementListWidget.Entry<ConfigScreenListEntry>() {

    }
}