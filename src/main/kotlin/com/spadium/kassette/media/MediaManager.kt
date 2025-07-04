package com.spadium.kassette.media

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.util.Identifier

object MediaManager {
    var provider: MediaProvider = DebugProvider()
        set(p) {
            p.init()
        }
    val info: MediaInfo = provider.getMedia()
    val state: MediaState = provider.state

    fun getDefaultCoverArt(): NativeImage {
        return NativeImage.read(
            MinecraftClient.getInstance().resourceManager
                .open(Identifier.of("kassette", "textures/placeholder.jpg"))!!.readAllBytes()
        )
    }

    enum class MediaState(val texture: Identifier) {
        PLAYING(Identifier.of("kassette", "play")),
        PAUSED(Identifier.of("kassette", "pause")),
        LOADING(Identifier.of("kassette", "loading")),
        OTHER(Identifier.of("kassette", "other"))
    }
}