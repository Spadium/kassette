package com.spadium.kassette.ui.screens

import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.media.MediaProvider
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.*
import net.minecraft.text.Text
import net.minecraft.util.Colors
import net.minecraft.util.Identifier
import javax.print.attribute.standard.Media

class MediaInfoScreen : Screen {
    private val screenWidth = 256
    private val screenHeight = 128
    private var centeredX = 0
    private var centeredY = 0

    constructor() : super(Text.translatable("kassette.popup.title", MediaManager.provider.info.provider))

    override fun init() {
        centeredX = (this.width / 2) - (screenWidth / 2)
        centeredY = (this.height / 2) - (screenHeight / 2)
        val containerWidget = DirectionalLayoutWidget(
            0, 0,
            DirectionalLayoutWidget.DisplayAxis.HORIZONTAL
        ).spacing(8)
        val firstLine = TextWidget(
            Text.literal(MediaManager.provider.info.title),
            textRenderer
        ).alignLeft()
        val secondLine = TextWidget(
            Text.literal(MediaManager.provider.info.album),
            textRenderer
        ).alignLeft()
        val thirdLine = TextWidget(
            Text.literal(MediaManager.provider.info.artist),
            textRenderer
        ).alignLeft()
        firstLine.width = 150
        secondLine.width = 150
        thirdLine.width = 150

        containerWidget.add(
            IconWidget.create(
                64, 64,
                Identifier.of("kassette", "coverart_large"),
                64, 64
            )
        )
        val gridWidget: GridWidget = containerWidget.add(GridWidget())
        val gridAdder: GridWidget.Adder = gridWidget.createAdder(3)
        gridWidget.setSpacing(4)
        gridAdder.add(
            firstLine, 3
        )
        gridAdder.add(
            secondLine, 3
        )
        gridAdder.add(
            thirdLine, 3
        )
        AvailableButtons.entries.forEach {
            val button = TextIconButtonWidget.builder(
                Text.empty(),
                it.onPress,
                true
            ).texture(it.sprite, 16, 16).width(20).build()
            button.active = it.isActive
            button.setTooltip(Tooltip.of(it.tooltip))
            gridAdder.add(
                button
            )
        }
        containerWidget.forEachChild { widget ->
            addDrawableChild(widget)
        }
        gridWidget.forEachChild { widget ->
            addDrawableChild(widget)
        }
        containerWidget.refreshPositions()
        containerWidget.x = (width / 2) - (containerWidget.width / 2)
        containerWidget.y = (height / 2) - (containerWidget.height / 2)
        containerWidget.refreshPositions()
        gridWidget.refreshPositions()
    }

    private enum class AvailableButtons(
        val sprite: Identifier, val onPress: ButtonWidget.PressAction,
        val tooltip: Text, val isActive: Boolean
    ) {
        PREVIOUS(
            Identifier.of("kassette", "previous"),
            { button -> MediaManager.provider.sendCommand("previousTrack", null) },
            Text.literal("Previous Track"), MediaManager.provider.availableCommands.contains("previousTrack")
        ),
        PLAY_PAUSE(
            when (MediaManager.provider.info.state) {
                MediaManager.MediaState.PLAYING -> Identifier.of("kassette", "pause")
                MediaManager.MediaState.PAUSED -> Identifier.of("kassette", "play")
                else -> Identifier.of("kassette", "loading")
            },
            { button -> MediaManager.provider.sendCommand("togglePlay", null) },
            Text.literal("Play/Pause"), MediaManager.provider.availableCommands.contains("togglePlay")
        ),
        NEXT(
            Identifier.of("kassette", "next"),
            { button -> MediaManager.provider.sendCommand("nextTrack", null) },
            Text.literal("Next Track"), MediaManager.provider.availableCommands.contains("nextTrack")
        )
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
        clearAndInit()
        context?.drawText(textRenderer, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    override fun shouldPause(): Boolean {
        return false
    }
}