package com.spadium.kassette.media

class MediaManager {
    val info: MediaInfo = MediaInfo(
        0,
        0,
        "Song", "Album", "Artist",
        arrayOf(
            javaClass.getResourceAsStream("/assets/kassette/placeholder.jpg")!!.readAllBytes()
        ),
        "placeholder"
    )

    companion object {
        val instance: MediaManager = MediaManager()
    }

    private constructor() {

    }
}