package com.spadium.kassette.spotify.model

class Artist {
    companion object {
        fun fetch(): Artist {
            TODO()
        }
    }

    private constructor(
        id: String,
        externalUrls: Map<String, String>,
        genres: Array<String>,
        images: Array<ImageReference>
    )
}