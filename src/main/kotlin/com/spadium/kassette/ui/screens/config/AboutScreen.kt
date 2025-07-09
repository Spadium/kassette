package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.ModInfo
import com.spadium.kassette.config.Config
import com.spadium.kassette.util.KassetteUtils
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.font.MultilineText
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.DirectionalLayoutWidget
import net.minecraft.client.gui.widget.GridWidget
import net.minecraft.client.gui.widget.IconWidget
import net.minecraft.client.gui.widget.MultilineTextWidget
import net.minecraft.client.gui.widget.Positioner
import net.minecraft.client.gui.widget.ScrollableLayoutWidget
import net.minecraft.client.gui.widget.ScrollableWidget
import net.minecraft.client.gui.widget.TextWidget
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class AboutScreen: Screen {
    private val parent: Screen?
    @OptIn(ExperimentalSerializationApi::class)
    private val aboutInfo: ModInfo = Json.decodeFromStream(javaClass.getResourceAsStream("/kassetteinfo.json")!!)
    private val dateFormat = LocalDateTime.Format {
        date(LocalDate.Formats.ISO); chars("  ")
        amPmHour(); chars(":"); minute(); chars(":"); second(); amPmMarker("AM", "PM")
    }
    private val layout = ThreePartsLayoutWidget(this, 32)

    constructor(parent: Screen?) : super(Text.translatable("kassette.config.about.title")) {
        this.parent = parent
    }

    override fun init() {
        val headerLayout = layout.addHeader(DirectionalLayoutWidget.vertical().spacing(4))
        headerLayout.add(TextWidget(title, textRenderer), Positioner::alignHorizontalCenter)
        headerLayout.mainPositioner.alignVerticalCenter()

        val gridLayout = GridWidget()
        gridLayout.setSpacing(8)
        gridLayout.mainPositioner
            .marginX(4)
            .marginBottom(4)
            .alignHorizontalCenter()

        val gridAdder = gridLayout.createAdder(2)
        gridAdder.add(IconWidget.create(
            256, 64,
            Identifier.of(
                "kassette",
                "textures/gui/about_banner.png"
            ),
            256, 64
        ), 2)
        gridAdder.add(
            TextWidget(
                Text.translatable("kassette.config.about.version"),
                textRenderer
            )
        )
        gridAdder.add(
            TextWidget(
                Text.literal(FabricLoader.getInstance().getModContainer("kassette").get().metadata.version.friendlyString),
                textRenderer
            )
        )
        gridAdder.add(
            TextWidget(
                Text.translatable("kassette.config.about.type"),
                textRenderer
            )
        )
        gridAdder.add(
            TextWidget(
                Text.literal("${aboutInfo.buildType}"),
                textRenderer
            )
        )

        gridAdder.add(
            TextWidget(
                Text.translatable("kassette.config.about.commit"),
                textRenderer
            )
        )
        gridAdder.add(
            TextWidget(
                Text.literal("${aboutInfo.gitCommitId}"),
                textRenderer
            )
        )

        gridAdder.add(
            TextWidget(
                Text.translatable("kassette.config.about.branch"),
                textRenderer
            )
        )
        gridAdder.add(
            TextWidget(
                Text.literal("${aboutInfo.gitBranchRef}"),
                textRenderer
            )
        )

        gridAdder.add(
            TextWidget(
                Text.translatable("kassette.config.about.date"),
                textRenderer
            )
        )
        gridAdder.add(
            TextWidget(
                Text.literal(
                    Instant.fromEpochMilliseconds(aboutInfo.buildDate)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).format(dateFormat)
                ),
                textRenderer
            )
        )
        layout.addBody(gridLayout)


        layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE,
                { button -> close() }
            ).width(200).build()
        )

        headerLayout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        layout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        gridLayout.forEachChild { widget ->
            addDrawableChild(widget)
        }
        headerLayout.refreshPositions()
        gridLayout.refreshPositions()
        layout.refreshPositions()
    }

    override fun refreshWidgetPositions() {
        super.refreshWidgetPositions()
    }

    override fun close() {
        client!!.setScreen(parent)
    }
}