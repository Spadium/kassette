package com.spadium.kassette.ui.screens.config

import com.spadium.kassette.ModInfo
import com.spadium.kassette.ui.widgets.LayoutListWidget
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageWidget
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.GridLayout
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout
import net.minecraft.client.gui.layouts.LayoutSettings
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class AboutScreen: Screen {
    private val parent: Screen?
    @OptIn(ExperimentalSerializationApi::class)
    private val aboutInfo: ModInfo = getInfo()
    private val dateFormat = LocalDateTime.Format {
        date(LocalDate.Formats.ISO); chars("  ")
        amPmHour(); chars(":"); minute(); chars(":"); second(); amPmMarker("AM", "PM")
    }
    private val layout = HeaderAndFooterLayout(this, 128, 32)
    private lateinit var sections: LayoutListWidget

    constructor(parent: Screen?) : super(Component.translatable("kassette.config.about.title")) {
        this.parent = parent
    }

    private fun getInfo(): ModInfo {
        val metadata = FabricLoader.getInstance().getModContainer("kassette")!!.get().metadata
        val infoMetadata = metadata.getCustomValue("info").asObject
        return ModInfo(
            ModInfo.BuildTypes.valueOf(infoMetadata.get("buildType").asString),
            infoMetadata.get("gitCommitId").asString,
            infoMetadata.get("gitBranchRef").asString,
            infoMetadata.get("buildDate").asString.toLong()
        )
    }

    override fun init() {
        val headerLayout = layout.addToHeader(LinearLayout.vertical().spacing(8))
        headerLayout.addChild(StringWidget(title, font), LayoutSettings::alignHorizontallyCenter)
        headerLayout.addChild(ImageWidget.texture(
            256, 64,
            ResourceLocation.fromNamespaceAndPath(
                "kassette",
                "textures/gui/about_banner.png"
            ),
            256, 64
        ))
        headerLayout.defaultCellSetting().alignVerticallyMiddle()

        val gridLayout = GridLayout()
        gridLayout.spacing(8)
        gridLayout.defaultCellSetting()
            .paddingHorizontal(4)
            .paddingBottom(4)
            .alignHorizontallyCenter()

        val gridAdder = gridLayout.createRowHelper(2)
        gridAdder.addChild(
            StringWidget(
                Component.translatable("kassette.config.about.version"),
                font
            )
        )
        gridAdder.addChild(
            StringWidget(
                Component.literal(FabricLoader.getInstance().getModContainer("kassette").get().metadata.version.friendlyString),
                font
            )
        )
        gridAdder.addChild(
            StringWidget(
                Component.translatable("kassette.config.about.type"),
                font
            )
        )
        gridAdder.addChild(
            StringWidget(
                Component.literal("${aboutInfo.buildType}"),
                font
            )
        )

        gridAdder.addChild(
            StringWidget(
                Component.translatable("kassette.config.about.commit"),
                font
            )
        )
        gridAdder.addChild(
            StringWidget(
                Component.literal("${aboutInfo.gitCommitId}"),
                font
            )
        )

        gridAdder.addChild(
            StringWidget(
                Component.translatable("kassette.config.about.branch"),
                font
            )
        )
        gridAdder.addChild(
            StringWidget(
                Component.literal("${aboutInfo.gitBranchRef}"),
                font
            )
        )

        gridAdder.addChild(
            StringWidget(
                Component.translatable("kassette.config.about.date"),
                font
            )
        )
        gridAdder.addChild(
            StringWidget(
                Component.literal(
                    Instant.fromEpochMilliseconds(aboutInfo.buildDate)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).format(dateFormat)
                ),
                font
            )
        )
        gridLayout.arrangeElements()
        sections = LayoutListWidget(
            minecraft, gridLayout, this, layout
        )
        layout.addToContents(sections)

        layout.addToFooter(
            Button.builder(
                CommonComponents.GUI_DONE,
                { button -> onClose() }
            ).width(200).build()
        )

        headerLayout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        layout.visitWidgets { widget ->
            addRenderableWidget(widget)
        }
        repositionElements()
    }

    override fun repositionElements() {
        layout.arrangeElements()
        sections.updateSize(width, layout)
    }

    override fun onClose() {
        minecraft!!.setScreen(parent)
    }
}