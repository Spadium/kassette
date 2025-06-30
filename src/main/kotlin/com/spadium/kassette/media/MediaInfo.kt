package com.spadium.kassette.media

import net.minecraft.client.texture.NativeImage

data class MediaInfo(
    var maximumTime: Long,
    var currentPosition: Long,
    var title: String,
    var album: String,
    var artist: String,
    var coverArt: NativeImage?,
    var provider: String
)
