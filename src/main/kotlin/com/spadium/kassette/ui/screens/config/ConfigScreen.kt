package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.util.KassetteUtils
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Overlay
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util

class ConfigScreen : Screen {
    private val parent: Screen?
    private val layout = ThreePartsLayoutWidget(this)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.title")) {
        this.parent = parent
    }

    override fun init() {
        layout.addHeader(title, textRenderer)
        val sectionButtons = layout.addBody(DirectionalLayoutWidget.vertical().spacing(8))
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
}