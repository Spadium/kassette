package com.spadium.kassette.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.Kassette.Companion.logger
import com.spadium.kassette.config.providers.LibreSpotConfig
import com.spadium.kassette.config.providers.ProvidersConfig
import com.spadium.kassette.config.providers.SpotifyConfig
import com.spadium.kassette.config.serializers.IdentifierSerializer
import com.spadium.kassette.util.ModNotification
import com.spotify.connectstate.Connect
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeBytes
import kotlin.properties.Delegates

/*
    Config class
    The only values that are absolutely required are spotify, color, and borderColor
 */

private val configVersion = 0u

@ConfigMeta(
    "main",
    ConfigMeta.ConfigType.MAIN
)
@Serializable
data class MainConfig(
    var providers: ProvidersConfig = ProvidersConfig(
        defaultProvider = Identifier.parse("kassette:placeholder"),
        spotify = SpotifyConfig(),
        librespot = LibreSpotConfig()
    ),
    @Serializable(with = IdentifierSerializer::class) var overlayName: Identifier = Identifier.parse("kassette:default"),
    var firstRun: Boolean = true,
    var configButtonReplacesTelemetry: Boolean = !(FabricLoader.getInstance().isModLoaded("modmenu")),
    val version: UInt = configVersion
) : Config<MainConfig>() {

    companion object : ConfigCompanion<MainConfig>() {
        private val configFile = configPath.resolve("main.json")
        @OptIn(ExperimentalSerializationApi::class)
        val json: Json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            allowComments = true
        }

        override var Instance: MainConfig by Delegates.observable(MainConfig()) {
            property, oldValue, newValue ->
            yellAtListeners(property, oldValue, newValue)
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun reload(): MainConfig {
            var config = MainConfig()
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
                    config = MainConfig()
                }
            } else {
                config.save()
            }
            return config
        }
    }

    override fun validate() {
        // Validate version
        if (version != configVersion) {
            throw RuntimeException("Config version doesn't match supported version! Trying to update!")
        }
    }

    override fun save() {
        val jsonOut = json.encodeToString(this)
        configFile.writeBytes(jsonOut.toByteArray())
        reload()
    }
}