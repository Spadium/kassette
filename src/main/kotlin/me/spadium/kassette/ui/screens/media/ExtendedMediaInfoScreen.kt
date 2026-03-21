package me.spadium.kassette.ui.screens.media

import me.spadium.kassette.media.MediaInfo
import me.spadium.kassette.media.MediaManager
import me.spadium.kassette.ui.widgets.MarqueeTextWidget
import me.spadium.kassette.ui.widgets.ProgressBarWidget
import me.spadium.kassette.util.KassetteUtils
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

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
        val containerWidget = LinearLayout(
            0, 0,
            LinearLayout.Orientation.VERTICAL
        ).spacing(4)
        val containerPositioner = containerWidget.defaultCellSetting()
        containerPositioner.alignHorizontallyCenter()
        val firstLine = MarqueeTextWidget(
            Component.literal(MediaManager.provider.info.title),
            font, 100
        )
        val secondLine = MarqueeTextWidget(
            Component.literal(MediaManager.provider.info.album),
            font, 100
        )
        val thirdLine = MarqueeTextWidget(
            Component.literal(MediaManager.provider.info.artist),
            font, 100
        )
        firstLine.width = 100
        secondLine.width = 100
        thirdLine.width = 100

        progressBar.height = 8
        progressBar.width = 100

        containerWidget.addChild(
            ImageWidget.texture(
                48, 48,
                Identifier.fromNamespaceAndPath("kassette", "coverart_large"),
                48, 48
            )
        )
        val infoLayout: LinearLayout = containerWidget.addChild(LinearLayout(0, 0,
            LinearLayout.Orientation.VERTICAL))
        val buttonsLayout: LinearLayout = containerWidget.addChild(LinearLayout(0, 0,
            LinearLayout.Orientation.HORIZONTAL))
        buttonsLayout.spacing(4)
        infoLayout.spacing(2)
        infoLayout.addChild(firstLine)
        infoLayout.addChild(secondLine)
        infoLayout.addChild(thirdLine)
        infoLayout.addChild(progressBar)
        val previousTrackButton = buttonsLayout.addChild(
            SpriteIconButton.builder(
                Component.empty(),
                { button ->
                    MediaManager.provider.sendCommand("previousTrack", null)
                    button.isFocused = false
                },
                true
            ).sprite(
                Identifier.fromNamespaceAndPath("kassette", "previous"),
                16, 16
            ).width(20).build()
        )
        previousTrackButton.active = MediaManager.provider.availableCommands.contains("previousTrack")
        previousTrackButton.setTooltip(Tooltip.create(Component.literal("Previous Track")))

        val playPauseButton = buttonsLayout.addChild(
            SpriteIconButton.builder(
                Component.empty(),
                { _ ->
                    MediaManager.provider.sendCommand("togglePlay", null)
                },
                true
            ).sprite(
                when (MediaManager.provider.info.state) {
                    MediaManager.MediaState.PLAYING -> Identifier.fromNamespaceAndPath("kassette", "pause")
                    MediaManager.MediaState.PAUSED -> Identifier.fromNamespaceAndPath("kassette", "play")
                    else -> Identifier.fromNamespaceAndPath("kassette", "loading")
                }, 16, 16
            ).width(20).build()
        )
        playPauseButton.active = MediaManager.provider.availableCommands.contains("togglePlay")
        playPauseButton.setTooltip(Tooltip.create(Component.literal("Play/Pause")))

        val nextTrackButton = buttonsLayout.addChild(
            SpriteIconButton.builder(
                Component.empty(),
                { _ -> MediaManager.provider.sendCommand("nextTrack", null) },
                true
            ).sprite(
                Identifier.fromNamespaceAndPath("kassette", "next"),
                16, 16
            ).width(20).build()
        )
        nextTrackButton.active = MediaManager.provider.availableCommands.contains("nextTrack")
        nextTrackButton.setTooltip(Tooltip.create(Component.literal("Next Track")))

        containerWidget.visitWidgets { widget ->
            addRenderableOnly(widget)
        }
        containerWidget.arrangeElements()
        containerWidget.x = ((width / 2) - (containerWidget.width / 2)) - 50
        containerWidget.y = (height / 2) - (containerWidget.height / 2) + 5
        containerWidget.arrangeElements()
    }

    override fun renderBackground(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks)
        context.blit(
            RenderPipelines.GUI_TEXTURED,
            Identifier.fromNamespaceAndPath("kassette", "textures/gui/ext_info_background.png"),
            centeredX, centeredY, 0f, 0f, screenWidth, screenHeight, screenWidth, screenHeight
        )
    }

    override fun render(context: GuiGraphics, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        if (savedInfo != MediaManager.provider.info) {
            savedInfo = MediaManager.provider.info.copy()
            progressBarFgColor = KassetteUtils.getAverageColor(savedInfo.coverArt)
            progressBar.foregroundColor = progressBarFgColor
            rebuildWidgets()
        }
        context.drawString(font, title, centeredX + 6, centeredY + 6, 0xff3f3f3f.toInt(), false)
        super.render(context, mouseX, mouseY, deltaTicks)
    }

    private fun renderTab() {

    }

    override fun isPauseScreen(): Boolean {
        return false
    }
}