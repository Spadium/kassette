package com.spadium.kassette.config

import com.spadium.kassette.Kassette
import com.spadium.kassette.Kassette.Companion.logger
import com.spadium.kassette.config.providers.LibreSpotConfig
import com.spadium.kassette.config.providers.ProvidersConfig
import com.spadium.kassette.config.providers.SpotifyConfig
import com.spadium.kassette.util.ModNotification
import com.spotify.connectstate.Connect
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text
import net.minecraft.util.Identifier
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
        defaultProvider = Identifier.of("kassette:placeholder"),
        spotify = SpotifyConfig(),
        librespot = LibreSpotConfig()
    ),
    var infoMode: InfoMode = InfoMode.HUD,
    var firstRun: Boolean = true,
    val version: UInt = configVersion
) : Config<MainConfig>() {
    enum class InfoMode {
        HIDDEN, HUD, TOAST
    }

    companion object : ConfigCompanion<MainConfig>() {
        val configPath: Path = FabricLoader.getInstance().configDir.resolve("kassette/")
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
//        override val configUpdateListeners: MutableList<(KProperty<*>, MainConfig, MainConfig) -> Unit> = mutableListOf()
        @OptIn(ExperimentalSerializationApi::class)
        fun load(): MainConfig {
//            val jsonIn: Config = json.decodeFromStream(configFile.toFile().inputStream())
            return load<MainConfig>()
        }

        @OptIn(ExperimentalSerializationApi::class)
        fun reload(): MainConfig {
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
                            Text.literal("Kassette Configuration"),
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

        @OptIn(ExperimentalSerializationApi::class)
        inline fun <reified T> load(): T where T : Config<T> {
            if (T::class.java.isAnnotationPresent(ConfigMeta::class.java)) {
                val annotationMeta = T::class.java.getAnnotation(ConfigMeta::class.java)
                val configFile = configPath.resolve("${annotationMeta.type.path}${annotationMeta.configCategory}.json")
                val jsonIn: T = json.decodeFromStream(configFile.toFile().inputStream())
                return jsonIn
            } else {
                throw Exception("Invalid class!")
            }
        }
    }

    override fun validate() {
        // Validate version
        if (version != configVersion) {
            throw RuntimeException("Config version doesn't match supported version! Trying to update!")
        }
    }

    fun save() {
        val jsonOut = json.encodeToString(this)
        configFile.writeBytes(jsonOut.toByteArray())
    }
}