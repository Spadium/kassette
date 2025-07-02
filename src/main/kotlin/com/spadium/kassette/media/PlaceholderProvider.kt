package com.spadium.kassette.media

import com.spadium.kassette.util.ImageUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class PlaceholderProvider: MediaProvider {
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
            ImageUtils.loadGenericImage(
                javaClass.getResourceAsStream("/assets/kassette/placeholder.jpg")!!.readAllBytes()
            ),
            "placeholder"
        )
    }

    override fun update() {
        runBlocking {
            println("PLACEHOLDER PROVIDER RUNNING!")
            delay(10000L)
        }
    }
}