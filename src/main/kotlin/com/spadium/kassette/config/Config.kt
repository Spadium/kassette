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
        progressType = HUDConfig.ProgressType.BAR
    ),
    var callbackPort: UInt = 61008u,
    val version: UInt = configVersion
) {

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        private var Instance: Config = Config()

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
                for (i in 0..color.size-1) {
                    if (color[i] > 255 || color[i] < 0) {
                        throw RuntimeException("Error while processing color (Value $i is too big or less than zero): $c")
                    }
                }
            } else {
                throw RuntimeException("Error while processing color (Too small or too big): $c")
            }
        }

        // Validate version
        if (version != configVersion) {
            throw RuntimeException("Config version doesn't match supported version!")
        }
    }

    fun save() {
        val jsonOut = json.encodeToString(this)
        configFile.writeBytes(jsonOut.toByteArray())
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun load() {
        val jsonIn: Config = json.decodeFromStream(configFile.toFile().inputStream())
        providers = jsonIn.providers
        hud = jsonIn.hud
        callbackPort = jsonIn.callbackPort
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun reload() {
        // Config stuff
        if (configFile.exists()) {
            try {
                load()
                validate()
            } catch (e: Exception) {
                logger.error("Error loading config! ${e.toString()}")
            }
        } else {
            save()
        }
    }
}
