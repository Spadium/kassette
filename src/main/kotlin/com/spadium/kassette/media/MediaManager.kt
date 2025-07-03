package com.spadium.kassette.media

import net.minecraft.util.Identifier

object MediaManager {
    var provider: MediaProvider = DebugProvider()
        set(p) {
            p.init()
        }
    var info: MediaInfo = provider.getMedia()
    var state: MediaState = MediaState.OTHER
        set(s) {
            provider.state = s
        }

    enum class MediaState(val texture: Identifier) {
        PLAYING(Identifier.of("kassette", "textures/gui/status/play.png")),
        PAUSED(Identifier.of("kassette", "textures/gui/status/pause.png")),
        LOADING(Identifier.of("kassette", "textures/gui/status/loading.png")),
        OTHER(Identifier.of("kassette", "textures/gui/status/other.png"))
    }
}