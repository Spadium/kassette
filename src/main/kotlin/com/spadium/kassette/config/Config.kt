package com.spadium.kassette.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.Kassette.Companion.logger
import com.spotify.connectstate.Connect
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import kotlin.io.path.exists
import kotlin.io.path.writeBytes
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/*
    Config class
    The only values that are absolutely required are spotify, color, and borderColor
 */

private val configVersion = 0u
@OptIn(ExperimentalSerializationApi::class)
private val json: Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    allowComments = true
}
private val configFile = FabricLoader.getInstance().configDir.resolve("kassette.json")

@Serializable
data class Config(
    var providers: ProvidersConfig = ProvidersConfig(
        defaultProvider = Identifier.of("kassette:placeholder"),
        spotify = SpotifyConfig("", "", "", "", 0L, 0L, false),
        librespot = LibreSpotConfig(
            Connect.DeviceType.COMPUTER, "Kassette LibreSpot Provider", "", "",
            0
        )
    ),
    var hud: HUDConfig = HUDConfig(
        width = 128,
        height = 48,
        imageSize = 32,
        backgroundColor = intArrayOf(0, 0, 0, 128),
        borderColor = intArrayOf(0, 128, 0, 255),
        progressBackgroundColor = intArrayOf(32, 32, 32, 255),
        progressForegroundColor = intArrayOf(16, 32, 128, 255),
        textSpeed = 1, // Characters per second
        fancyTextSpeed = 5, // Pixels per second
        showCover = true,
        fancyText = true,
        progressType = HUDConfig.ProgressType.BAR,
        lineSpacing = 1,
        progressBarThickness = 4
    ),
    var callbackPort: UInt = 61008u,
    var infoMode: InfoMode = InfoMode.HUD,
    var firstRun: Boolean = true,
    val version: UInt = configVersion
) {
    enum class InfoMode {
        HIDDEN, HUD, TOAST
    }

    companion object {
        var Instance: Config by Delegates.observable(Config()) {
            property, oldValue, newValue ->
            yellAtListeners(property, oldValue, newValue)
        }
        val configUpdateListeners: MutableList<(KProperty<*>, Config, Config) -> Unit> = mutableListOf()

        fun addListener(listener: (KProperty<*>, Config, Config) -> Unit) {
            configUpdateListeners.add(listener)
        }

        private fun yellAtListeners(property: KProperty<*>, oldValue: Config, newValue: Config) {
            val invalidListeners: MutableList<(KProperty<*>, Config, Config) -> Unit> = mutableListOf()
            configUpdateListeners.forEach {
                try {
                    it.invoke(property, oldValue, newValue)
                } catch (t: Throwable) {
                    invalidListeners.add(it)
                }
            }
            if (invalidListeners.isNotEmpty()) {
                invalidListeners.forEach {
                    configUpdateListeners.remove(it)
                }
            }
        }
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
                    Kassette.errors.put("Kassette Configuration", e)
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
        hud.backgroundColor = checkColorArray(hud.backgroundColor, 3, 4, 255)
        hud.borderColor = checkColorArray(hud.borderColor, 3, 4, 255)

        // Validate version
        if (version != configVersion) {
            throw RuntimeException("Config version doesn't match supported version! Trying to update!")
        }
    }

    private fun checkColorArray(arr: IntArray, minimumSize: Int, maximumSize: Int, defaultValue: Int): IntArray {
        val list = arr.toMutableList()
        if (arr.size in minimumSize..maximumSize && (maximumSize - minimumSize) == 1) {
            if (arr.size == maximumSize) {
                return arr
            } else if (arr.size == minimumSize) {
                list.addLast(defaultValue)
            }
        } else {
            throw RuntimeException("Kassette Config: Color array to small/too big!")
        }
        return list.toIntArray()
    }

    fun save() {
        val jsonOut = json.encodeToString(this)
        configFile.writeBytes(jsonOut.toByteArray())
    }
}
