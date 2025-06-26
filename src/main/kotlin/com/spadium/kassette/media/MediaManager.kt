package com.spadium.kassette.media

import kotlinx.coroutines.delay

class MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
    var info: MediaInfo = provider.getMedia()

    companion object {
        val instance: MediaManager = MediaManager()
    }

    inner class MediaManagerThread(): Thread("Kassette MediaManager") {
        override fun run() {
            this@MediaManager.provider.update()
        }
    }

    private constructor() {
        MediaManagerThread().start()
    }
}