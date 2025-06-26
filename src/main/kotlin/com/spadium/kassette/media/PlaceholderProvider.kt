package com.spadium.kassette.media

import com.spadium.kassette.util.ImageUtils

class PlaceholderProvider: MediaProvider() {
    override fun getServiceName(): String {
        return "placeholder"
    }

    override suspend fun init() {

    }

    override fun destroy() {

    }

    override fun getMedia(): MediaInfo {
        return MediaInfo(
            255,
            128,
            "Song", "Album", "Artist",
            ImageUtils.loadGenericImage(
                javaClass.getResourceAsStream("/assets/kassette/placeholder.jpg")!!.readAllBytes()
            ),
            "placeholder"
        )
    }

    override fun update() {

    }
}