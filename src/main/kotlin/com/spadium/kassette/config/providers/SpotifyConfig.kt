package com.spadium.kassette.config.providers

import com.spadium.kassette.Kassette
import com.spadium.kassette.Kassette.Companion.logger
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.ConfigMeta
import com.spadium.kassette.util.ModNotification
import kotlinx.serialization.Serializable
import net.minecraft.network.chat.Component
import kotlin.io.path.exists

@ConfigMeta(
    "spotify",
    ConfigMeta.ConfigType.PROVIDER
)
@Serializable
data class SpotifyConfig(
    var clientId: String = "",
    var clientSecret: String = "",
    var accessToken: String = "",
    var refreshToken: String = "",
    var createdAt: Long = 0L,
    var nextRefresh: Long = 0L,
    var ignoreRateLimits: Boolean = false
) : Config<SpotifyConfig>() {
    override fun validate() {

    }

    companion object : ConfigCompanion<SpotifyConfig>() {
        private val configFile = configPath.resolve("providers/spotify.json")
        override var Instance: SpotifyConfig = SpotifyConfig()

        override fun reload(): SpotifyConfig {
            var config = SpotifyConfig()

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
                    config = SpotifyConfig()
                }
            } else {
                config.save()
            }

            return config
        }

    }
}
