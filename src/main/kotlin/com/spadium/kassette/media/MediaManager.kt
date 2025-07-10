package com.spadium.kassette.media

import com.spadium.kassette.media.spotify.SpotifyProvider
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.util.Identifier
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
        private set
    val providers: MutableMap<Identifier, KClass<out MediaProvider>> = mutableMapOf(
        Identifier.of("kassette:placeholder") to PlaceholderProvider::class,
        Identifier.of("kassette:spotify") to SpotifyProvider::class,
        Identifier.of("kassette:debug") to DebugProvider::class
    ).withDefault {
        PlaceholderProvider::class
    }

    fun getDefaultCoverArt(): NativeImage {
        return NativeImage.read(
            MinecraftClient.getInstance().resourceManager
                .open(Identifier.of("kassette", "textures/placeholder.jpg"))!!.readAllBytes()
        )
    }

    fun setProvider(identifier: Identifier) {
        provider.destroy()
        this.provider = providers.getValue(identifier).createInstance()
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