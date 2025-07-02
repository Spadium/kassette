package com.spadium.kassette.media

import com.spadium.kassette.Kassette

class DebugProvider: AccountMediaProvider {
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

    override fun update() {
        Kassette.logger.debug("MEDIA PROVIDER UPDATE")
    }
}