package me.spadium.kassette.media

import com.mojang.blaze3d.platform.NativeImage
import me.spadium.kassette.Kassette
import me.spadium.kassette.config.MainConfig
import me.spadium.kassette.media.librespot.LibreSpotProvider
import me.spadium.kassette.media.spotify.SpotifyProvider
import me.spadium.kassette.ui.toasts.WarningToast
import me.spadium.kassette.media.images.ImageUtils
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance

object MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
        private set
    val providers: MutableMap<Identifier, KClass<out MediaProvider>> = mutableMapOf(
        Identifier.parse("kassette:placeholder") to PlaceholderProvider::class,
        Identifier.parse("kassette:spotify") to SpotifyProvider::class,
        Identifier.parse("kassette:librespot") to LibreSpotProvider::class,
        Identifier.parse("kassette:debug") to DebugProvider::class
    ).withDefault {
        PlaceholderProvider::class
    }
    val warningListeners: MutableList<(KProperty<*>, LinkedHashMap<String, Component>, LinkedHashMap<String, Component>) -> Unit> = mutableListOf()
    val warnings: LinkedHashMap<String, Component> by Delegates.observable(linkedMapOf()) {
            prop, oldVal, newVal ->
        if (oldVal.lastEntry() != newVal.lastEntry()) {
            if (Minecraft.getInstance().toastManager != null) {
                Minecraft.getInstance().toastManager.addToast(
                    WarningToast(newVal.lastEntry().value)
                )
            }
        }
    }

    fun throwWarning() {

    }

    fun getDefaultCoverArt(): NativeImage {
        return ImageUtils.loadStream(
            Minecraft.getInstance().resourceManager
                .open(Identifier.fromNamespaceAndPath("kassette", "textures/placeholder.jpg"))!!,
            true
        )
    }

    suspend fun update() {
        provider.update()
    }

    fun setProvider(identifier: Identifier) {
        this.provider.destroy()
        this.provider = providers.getValue(identifier).createInstance()
    }

    fun onConfigChange(property: KProperty<*>, oldValue: MainConfig, newValue: MainConfig) {
        setProvider(newValue.providers.defaultProvider)
        Kassette.logger.info("Changing provider to ${newValue.providers.defaultProvider}")
    }

    fun addProvider(identifier: Identifier, klazz: KClass<out MediaProvider>) {
        if (providers[identifier] == null) {
            providers[identifier] = klazz
        }
    }

    enum class MediaState(val texture: Identifier) {
        PLAYING(Identifier.fromNamespaceAndPath("kassette", "play")),
        PAUSED(Identifier.fromNamespaceAndPath("kassette", "pause")),
        LOADING(Identifier.fromNamespaceAndPath("kassette", "loading")),
        OTHER(Identifier.fromNamespaceAndPath("kassette", "other"))
    }
}