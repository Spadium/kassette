package com.spadium.kassette.config

import kotlinx.serialization.Serializable

@Serializable
data class ProvidersConfig(
    var spotify: SpotifyConfig
)