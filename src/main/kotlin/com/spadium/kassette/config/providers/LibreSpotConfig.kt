package com.spadium.kassette.config.providers

import com.spadium.kassette.Kassette
import com.spadium.kassette.Kassette.Companion.logger
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.ConfigMeta
import com.spadium.kassette.util.ModNotification
import com.spotify.connectstate.Connect
import kotlinx.serialization.Serializable
import net.minecraft.network.chat.Component
import kotlin.io.path.exists

//Connect.DeviceType.COMPUTER, "Kassette LibreSpot Provider", "", "",
//            0
@ConfigMeta(
    "librespot",
    ConfigMeta.ConfigType.PROVIDER
)
@Serializable
data class LibreSpotConfig(
    var deviceType: Connect.DeviceType = Connect.DeviceType.COMPUTER,
    var deviceName: String = "Kassette LibreSpot Provider",
    var token: String = "",
    var deviceId: String = "",
    var listenPort: Short = 0
) : Config<LibreSpotConfig>() {
    companion object : ConfigCompanion<LibreSpotConfig>() {
        private val configFile = configPath.resolve("providers/librespot.json")
        override var Instance: LibreSpotConfig = LibreSpotConfig()

        override fun reload(): LibreSpotConfig {
            var config = LibreSpotConfig()
            // Config stuff
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
                    config = LibreSpotConfig()
                }
            } else {
                config.save()
            }
            return config
        }

    }

    override fun validate() {

    }

}