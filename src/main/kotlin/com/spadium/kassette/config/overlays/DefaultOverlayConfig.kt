package com.spadium.kassette.config.overlays

import com.spadium.kassette.config.Config
import com.spadium.kassette.config.MainConfig.Companion.checkColorArray
import com.spadium.kassette.config.ConfigMeta
import kotlinx.serialization.Serializable

// width = 128,
//            height = 48,
//            imageSize = 32,
//            backgroundColor = intArrayOf(0, 0, 0, 128),
//            borderColor = intArrayOf(0, 128, 0, 255),
//            progressBackgroundColor = intArrayOf(32, 32, 32, 255),
//            progressForegroundColor = intArrayOf(16, 32, 128, 255),
//            textSpeed = 1, // Characters per second
//            fancyTextSpeed = 5, // Pixels per second
//            showCover = true,
//            fancyText = true,
//            progressType = DefaultOverlayConfig.ProgressType.BAR,
//            lineSpacing = 1,
//            progressBarThickness = 4

@ConfigMeta(
    "default",
    ConfigMeta.ConfigType.OVERLAY
)
@Serializable
data class DefaultOverlayConfig(
    var width: Int = 128,
    var height: Int = 48,
    val imageSize: Int = 32,
    var backgroundColor: IntArray = intArrayOf(0, 0, 0, 128),
    var borderColor: IntArray = intArrayOf(0, 128, 0, 255),
    val progressBackgroundColor: IntArray = intArrayOf(32, 32, 32, 255),
    val progressForegroundColor: IntArray = intArrayOf(16, 32, 128, 255),
    var textSpeed: Int = 1,
    var fancyTextSpeed: Int = 5,
    var lineSpacing: Int = 1,
    var progressBarThickness: Int = 4,
    var showCover: Boolean = true,
    var fancyText: Boolean = true,
    var progressType: ProgressType = ProgressType.BAR
) : Config<DefaultOverlayConfig> {
    override fun validate() {
        // Validate colors
        backgroundColor = checkColorArray(backgroundColor, 3, 4, 255)
        borderColor = checkColorArray(borderColor, 3, 4, 255)
    }

    @Serializable
    enum class ProgressType {
        BAR, TEXT, NONE,
        VERYLONGOPTIONTHATDEFAULTSTOBARMEANTFORDEBUGGINGPLEASEDONTUSEPLEASENO,
        OTHERVERYLONGOPTIONTHATDEFAULTSTOTEXTMEANTFORDEBUGGINGPLEASEDONTUSEOK,
        PLEASEDONTUSETHISVERYLONGOPTIONTHATHASNOPROGRESSTYPEMEANTFORDEBUGGING,
    }
}