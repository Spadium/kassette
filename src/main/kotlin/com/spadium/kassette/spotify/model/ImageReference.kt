package com.spadium.kassette.spotify.model

data class ImageReference(
    val url: String,
    val height: Int,
    val width: Int
) {
    fun fetch(): ByteArray {
        return byteArrayOf(0x00, 0x00)
    }
}