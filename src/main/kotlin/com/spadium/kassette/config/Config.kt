package com.spadium.kassette.config

import com.spadium.kassette.Kassette.Companion.logger
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive
import net.fabricmc.loader.api.FabricLoader
import kotlin.io.path.exists
import kotlin.io.path.writeBytes
import kotlin.system.exitProcess

/*
    Config class
    The only values that are absolutely required are spotify, color, and borderColor
 */

private val configVersion = 0u
private val json: Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}
private val configFile = FabricLoader.getInstance().configDir.resolve("kassette.json")

@Serializable
data class Config(
    var providers: ProvidersConfig = ProvidersConfig(
        spotify = SpotifyConfig("", ""),
    ),
    var hud: HUDConfig = HUDConfig(
        width = 128,
        height = 48,
        imageSize = 32,
        backgroundColor = intArrayOf(0,0,0, 255),
        borderColor = intArrayOf(0,255,0, 255),
        textSpeed = 1,
        fancyTextSpeed = 5,
        showCover = true,
        fancyText = true,
        progressType = HUDConfig.ProgressType.BAR,
        lineSpacing = 1
    ),
    var callbackPort: UInt = 61008u,
    val version: UInt = configVersion
) {

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        var Instance: Config = Config()

        @OptIn(ExperimentalSerializationApi::class)
        fun load(): Config {
            val jsonIn: Config = json.decodeFromStream(configFile.toFile().inputStream())
            return jsonIn
        }

        @OptIn(ExperimentalSerializationApi::class)
        fun reload(): Config {
            var config = Config()
            // Config stuff
            if (configFile.exists()) {
                try {
                    config = load()
                    config.validate()
                } catch (e: Exception) {
                    logger.error("Error loading config! ${e.toString()}")
                    config = Config()
                }
            } else {
                config.save()
            }
            return config
        }
    }

    fun validate() {
        // Validate colors
        hud.backgroundColor = checkArray(hud.backgroundColor, 3, 4, 255)
        hud.borderColor = checkArray(hud.backgroundColor, 3, 4, 255)

        // Validate version
        if (version != configVersion) {
            throw RuntimeException("Config version doesn't match supported version!")
        }
    }

    private fun checkArray(arr: IntArray, minimumSize: Int, maximumSize: Int, defaultValue: Int): IntArray {
        if (arr.size in minimumSize..maximumSize) {
            if (arr.size == maximumSize) {
                return arr
            } else if (arr.size == minimumSize) {
                return arr + defaultValue
            }
        }
        return arr
    }

    fun save() {
        val jsonOut = json.encodeToString(this)
        configFile.writeBytes(jsonOut.toByteArray())
    }
}
