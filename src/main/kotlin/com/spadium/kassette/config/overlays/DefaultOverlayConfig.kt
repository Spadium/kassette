package com.spadium.kassette.config.overlays

import com.spadium.kassette.config.Config
import com.spadium.kassette.config.ConfigMeta
import com.spadium.kassette.media.images.ImageScalers
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

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
    var downscaleMethod: ImageScalers = ImageScalers.BILINEAR,
    var upscaleMethod: ImageScalers = ImageScalers.BILINEAR,
    var downscaleCoverArt: Boolean = true,
    var progressType: ProgressType = ProgressType.BAR
) : Config<DefaultOverlayConfig>() {
    override fun validate() {
        // Validate colors
        backgroundColor = checkColorArray(backgroundColor, 3, 4, 255)
        borderColor = checkColorArray(borderColor, 3, 4, 255)
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    companion object : ConfigCompanion<DefaultOverlayConfig>() {
        override var Instance: DefaultOverlayConfig by Delegates.observable(DefaultOverlayConfig()) {
                property, oldValue, newValue ->
            yellAtListeners(property, oldValue, newValue)
        }

    }

    @Serializable
    enum class ProgressType {
        BAR, TEXT, NONE,
        VERYLONGOPTIONTHATDEFAULTSTOBARMEANTFORDEBUGGINGPLEASEDONTUSEPLEASENO,
        OTHERVERYLONGOPTIONTHATDEFAULTSTOTEXTMEANTFORDEBUGGINGPLEASEDONTUSEOK,
        PLEASEDONTUSETHISVERYLONGOPTIONTHATHASNOPROGRESSTYPEMEANTFORDEBUGGING,
    }
}