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
    private val categories: Map<Text, Screen?> = mapOf(
        Text.translatable("kassette.config.button.providers") to ProvidersScreen(this)
    )

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val sections = layout.addBody(ConfigCategoryListWidget(categories))
        val sectionButtons = DirectionalLayoutWidget.vertical().spacing(8)
        sectionButtons.mainPositioner.alignHorizontalCenter()
        val banner = sectionButtons.add(
            IconWidget.create(
                200, 50, Identifier.of("kassette", "textures/gui/under_construction_banner.png"),
                200, 50
            )
        )
        banner.setTooltip(Tooltip.of(Text.translatable("kassette.config.tooltip.disclaimer")))

        sectionButtons.add(
            ButtonWidget.builder(
                Text.translatable("kassette.config.button.openfile"),
                { button -> Util.getOperatingSystem().open(FabricLoader.getInstance().configDir.resolve("kassette.json")) }
            ).width(200).build()
        )
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
        sectionButtons.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.help"),
                HelpScreen(this)
            )
        )

        sectionButtons.refreshPositions()

        layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(200).build()
        )

        if (!Kassette.errors.isEmpty()) {
            sectionButtons.add(
                KassetteUtils.createButtonToScreen(
                    Text.translatable("kassette.config.button.errors"),
                    ErrorScreen(this, Kassette.errors)
                )
            )
        }

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

    inner class ConfigCategoryListWidget: ElementListWidget<ConfigScreenListEntry> {
        constructor(categories: Map<Text, Screen?>) : super(
            this@ConfigScreen.client, this@ConfigScreen.width,
            this@ConfigScreen.layout.contentHeight, this@ConfigScreen.layout.headerHeight, 20
        ) {
            addEntry(BannerImage())
            addEntry(
                ConfigCategory(
                    ButtonWidget.builder(
                        Text.translatable("kassette.config.button.openfile"),
                        { button -> Util.getOperatingSystem().open(FabricLoader.getInstance().configDir.resolve("kassette.json")) }
                    ).width(200).build()
                )
            )
            categories.forEach { msg, dest ->
                addEntry(ConfigCategory(msg, dest))
            }
        }


    }

    inner class BannerImage: ConfigScreenListEntry {
        val bannerLocation: Identifier = Identifier.of("kassette", "textures/gui/under_construction_banner.png")
        val banner: IconWidget = IconWidget.create(
            200, 50, bannerLocation,
            200, 50
        )

        constructor()

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
            button.setPosition(20, 20)
            button.render(context, mouseX, mouseY, tickProgress)
        }

        override fun selectableChildren(): List<Selectable?>? {
            return listOf(button)
        }

        override fun children(): List<Element?>? {
            return listOf(button)
        }
    }

    abstract inner class ConfigScreenListEntry: ElementListWidget.Entry<ConfigScreenListEntry>() {

    }
}