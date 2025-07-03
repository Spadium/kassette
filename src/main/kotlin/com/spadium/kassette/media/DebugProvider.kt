package com.spadium.kassette.media

import com.spadium.kassette.Kassette
import kotlinx.coroutines.delay

class DebugProvider: AccountMediaProvider() {
    override var state: MediaManager.MediaState = MediaManager.MediaState.OTHER

    override fun initiateLogin() {
        Kassette.logger.debug("MEDIA PROVIDER INITIATE LOGIN")
    }

    override fun getServiceName(): String {
        return "Debug"
    }

    override fun init() {
        Kassette.logger.debug("MEDIA PROVIDER INIT")
    }

    override fun destroy() {
        Kassette.logger.debug("MEDIA PROVIDER DESTROYED")
    }

    override fun getMedia(): MediaInfo {
        return MediaInfo(
            0L, 0L, "TITLE", "ALBUM",
            "ARTIST", null, getServiceName()
        )
    }

    override suspend fun update() {
        delay(500)
        println("DEBUG UPDATE")
    }
}