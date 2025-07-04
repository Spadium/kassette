package com.spadium.kassette.media

import com.spadium.kassette.util.ImageUtils
import kotlinx.coroutines.delay

class PlaceholderProvider: MediaProvider() {
    override var state: MediaManager.MediaState = MediaManager.MediaState.OTHER

    override fun getServiceName(): String {
        return "placeholder"
    }

    override fun init() {

    }

    override fun destroy() {

    }

    override fun getMedia(): MediaInfo {
        return MediaInfo(
            255,
            128,
            "View Kassette settings for more information", "N/A", "Kassette",
            MediaManager.getDefaultCoverArt(),
            "placeholder"
        )
    }

    override suspend fun update() {
        println("PLACEHOLDER PROVIDER RUNNING!")
        delay(10000L)
    }
}