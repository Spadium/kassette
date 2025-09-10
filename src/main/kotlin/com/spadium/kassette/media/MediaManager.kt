package com.spadium.kassette.media

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.media.spotify.LibreSpotProvider
import com.spadium.kassette.media.spotify.SpotifyProvider
import com.spadium.kassette.ui.toasts.WarningToast
import com.spadium.kassette.media.images.ImageUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance

object MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
        private set
    val providers: MutableMap<Identifier, KClass<out MediaProvider>> = mutableMapOf(
        Identifier.of("kassette:placeholder") to PlaceholderProvider::class,
        Identifier.of("kassette:spotify") to SpotifyProvider::class,
        Identifier.of("kassette:librespot") to LibreSpotProvider::class,
        Identifier.of("kassette:debug") to DebugProvider::class
    ).withDefault {
        PlaceholderProvider::class
    }
    val warningListeners: MutableList<(KProperty<*>, LinkedHashMap<String, Text>, LinkedHashMap<String, Text>) -> Unit> = mutableListOf()
    val warnings: LinkedHashMap<String, Text> by Delegates.observable(linkedMapOf()) {
            prop, oldVal, newVal ->
        if (oldVal.lastEntry() != newVal.lastEntry()) {
            if (MinecraftClient.getInstance().toastManager != null) {
                MinecraftClient.getInstance().toastManager.add(
                    WarningToast(newVal.lastEntry().value)
                )
            }
        }
    }

    fun throwWarning() {

    }

    fun getDefaultCoverArt(): NativeImage {
        return ImageUtils.loadStream(
            MinecraftClient.getInstance().resourceManager
                .open(Identifier.of("kassette", "textures/placeholder.jpg"))!!,
            true
        )
    }

    suspend fun update() {
        provider.update()
    }

    fun setProvider(identifier: Identifier) {
        this.provider = providers.getValue(identifier).createInstance()
    }

    fun onConfigChange(property: KProperty<*>, oldValue: MainConfig, newValue: MainConfig) {
        setProvider(newValue.providers.defaultProvider)
        Kassette.logger.info("Changing provider to ${newValue.providers.defaultProvider}")
    }

    fun addProvider(identifier: Identifier, klazz: KClass<out MediaProvider>) {
        if (providers[identifier] == null) {
            providers.put(identifier, klazz)
        }
    }

    enum class MediaState(val texture: Identifier) {
        PLAYING(Identifier.of("kassette", "play")),
        PAUSED(Identifier.of("kassette", "pause")),
        LOADING(Identifier.of("kassette", "loading")),
        OTHER(Identifier.of("kassette", "other"))
    }
}