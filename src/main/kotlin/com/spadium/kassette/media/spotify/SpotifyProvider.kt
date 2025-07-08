package com.spadium.kassette.media.spotify

import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.SpotifyConfig
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import kotlinx.coroutines.runBlocking
import net.minecraft.client.texture.NativeImage
import net.minecraft.util.Util
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.enums.ModelObjectType
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials
import se.michaelthelin.spotify.model_objects.specification.Track
import java.net.URI
import kotlin.math.PI

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
    private var lastImageUrl = ""
    override var info: MediaInfo = infoToReturn

    enum class ProviderState {
        NONE, GOT_TOKEN, POST_TOKEN_SETUP, SIGNED_IN
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

    }

    override fun getMedia(): MediaInfo {
        return infoToReturn
    }

    override suspend fun update() {
        if (providerState == ProviderState.GOT_TOKEN) {
            config.providers.spotify.createdAt = System.currentTimeMillis()
            val authCodeCredentials: AuthorizationCodeCredentials = clientApi
                .authorizationCode(authCode).build().execute()
            // config stuff
            config.providers.spotify.nextRefresh = nextTokenRefresh - 1000
            clientApi.accessToken = authCodeCredentials.accessToken
            clientApi.refreshToken = authCodeCredentials.refreshToken
            config.providers.spotify.accessToken = clientApi.accessToken
            config.providers.spotify.refreshToken = clientApi.refreshToken
            // actually save the config
            config.save()
            // move to the next step
            providerState = ProviderState.POST_TOKEN_SETUP
        } else if (providerState == ProviderState.POST_TOKEN_SETUP) {
            providerState = ProviderState.SIGNED_IN
        } else if (providerState == ProviderState.SIGNED_IN) {
            // Two-seconds before the maximum age of the token in case the update thread is running slow
            if (System.currentTimeMillis() >= nextTokenRefresh - 2000) {
                refreshTokens()
            }
            val currentlyPlayingCtx = clientApi.informationAboutUsersCurrentPlayback.build().execute()
            if (currentlyPlayingCtx != null) {
                val item = currentlyPlayingCtx.item
                if (infoToReturn.currentPosition != currentlyPlayingCtx.progress_ms.toLong()) {
                    println("(${infoToReturn.maximumTime} / ${infoToReturn.currentPosition})")
                }
                infoToReturn.currentPosition = currentlyPlayingCtx.progress_ms.toLong()
                infoToReturn.maximumTime = currentlyPlayingCtx.item.durationMs.toLong()
                if (currentlyPlayingCtx.item.type == ModelObjectType.TRACK && item is Track) {
                    infoToReturn.album = item.album.name
                    infoToReturn.title = item.name
                    val artistStr = buildString {
                        if (item.artists.size == 1) {
                            append(item.artists[0].name)
                        } else {
                            item.artists.forEachIndexed { i, artist ->
                                if (i == item.artists.size - 1) {
                                    append(artist.name)
                                } else {
                                    append("${artist.name}, ")
                                }
                            }
                        }
                    }
                    infoToReturn.artist = artistStr
                    if (item.album.images[0].url != lastImageUrl) {
                        lastImageUrl = item.album.images[0].url
                        infoToReturn.coverArt = NativeImage.read(URI(item.album.images[0].url).toURL().openStream())
                    }
                }
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
        } else if ((config.providers.spotify.nextRefresh - 2000) >= System.currentTimeMillis()) {
            Kassette.logger.info("Spotify tokens need a refresh!")
            refreshTokens()
        } else {
            Kassette.logger.info("Valid spotify tokens already in config!")
            providerState = ProviderState.POST_TOKEN_SETUP
        }
    }

    private fun refreshTokens() {
        val refreshToken = clientApi.authorizationCodeRefresh()
            .refresh_token(config.providers.spotify.refreshToken).build().execute()
        nextTokenRefresh = System.currentTimeMillis() + (refreshToken.expiresIn * 1000)
        config.providers.spotify.createdAt = System.currentTimeMillis()
        config.providers.spotify.nextRefresh = nextTokenRefresh - 1000 // 1-second behind in case these calls take too long
        clientApi.accessToken = refreshToken.accessToken
        config.providers.spotify.accessToken = clientApi.accessToken
        if (!refreshToken.refreshToken.isNullOrBlank()) {
            clientApi.refreshToken = refreshToken.refreshToken
            config.providers.spotify.refreshToken = clientApi.refreshToken
        }
        config.save()
        providerState = ProviderState.POST_TOKEN_SETUP
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