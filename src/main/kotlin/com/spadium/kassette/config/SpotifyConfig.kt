package com.spadium.kassette.config

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyConfig(
    var clientId: String,
    var clientSecret: String,
)
