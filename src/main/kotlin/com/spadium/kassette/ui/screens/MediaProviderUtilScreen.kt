package com.spadium.kassette.ui.screens

import com.spadium.kassette.media.MediaManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.EmptyWidget
import net.minecraft.client.gui.widget.GridWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.TextIconButtonWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Identifier

class MediaProviderUtilScreen(title: Text): Screen(title) {
    private val screenWidth = 256
    private val screenHeight = 128
    private var centeredX = 0
    private var centeredY = 0


    override fun init() {
        centeredX = (this.width / 2) - (screenWidth / 2)
        centeredY = (this.height / 2) - (screenHeight / 2)
        val gridWidget: GridWidget = GridWidget(
            centeredX + 80 + 4,
            centeredY + 32
        )
        val gridAdder: GridWidget.Adder = gridWidget.createAdder(4)
        gridWidget.setSpacing(8)
        gridAdder.add(IconWidget.create(
            32, 32, Identifier.of("kassette", "coverart"), 32 , 32
        ))

        gridWidget.forEachChild { widget ->
            addDrawableChild(widget)
        }
        gridWidget.refreshPositions()
    }

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)

        context?.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette", "textures/gui/info_background.png"),
            centeredX, centeredY, 0f, 0f, screenWidth, screenHeight, screenWidth, screenHeight
        )
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        context?.drawText(textRenderer, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        context?.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED, MediaManager.provider.state.texture,
            centeredX + textRenderer.getWidth(title) + 8, centeredY + 6,
            8,8, Colors.BLACK
        )
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    override fun shouldPause(): Boolean {
        return false
    }
}