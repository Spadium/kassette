package com.spadium.kassette.media.spotify

import com.google.gson.JsonParseException
import com.spadium.kassette.Kassette
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.SpotifyConfig
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.toasts.WarningToast
import com.spadium.kassette.util.ImageUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Util
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.enums.ModelObjectType
import se.michaelthelin.spotify.enums.ProductType
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials
import se.michaelthelin.spotify.model_objects.specification.Track
import java.net.URI
import kotlin.reflect.KProperty

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
    override val availableCommands: MutableList<String> = mutableListOf(
        "authSendStr", "togglePlay", "nextTrack", "previousTrack"
    )
    private var requestsMadeBeforeLimit = 0
    private var cooldownStartTime = 0L
    override var isAuthenticated: Boolean = false

    var usingPremiumAccount = false

    enum class ProviderState {
        NONE, GOT_TOKEN, POST_TOKEN_SETUP, SIGNED_IN, COOLDOWN
    }

    override fun getServiceName(): String {
        return "Spotify"
    }

    constructor() {
        runBlocking {
            clientApi = SpotifyApi.builder()
                .setClientId(spotifySettings.clientId).setClientSecret(spotifySettings.clientSecret)
                .setRedirectUri(URI("http://127.0.0.1:${config.callbackPort}/callback")).build()
            if (!config.providers.spotify.refreshToken.isBlank() && !config.providers.spotify.accessToken.isBlank()) {
                clientApi.refreshToken = config.providers.spotify.refreshToken
                clientApi.accessToken = config.providers.spotify.accessToken
                if (System.currentTimeMillis() >= nextTokenRefresh - 2000) {
                    refreshTokens()
                }
            }
            requestsMadeBeforeLimit++
        }
    }

    private fun onConfigUpdate(property: KProperty<*>, oldValue: Config, newValue: Config) {
        config = newValue
    }

    override fun destroy() {

    }

    override fun getMedia(): MediaInfo {
        return infoToReturn
    }

    override suspend fun update() {
        when (providerState) {
            ProviderState.GOT_TOKEN -> {
                config.providers.spotify.createdAt = System.currentTimeMillis()
                val authCodeCredentials: AuthorizationCodeCredentials = clientApi
                    .authorizationCode(authCode).build().execute()
                // config stuff
                config.providers.spotify.nextRefresh = nextTokenRefresh - 1000
                clientApi.accessToken = authCodeCredentials.accessToken
                clientApi.refreshToken = authCodeCredentials.refreshToken
                config.providers.spotify.accessToken = clientApi.accessToken
                config.providers.spotify.refreshToken = clientApi.refreshToken
                requestsMadeBeforeLimit++
                // actually save the config
                config.save()
                // move to the next step
                providerState = ProviderState.POST_TOKEN_SETUP
            }
            ProviderState.POST_TOKEN_SETUP -> {
                val profile = clientApi.currentUsersProfile.build().execute()
                usingPremiumAccount = (profile.product == ProductType.PREMIUM)

                if (!usingPremiumAccount) {

                    availableCommands.removeAll(arrayOf("togglePlay", "nextTrack", "previousTrack"))
                }

                isAuthenticated = true
                providerState = ProviderState.SIGNED_IN
            }
            ProviderState.SIGNED_IN -> {
                try { getCurrentPlayback() } catch (jsonException: JsonParseException) {}
            }
            ProviderState.COOLDOWN -> {
                delay(1000)
                try {
                    getCurrentPlayback()
                } catch (jsonException: JsonParseException) {
                    // Fail silently on JsonParseExceptions to avoid switching to the placeholder provider
                    // when we wouldn't need to! For some reason spotify randomly gives us malformed json.
                }
            }
            ProviderState.NONE -> {}
        }
        if (requestsMadeBeforeLimit >= 100 && config.providers.spotify.ignoreRateLimits) {
            if (cooldownStartTime == 0L) {
                cooldownStartTime = System.currentTimeMillis()
            } else if (cooldownStartTime + 30000 <= System.currentTimeMillis()) {
                cooldownStartTime = 0L
                requestsMadeBeforeLimit = 0
                providerState = ProviderState.SIGNED_IN
            }
            providerState = ProviderState.COOLDOWN
        }
    }

    override fun initiateLogin(titleScreen: Boolean) {
        Config.addListener(this::onConfigUpdate)
        if (config.providers.spotify.refreshToken.isEmpty() && config.providers.spotify.accessToken.isEmpty() && !titleScreen) {
            val authCodeUriReq = clientApi.authorizationCodeUri()
                .show_dialog(true)
                .scope(
                    "streaming user-read-playback-position user-modify-playback-state"
                ).build()
            val authReqUri = authCodeUriReq.execute()
            requestsMadeBeforeLimit++
            Util.getOperatingSystem().open(authReqUri)
        } else if ((config.providers.spotify.nextRefresh - 2000) >= System.currentTimeMillis()) {
            Kassette.logger.info("Spotify tokens need a refresh!")
            refreshTokens()
        } else if (config.providers.spotify.accessToken.isNotBlank() && config.providers.spotify.refreshToken.isNotBlank()) {
            Kassette.logger.info("Valid spotify tokens already in config!")
            providerState = ProviderState.POST_TOKEN_SETUP
        } else {
            Kassette.logger.info("No Spotify login info exists! Not logging in.")
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
        requestsMadeBeforeLimit++
        config.save()
        providerState = ProviderState.POST_TOKEN_SETUP
    }

    private suspend fun getCurrentPlayback() {
        // Two-seconds before the maximum age of the token in case the update thread is running slow
        if (System.currentTimeMillis() >= nextTokenRefresh - 2000) {
            refreshTokens()
        }
        val currentlyPlayingCtx = clientApi.informationAboutUsersCurrentPlayback.build().execute()
        requestsMadeBeforeLimit++
        if (currentlyPlayingCtx != null) {
            val item = currentlyPlayingCtx.item
            infoToReturn.currentPosition = currentlyPlayingCtx.progress_ms.toLong()
            infoToReturn.maximumTime = currentlyPlayingCtx.item.durationMs.toLong()

            infoToReturn.state = if (currentlyPlayingCtx.is_playing) {
                MediaManager.MediaState.PLAYING
            } else {
                MediaManager.MediaState.PAUSED
            }
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
                    val stream = URI(item.album.images[0].url).toURL().openStream()
                    infoToReturn.coverArt = ImageUtils.loadStream(stream)
                    stream.close()
                }
            }
        }
    }

    override fun sendCommand(cmd: String, payload: Any?): Int {
        if (cmd == "authSendStr" && payload is String) {
            val splitPayload = payload.split("=")
            if (splitPayload[0] == "code") {
                authCode = payload.split("=")[1]
                providerState = ProviderState.GOT_TOKEN
            } else {
                return 1
            }
        } else if (cmd == "togglePlay") {
            try {
                if (info.state == MediaManager.MediaState.PLAYING) {
                    clientApi.pauseUsersPlayback().build().execute()
                    infoToReturn.state = MediaManager.MediaState.PAUSED
                } else {
                    clientApi.startResumeUsersPlayback().build().execute()
                    infoToReturn.state = MediaManager.MediaState.PLAYING
                }
            } catch (e: Exception) {
                if (e.message == "Player command failed: No active device found") {
                    MinecraftClient.getInstance().toastManager.add(WarningToast(Text.translatable("kassette.warning.spotify.device")))

                } else {
                    e.printStackTrace()
                }
                return 2
            }
        } else if (cmd == "nextTrack") {
            try {
                clientApi.skipUsersPlaybackToNextTrack().build().execute()
            } catch (e: Exception) {
                e.printStackTrace()
                return 2
            }
        } else if (cmd == "previousTrack") {
            try {
                clientApi.skipUsersPlaybackToPreviousTrack().build().execute()
            } catch (e: Exception) {
                e.printStackTrace()
                return 2
            }
        } else {
            return Int.MIN_VALUE
        }
        return 0
    }
}