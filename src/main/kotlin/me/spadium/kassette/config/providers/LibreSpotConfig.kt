package me.spadium.kassette.config.providers

import me.spadium.kassette.Kassette
import me.spadium.kassette.Kassette.Companion.logger
import me.spadium.kassette.config.Config
import me.spadium.kassette.config.ConfigMeta
import me.spadium.kassette.config.MainConfig.Companion.json
import me.spadium.kassette.util.ModNotification
import com.spotify.connectstate.Connect
import kotlinx.serialization.Serializable
import net.minecraft.network.chat.Component
import kotlin.io.path.exists
import kotlin.io.path.writeBytes

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

    override fun save() {
        val jsonOut = json.encodeToString(this)
        configFile.writeBytes(jsonOut.toByteArray())
        reload()
    }

}