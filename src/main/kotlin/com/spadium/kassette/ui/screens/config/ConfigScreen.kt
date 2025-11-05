package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.ui.widgets.LayoutListWidget
import com.spadium.kassette.util.KassetteUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.Util
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ConfigScreen : Screen {
    val parent: Screen?
    val layout = HeaderAndFooterLayout(this, 32)
    lateinit var sections: LayoutListWidget

    constructor(parent: Screen?) : super(Component.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val buttonList: LinearLayout = LinearLayout.vertical().spacing(2)
        buttonList.addChild(
            ImageWidget.texture(
                200, 50, ResourceLocation.fromNamespaceAndPath("kassette", "textures/gui/under_construction_banner.png"),
                200, 50
            )
        )
        buttonList.addChild(
            Button.builder(
                Component.translatable("kassette.config.button.openfile"),
                { button ->
                    Util.getPlatform().openPath(Config.configPath)
                }
            ).width(200).build()
        )
        buttonList.addChild(
            KassetteUtils.createButtonToScreen(
                Component.translatable("kassette.config.button.providers"),
                ProvidersScreen(this)
            )
        )
        buttonList.addChild(
            KassetteUtils.createButtonToScreen(
                Component.translatable("kassette.config.button.hud"),
                null
            )
        )
        buttonList.addChild(
            KassetteUtils.createButtonToScreen(
                Component.translatable("kassette.config.button.help"),
                HelpScreen(this)
            )
        )
        buttonList.addChild(
            KassetteUtils.createButtonToScreen(
                Component.translatable("kassette.config.button.about"),
                AboutScreen(this)
            )
        )
            buttonList.addChild(
                KassetteUtils.createButtonToScreen(
                    Component.translatable("kassette.config.button.notifications"),
                    NotificationScreen(this, Kassette.notifications)
                )
            )
        buttonList.arrangeElements()
        sections = LayoutListWidget(
            minecraft, buttonList, this, layout
        )
        layout.addToContents(sections)

        layout.addToFooter(
            Button.builder(
                CommonComponents.GUI_DONE,
                { button -> close() }
            ).width(200).build()
        )

        layout.visitWidgets { widget ->
            addRenderableOnly(widget)
        }
        refreshWidgetPositions()
    }

    override fun refreshWidgetPositions() {
        layout.refreshPositions()
        sections.position(width, layout)
    }

    override fun close() {
        MainConfig.Instance.save()
        this.client!!.setScreen(parent)
    }
}