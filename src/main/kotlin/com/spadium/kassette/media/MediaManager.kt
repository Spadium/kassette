package com.spadium.kassette.media

import kotlinx.coroutines.delay
import net.minecraft.client.MinecraftClient

object MediaManager {
    var provider: MediaProvider? = null
        set(p) {
            p?.init()
        }
    var info: MediaInfo? = provider?.getMedia()
    var state: MediaState = MediaState.OTHER
        set(s) {
            provider
        }

    enum class MediaState {
        PLAYING, PAUSED, LOADING, OTHER
    }
}