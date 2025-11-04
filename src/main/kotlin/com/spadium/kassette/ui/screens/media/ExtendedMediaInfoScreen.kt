package com.spadium.kassette.ui.screens.media

import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.widgets.MarqueeTextWidget
import com.spadium.kassette.ui.widgets.ProgressBarWidget
import com.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class ExtendedMediaInfoScreen : Screen {
    private val screenWidth = 256
    private val screenHeight = 150
    private var centeredX = 0
    private var centeredY = 0
    private var savedInfo: MediaInfo = MediaInfo(1L, 0L, "", "", "", MediaManager.getDefaultCoverArt(), "")
    private val progressBar = ProgressBarWidget(savedInfo.currentPosition, savedInfo.maximumTime)
    constructor() : super(Component.translatable("kassette.popup.title", MediaManager.provider.info.provider))
    private var progressBarFgColor = 0xFF0FF00

    override fun init() {
        centeredX = (this.width / 2) - (screenWidth / 2)
        centeredY = (this.height / 2) - (screenHeight / 2)
        val containerWidget = DirectionalLayoutWidget(
            0, 0,
            DirectionalLayoutWidget.DisplayAxis.VERTICAL
        ).spacing(4)
        val containerPositioner = containerWidget.mainPositioner
        containerPositioner.alignHorizontalCenter()
        val firstLine = MarqueeTextWidget(
            Text.literal(MediaManager.provider.info.title),
            textRenderer, 100
        ).alignLeft()
        val secondLine = MarqueeTextWidget(
            Text.literal(MediaManager.provider.info.album),
            textRenderer, 100
        ).alignLeft()
        val thirdLine = MarqueeTextWidget(
            Text.literal(MediaManager.provider.info.artist),
            textRenderer, 100
        ).alignLeft()
        firstLine.width = 100
        secondLine.width = 100
        thirdLine.width = 100

        progressBar.height = 8
        progressBar.width = 100

        containerWidget.add(
            IconWidget.create(
                48, 48,
                Identifier.of("kassette", "coverart_large"),
                48, 48
            )
        )
        val infoLayout: DirectionalLayoutWidget = containerWidget.add(DirectionalLayoutWidget(0, 0,
            DirectionalLayoutWidget.DisplayAxis.VERTICAL))
        val buttonsLayout: DirectionalLayoutWidget = containerWidget.add(DirectionalLayoutWidget(0, 0,
            DirectionalLayoutWidget.DisplayAxis.HORIZONTAL))
        buttonsLayout.spacing(4)
        infoLayout.spacing(2)
        infoLayout.add(firstLine)
        infoLayout.add(secondLine)
        infoLayout.add(thirdLine)
        infoLayout.add(progressBar)
        val previousTrackButton = buttonsLayout.add(
            TextIconButtonWidget.builder(
                Text.empty(),
                { button ->
                    MediaManager.provider.sendCommand("previousTrack", null)
                    button.isFocused = false
                },
                true
            ).texture(
                Identifier.of("kassette", "previous"),
                16, 16
            ).width(20).build()
        )
        previousTrackButton.active = MediaManager.provider.availableCommands.contains("previousTrack")
        previousTrackButton.setTooltip(Tooltip.of(Text.literal("Previous Track")))

        val playPauseButton = buttonsLayout.add(
            TextIconButtonWidget.builder(
                Text.empty(),
                { button ->
                    MediaManager.provider.sendCommand("togglePlay", null)
                },
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

        val nextTrackButton = buttonsLayout.add(
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
        containerWidget.refreshPositions()
        containerWidget.x = ((width / 2) - (containerWidget.width / 2)) - 50
        containerWidget.y = (height / 2) - (containerWidget.height / 2) + 5
        containerWidget.refreshPositions()
    }

    override fun renderBackground(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)
        context?.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            Identifier.of("kassette", "textures/gui/ext_info_background.png"),
            centeredX, centeredY, 0f, 0f, screenWidth, screenHeight, screenWidth, screenHeight
        )
    }

    override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        if (savedInfo != MediaManager.provider.info) {
            savedInfo = MediaManager.provider.info.copy()
            progressBarFgColor = KassetteUtils.getAverageColor(savedInfo.coverArt)
            progressBar.foregroundColor = progressBarFgColor
            clearAndInit()
        }
        context?.drawText(textRenderer, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    private fun renderTab() {

    }

    override fun shouldPause(): Boolean {
        return false
    }
}