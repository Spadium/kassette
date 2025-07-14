package com.spadium.kassette.media

import net.minecraft.client.texture.NativeImage

data class MediaInfo(
    var maximumTime: Long,
    var currentPosition: Long,
    var title: String,
    var album: String,
    var artist: String,
    var coverArt: NativeImage,
    var provider: String,
    var state: MediaManager.MediaState = MediaManager.MediaState.OTHER
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaInfo
        // ignore currentPosition for comparisons
        if (maximumTime != other.maximumTime) return false
        if (title != other.title) return false
        if (album != other.album) return false
        if (artist != other.artist) return false
        if (coverArt != other.coverArt) return false
        if (provider != other.provider) return false
        if (state != other.state) return false

        return true
    }

}
