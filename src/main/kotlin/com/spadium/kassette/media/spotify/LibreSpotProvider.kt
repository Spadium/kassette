package com.spadium.kassette.media.spotify

import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.media.MediaProvider
import com.spotify.connectstate.Connect
import com.spotify.connectstate.Player
import xyz.gianlu.librespot.ZeroconfServer
import xyz.gianlu.librespot.core.Session


class LibreSpotProvider : MediaProvider {
    private var infoToReturn: MediaInfo = MediaInfo(
        0L, 0L, "", "", "",
        MediaManager.getDefaultCoverArt(), getServiceName()
    )
    override var info: MediaInfo = infoToReturn
    val properties: Map<String, Any?> = mapOf(
        "commands" to listOf("togglePlay", "nextTrack", "previousTrack")
    )
    override val availableCommands: List<String> = properties["commands"] as List<String>

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }

    val sessionConf = Session.Configuration.Builder()
        .setCacheEnabled(true)
        .build()

    var config = MainConfig.Instance

    override fun getServiceName(): String {
        return "Spotify"
    }

    constructor() {
        val serverBuilder: ZeroconfServer.Builder = ZeroconfServer.Builder(sessionConf)
            .setPreferredLocale("en-US").setDeviceType(Connect.DeviceType.COMPUTER)
            .setDeviceName("Kassette LibreSpot Provider").setClientToken("")

        val server: ZeroconfServer = serverBuilder.create()
        server.addSessionListener(object : ZeroconfServer.SessionListener {
            var lastPlayer: Player? = null

            override fun sessionClosing(session: Session) {

            }

            override fun sessionChanged(session: Session) {

            }
        })
    }
}