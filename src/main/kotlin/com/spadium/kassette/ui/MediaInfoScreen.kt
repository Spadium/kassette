package com.spadium.kassette.ui

import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.widgets.IconButtonWidget
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
import java.awt.Button

class MediaInfoScreen(title: Text) : Screen(title) {
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
        val gridAdder: GridWidget.Adder = gridWidget.createAdder(5)
        gridWidget.setSpacing(8)
        gridAdder.add(
            TextWidget(
                Text.literal("${MediaManager.info.title} - ${MediaManager.info.artist}"),
                textRenderer
            ), 5
        )
        gridAdder.add(
            TextWidget(
                Text.literal(MediaManager.info.album),
                textRenderer
            ), 5
        )
        gridAdder.add(EmptyWidget(1, 2), 5)
        AvailableButtons.entries.forEach {
            gridAdder.add(
                TextIconButtonWidget.builder(
                    Text.empty(),
                    it.onPress,
                    true
                ).texture(it.sprite, 16, 16).width(20).build()
            )
        }
        gridWidget.forEachChild { widget ->
            addDrawableChild(widget)
        }
        gridWidget.refreshPositions()
    }

    private enum class AvailableButtons(val sprite: Identifier, val onPress: ButtonWidget.PressAction) {
        PLAY(Identifier.of("kassette", "play"), { button -> println("play")}),
        PAUSE(Identifier.of("kassette", "pause"), { button -> println("pause")}),
        NEXT(Identifier.of("kassette", "other"), { button -> println("next")}),
        PREVIOUS(Identifier.of("kassette", "other"), { button -> println("previous")}),
        CLOSE(Identifier.of("kassette", "other"), { button -> MinecraftClient.getInstance().setScreen(null) })
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
        super.render(context, mouseX, mouseY, deltaTicks)
        context?.drawText(textRenderer, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        context?.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette", "coverart"),
            centeredX + 16, centeredY + 32, 0f, 0f, 64, 64, 64, 64
        )
        context?.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED, MediaManager.provider.state.texture,
            centeredX + textRenderer.getWidth(title) + 8, centeredY + 6,
            8,8, Colors.BLACK
        )
    }

    override fun shouldPause(): Boolean {
        return false
    }
}