package com.spadium.kassette.config

import com.spadium.kassette.Kassette.Companion.logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Colors
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
    var spotify: SpotifyConfig,
    var hud: HUDConfig,
    var callbackPort: UInt = 61008u,
    final val version: UInt = 0u
) {
    companion object {
        internal var Instance: Config = Config(
            SpotifyConfig("", ""),
            HUDConfig(
                128, 48,
                Colors.BLACK, Colors.GREEN,
                1, 5, true, true,
                HUDConfig.ProgressType.BAR
            )
        )

        fun getInstance(): Config {
            return Instance
        }
    }

    fun save() {}

    @OptIn(ExperimentalSerializationApi::class)
    fun reload() {
        // Config stuff
        val configFile = FabricLoader.getInstance().configDir.resolve("kassette.json")

        if (configFile.exists()) {
            try {
                Instance = json.decodeFromStream<Config>(
                    configFile.toFile().inputStream()
                )
            } catch (e: Exception) {
                logger.error("Error loading config! ${e.toString()}")
            }

        } else {
            val jsonOut = json.encodeToString(Instance)
            configFile.writeBytes(jsonOut.toByteArray())
        }
    }
}
