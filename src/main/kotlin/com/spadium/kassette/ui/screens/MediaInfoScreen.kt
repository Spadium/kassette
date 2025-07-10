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
    private var savedInfo = MediaManager.provider.info
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
        val previousTrackButton = gridAdder.add(
            TextIconButtonWidget.builder(
                Text.empty(),
                { button -> MediaManager.provider.sendCommand("previousTrack", null) },
                true
            ).texture(
                Identifier.of("kassette", "previous"),
                16, 16
            ).width(20).build()
        )
        previousTrackButton.active = MediaManager.provider.availableCommands.contains("previousTrack")
        previousTrackButton.setTooltip(Tooltip.of(Text.literal("Previous Track")))

        val playPauseButton = gridAdder.add(
            TextIconButtonWidget.builder(
                Text.empty(),
                { button -> MediaManager.provider.sendCommand("togglePlay", null) },
                true
            ).texture(
                when (MediaManager.provider.info.state) {
                    MediaManager.MediaState.PLAYING -> Identifier.of("kassette", "pause")
                    MediaManager.MediaState.PAUSED -> Identifier.of("kassette", "play")
                    else -> Identifier.of("kassette", "loading")
                }, 16, 16
            ).width(20).build()
        )
        playPauseButton.active = MediaManager.provider.availableCommands.contains("togglePlay")
        playPauseButton.setTooltip(Tooltip.of(Text.literal("Play/Pause")))

        val nextTrackButton = gridAdder.add(
            TextIconButtonWidget.builder(
                Text.empty(),
                { button -> MediaManager.provider.sendCommand("nextTrack", null) },
                true
            ).texture(
                Identifier.of("kassette", "next"),
                16, 16
            ).width(20).build()
        )
        nextTrackButton.active = MediaManager.provider.availableCommands.contains("nextTrack")
        nextTrackButton.setTooltip(Tooltip.of(Text.literal("Next Track")))

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

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)
        context?.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette", "textures/gui/info_background.png"),
            centeredX, centeredY, 0f, 0f, screenWidth, screenHeight, screenWidth, screenHeight
        )
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        if (savedInfo != MediaManager.provider.info) {
            clearAndInit()
            println("different infos!")
        }
        context?.drawText(textRenderer, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    override fun shouldPause(): Boolean {
        return false
    }
}