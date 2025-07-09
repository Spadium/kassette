package com.spadium.kassette.media

import com.spadium.kassette.util.ImageUtils
import kotlinx.coroutines.delay

class PlaceholderProvider: MediaProvider {
    private var state: MediaManager.MediaState = MediaManager.MediaState.OTHER
    override val info: MediaInfo = getMedia()

    override fun getServiceName(): String {
        return "placeholder"
    }

    constructor() {
        println("test")
    }

    override fun destroy() {

    }

    override fun getMedia(): MediaInfo {
        return MediaInfo(
            255,
            128,
            "View Kassette settings for more information", "N/A", "Kassette",
            MediaManager.getDefaultCoverArt(),
            "placeholder",
            state
        )
    }

    override suspend fun update() {
        println("PLACEHOLDER PROVIDER RUNNING!")
        delay(10000L)
    }
}