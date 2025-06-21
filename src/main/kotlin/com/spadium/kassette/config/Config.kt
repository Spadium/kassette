package com.spadium.kassette.config

import kotlinx.serialization.Serializable
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
    var spotify: SpotifyConfig,
    var color: Long,
    var borderColor: Long,
    var textSpeed: Float,
    var fancyTextSpeed: Float,
    var fancyText: Boolean,
    var callbackPort: UInt = 61008u,
    final var version: UInt = 0u
) {
    companion object {
        internal var Instance: Config = Config(
            SpotifyConfig("", ""),
            0L,
            0L,
            1f,
            5f,
            false,
        )

        fun getInstance(): Config {
            return Instance
        }
    }

    fun save() {}

    fun reload() {
        // Config stuff
        val configFile = FabricLoader.getInstance().configDir.resolve("kassette.json")

        if (configFile.exists()) {
            Config.Instance = json.decodeFromStream<Config>(
                configFile.toFile().inputStream()
            )
        } else {
            val jsonOut = json.encodeToString(Config.Instance)
            configFile.writeBytes(jsonOut.toByteArray())
        }
    }
}
