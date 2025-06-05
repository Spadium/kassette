package com.spadium.kassette.info

data class MediaInfo(
    val maximumTime: Long,
    val currentPosition: Long,
    val title: String,
    val album: String,
    val artist: String,
    val coverArts: Array<ByteArray>,
    val provider: MediaProvider
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaInfo

        if (maximumTime != other.maximumTime) return false
        if (currentPosition != other.currentPosition) return false
        if (title != other.title) return false
        if (album != other.album) return false
        if (artist != other.artist) return false
        if (!coverArts.contentEquals(other.coverArts)) return false
        if (provider != other.provider) return false

        return true
    }

    override fun hashCode(): Int {
        var result = maximumTime.hashCode()
        result = 31 * result + currentPosition.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + coverArts.contentHashCode()
        result = 31 * result + provider.hashCode()
        return result
    }
}
