package com.spadium.kassette.media.spotify

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.models.CurrentlyPlayingObject
import com.adamratzman.spotify.models.CurrentlyPlayingType
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.SpotifyConfig
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.MediaInfo
import com.spadium.kassette.media.MediaProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SpotifyProvider : AccountMediaProvider {
    private lateinit var clientApi: SpotifyClientApi
    private var config = Config.Instance
    private var spotifySettings: SpotifyConfig = config.providers.spotify
    private var infoToReturn: MediaInfo = MediaInfo(
        0L, 0L, "", "", "",
        null, getServiceName()
    )

    override fun getServiceName(): String {
        return "Spotify"
    }

    override fun init() {
        runBlocking {
            clientApi = spotifyClientApi(
                spotifySettings.clientId,
                spotifySettings.clientId,
                "127.0.0.1:${config.callbackPort}"
            ).build()
        }
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun getMedia(): MediaInfo {
        return infoToReturn
    }

    override fun update() {
        runBlocking {
            val currentlyPlaying: CurrentlyPlayingObject? = clientApi.player.getCurrentlyPlaying()
            val info = infoToReturn

            if (currentlyPlaying != null) {
                info.currentPosition = currentlyPlaying.progressMs!!.toLong()
                if (currentlyPlaying.currentlyPlayingType == CurrentlyPlayingType.Track) {
                    if (currentlyPlaying.item?.asLocalTrack != null) {
                        val track = currentlyPlaying.item?.asLocalTrack
                        info.title = track?.name!!
                        info.maximumTime = track.durationMs!!.toLong()
                    } else {
                        val track = currentlyPlaying.item?.asTrack
                        info.title = track?.name!!
                        info.maximumTime = track.durationMs.toLong()
                    }

                } else if (currentlyPlaying.currentlyPlayingType == CurrentlyPlayingType.Episode) {
                    val episode = currentlyPlaying.item?.asPodcastEpisodeTrack
                } else if (currentlyPlaying.currentlyPlayingType == CurrentlyPlayingType.Ad) {
                    info.title = "Advert"
                    info.album = "N/A"
                    info.album = "N/A"
                }
            }
            infoToReturn = info
        }
    }

    override fun initiateLogin() {
        TODO("Not yet implemented")
    }
}