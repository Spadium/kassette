package com.spadium.kassette.media.spotify

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import com.spadium.kassette.config.Config
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaProvider

class SpotifyProvider : AccountMediaProvider() {
    private lateinit var appApi: SpotifyAppApi
    private lateinit var clientApi: SpotifyClientApi

    override fun getServiceName(): String {
        return "Spotify"
    }

    override suspend fun init() {
        appApi = spotifyAppApi(
            Config.Instance.spotify.clientId,
            Config.Instance.spotify.clientId
        ).build()
        clientApi = spotifyClientApi(
            Config.Instance.spotify.clientId,
            Config.Instance.spotify.clientId,
            "127.0.0.1:${Config.Instance.callbackPort}"
        ).build()
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun getMedia(): MediaInfo {
        TODO("Not yet implemented")
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun initiateLogin() {
        TODO("Not yet implemented")
    }
}