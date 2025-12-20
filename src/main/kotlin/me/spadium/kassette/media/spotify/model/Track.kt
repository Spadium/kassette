package me.spadium.kassette.media.spotify.model

data class Track(
    val album: Album,
    val href: String,
    val limit: Int,
    val next: String,
    val offset: String,
    val previous: String,

) {

}
