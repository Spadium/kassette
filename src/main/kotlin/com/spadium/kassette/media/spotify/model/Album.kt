package com.spadium.kassette.media.spotify.model

data class Album(
    val albumType: Type,
    val totalTracks: Int,
    val availableMarkets: Array<String>,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val images: Array<Image>,
    val name: String,
    val releaseDate: String,
    val releaseDatePrecision: String,
    val restrictions: Object,
    val type: String = "album",
    val uri: String,
    val artists: Array<SimplifiedArtist>,
    val tracks: Tracks,
    val copyrights: String,
    val externalIds: Map<String, String>,
    val genres: Array<String>,
    val label: String,
    val popularity: Int
) {
    enum class Type {
        ALBUM, SINGLE, COMPILATION
    }

    data class SimplifiedTrack(
        val artist: Array<SimplifiedArtist>,
        val availableMarkets: Array<String>,
        val discNumber: Int,
        val durationMs: Int,
        val explicit: Boolean,
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val isPlayable: Boolean,
        val linkedFrom: TrackLinking,
        val restrictions: Restriction,
        val name: String,
        val previewUrl: String? = null,
        val trackNumber: Int,
        val type: String,
        val uri: String,
        val isLocal: Boolean
    )

    data class TrackLinking(
        val externalUrls: Map<String, String>,
        val href: String,
        val id: String,
        val type: String,
        val url: String,
    )

    data class Tracks(
        val href: String,
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int,
        val tracks: Array<SimplifiedTrack>
    )

    data class Restriction(
        val reason: String
    )
}