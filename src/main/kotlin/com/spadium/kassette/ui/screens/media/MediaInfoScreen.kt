package com.spadium.kassette.ui.screens.media

import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class MediaInfoScreen : Screen {
    private val screenWidth = 256
    private val screenHeight = 128
    private var centeredX = 0
    private var centeredY = 0
    private var savedInfo: MediaInfo = MediaInfo(1L, 0L, "", "", "", MediaManager.getDefaultCoverArt(), "")
    constructor() : super(Component.translatable("kassette.popup.title", MediaManager.provider.info.provider))

    override fun init() {
        centeredX = (this.width / 2) - (screenWidth / 2)
        centeredY = (this.height / 2) - (screenHeight / 2)
        val containerWidget = LinearLayout(
            0, 0,
            LinearLayout.Orientation.HORIZONTAL
        ).spacing(8)
        val firstLine = StringWidget(
            Component.literal(MediaManager.provider.info.title),
            font
        ).alignLeft()
        val secondLine = StringWidget(
            Component.literal(MediaManager.provider.info.album),
            font
        ).alignLeft()
        val thirdLine = StringWidget(
            Component.literal(MediaManager.provider.info.artist),
            font
        ).alignLeft()
        firstLine.width = 150
        secondLine.width = 150
        thirdLine.width = 150

        containerWidget.addChild(
            ImageWidget.texture(
                64, 64,
                ResourceLocation.fromNamespaceAndPath("kassette", "coverart_large"),
                64, 64
            )
        )
        val gridWidget: GridLayout = containerWidget.addChild(GridLayout())
        val gridAdder: GridLayout.RowHelper = gridWidget.createRowHelper(3)
        gridWidget.spacing(4)
        gridAdder.addChild(
            firstLine, 3
        )
        gridAdder.addChild(
            secondLine, 3
        )
        gridAdder.addChild(
            thirdLine, 3
        )
        val previousTrackButton = gridAdder.addChild(
            SpriteIconButton.builder(
                Component.empty(),
                { button ->
                    MediaManager.provider.sendCommand("previousTrack", null)
                    button.isFocused = false
                },
                true
            ).sprite(
                ResourceLocation.fromNamespaceAndPath("kassette", "previous"),
                16, 16
            ).width(20).build()
        )
        previousTrackButton.active = MediaManager.provider.availableCommands.contains("previousTrack")
        previousTrackButton.setTooltip(Tooltip.create(Component.literal("Previous Track")))

        val playPauseButton = gridAdder.addChild(
            SpriteIconButton.builder(
                Component.empty(),
                { button ->
                    MediaManager.provider.sendCommand("togglePlay", null)
                },
                true
            ).sprite(
                when (MediaManager.provider.info.state) {
                    MediaManager.MediaState.PLAYING -> ResourceLocation.fromNamespaceAndPath("kassette", "pause")
                    MediaManager.MediaState.PAUSED -> ResourceLocation.fromNamespaceAndPath("kassette", "play")
                    else -> ResourceLocation.fromNamespaceAndPath("kassette", "loading")
                }, 16, 16
            ).width(20).build()
        )
        playPauseButton.active = MediaManager.provider.availableCommands.contains("togglePlay")
        playPauseButton.setTooltip(Tooltip.create(Component.literal("Play/Pause")))

        val nextTrackButton = gridAdder.addChild(
            SpriteIconButton.builder(
                Component.empty(),
                { button -> MediaManager.provider.sendCommand("nextTrack", null) },
                true
            ).sprite(
                ResourceLocation.fromNamespaceAndPath("kassette", "next"),
                16, 16
            ).width(20).build()
        )
        nextTrackButton.active = MediaManager.provider.availableCommands.contains("nextTrack")
        nextTrackButton.setTooltip(Tooltip.create(Component.literal("Next Track")))

        containerWidget.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        gridWidget.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        containerWidget.arrangeElements()
        containerWidget.x = (width / 2) - (containerWidget.width / 2)
        containerWidget.y = (height / 2) - (containerWidget.height / 2)
        containerWidget.arrangeElements()
        gridWidget.arrangeElements()
    }

    override fun renderBackground(context: GuiGraphics?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)
        context?.blit(
            RenderPipelines.GUI_TEXTURED,
            ResourceLocation.fromNamespaceAndPath("kassette", "textures/gui/info_background.png"),
            centeredX, centeredY, 0f, 0f, screenWidth, screenHeight, screenWidth, screenHeight
        )
    }

    override fun render(context: GuiGraphics?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        if (savedInfo != MediaManager.provider.info) {
            savedInfo = MediaManager.provider.info.copy()
            rebuildWidgets()
        }
        context?.drawString(font, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    override fun isPauseScreen(): Boolean {
        return false
    }
}