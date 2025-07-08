package com.spadium.kassette.media.spotify

import com.spadium.kassette.config.Config
import com.spadium.kassette.config.SpotifyConfig
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import kotlinx.coroutines.runBlocking
import net.minecraft.util.Util
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials
import java.net.URI

class SpotifyProvider : AccountMediaProvider {
    private var clientApi: SpotifyApi
    private var config = Config.Instance
    private var spotifySettings: SpotifyConfig = config.providers.spotify
    private var infoToReturn: MediaInfo = MediaInfo(
        0L, 0L, "", "", "",
        MediaManager.getDefaultCoverArt(), getServiceName()
    )
    private var authCode = ""
    private var providerState = ProviderState.NONE
    private var nextTokenRefresh: Long = 0

    enum class ProviderState {
        NONE, AUTHENTICATION, GOT_TOKEN, POST_TOKEN_SETUP, SIGNED_IN
    }

    override fun getServiceName(): String {
        return "Spotify"
    }

    constructor() {
        runBlocking {
            clientApi = SpotifyApi.builder()
                .setClientId(spotifySettings.clientId).setClientSecret(spotifySettings.clientSecret)
                .setRedirectUri(URI("http://127.0.0.1:${config.callbackPort}/callback")).build()
            println("${spotifySettings.clientId}, ${spotifySettings.clientSecret}")
            if (!config.providers.spotify.refreshToken.isBlank() && !config.providers.spotify.accessToken.isBlank()) {
                clientApi.refreshToken = config.providers.spotify.refreshToken
                clientApi.accessToken = config.providers.spotify.accessToken
            }
        }
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun getMedia(): MediaInfo {
        return infoToReturn
    }

    override suspend fun update() {
        if (providerState == ProviderState.GOT_TOKEN) {
            val authCodeCredentials: AuthorizationCodeCredentials = clientApi
                .authorizationCode(authCode).build().execute()
            clientApi.accessToken = authCodeCredentials.accessToken
            clientApi.refreshToken = authCodeCredentials.refreshToken
            config.providers.spotify.accessToken = clientApi.accessToken
            config.providers.spotify.refreshToken = clientApi.refreshToken
            println("Expires in ${authCodeCredentials.expiresIn}")
            config.save()
            providerState = ProviderState.POST_TOKEN_SETUP
        } else if (providerState == ProviderState.POST_TOKEN_SETUP) {

        } else if (providerState == ProviderState.SIGNED_IN) {
            // Two-seconds before the maximum age of the token in case the update thread is running slow
            if (System.currentTimeMillis() >= nextTokenRefresh - 2000) {
                refreshTokens()
            }
        }
    }

    override fun initiateLogin() {
        if (config.providers.spotify.refreshToken.isEmpty() && config.providers.spotify.accessToken.isEmpty()) {
            val authCodeUriReq = clientApi.authorizationCodeUri()
                .show_dialog(true)
                .scope(
                    "streaming user-read-playback-position"
                ).build()
            val authReqUri = authCodeUriReq.execute()
            Util.getOperatingSystem().open(authReqUri)
        } else {
            refreshTokens()
        }
    }

    private fun refreshTokens() {
        val refreshToken = clientApi.authorizationCodeRefresh()
            .refresh_token(config.providers.spotify.refreshToken).build().execute()
        nextTokenRefresh = System.currentTimeMillis() + (refreshToken.expiresIn * 1000)
        clientApi.accessToken = refreshToken.accessToken
        config.providers.spotify.accessToken = clientApi.accessToken
        if (!refreshToken.refreshToken.isNullOrBlank()) {
            clientApi.refreshToken = refreshToken.refreshToken
            config.providers.spotify.refreshToken = clientApi.refreshToken
        }
        println("${refreshToken.refreshToken}, ${refreshToken.accessToken}")
        config.save()
    }

    override fun sendCommand(cmd: String, payload: Any): Int {
        if (cmd == "authSendStr" && payload is String) {
            val splitPayload = payload.split("=")
            if (splitPayload[0] == "code") {
                authCode = payload.split("=")[1]
                providerState = ProviderState.GOT_TOKEN
            } else {
                return 1
            }
        } else {
            return Int.MIN_VALUE
        }
        return 0
    }
}