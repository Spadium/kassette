package com.spadium.kassette.media

import net.minecraft.client.texture.NativeImage

data class MediaInfo(
    var maximumTime: Long,
    var currentPosition: Long,
    val title: String,
    val album: String,
    val artist: String,
    val coverArt: NativeImage,
    val provider: String
)
