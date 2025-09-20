package com.spadium.kassette.media.librespot

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.media.MediaProvider
import com.spotify.connectstate.Connect
import xyz.gianlu.librespot.ZeroconfServer
import xyz.gianlu.librespot.core.Session

class LibreSpotProvider : MediaProvider {
    private val server: ZeroconfServer
    private var librespotSession: Session? = null
    private var infoToReturn: MediaInfo = MediaInfo(
        0L, 0L, "", "", "",
        MediaManager.getDefaultCoverArt(), getServiceName()
    )
    override var info: MediaInfo = infoToReturn
    val properties: Map<String, Any?> = mapOf(

    )
    override val availableCommands: List<String> = listOf("togglePlay", "nextTrack", "previousTrack")

    override fun destroy() {
        server.closeSession()
    }

    override suspend fun update() {

    }

    val sessionConf = Session.Configuration.Builder()
        .setCacheEnabled(true)
        .build()

    var config = MainConfig.Companion.Instance

    override fun getServiceName(): String {
        return "LIBRESPOTify"
    }

    constructor() {
        val serverBuilder: ZeroconfServer.Builder = ZeroconfServer.Builder(sessionConf)
            .setPreferredLocale("en-US").setDeviceType(Connect.DeviceType.COMPUTER)
            .setDeviceName("Kassette LibreSpot Provider").setClientToken("")

        server = serverBuilder.create()
        server.addSessionListener(object : ZeroconfServer.SessionListener {
            var lastPlayer: Any? = null

            override fun sessionClosing(session: Session) {
                this@LibreSpotProvider.librespotSession = null
            }

            override fun sessionChanged(session: Session) {
                this@LibreSpotProvider.librespotSession = session
            }
        })
    }

    override fun sendCommand(cmd: String, payload: Any?): Int {
        if (cmd == "togglePlay" && this.librespotSession != null) {

        } else {
            return Int.MIN_VALUE
        }
        return 0;
    }
}