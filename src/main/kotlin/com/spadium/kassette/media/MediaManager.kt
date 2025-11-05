package com.spadium.kassette.media

import com.mojang.blaze3d.platform.NativeImage
import com.spadium.kassette.Kassette
import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.media.librespot.LibreSpotProvider
import com.spadium.kassette.media.spotify.SpotifyProvider
import com.spadium.kassette.ui.toasts.WarningToast
import com.spadium.kassette.media.images.ImageUtils
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance

object MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
        private set
    val providers: MutableMap<ResourceLocation, KClass<out MediaProvider>> = mutableMapOf(
        ResourceLocation.parse("kassette:placeholder") to PlaceholderProvider::class,
        ResourceLocation.parse("kassette:spotify") to SpotifyProvider::class,
        ResourceLocation.parse("kassette:librespot") to LibreSpotProvider::class,
        ResourceLocation.parse("kassette:debug") to DebugProvider::class
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
                .open(ResourceLocation.fromNamespaceAndPath("kassette", "textures/placeholder.jpg"))!!,
            true
        )
    }

    suspend fun update() {
        provider.update()
    }

    fun setProvider(identifier: ResourceLocation) {
        this.provider.destroy()
        this.provider = providers.getValue(identifier).createInstance()
    }

    fun onConfigChange(property: KProperty<*>, oldValue: MainConfig, newValue: MainConfig) {
        setProvider(newValue.providers.defaultProvider)
        Kassette.logger.info("Changing provider to ${newValue.providers.defaultProvider}")
    }

    fun addProvider(identifier: ResourceLocation, klazz: KClass<out MediaProvider>) {
        if (providers[identifier] == null) {
            providers[identifier] = klazz
        }
    }

    enum class MediaState(val texture: ResourceLocation) {
        PLAYING(ResourceLocation.fromNamespaceAndPath("kassette", "play")),
        PAUSED(ResourceLocation.fromNamespaceAndPath("kassette", "pause")),
        LOADING(ResourceLocation.fromNamespaceAndPath("kassette", "loading")),
        OTHER(ResourceLocation.fromNamespaceAndPath("kassette", "other"))
    }
}