package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.util.KassetteUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class ConfigScreen : Screen {
    val parent: Screen?
    val layout = ThreePartsLayoutWidget(this)
    lateinit var sections: LayoutListWidget

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val buttonList: DirectionalLayoutWidget = DirectionalLayoutWidget.vertical().spacing(2)
        buttonList.add(
            IconWidget.create(
                200, 50, Identifier.of("kassette", "textures/gui/under_construction_banner.png"),
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
                ProvidersScreen(this)
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
                HelpScreen(this)
            )
        )
        buttonList.add(
            KassetteUtils.createButtonToScreen(
                Text.translatable("kassette.config.button.about"),
                AboutScreen(this)
            )
        )
        if (!Kassette.errors.isEmpty()) {
            buttonList.add(
                KassetteUtils.createButtonToScreen(
                    Text.translatable("kassette.config.button.errors"),
                    ErrorScreen(this, Kassette.errors)
                )
            )
        }
        buttonList.refreshPositions()
        sections = LayoutListWidget(
            client, buttonList, this, layout
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

    override fun refreshWidgetPositions() {
        layout.refreshPositions()
        sections.position(width, layout)
    }

    override fun close() {
        Config.Instance.save()
        this.client!!.setScreen(parent)
    }
}