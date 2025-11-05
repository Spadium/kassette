package com.spadium.kassette.config.overlays

import com.spadium.kassette.Kassette
import com.spadium.kassette.Kassette.Companion.logger
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.ConfigMeta
import com.spadium.kassette.media.images.ImageScalers
import com.spadium.kassette.util.ModNotification
import kotlinx.serialization.Serializable
import net.minecraft.network.chat.Component
import kotlin.io.path.exists
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

    companion object : ConfigCompanion<DefaultOverlayConfig>() {
        private val configFile = configPath.resolve("overlays/default.json")
        override var Instance: DefaultOverlayConfig by Delegates.observable(DefaultOverlayConfig()) {
                property, oldValue, newValue ->
            yellAtListeners(property, oldValue, newValue)
        }

        override fun reload(): DefaultOverlayConfig {
            var config = DefaultOverlayConfig()

            if (configFile.exists()) {
                try {
                    config = load()
                    config.validate()
                } catch (e: Exception) {
                    logger.error("Error loading config! ${e.toString()}")
                    Kassette.notifications.add(
                        ModNotification(
                            ModNotification.NotificationType.ERROR,
                            Component.literal("Kassette Configuration"),
                            ModNotification.SourceType.MOD,
                            e
                        )
                    )
                    config = DefaultOverlayConfig()
                }
            } else {
                config.save()
            }

            return config
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