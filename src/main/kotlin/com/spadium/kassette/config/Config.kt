package com.spadium.kassette.config

import com.spadium.kassette.Kassette.Companion.logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.exists
import kotlin.io.path.writeBytes

/*
    Config class
    The only values that are absolutely required are spotify, color, and borderColor
 */

private val json: Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

@Serializable
data class Config(
    var providers: ProvidersConfig,
    var hud: HUDConfig,
    var callbackPort: UInt = 61008u,
    final val version: UInt = 0u
) {
    @Transient
    private val configFile = FabricLoader.getInstance().configDir.resolve("kassette.json")

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        internal var Instance: Config = Config(
            providers = ProvidersConfig(
                spotify = SpotifyConfig("", ""),
            ),
            hud = HUDConfig(
                width = 128,
                height = 48,
                imageSize = 32,
                backgroundColor = intArrayOf(0,0,0, 255),
                borderColor = intArrayOf(0,255,0, 255),
                textSpeed = 1,
                fancyTextSpeed = 5,
                showCover = true,
                fancyText = true,
                progressType = HUDConfig.ProgressType.BAR
            )
        )

        fun getInstance(): Config {
            return Instance
        }
    }

    fun validate() {
        // Validate colors
        val colors = arrayOf(
            hud.backgroundColor,
            hud.borderColor
        )

        colors.forEachIndexed { c, color ->
            if (color.size >= 3 && color.size <= 4) {
                for (i in 0..color.size) {
                    if (color[i] > 255 || color[i] < 0) {
                        throw RuntimeException("Error while processing color (Value $i is too big or less than zero): $c")
                    }
                }
            } else {
                throw RuntimeException("Error while processing color (Too small or too big): $c")
            }
        }

        // Validate version
        if (version != 0u) {
            throw RuntimeException("Config version doesn't match supported version!")
        }
    }

    fun save() {}

    @OptIn(ExperimentalSerializationApi::class)
    fun reload() {
        // Config stuff
        if (configFile.exists()) {
            try {
                Instance = json.decodeFromStream<Config>(
                    configFile.toFile().inputStream()
                )
                validate()
            } catch (e: Exception) {
                logger.error("Error loading config! ${e.toString()}")
            }
        } else {
            val jsonOut = json.encodeToString(this)
            configFile.writeBytes(jsonOut.toByteArray())
        }
    }

    @Serializable
    data class ProvidersConfig(
        var spotify: SpotifyConfig,
    )
}
