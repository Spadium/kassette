package com.spadium.kassette.media.spotify

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.spotifyAppApi
import com.spadium.kassette.config.Config
import com.spadium.kassette.media.MediaProvider

class SpotifyProvider : MediaProvider() {
    private lateinit var token: SpotifyAppApi

    override fun getServiceName(): String {
        return "Spotify"
    }

    override suspend fun init() {
        token = spotifyAppApi(
            Config.Instance.spotify.clientId,
            Config.Instance.spotify.clientId
        ).build()
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }
}