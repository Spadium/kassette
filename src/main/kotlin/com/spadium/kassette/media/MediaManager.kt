package com.spadium.kassette.media

class MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
    var info: MediaInfo = provider.getMedia()

    companion object {
        val instance: MediaManager = MediaManager()
    }

    class MediaManagerThread(): Thread("Kassette MediaManager") {
        override fun run() {
            println("hello thread world!")
        }
    }

    private constructor() {
//        MediaManagerThread().start()
    }
}