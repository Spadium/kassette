package com.spadium.kassette.media

class MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
    var info: MediaInfo = provider.getMedia()

    companion object {
        val instance: MediaManager = MediaManager()
    }

    fun createThread() {

    }

    private constructor() {
        createThread()
    }
}